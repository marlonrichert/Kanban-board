package org.vaadin.kanban.web;

import org.vaadin.kanban.KanbanBoard;
import org.vaadin.kanban.domain.Board;
import org.vaadin.navigator.Navigator;
import org.vaadin.navigator.Navigator.View;

import com.vaadin.Application;
import com.vaadin.ui.AbsoluteLayout;

@SuppressWarnings("serial")
public class BoardView extends AbsoluteLayout implements View {

    private KanbanBoard board;

    public BoardView() {
        setMargin(false);
        board = new KanbanBoard(new Board());
        addComponent(board, "top:0.0px;left:0.0px;");
    }

    @Override
    public void init(Navigator navigator, Application application) {
        // nothing to do
    }

    @Override
    public void navigateTo(String requestedDataId) {
        // nothing to do
    }

    @Override
    public String getWarningForNavigatingFrom() {
        return null;
    }

}
