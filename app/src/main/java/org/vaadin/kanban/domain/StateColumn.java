package org.vaadin.kanban.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.vaadin.kanban.CardModel;
import org.vaadin.kanban.ColumnModel;

@RooJavaBean
@RooToString
@RooEntity
public class StateColumn implements ColumnModel, Sortable {

    @NotNull
    @Column(unique = true)
    @Size(min = 2)
    private String name;

    @Min(0L)
    private int workInProgressLimit = 0;

    @NotNull
    @Column(unique = true)
    @Min(0L)
    private int sortOrder;

    private String definitionOfDone = "";

    @Override
    @Transient
    public Card append(CardModel card) {
        Card c = (Card) card;
        c.setSortOrder(Card.findCardsByStateColumn(this).getResultList().size());
        c.setStateColumn(this);
        return c.merge();
    }

    @Override
    @Transient
    public List<CardModel> getCards() {
        List<Card> list = new ArrayList<Card>();
        list.addAll(Card.findCardsByStateColumn(this).getResultList());
        Collections.sort(list, new SortOrderComparator());
        return new ArrayList<CardModel>(list);
    }

    @Override
    @Transient
    public Card insert(CardModel cardModel, int index) {
        for (Card c : Card.findCardsByStateColumn(this).getResultList()) {
            final int sortOrder = c.getSortOrder();
            if (sortOrder >= index) {
                c.setSortOrder(sortOrder + 1);
                c.merge();
            }
        }
        Card card = (Card) cardModel;
        card.setSortOrder(index);
        card.setStateColumn(this);
        return card.merge();
    }

    @Override
    @Transient
    public Card remove(CardModel cardModel) {
        Card card = (Card) cardModel;
        int index = card.getSortOrder();
        for (Card c : Card.findCardsByStateColumn(this).getResultList()) {
            final int sortOrder = c.getSortOrder();
            if (sortOrder > index) {
                c.setSortOrder(sortOrder - 1);
                c.merge();
            }
        }
        card.setStateColumn(null);
        return card.merge();
    }
}
