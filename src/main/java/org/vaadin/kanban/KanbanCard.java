package org.vaadin.kanban;

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
        setSizeFull();
        setHeight(20, UNITS_EX);

        VerticalLayout root = (VerticalLayout) getCompositionRoot();
        root.addComponent(new Label(model.getDescription()));
        root.setSizeFull();

        root.setMargin(true);
        root.setSpacing(true);

        // root.addListener(this);
    }

    public Card getModel() {
        return model;
    }

    @Override
    public void layoutClick(LayoutClickEvent event) {
        // TODO Auto-generated method stub
    }
}
