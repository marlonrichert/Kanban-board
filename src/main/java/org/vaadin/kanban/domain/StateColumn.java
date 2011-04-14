package org.vaadin.kanban.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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
public class StateColumn implements ColumnModel {

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
    public Card append(CardModel card) {
        card.setSortOrder(Card.findCardsByStateColumn(this).getResultList()
                .size());
        card.setColumn(this);
        return ((Card) card).merge();
    }

    @Override
    public List<CardModel> getCards() {
        List<CardModel> list = new ArrayList<CardModel>();
        list.addAll(Card.findCardsByStateColumn(this).getResultList());
        return list;
    }

    @Override
    public Card insert(CardModel card, int index) {
        for (Card c : Card.findCardsByStateColumn(this).getResultList()) {
            final int sortOrder = c.getSortOrder();
            if (sortOrder >= index) {
                c.setSortOrder(sortOrder + 1);
                c.merge();
            }
        }
        card.setSortOrder(index);
        card.setColumn(this);
        return ((Card) card).merge();
    }

    @Override
    public Card remove(CardModel card) {
        int index = card.getSortOrder();
        for (Card c : Card.findCardsByStateColumn(this).getResultList()) {
            final int sortOrder = c.getSortOrder();
            if (sortOrder > index) {
                c.setSortOrder(sortOrder - 1);
                c.merge();
            }
        }
        card.setColumn(null);
        return ((Card) card).merge();
    }
}
