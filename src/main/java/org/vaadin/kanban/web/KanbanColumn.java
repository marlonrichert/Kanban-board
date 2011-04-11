package org.vaadin.kanban.web;

import org.vaadin.kanban.domain.Card;
import org.vaadin.kanban.domain.StateColumn;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class KanbanColumn extends DragAndDropWrapper implements DropHandler {

    private StateColumn model;
    private Layout compositionRoot;
    private KanbanBoard board;

    public KanbanColumn(KanbanBoard board, StateColumn model) {
        super(new VerticalLayout());
        compositionRoot = (Layout) getCompositionRoot();
        compositionRoot.setSizeFull();
        this.board = board;
        this.model = model;
        setStyleName("column");
        setSizeFull();
        setDropHandler(this);
    }

    @Override
    public void addComponent(Component c) {
        compositionRoot.addComponent(c);
    }

    @Override
    public void drop(DragAndDropEvent event) {
        Component sourceComponent = event.getTransferable()
                .getSourceComponent();
        if (sourceComponent instanceof KanbanCard) {
            KanbanCard card = (KanbanCard) sourceComponent;
            Card cardModel = card.getModel();
            cardModel.setStateColumn(model);
            cardModel.merge();
            board.update();
        }
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }
}
