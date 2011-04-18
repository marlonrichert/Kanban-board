package org.vaadin.kanban.web;

import java.util.ArrayList;

import org.vaadin.kanban.EntityEditor;
import org.vaadin.navigator.Navigator;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.spring.roo.addon.annotations.RooVaadinAbstractEntityView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.Reindeer;

/**
 * Base class for that defines the common layout and some UI logic for all
 * entity views. This class is not specific to any entity class.
 */
@RooVaadinAbstractEntityView(useJpaContainer = true)
public abstract class AbstractEntityView<E> extends CustomComponent implements
        Navigator.View {

    private VerticalSplitPanel mainLayout;
    private Table table;
    private EntityEditor form;
    private Navigator navigator;
    private boolean dirty = false;

    /**
     * Constructor for an abstract entity view.
     * 
     * The methods {@link #createTable()} and {@link #createForm()} are used to
     * create the main parts of the view.
     */
    public AbstractEntityView() {
        // custom component size, must be set to allow inner layout take 100%
        // size
        setSizeFull();

        mainLayout = new VerticalSplitPanel();
        mainLayout.addStyleName("blue-bottom");
        mainLayout.setSplitPosition(30);
        setCompositionRoot(mainLayout);

        // table settings, display a fixed number of rows
        getTable().setSizeFull();
        getTable().setImmediate(true);
        getTable().setSelectable(true);
        getTable().addStyleName(Reindeer.TABLE_BORDERLESS);
        getTable().addStyleName(Reindeer.TABLE_STRONG);

        mainLayout.setFirstComponent(getTable());
        mainLayout.setSecondComponent(getForm());

        // hide buttons if certain operations are not allowed
        getForm().setSaveAllowed(isCreateAllowed() || isUpdateAllowed());
        getForm().setDeleteAllowed(isDeleteAllowed());

        // add listeners for the buttons
        addListeners();

        // initially nothing on the form
        setCurrentEntity(null);
    }

    // View interface and related

    @Override
    public void init(Navigator navigator, Application application) {
        this.navigator = navigator;
    }

    @Override
    public void navigateTo(String requestedDataId) {
        // refresh table
        refresh();

        setDirty(false);

        if (isCreateAllowed() && "new".equals(requestedDataId)) {
            createNewEntity();
            return;
        }

        if (isUpdateAllowed() && requestedDataId != null
                && requestedDataId.startsWith("edit/")) {
            try {
                Long id = Long.valueOf(requestedDataId.substring(5));
                setCurrentEntity(getEntityForItem(getTable().getItem(id)));
                getTable().setValue(id);
                return;
            } catch (NumberFormatException e) {
                navigateToFragment(null);
                return;
            }
        }

        setCurrentEntity(null);
    }

    @Override
    public String getWarningForNavigatingFrom() {
        return (isDirty() && getForm().isModified()) ? "Discard unsaved changes?"
                : null;
    }

    protected void navigateToFragment(String fragment) {
        if (navigator != null) {
            String uri = navigator.getUri(getClass());
            // remove fragment
            uri = uri.replaceAll("/.*", "");
            if (fragment == null) {
                fragment = "";
            } else {
                fragment = "/" + fragment;
            }
            navigator.navigateTo(uri + fragment);
        }
    }

    protected void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    protected boolean isDirty() {
        return dirty;
    }

    // other methods

    public void createNewEntity() {
        if (isCreateAllowed()) {
            getTable().setValue(null);

            getForm().setVisible(true);

            getForm().setDeleteAllowed(false);
            getForm().setSaveAllowed(isCreateAllowed());
            setCurrentEntity(createEntityInstance());
            getForm().setCaption("New " + getEntityName());

            getForm().focus();
        }
    }

    /**
     * Adds listeners for the various buttons on the form and for table
     * selection change.
     */
    protected void addListeners() {
        getTable().addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                if (value != null) {
                    navigateToFragment("edit/"
                            + String.valueOf(value).replaceAll("[^0-9]", ""));
                }
            }
        });

        getForm().addSaveActionListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (doCommit()) {
                    setDirty(false);
                    navigateToFragment(null);
                }
            }
        });
        getForm().addDeleteActionListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                doDelete();
                setDirty(false);
                navigateToFragment(null);
            }
        });
        getForm().addCancelActionListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                setDirty(false);
                navigateToFragment(null);
            }
        });
    }

    /**
     * Set the current entity to display on the form and to edit.
     * 
     * If the table contains an item with the entity as its key, that item is
     * reused. Otherwise, a new Item is created.
     * 
     * @param entity
     */
    protected void setCurrentEntity(E entity) {
        getForm().setVisible(entity != null);
        if (entity != null) {
            getForm().refresh();
            getForm().setCaption("Edit " + getEntityName());

            boolean newEntity = isNewEntity(entity);
            getForm().setDeleteAllowed(isDeleteAllowed());
            boolean saveAllowed = newEntity ? isCreateAllowed()
                    : isUpdateAllowed();
            getForm().setSaveAllowed(saveAllowed);
            setDirty(entity != null && saveAllowed);

            Item item = getItemForEntity(entity);

            getForm().setItemDataSource(item);
        } else {
            getForm().setItemDataSource(null);
        }
    }

    // getters for the components
    protected Table getTable() {
        if (table == null) {
            table = createTable();
        }
        return table;
    }

    protected EntityEditor getForm() {
        if (form == null) {
            form = createForm();
        }
        return form;
    }

    /**
     * Creates the table listing the instances of this entity.
     * 
     * Subclasses can override this.
     * 
     * @return
     */
    protected Table createTable() {
        return new Table();
    }

    /**
     * Refresh the table and any other relevant parts of the entity view.
     * 
     * This is called after entities are saved or deleted or when navigating to
     * a new view.
     */
    protected void refresh() {
        Object sortContainerPropertyId = getTable()
                .getSortContainerPropertyId();
        boolean sortAscending = getTable().isSortAscending();

        configureTable(getTable());

        if (sortContainerPropertyId != null) {
            getTable().setSortAscending(sortAscending);
            getTable().setSortContainerPropertyId(sortContainerPropertyId);
            getTable().sort();
        }
    }

    /**
     * Return the columns (property ids) to show in the entity table.
     * 
     * @return array of property ids for the entity type
     */
    public Object[] getTableColumns() {
        ArrayList<Object> columnIds = new ArrayList<Object>();
        for (Object property : getTable().getContainerPropertyIds()) {
            if (property != null && property.equals(getIdProperty())) {
                columnIds.add(0, property);
            } else if (property != null
                    && !property.equals(getVersionProperty())) {
                columnIds.add(property);
            }
        }
        return columnIds.toArray();
    }

    // these are typically defined in the ITD (aspect) of the subclass

    protected abstract Class<? extends E> getEntityClass();

    protected abstract String getEntityName();

    public abstract boolean isCreateAllowed();

    protected abstract boolean isUpdateAllowed();

    protected abstract boolean isDeleteAllowed();

    protected abstract boolean isNewEntity(E entity);

    protected abstract void deleteEntity(E entity);

    protected abstract E saveEntity(E entity);

    public abstract Object getIdProperty();

    public abstract Object getVersionProperty();

    /**
     * Returns an existing or new Item for an entity to be edited on a form. For
     * already stored entities, the item is retrieved from the table. For new
     * entities, a new item is created.
     * 
     * @param entity
     * @return Item
     */
    protected abstract Item getItemForEntity(E entity);

    /**
     * Obtains the entity from an item in a container specific manner.
     * 
     * @param item
     *            the item for which to get the entity
     * @return entity
     */
    protected abstract E getEntityForItem(Item item);

    /**
     * Create a new entity instance.
     * 
     * Concrete entity view classes must implement this method, and any special
     * initialization of the entity can be performed here.
     */
    protected abstract E createEntityInstance();

    // these are typically defined in the concrete subclass

    /**
     * Create the form used for editing an entity.
     * 
     * Concrete subclasses must implement this method.
     * 
     * @return
     */
    protected abstract EntityEditor createForm();

    protected abstract void configureTable(Table table);

}
