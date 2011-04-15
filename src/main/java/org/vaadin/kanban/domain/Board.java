package org.vaadin.kanban.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.kanban.BoardModel;
import org.vaadin.kanban.ColumnModel;

public class Board implements BoardModel {

    @Override
    public List<? extends ColumnModel> getColumns() {
        List<StateColumn> list = new ArrayList<StateColumn>();
        list.addAll(StateColumn.findAllStateColumns());
        Collections.sort(list, new SortOrderComparator());
        return list;
    }
}
