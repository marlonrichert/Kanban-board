package org.vaadin.kanban;

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

    private KanbanBoard parent;
    private VerticalLayout root;
    private ColumnModel model;

    public KanbanColumn(KanbanBoard parent, ColumnModel model) {
        super(new VerticalLayout());
        root = (VerticalLayout) getCompositionRoot();
        root.setSizeUndefined();
        root.setWidth(100, UNITS_PERCENTAGE);

        root.setMargin(true);
        root.setSpacing(true);

        this.parent = parent;
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
            CardModel sourceCard = ((KanbanCard) sourceComponent).getModel();
            ColumnModel sourceColumn = sourceCard.getColumn();

            if (target == sourceComponent) {
                return;
            }

            sourceCard = remove(sourceCard, sourceColumn);
            if (target instanceof KanbanCard) {
                final int index = ((KanbanCard) target).getModel()
                        .getSortOrder();

                insert(sourceCard,
                        details.getVerticalDropLocation().equals(
                                VerticalDropLocation.BOTTOM) ? index + 1
                                : index);
            } else {
                if (details.getVerticalDropLocation().equals(
                        VerticalDropLocation.TOP)) {
                    insert(sourceCard, 0);
                } else {
                    append(sourceCard);
                }
            }
            parent.refresh();
        }
    }

    private CardModel append(CardModel card) {
        return model.append(card);
    }

    private CardModel insert(CardModel card, int index) {
        return model.insert(card, index);
    }

    private CardModel remove(CardModel card, ColumnModel column) {
        return column.remove(card);
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }
}