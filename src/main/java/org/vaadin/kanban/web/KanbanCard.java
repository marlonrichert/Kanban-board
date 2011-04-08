package org.vaadin.kanban.web;

import org.vaadin.kanban.domain.Card;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class KanbanCard extends DragAndDropWrapper implements
        LayoutClickListener {

    public KanbanCard(Card card) {
        super(new VerticalLayout());
        setDragStartMode(DragStartMode.WRAPPER);

        VerticalLayout root = (VerticalLayout) getCompositionRoot();
        root.setSizeFull();
        root.addComponent(new Label(card.getDescription()));
    }

    @Override
    public void layoutClick(LayoutClickEvent event) {
        // TODO Auto-generated method stub

    }

}
