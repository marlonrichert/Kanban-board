package org.vaadin.kanban.web;

import org.vaadin.kanban.domain.StateColumn;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class KanbanColumn extends DragAndDropWrapper implements DropHandler {

    private StateColumn column;
    private Layout root;

    public KanbanColumn(StateColumn column) {
        super(new VerticalLayout());
        this.column = column;
        root = (Layout) getCompositionRoot();
        root.setSizeFull();
        setStyleName("column");
        setSizeFull();
    }

    @Override
    public void addComponent(Component c) {
        root.addComponent(c);
    }

    @Override
    public void drop(DragAndDropEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        // TODO Auto-generated method stub
        return null;
    }

}
