package org.vaadin.kanban;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class KanbanColumnHeader extends VerticalLayout {
    final ColumnModel model;
    KanbanBoard board;

    public KanbanColumnHeader(KanbanBoard board, ColumnModel model, int wip) {
        this.board = board;
        this.model = model;

        Label nameLabel = new Label("<h2>" + model.getName() + "</h2>",
                Label.CONTENT_XHTML);
        nameLabel.setStyleName("header");
        nameLabel.setSizeUndefined();
        nameLabel.setWidth(100, UNITS_PERCENTAGE);

        int wipLimit = model.getWorkInProgressLimit();
        Label wipLimitLabel = new Label(wipLimit > 0 ? "" + wipLimit : "",
                Label.CONTENT_XHTML);
        wipLimitLabel.setStyleName("wip");
        wipLimitLabel.setSizeUndefined();
        wipLimitLabel.setWidth(100, UNITS_PERCENTAGE);
        if (wip == wipLimit) {
            wipLimitLabel.addStyleName("maximum");
        } else if (wip > wipLimit) {
            wipLimitLabel.addStyleName("overflow");
        }

        addComponent(nameLabel);
        addComponent(wipLimitLabel);
        setSizeUndefined();
        setWidth(100, UNITS_PERCENTAGE);

        addListener(new LayoutClickListener() {

            @Override
            public void layoutClick(LayoutClickEvent event) {
                VerticalLayout layout = new VerticalLayout();
                Window dialog = new Window("Edit column", layout);
                dialog.setModal(true);
                dialog.setResizable(false);

                layout.setMargin(false);
                layout.setSpacing(false);
                layout.setSizeUndefined();

                final EntityEditor form = KanbanColumnHeader.this.model
                        .getEditor();
                form.setSizeUndefined();
                layout.addComponent(form);
                layout.setExpandRatio(form, 1.0f);

                Window browserWindow = getWindow();
                while (browserWindow.getParent() != null) {
                    browserWindow = browserWindow.getParent();
                }
                browserWindow.addWindow(dialog);

                form.addCancelActionListener(new CancelHandler(dialog));

                form.addSaveActionListener(new SaveHandler(
                        KanbanColumnHeader.this, dialog, form));

                form.setDeleteAllowed(false);
                form.addDeleteActionListener(new DeleteHandler(
                        KanbanColumnHeader.this, dialog));
            }
        });
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

    private static final class DeleteHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final KanbanColumnHeader column;
        private final Window dialog;

        DeleteHandler(KanbanColumnHeader kanbanColumnHeader, Window dialog) {
            column = kanbanColumnHeader;
            this.dialog = dialog;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            dialog.getParent().removeWindow(dialog);
            column.model.remove();
            column.board.sync();
        }
    }

    private static final class SaveHandler implements ClickListener {
        private static final long serialVersionUID = 1L;
        private final KanbanColumnHeader column;
        private final Window dialog;
        private final EntityEditor form;

        SaveHandler(KanbanColumnHeader kanbanColumnHeader, Window dialog,
                EntityEditor form) {
            column = kanbanColumnHeader;
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
            column.model.merge();
            column.board.sync();
            dialog.getParent().removeWindow(dialog);
        }
    }
}
