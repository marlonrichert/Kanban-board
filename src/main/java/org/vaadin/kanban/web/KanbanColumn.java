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
    private VerticalLayout compositionRoot;
    private StateColumn model;

    public KanbanColumn(KanbanBoard board, StateColumn model) {
        super(new VerticalLayout());
        compositionRoot = (VerticalLayout) getCompositionRoot();
        compositionRoot.setSizeUndefined();
        compositionRoot.setWidth(100, UNITS_PERCENTAGE);
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
            while (index < compositionRoot.getComponentCount()) {
                if (sortOrder < ((KanbanCard) compositionRoot
                        .getComponent(index)).getModel().getSortOrder()) {
                    break;
                }
                index++;
            }
            compositionRoot.addComponent(card, index);
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

            // FIXME: first remove from old column, then insert into new

            if (target == this) {

                System.out.println("target: " + model.getName());

                int size = Card.findCardsByStateColumn(model).getResultList()
                        .size();
                if (sourceCard.getStateColumn().equals(model)) {
                    int index = size - 1;
                    int oldSpot = sourceCard.getSortOrder();
                    sourceCard.setSortOrder(index--);
                    while (index > oldSpot) {
                        Card otherCard = ((KanbanCard) compositionRoot
                                .getComponent(index)).getModel();
                        otherCard.setSortOrder(index--);
                        otherCard.merge();
                    }
                } else {
                    sourceCard.setSortOrder(size);
                }

                System.out.println("index: " + sourceCard.getSortOrder());

            } else if (target instanceof KanbanCard) {

                System.out.println("target: "
                        + ((KanbanCard) target).getModel().getDescription());
                System.out.println("dropLocation: "
                        + details.verticalDropLocation());

                if (target == sourceComponent) {
                    return;
                }
                int index = ((KanbanCard) target).getModel().getSortOrder();

                if (details.verticalDropLocation().equals(
                        VerticalDropLocation.BOTTOM)) {
                    index++;
                }

                System.out.println("index: " + index);

                sourceCard.setSortOrder(index);
                while (index < compositionRoot.getComponentCount()) {
                    Card otherCard = ((KanbanCard) compositionRoot
                            .getComponent(index)).getModel();
                    otherCard.setSortOrder(++index);
                    otherCard.merge();
                }
            }
            sourceCard.setStateColumn(model);
            sourceCard = sourceCard.merge();
            sourceCard.flush();
            board.update();
        }
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }
}
