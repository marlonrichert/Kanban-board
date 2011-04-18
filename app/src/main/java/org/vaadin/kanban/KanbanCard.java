package org.vaadin.kanban;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class KanbanCard extends DragAndDropWrapper implements
        LayoutClickListener {

    private CardModel model;

    public KanbanCard(CardModel model) {
        super(new VerticalLayout());
        this.model = model;
        setDragStartMode(DragStartMode.WRAPPER);
        setStyleName("card");
        setSizeUndefined();
        setWidth(100, UNITS_PERCENTAGE);

        VerticalLayout root = (VerticalLayout) getCompositionRoot();
        root.addComponent(new Label(model.getDescription()));
        root.setSizeFull();

        root.setMargin(true);
        root.setSpacing(true);

        // root.addListener(this);
    }

    public CardModel getModel() {
        return model;
    }

    @Override
    public void layoutClick(LayoutClickEvent event) {
        // TODO Auto-generated method stub
    }
}
