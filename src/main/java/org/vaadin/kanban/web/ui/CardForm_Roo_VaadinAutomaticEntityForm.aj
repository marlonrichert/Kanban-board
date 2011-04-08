// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vaadin.kanban.web.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.kanban.domain.Card;
import org.vaadin.kanban.domain.StateColumn;
import org.vaadin.kanban.web.EntityFieldWrapper;

privileged aspect CardForm_Roo_VaadinAutomaticEntityForm {
    
    private JPAContainer<StateColumn> CardForm.containerForStateColumns;
    
    public ComboBox CardForm.buildStateColumnCombo() {
        ComboBox combo = new ComboBox(null, getContainerForStateColumns());
        Object captionPropertyId = getStateColumnCaptionPropertyId();
        if (captionPropertyId != null) {
            combo.setItemCaptionPropertyId(captionPropertyId);
        }
        return combo;
    }
    
    public FormFieldFactory CardForm.getFormFieldFactory() {
        return new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId, Component uiContext) {
                Field field = null;
                if (getIdProperty().equals(propertyId) || getVersionProperty().equals(propertyId)) {
                    return null;
                } else if ("stateColumn".equals(propertyId)) {
                    ComboBox combo = buildStateColumnCombo();
                    field = new EntityFieldWrapper<StateColumn>(combo, StateColumn.class, getContainerForStateColumns(), "id");
                    field.setCaption(createCaptionByPropertyId(propertyId));
                } else {
                    field = super.createField(item, propertyId, uiContext);
                    if (field instanceof TextField) {
                        ((TextField) field).setNullRepresentation("");
                    }
                    if (field instanceof DateField) {
                        ((DateField) field).setLocale(LocaleContextHolder.getLocale());
                        field.setInvalidAllowed(true);
                    }
                }
                return field;
            }
        };
    }
    
    public JPAContainer<StateColumn> CardForm.getContainerForStateColumns() {
        if (containerForStateColumns == null) {
            LocalEntityProvider<StateColumn> entityProvider = new LocalEntityProvider<StateColumn>(StateColumn.class, StateColumn.entityManager());
            JPAContainer<StateColumn> container = new JPAContainer<StateColumn>(StateColumn.class);
            container.setEntityProvider(entityProvider);
            containerForStateColumns = container;
        }
        return containerForStateColumns;
    }
    
    public Class<Card> CardForm.getEntityClass() {
        return Card.class;
    }
    
    public Object CardForm.getStateColumnCaptionPropertyId() {
        return "name";
    }
    
    public String CardForm.getIdProperty() {
        return "id";
    }
    
    public String CardForm.getVersionProperty() {
        return "version";
    }
    
}
