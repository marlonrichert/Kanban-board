package org.vaadin.kanban;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class KanbanBoard extends CustomComponent {

    private static final Map<KanbanBoard, ?> allBoards = new WeakHashMap<KanbanBoard, Object>();
    private GridLayout grid;
    private BoardModel model;
    private ICEPush pusher;

    public KanbanBoard(BoardModel model) {
        this.model = model;
        allBoards.put(this, null);

        grid = new GridLayout();
        grid.setStyleName("board");
        grid.setImmediate(true);

        // board should use all available space
        grid.setMargin(false);
        grid.setSizeFull();

        grid.setSpacing(true);

        VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(false);
        root.addComponent(grid);
        root.setExpandRatio(grid, 1);
        pusher = new ICEPush();
        root.addComponent(pusher);
        setCompositionRoot(root);
        addStyleName("no-horizontal-drag-hints");
        setSizeFull();
        refresh();
    }

    private void refresh() {
        if (model == null || grid == null) {
            return;
        }
        List<? extends ColumnModel> columns = model.getColumns();
        int size = columns.size();

        grid.removeAllComponents();
        if (size < 1) {
            return;
        }
        grid.setColumns(size);
        grid.setRows(3);
        grid.setRowExpandRatio(0, 0);
        grid.setRowExpandRatio(1, 2);
        grid.setRowExpandRatio(2, 0);
        for (int i = 0; i < size; i++) {
            ColumnModel column = columns.get(i);
            Label name = new Label("<h2>" + column.getName() + "</h2>",
                    Label.CONTENT_XHTML);
            name.setStyleName("header");
            name.setSizeUndefined();
            name.setWidth(100, UNITS_PERCENTAGE);

            int wipLimit = column.getWorkInProgressLimit();
            Label wip = new Label("" + (wipLimit > 0 ? wipLimit : ""),
                    Label.CONTENT_XHTML);
            wip.setStyleName("wip");
            wip.setSizeUndefined();
            wip.setWidth(100, UNITS_PERCENTAGE);

            VerticalLayout header = new VerticalLayout();
            header.setSizeUndefined();
            header.setWidth(100, UNITS_PERCENTAGE);
            header.addComponent(name);
            header.addComponent(wip);

            KanbanColumn columnView = new KanbanColumn(this, column);
            for (CardModel card : column.getCards()) {
                columnView.addComponent(new KanbanCard(this, card));
            }

            int row = 0;
            grid.addComponent(header, i, row++);

            if (i == 0) {
                CardModel cardModel = model.newCard("New card");
                cardModel.setColumn(column);
                KanbanCard newCard = new KanbanCard(this, cardModel);
                newCard.addStyleName("new");
                newCard.setDragStartMode(DragStartMode.NONE);
                columnView.addComponent(newCard);
                grid.addComponent(columnView, i, row++, i, row++);
            } else if (i == size - 1) {
                grid.addComponent(columnView, i, row++, i, row++);
            } else {
                grid.addComponent(columnView, i, row++);

                Label dod = new Label("<h3>Definition of done</h3>"
                        + column.getDefinitionOfDone(), Label.CONTENT_XHTML);
                dod.setStyleName("dod");
                dod.setSizeUndefined();
                dod.setWidth(100, UNITS_PERCENTAGE);

                grid.addComponent(dod, i, row++);
            }
            grid.setColumnExpandRatio(i, 1);
        }
    }

    public BoardModel getModel() {
        return model;
    }

    public void sync() {
        for (KanbanBoard board : allBoards.keySet()) {
            synchronized (board.getApplication()) {
                board.refresh();
                board.pusher.push();
            }
        }
    }
}
