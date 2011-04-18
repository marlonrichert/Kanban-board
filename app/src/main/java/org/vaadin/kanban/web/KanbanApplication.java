package org.vaadin.kanban.web;

import org.vaadin.kanban.domain.Card;
import org.vaadin.kanban.domain.StateColumn;

import com.vaadin.Application;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class KanbanApplication extends Application {

    @Override
    public void init() {
        if (StateColumn.findAllStateColumns().size() == 0) {
            int index = 0;

            StateColumn backlog = new StateColumn();
            backlog.setSortOrder(index++);
            backlog.setName("Backlog");
            backlog = backlog.merge();

            if (Card.findAllCards().size() == 0) {
                Card kanban = new Card();
                kanban.setDescription("As a developer, I want a kanban board, so that I can be lean.");
                kanban.setStateColumn(backlog);
                kanban.setSortOrder(0);
                kanban.persist();

                Card scrum = new Card();
                scrum.setDescription("As a developer, I want a scrum task board, so that I can be agile.");
                scrum.setStateColumn(backlog);
                scrum.setSortOrder(1);
                scrum.persist();
            }

            StateColumn todo = new StateColumn();
            todo.setSortOrder(index++);
            todo.setName("To Do");
            todo.setWorkInProgressLimit(2);
            todo.persist();

            StateColumn analysis = new StateColumn();
            analysis.setSortOrder(index++);
            analysis.setName("Analysis");
            analysis.setWorkInProgressLimit(3);
            analysis.setDefinitionOfDone("<ul>" + "<li>Goal is clear</li>"
                    + "<li>First tasks defined</li>"
                    + "<li>Story split (if necessary)</li>" + "</ul>");
            analysis.persist();

            StateColumn development = new StateColumn();
            development.setSortOrder(index++);
            development.setName("Development");
            development.setWorkInProgressLimit(3);
            development.setDefinitionOfDone("<ul>"
                    + "<li>Code clean & checked in on trunk</li>"
                    + "<li>Integrated & regression tested</li>"
                    + "<li>Running on User Acceptance Testing environment</li>"
                    + "</ul>");
            development.persist();

            StateColumn acceptance = new StateColumn();
            acceptance.setSortOrder(index++);
            acceptance.setName("Acceptance");
            acceptance.setWorkInProgressLimit(2);
            acceptance.setDefinitionOfDone("<ul>"
                    + "<li>Customer-accepted</li>"
                    + "<li>Ready for production</li>" + "</ul>");
            acceptance.persist();

            StateColumn done = new StateColumn();
            done.setSortOrder(index++);
            done.setName("Done");
            done.persist();
        }

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
