package org.vaadin.kanban.web;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class KanbanWindow extends Window {

    public KanbanWindow() {
        super("Kanban board");

        // entity manager
        KanbanEntityManagerView entityManagerView = new KanbanEntityManagerView();
        setContent(entityManagerView);

        // select window theme
        setTheme("kanban");
    }
}
