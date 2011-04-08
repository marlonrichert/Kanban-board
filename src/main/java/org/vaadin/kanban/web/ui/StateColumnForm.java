package org.vaadin.kanban.web.ui;

import org.vaadin.kanban.web.AutomaticEntityForm;

import com.vaadin.spring.roo.addon.annotations.RooVaadinAutomaticEntityForm;

@RooVaadinAutomaticEntityForm(formBackingObject = org.vaadin.kanban.domain.StateColumn.class)
public class StateColumnForm extends
        AutomaticEntityForm<org.vaadin.kanban.domain.StateColumn> {

    public StateColumnForm() {
        super(org.vaadin.kanban.domain.StateColumn.class);

        getForm().setFormFieldFactory(getFormFieldFactory());
    }
}
