package org.vaadin.kanban.web.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.vaadin.kanban.EntityEditor;
import org.vaadin.kanban.web.AbstractEntityView;

import com.vaadin.spring.roo.addon.annotations.RooVaadinEntityView;
import com.vaadin.ui.Table;

@RooVaadinEntityView(formBackingObject = org.vaadin.kanban.domain.StateColumn.class)
public class StateColumnView extends
        AbstractEntityView<org.vaadin.kanban.domain.StateColumn> {

    @Override
    protected EntityEditor createForm() {
        return new StateColumnForm();
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
        tableColumns.remove("cards");
        tableColumns.remove("editor");
        return tableColumns.toArray();
    }
}
