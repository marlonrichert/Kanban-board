package org.vaadin.kanban;

import java.util.Date;

public interface CardModel {

    String getDescription();

    Date getEndDate();

    String getOwner();

    int getSortOrder();

    Date getStartDate();

    void setDescription(String description);

    void setEndDate(Date endDate);

    void setOwner(String owner);

    void setStartDate(Date startDate);

    /** Returns the column this card is in. */
    ColumnModel getColumn();

    void setColumn(ColumnModel column);

    /** Returns a visual editor for this entity. */
    EntityEditor getEditor();

    /**
     * Saves changes made to this entity to the back-end and returns a new
     * representation of it.
     */
    CardModel merge();

    /** Deletes this entity from the back-end. */
    void remove();
}
