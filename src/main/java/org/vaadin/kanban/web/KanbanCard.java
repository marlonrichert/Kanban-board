package org.vaadin.kanban.web;

import org.vaadin.kanban.domain.Card;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class KanbanCard extends DragAndDropWrapper implements
        LayoutClickListener {

    private Card model;

    public KanbanCard(Card model) {
        super(new VerticalLayout());
        this.model = model;
        setDragStartMode(DragStartMode.WRAPPER);
        setStyleName("card");

        VerticalLayout root = (VerticalLayout) getCompositionRoot();
        root.addComponent(new Label(model.getDescription()));
        root.addListener(this);
    }

    @Override
    public void layoutClick(LayoutClickEvent event) {
        // TODO Auto-generated method stub
    }

    public Card getModel() {
        return model;
    }
}
