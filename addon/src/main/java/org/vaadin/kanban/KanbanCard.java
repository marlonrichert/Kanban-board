package org.vaadin.kanban;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class KanbanCard extends DragAndDropWrapper implements
        LayoutClickListener {

    private CardModel model;
    private KanbanBoard board;

    public KanbanCard(KanbanBoard board, CardModel model) {
        super(new VerticalLayout());
        this.board = board;
        this.model = model;
        setDragStartMode(DragStartMode.WRAPPER);
        setStyleName("card");
        setSizeUndefined();
        setWidth(100, UNITS_PERCENTAGE);

        VerticalLayout root = (VerticalLayout) getCompositionRoot();
        Label description = new Label(model.getDescription());
        description.setStyleName("description");
        root.addComponent(description);
        Label owner = new Label(model.getOwner());
        owner.setStyleName("owner");
        root.addComponent(owner);
        root.setSizeFull();

        root.setMargin(true);
        root.setSpacing(true);

        root.addListener(this);
    }

    public CardModel getModel() {
        return model;
    }

    @Override
    public void layoutClick(LayoutClickEvent event) {
        VerticalLayout layout = new VerticalLayout();
        final Window dialog = new Window("Card", layout);
        dialog.setModal(true);
        dialog.setResizable(false);

        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setSizeUndefined();

        final EntityEditor form = model.getEditor();
        form.setSizeUndefined();
        layout.addComponent(form);
        layout.setExpandRatio(form, 1.0f);

        Window browserWindow = getWindow();
        while (browserWindow.getParent() != null) {
            browserWindow = browserWindow.getParent();
        }
        browserWindow.addWindow(dialog);

        form.addCancelActionListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                dialog.getParent().removeWindow(dialog);
            }
        });

        form.addSaveActionListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                dialog.getParent().removeWindow(dialog);
                form.commit();
                model = model.merge();
                // board.refresh();
                board.sync();
            }
        });

        form.setDeleteAllowed(false);
        form.addDeleteActionListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                dialog.getParent().removeWindow(dialog);
                model.remove();
                // board.refresh();
                board.sync();
            }
        });
    }
}
