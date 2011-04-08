// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vaadin.kanban.web;

import java.lang.Class;
import java.lang.String;
import java.util.Map;
import java.util.TreeMap;
import org.vaadin.kanban.web.ui.CardView;
import org.vaadin.kanban.web.ui.StateColumnView;

privileged aspect KanbanEntityManagerView_Roo_VaadinEntityManagerView {
    
    public Map<String, Class> KanbanEntityManagerView.listEntityViews() {
        Map<String, Class> result = new TreeMap<String, Class>();
        result.put("State Column",StateColumnView.class);
        result.put("Card",CardView.class);
        return result;
    }
    
}
