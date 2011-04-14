package org.vaadin.kanban.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.vaadin.kanban.CardModel;
import org.vaadin.kanban.ColumnModel;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findCardsByStateColumn" })
public class Card implements CardModel {

    @NotNull
    @Column(unique = true)
    @Size(min = 2)
    private String description;

    @ManyToOne
    private StateColumn stateColumn;

    @NotNull
    @Min(0L)
    private int sortOrder;

    private String owner = "";

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date endDate;

    @Override
    public ColumnModel getColumn() {
        return getStateColumn();
    }

    @Override
    public void setColumn(ColumnModel column) {
        setStateColumn((StateColumn) column);
    }
}
