package org.vaadin.kanban.web.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.vaadin.kanban.EntityEditor;
import org.vaadin.kanban.web.AbstractEntityView;

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

    @Override
    public Object[] getTableColumns() {
        Collection<?> tableColumns = new ArrayList<Object>(Arrays.asList(super
                .getTableColumns()));
        tableColumns.remove("column");
        tableColumns.remove("editor");
        return tableColumns.toArray();
    }
}
