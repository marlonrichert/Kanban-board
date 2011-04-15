package org.vaadin.kanban.domain;

import java.io.Serializable;
import java.util.Comparator;

class SortOrderComparator implements Comparator<Sortable>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Sortable o1, Sortable o2) {
        return o1.getSortOrder() - o2.getSortOrder();
    }
}
