package org.vaadin.kanban;

import java.util.Date;

public interface CardModel {

    String getDescription();

    void setDescription(String description);

    /** Returns the column this card is in. */
    ColumnModel getColumn();

    /** Sets the column this card is in. */
    void setColumn(ColumnModel stateColumn);

    int getSortOrder();

    void setSortOrder(int sortOrder);

    String getOwner();

    void setOwner(String owner);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getEndDate();

    void setEndDate(Date endDate);
}
