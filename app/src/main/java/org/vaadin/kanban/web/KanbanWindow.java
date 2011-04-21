package org.vaadin.kanban.web;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class KanbanWindow extends Window {

    public KanbanWindow(String caption) {
        super(caption);

        // entity manager
        KanbanEntityManagerView entityManagerView = new KanbanEntityManagerView();
        setContent(entityManagerView);

        // select window theme
        setTheme("kanban");
    }
}
