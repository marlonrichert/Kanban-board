package org.vaadin.kanban;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.Application;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class KanbanBoard extends CustomComponent {
    private static final Map<KanbanBoard, ?> ALL_BOARDS = new WeakHashMap<KanbanBoard, Object>();

    private final GridLayout grid;
    private final BoardModel model;
    private final ICEPush pusher = new ICEPush();

    public KanbanBoard(BoardModel model) {
        this.model = model;
        grid = new GridLayout();
        ALL_BOARDS.put(this, null);

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
            List<CardModel> cards = column.getCards();

            KanbanColumnHeader header = new KanbanColumnHeader(this, column,
                    cards.size());

            KanbanColumn columnView = new KanbanColumn(this, column);
            for (CardModel card : cards) {
                columnView.addComponent(new KanbanCard(this, card));
            }

            int row = 0;
            grid.addComponent(header, i, row++);

            if (i == 0) {
                columnView.addComponent(new KanbanCard(this, column));
                grid.addComponent(columnView, i, row++, i, row++);
            } else if (i == size - 1) {
                grid.addComponent(columnView, i, row++, i, row++);
            } else {
                grid.addComponent(columnView, i, row++);

                KanbanColumnDod dod = new KanbanColumnDod(column);

                grid.addComponent(dod, i, row++);
            }
            grid.setColumnExpandRatio(i, 1);
        }
    }

    public BoardModel getModel() {
        return model;
    }

    public void sync() {
        for (KanbanBoard board : ALL_BOARDS.keySet()) {
            Application application = board.getApplication();

            if (application == null) {

                System.out.println("application == null");

                board.refresh();
                continue;
            }
            synchronized (application) {
                board.refresh();
                board.pusher.push();
            }
        }
    }
}
