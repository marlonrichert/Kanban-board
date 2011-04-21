package org.vaadin.kanban;

import java.util.Date;

public interface CardModel {

    /** Returns the column this card is in. */
    ColumnModel getColumn();

    String getDescription();

    /** Returns a visual editor for this entity. */
    EntityEditor getEditor();

    Date getEndDate();

    String getOwner();

    int getSortOrder();

    Date getStartDate();

    /**
     * Saves changes made to this entity to the back-end and returns a new
     * representation of it.
     */
    CardModel merge();

    /** Deletes this entity from the back-end. */
    void remove();

    void setColumn(ColumnModel column);

    void setDescription(String description);

    void setEndDate(Date endDate);

    void setOwner(String owner);

    void setStartDate(Date startDate);
}
