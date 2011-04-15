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
}
