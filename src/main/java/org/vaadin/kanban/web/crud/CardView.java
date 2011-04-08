package org.vaadin.kanban.web.crud;

import org.vaadin.kanban.web.AbstractEntityView;
import org.vaadin.kanban.web.EntityEditor;
import com.vaadin.spring.roo.addon.annotations.RooVaadinEntityView;
import com.vaadin.ui.Table;

@RooVaadinEntityView(formBackingObject = org.vaadin.kanban.domain.Card.class)
public class CardView extends AbstractEntityView<org.vaadin.kanban.domain.Card> {

    @Override
    protected EntityEditor createForm() {
        return new CardForm();
    }

    @Override
    protected void configureTable(Table table) {
        table.setContainerDataSource(getTableContainer());
        table.setVisibleColumns(getTableColumns());

        setupGeneratedColumns(table);
    }

}
