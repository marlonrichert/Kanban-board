package org.vaadin.kanban;

import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class KanbanColumnDod extends Label {
    public KanbanColumnDod(ColumnModel model) {
        super("<h3>Definition of done</h3>" + model.getDefinitionOfDone(),
                Label.CONTENT_XHTML);
        setSizeUndefined();
        setWidth(100, UNITS_PERCENTAGE);
        setStyleName("dod");
    }
}
