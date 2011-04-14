package org.vaadin.kanban;

import java.util.List;

public interface ColumnModel {
    String getName();

    void setName(String name);

    int getWorkInProgressLimit();

    void setWorkInProgressLimit(int workInProgressLimit);

    int getSortOrder();

    void setSortOrder(int sortOrder);

    String getDefinitionOfDone();

    void setDefinitionOfDone(String definitionOfDone);

    /**
     * Returns a list of all cards in this column, in the order they should be
     * displayed.
     */
    List<CardModel> getCards();

    /**
     * Appends the given card to the end of this column and returns a new
     * representation of it.
     */
    CardModel append(CardModel card);

    /**
     * Inserts the given card into this column at the given index and returns a
     * new representation of it.
     */
    CardModel insert(CardModel card, int index);

    /**
     * Removes the given card from this column and returns a new representation
     * of it.
     */
    CardModel remove(CardModel card);
}
