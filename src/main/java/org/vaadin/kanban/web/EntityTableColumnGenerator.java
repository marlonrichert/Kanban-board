package org.vaadin.kanban.web;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Property;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Column generator to use in a Vaadin {@link Table} component for
 * pretty-printing entities.
 * 
 * Uses the given entity property for pretty-printing individual entities.
 * Collections of entities are displayed as a comma separated list.
 */
public class EntityTableColumnGenerator implements ColumnGenerator {

    private final String displayProperty;

    public EntityTableColumnGenerator(String displayProperty) {
        this.displayProperty = displayProperty;
    }

    @Override
    public Component generateCell(Table source, Object itemId, Object columnId) {
        Property property = source.getContainerProperty(itemId, columnId);
        if (property == null) {
            return null;
        }
        Object cellContent = property.getValue();
        if (cellContent instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            Iterator<?> it = ((Collection<?>) cellContent).iterator();
            while (it.hasNext()) {
                if (displayProperty != null) {
                    Object value = new MethodProperty(it.next(),
                            displayProperty).getValue();
                    sb.append(value);
                } else {
                    sb.append(it.next());
                }
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            return new Label(sb.toString());
        } else if (cellContent != null) {
            Object value = cellContent;
            if (displayProperty != null) {
                value = new MethodProperty(cellContent, displayProperty)
                        .getValue();
            }
            return new Label(value != null ? value.toString() : "");
        } else {
            return null;
        }
    }

}