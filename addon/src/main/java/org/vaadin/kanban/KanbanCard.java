package org.vaadin.kanban;

import java.util.Date;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class KanbanCard extends DragAndDropWrapper {
    static final String NEW_CARD = "New card";
    final KanbanBoard board;
    boolean isNew = false;
    final CardModel model;

    public KanbanCard(KanbanBoard board, CardModel model) {
        super(new VerticalLayout());
        this.board = board;
        this.model = model;
        VerticalLayout root = (VerticalLayout) getCompositionRoot();
        Label owner = createOwnerLabel();

        root.addComponent(createDateLabel());
        root.addComponent(createDescriptionLabel());
        root.addComponent(owner);
        root.setComponentAlignment(owner, Alignment.MIDDLE_RIGHT);

        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);

        setDragStartMode(DragStartMode.WRAPPER);
        setStyleName("card");
        setSizeUndefined();
        setWidth(100, UNITS_PERCENTAGE);

        root.addListener(new LayoutClickListener() {

            @Override
            public void layoutClick(LayoutClickEvent event) {
                if (isNew) {
                    KanbanCard.this.model.setDescription("");
                }
                VerticalLayout layout = new VerticalLayout();
                Window dialog = new Window(isNew ? NEW_CARD : "Edit card",
                        layout);
                dialog.setModal(true);
                dialog.setResizable(false);

                layout.setMargin(false);
                layout.setSpacing(false);
                layout.setSizeUndefined();

                final EntityEditor form = KanbanCard.this.model.getEditor();
                form.setSizeUndefined();
                layout.addComponent(form);
                layout.setExpandRatio(form, 1.0f);

                Window browserWindow = getWindow();
                while (browserWindow.getParent() != null) {
                    browserWindow = browserWindow.getParent();
                }
                browserWindow.addWindow(dialog);

                form.addCancelActionListener(new CancelHandler(KanbanCard.this,
                        dialog));

                form.addSaveActionListener(new SaveHandler(KanbanCard.this,
                        dialog, form));

                form.setDeleteAllowed(false);
                form.addDeleteActionListener(new DeleteHandler(KanbanCard.this,
                        dialog));
            }
        });
    }

    private Label createOwnerLabel() {
        String ownerString = model.getOwner();
        Label owner = new Label(ownerString);
        owner.setStyleName("owner");
        owner.setDescription("Owner");
        owner.setSizeUndefined();
        return owner;
    }

    private Label createDescriptionLabel() {
        Label description = new Label(model.getDescription());
        description.setStyleName("description");
        return description;
    }

    private Label createDateLabel() {
        Date startDate = model.getStartDate();
        Date endDate = model.getEndDate();
        String dateString = "";
        if (startDate != null) {
            dateString += String.format("%tF", startDate);
            if (endDate != null) {
                dateString += " &ndash; " + String.format("%tF", endDate);
            }
        }
        Label date = new Label(dateString, Label.CONTENT_XHTML);
        date.setStyleName("date");
        date.setDescription("Start date"
                + (endDate == null ? "" : " &ndash; end date"));
        return date;
    }

    public KanbanCard(KanbanBoard board, ColumnModel column) {
        this(board, board.getModel().newCard(NEW_CARD));
        model.setColumn(column);
        isNew = true;
        addStyleName("new");
        setDragStartMode(DragStartMode.NONE);
    }

    public CardModel getModel() {
        return model;
    }

    private static final class CancelHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final KanbanCard card;
        private final Window dialog;

        CancelHandler(KanbanCard card, Window dialog) {
            this.card = card;
            this.dialog = dialog;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            if (card.isNew) {
                card.model.setDescription(NEW_CARD);
            }
            dialog.getParent().removeWindow(dialog);
        }
    }

    private static final class DeleteHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final KanbanCard card;
        private final Window dialog;

        DeleteHandler(KanbanCard card, Window dialog) {
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
            try {
                form.commit();
            } catch (InvalidValueException e) {
                form.setCommitErrorMessage(e.getMessage());
                return;
            }
            if (card.isNew) {
                card.removeStyleName("new");
                card.isNew = false;
                card.model.getColumn().append(card.model);
            } else {
                card.model.merge();
            }
            card.board.sync();
            dialog.getParent().removeWindow(dialog);
        }
    }
}
