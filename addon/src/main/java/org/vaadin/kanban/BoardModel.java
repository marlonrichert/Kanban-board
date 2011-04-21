package org.vaadin.kanban;

import java.util.List;

public interface BoardModel {

    /**
     * Returns a list of all columns in this board in the order in which they
     * should be displayed.
     */
    List<? extends ColumnModel> getColumns();

    CardModel newCard(String description);
}
