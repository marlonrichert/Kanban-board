package org.vaadin.kanban.web;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.kanban.BoardModel;
import org.vaadin.kanban.ColumnModel;
import org.vaadin.kanban.domain.StateColumn;

public class PersistentBoardModel implements BoardModel {

    @Override
    public List<ColumnModel> getColumns() {
        List<ColumnModel> list = new ArrayList<ColumnModel>();
        list.addAll(StateColumn.findAllStateColumns());
        return list;
    }
}
