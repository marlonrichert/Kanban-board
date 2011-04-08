package org.vaadin.kanban.web.ui;

import org.vaadin.kanban.web.AutomaticEntityForm;

import com.vaadin.spring.roo.addon.annotations.RooVaadinAutomaticEntityForm;

@RooVaadinAutomaticEntityForm(formBackingObject = org.vaadin.kanban.domain.Card.class)
public class CardForm extends
        AutomaticEntityForm<org.vaadin.kanban.domain.Card> {

    public CardForm() {
        super(org.vaadin.kanban.domain.Card.class);

        getForm().setFormFieldFactory(getFormFieldFactory());
    }
}
