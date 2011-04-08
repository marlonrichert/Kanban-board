package org.vaadin.kanban.web;

import com.vaadin.Application;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class KanbanApplication extends Application {

    @Override
    public void init() {
        Window window = createNewWindow();
        setMainWindow(window);
    }

    public Window createNewWindow() {
        final Window window = new KanbanWindow();

        // remove window on close to avoid memory leaks
        window.addListener(new CloseListener() {
            @Override
            public void windowClose(CloseEvent e) {
                if (getMainWindow() != window) {
                    KanbanApplication.this.removeWindow(window);
                }
            }
        });

        return window;
    }

    @Override
    public Window getWindow(String name) {
        // See if the window already exists in the application
        Window window = super.getWindow(name);

        // If a dynamically created window is requested, but
        // it does not exist yet, create it.
        if (window == null) {
            // Create the window object.
            window = createNewWindow();
            window.setName(name);

            // Add it to the application as a regular
            // application-level window
            addWindow(window);
        }

        return window;
    }

}
