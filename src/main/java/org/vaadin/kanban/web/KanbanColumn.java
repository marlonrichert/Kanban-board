package org.vaadin.kanban.web;

import org.vaadin.kanban.domain.Card;
import org.vaadin.kanban.domain.StateColumn;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class KanbanColumn extends DragAndDropWrapper implements DropHandler {

    private KanbanBoard board;
    private VerticalLayout root;
    private StateColumn model;

    public KanbanColumn(KanbanBoard board, StateColumn model) {
        super(new VerticalLayout());
        root = (VerticalLayout) getCompositionRoot();
        root.setSizeUndefined();
        root.setWidth(100, UNITS_PERCENTAGE);

        root.setMargin(true);
        root.setSpacing(true);

        this.board = board;
        this.model = model;
        setStyleName("column");
        setSizeFull();
        setDropHandler(this);
    }

    @Override
    public void addComponent(Component c) {
        if (c instanceof KanbanCard) {
            KanbanCard card = (KanbanCard) c;
            int sortOrder = card.getModel().getSortOrder();
            int index = 0;
            while (index < root.getComponentCount()) {
                if (sortOrder < ((KanbanCard) root.getComponent(index))
                        .getModel().getSortOrder()) {
                    break;
                }
                index++;
            }
            root.addComponent(card, index);
            card.setDropHandler(this);
        }
    }

    @Override
    public void drop(DragAndDropEvent event) {
        Component sourceComponent = event.getTransferable()
                .getSourceComponent();
        if (sourceComponent instanceof KanbanCard) {
            WrapperTargetDetails details = (WrapperTargetDetails) event
                    .getTargetDetails();
            DropTarget target = details.getTarget();
            Card sourceCard = ((KanbanCard) sourceComponent).getModel();
            StateColumn sourceColumn = sourceCard.getStateColumn();

            if (target == sourceComponent) {
                return;
            }

            sourceCard = remove(sourceCard, sourceColumn);
            if (target instanceof KanbanCard) {
                final int index = ((KanbanCard) target).getModel()
                        .getSortOrder();

                insert(sourceCard,
                        details.verticalDropLocation().equals(
                                VerticalDropLocation.BOTTOM) ? index + 1
                                : index);
            } else {
                if (details.verticalDropLocation().equals(
                        VerticalDropLocation.TOP)) {
                    insert(sourceCard, 0);
                } else {
                    append(sourceCard);
                }
            }
            board.update();
        }
    }

    private Card append(Card card) {
        card.setSortOrder(Card.findCardsByStateColumn(model).getResultList()
                .size());
        card.setStateColumn(model);
        return card.merge();
    }

    private Card insert(Card card, int index) {
        for (Card c : Card.findCardsByStateColumn(model).getResultList()) {
            final int sortOrder = c.getSortOrder();
            if (sortOrder >= index) {
                c.setSortOrder(sortOrder + 1);
                c.merge();
            }
        }
        card.setSortOrder(index);
        card.setStateColumn(model);
        return card.merge();
    }

    private Card remove(Card card, StateColumn column) {
        int index = card.getSortOrder();
        for (Card c : Card.findCardsByStateColumn(column).getResultList()) {
            final int sortOrder = c.getSortOrder();
            if (sortOrder > index) {
                c.setSortOrder(sortOrder - 1);
                c.merge();
            }
        }
        card.setStateColumn(null);
        return card.merge();
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {

        return AcceptAll.get();
    }
}