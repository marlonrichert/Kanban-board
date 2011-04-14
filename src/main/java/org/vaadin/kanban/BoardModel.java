package org.vaadin.kanban;

import java.util.List;

public interface BoardModel {

    /** Returns a list of all columns in this board. */
    List<ColumnModel> getColumns();
}
