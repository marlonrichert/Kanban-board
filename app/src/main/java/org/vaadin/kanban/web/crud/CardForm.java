package org.vaadin.kanban.web.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.vaadin.kanban.web.AutomaticEntityForm;

import com.vaadin.data.Item;
import com.vaadin.spring.roo.addon.annotations.RooVaadinAutomaticEntityForm;

@RooVaadinAutomaticEntityForm(formBackingObject = org.vaadin.kanban.domain.Card.class)
public class CardForm extends
        AutomaticEntityForm<org.vaadin.kanban.domain.Card> {

    public CardForm() {
        super(org.vaadin.kanban.domain.Card.class);

        getForm().setFormFieldFactory(getFormFieldFactory());
    }

    @Override
    public void setItemDataSource(Item item) {
        getForm().setItemDataSource(item, getVisiblePropertyIds(item));

        // may reuse form and button, so clear any old error messages
        getSaveButton().setComponentError(null);
        getForm().setComponentError(null);

        // don't show validation errors before user tries to commit the form
        getForm().setValidationVisible(false);
    }

    private Collection<?> getVisiblePropertyIds(Item item) {
        if (null == item) {
            return Collections.EMPTY_LIST;
        }
        Collection<?> propertyIds = new ArrayList<Object>(
                item.getItemPropertyIds());
        propertyIds.remove("column");
        propertyIds.remove("editor");
        propertyIds.remove("startDate");
        propertyIds.remove("endDate");
        propertyIds.remove("sortOrder");
        propertyIds.remove("stateColumn");
        return propertyIds;
    }
}
