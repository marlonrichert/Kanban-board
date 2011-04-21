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
    KanbanBoard board;
    CardModel model;

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
        final Window dialog = new Window("Edit card", layout);
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

        form.addCancelActionListener(new CancelHandler(dialog));

        form.addSaveActionListener(new SaveHandler(this, dialog, form));

        form.setDeleteAllowed(false);
        form.addDeleteActionListener(new ClickHandler(this, dialog));
    }

    private static final class CancelHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final Window dialog;

        CancelHandler(Window dialog) {
            this.dialog = dialog;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            dialog.getParent().removeWindow(dialog);
        }
    }

    private static final class ClickHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final KanbanCard card;
        private final Window dialog;

        ClickHandler(KanbanCard card, Window dialog) {
            this.card = card;
            this.dialog = dialog;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            dialog.getParent().removeWindow(dialog);
            card.model.remove();
            card.board.sync();
        }
    }

    private static final class SaveHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final KanbanCard card;
        private final Window dialog;
        private final EntityEditor form;

        SaveHandler(KanbanCard card, Window dialog, EntityEditor form) {
            this.card = card;
            this.dialog = dialog;
            this.form = form;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            dialog.getParent().removeWindow(dialog);
            form.commit();
            card.model = card.model.merge();
            card.removeStyleName("new");
            card.board.sync();
        }
    }
}
