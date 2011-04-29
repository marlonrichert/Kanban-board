package org.vaadin.kanban.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.vaadin.kanban.CardModel;
import org.vaadin.kanban.ColumnModel;
import org.vaadin.kanban.EntityEditor;
import org.vaadin.kanban.web.crud.CardForm;
import com.vaadin.data.util.BeanItem;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findCardsByStateColumn" })
public class Card implements CardModel, Sortable {

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

    private String link;

    @Override
    @Transient
    public StateColumn getColumn() {
        return getStateColumn();
    }

    @Override
    @Transient
    public void setColumn(ColumnModel column) {
        setStateColumn((StateColumn) column);
    }

    @Override
    @Transient
    public EntityEditor getEditor() {
        CardForm form = new CardForm();
        form.setItemDataSource(new BeanItem<Card>(this));
        return form;
    }
}
