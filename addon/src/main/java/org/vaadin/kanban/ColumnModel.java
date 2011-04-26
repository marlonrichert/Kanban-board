package org.vaadin.kanban;

import java.util.List;

public interface ColumnModel {
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

    String getDefinitionOfDone();

    /** Returns a visual editor for this entity. */
    EntityEditor getEditor();

    String getName();

    int getSortOrder();

    int getWorkInProgressLimit();

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

    void setDefinitionOfDone(String definitionOfDone);

    void setName(String name);

    void setWorkInProgressLimit(int workInProgressLimit);

    void remove();

    ColumnModel merge();
}
