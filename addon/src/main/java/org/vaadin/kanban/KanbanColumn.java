package org.vaadin.kanban;

import java.util.Date;

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
    private ColumnModel model;

    public KanbanColumn(KanbanBoard board, ColumnModel model) {
        super(new VerticalLayout());
        root = (VerticalLayout) getCompositionRoot();
        root.setSizeUndefined();
        root.setWidth(100, UNITS_PERCENTAGE);

        root.setMargin(false);
        root.setSpacing(false);

        this.board = board;
        this.model = model;
        setStyleName("column");
        addStyleName("no-box-drag-hints");
        setSizeFull();
        setDropHandler(this);
    }

    @Override
    public void addComponent(Component c) {
        if (c instanceof KanbanCard) {
            KanbanCard card = (KanbanCard) c;
            root.addComponent(card);
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
            CardModel sourceCard = ((KanbanCard) sourceComponent).getModel();
            ColumnModel sourceColumn = sourceCard.getColumn();

            if (target == sourceComponent) {
                return;
            }
            if (sourceColumn.getSortOrder() == 0 && model.getSortOrder() != 0) {
                sourceCard.setStartDate(new Date());
            }
            if (sourceColumn != model
                    && model.getSortOrder() == board.getModel().getColumns()
                            .size() - 1) {
                sourceCard.setEndDate(new Date());
            }
            sourceCard = sourceColumn.remove(sourceCard);
            if (target instanceof KanbanCard) {
                final int index = ((KanbanCard) target).getModel()
                        .getSortOrder();

                model.insert(sourceCard, (details.verticalDropLocation()
                        .equals(VerticalDropLocation.BOTTOM) ? index + 1
                        : index));
            } else {
                if (details.verticalDropLocation().equals(
                        VerticalDropLocation.TOP)) {
                    model.insert(sourceCard, 0);
                } else {
                    model.append(sourceCard);
                }
            }
            board.sync();
        }
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }
}