package org.vaadin.kanban.web;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class AutomaticEntityForm<T> extends CustomComponent implements
        EntityEditor {

    private Button deleteButton;
    private Button cancelButton;
    private Button saveButton;

    private Panel formPanel;
    private BeanValidationForm<T> form;

    public AutomaticEntityForm(Class<T> entityClass) {
        form = new BeanValidationForm<T>(entityClass);

        // form editor layout
        CssLayout wrapper = new CssLayout();
        wrapper.setMargin(true);
        wrapper.setWidth("100%");
        formPanel = new Panel();
        formPanel.setIcon(new ThemeResource("img/edit-icon.png"));
        formPanel.addComponent(getForm());
        wrapper.addComponent(formPanel);

        setCompositionRoot(wrapper);

        // immediate validation but no changes to the underlying entity
        getForm().setImmediate(true);
        getForm().setWriteThrough(false);

        createFormFooter();

        // make saving the form the default action on Enter keypress
        getSaveButton().setClickShortcut(KeyCode.ENTER);
    }

    @Override
    public void addSaveActionListener(ClickListener listener) {
        getSaveButton().addListener(listener);
    }

    @Override
    public void addCancelActionListener(ClickListener listener) {
        getCancelButton().addListener(listener);
    }

    @Override
    public void addDeleteActionListener(ClickListener listener) {
        getDeleteButton().addListener(listener);
    }

    @Override
    public void setSaveAllowed(boolean canSave) {
        getSaveButton().setVisible(canSave);
        getCancelButton().setVisible(canSave);
        getSaveButton().setEnabled(canSave);
        getCancelButton().setEnabled(canSave);

        // do not change the enabled state of the delete button
        getForm().getLayout().setEnabled(canSave);
    }

    @Override
    public void setDeleteAllowed(boolean canDelete) {
        getDeleteButton().setVisible(canDelete);
        getDeleteButton().setEnabled(canDelete);
    }

    @Override
    public void commit() {
        getForm().commit();
    }

    @Override
    public void setItemDataSource(Item item) {
        getForm().setItemDataSource(item);

        // may reuse form and button, so clear any old error messages
        getSaveButton().setComponentError(null);
        getForm().setComponentError(null);

        // don't show validation errors before user tries to commit the form
        getForm().setValidationVisible(false);
    }

    @Override
    public Item getItemDataSource() {
        return getForm().getItemDataSource();
    }

    @Override
    public void setCommitErrorMessage(String message) {
    }

    protected BeanValidationForm<T> getForm() {
        return form;
    }

    protected Button getSaveButton() {
        if (saveButton == null) {
            saveButton = new Button("Save");
            saveButton.setStyleName(Reindeer.BUTTON_DEFAULT);
        }
        return saveButton;
    }

    protected Button getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new Button("Cancel");
        }
        return cancelButton;
    }

    protected Button getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new Button("Delete");
            deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
        }
        return deleteButton;
    }

    protected void createFormFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        getForm().setFooter(footer);

        footer.addComponent(getSaveButton());
        footer.addComponent(getCancelButton());
        footer.addComponent(getDeleteButton());
    }

    @Override
    public void setCaption(String caption) {
        formPanel.setCaption(caption);
    }

    @Override
    public void focus() {
        getForm().focus();
    }

    @Override
    public void refresh() {
        // nothing to do
    }

    @Override
    public boolean isModified() {
        return getForm().isModified();
    }

}