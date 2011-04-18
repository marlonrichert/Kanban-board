package org.vaadin.kanban;

import java.util.List;

public interface ColumnModel {
    String getDefinitionOfDone();

    String getName();

    int getWorkInProgressLimit();

    void setDefinitionOfDone(String definitionOfDone);

    void setName(String name);

    void setWorkInProgressLimit(int workInProgressLimit);

    /**
     * Appends the given card to the end of this column and returns a new
     * representation of it.
     */
    CardModel append(CardModel card);

    /**
     * Returns a list of all cards in this column, in the order they should be
     * displayed.
     */
    List<CardModel> getCards();

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
