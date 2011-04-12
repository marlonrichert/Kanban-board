package org.vaadin.kanban.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.client.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class DragDropRearrangeComponentsExample extends VerticalLayout {

    public DragDropRearrangeComponentsExample() {
        SortableLayout layout = new SortableLayout(true);
        layout.setSizeUndefined();
        layout.setHeight("100px");

        // Use these styles to hide irrelevant drag hints
        layout.addStyleName("no-vertical-drag-hints");
        // layout.addStyleName("no-horizontal-drag-hints");
        // layout.addStyleName("no-box-drag-hints");

        for (Component component : createComponents()) {
            layout.addComponent(component);
        }

        addComponent(layout);
    }

    private List<Component> createComponents() {
        List<Component> components = new ArrayList<Component>();

        Label label = new Label("This is a long text block that will wrap.");
        label.setWidth("120px");
        components.add(label);

        Embedded image = new Embedded("", new ThemeResource(
                "../runo/icons/64/document.png"));
        components.add(image);

        CssLayout documentLayout = new CssLayout();
        documentLayout.setWidth("19px");
        for (int i = 0; i < 5; ++i) {
            Embedded e = new Embedded(null, new ThemeResource(
                    "../runo/icons/16/document.png"));
            e.setHeight("16px");
            e.setWidth("16px");
            documentLayout.addComponent(e);
        }
        components.add(documentLayout);

        VerticalLayout buttonLayout = new VerticalLayout();
        Button button = new Button("Button");
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getWindow().showNotification("Button clicked");
            }
        });
        buttonLayout.addComponent(button);
        buttonLayout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
        components.add(buttonLayout);

        return components;
    }

    private static class SortableLayout extends CustomComponent {
        private final AbstractOrderedLayout layout;
        private final boolean horizontal;
        private final DropHandler dropHandler;

        public SortableLayout(boolean horizontal) {
            this.horizontal = horizontal;
            if (horizontal) {
                layout = new HorizontalLayout();
            } else {
                layout = new VerticalLayout();
            }
            dropHandler = new ReorderLayoutDropHandler(layout);

            DragAndDropWrapper pane = new DragAndDropWrapper(layout);
            setCompositionRoot(pane);
        }

        @Override
        public void addComponent(Component component) {
            WrappedComponent wrapper = new WrappedComponent(component,
                    dropHandler);
            wrapper.setSizeUndefined();
            if (horizontal) {
                component.setHeight("100%");
                wrapper.setHeight("100%");
            } else {
                component.setWidth("100%");
                wrapper.setWidth("100%");
            }
            layout.addComponent(wrapper);
        }
    }

    private static class WrappedComponent extends DragAndDropWrapper {

        private final DropHandler dropHandler;

        public WrappedComponent(Component content, DropHandler dropHandler) {
            super(content);
            this.dropHandler = dropHandler;
            setDragStartMode(DragStartMode.WRAPPER);
        }

        @Override
        public DropHandler getDropHandler() {
            return dropHandler;
        }

    }

    private static class ReorderLayoutDropHandler implements DropHandler {

        private AbstractOrderedLayout layout;

        public ReorderLayoutDropHandler(AbstractOrderedLayout layout) {
            this.layout = layout;
        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return new Not(SourceIsTarget.get());
        }

        @Override
        public void drop(DragAndDropEvent dropEvent) {
            Transferable transferable = dropEvent.getTransferable();
            Component sourceComponent = transferable.getSourceComponent();
            if (sourceComponent instanceof WrappedComponent) {
                TargetDetails dropTargetData = dropEvent.getTargetDetails();
                DropTarget target = dropTargetData.getTarget();

                // find the location where to move the dragged component
                boolean sourceWasAfterTarget = true;
                int index = 0;
                Iterator<Component> componentIterator = layout
                        .getComponentIterator();
                Component next = null;
                while (next != target && componentIterator.hasNext()) {
                    next = componentIterator.next();
                    if (next != sourceComponent) {
                        index++;
                    } else {
                        sourceWasAfterTarget = false;
                    }
                }
                if (next == null || next != target) {
                    // component not found - if dragging from another layout
                    return;
                }

                // drop on top of target?
                if (dropTargetData.getData("horizontalLocation").equals(
                        HorizontalDropLocation.CENTER.toString())) {
                    if (sourceWasAfterTarget) {
                        index--;
                    }
                }

                // drop before the target?
                else if (dropTargetData.getData("horizontalLocation").equals(
                        HorizontalDropLocation.LEFT.toString())) {
                    index--;
                    if (index < 0) {
                        index = 0;
                    }
                }

                // move component within the layout
                layout.removeComponent(sourceComponent);
                layout.addComponent(sourceComponent, index);
            }
        }
    };

}
