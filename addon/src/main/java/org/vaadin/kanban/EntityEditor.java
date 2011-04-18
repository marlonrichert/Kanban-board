package org.vaadin.kanban;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

public interface EntityEditor extends Component {

    public void addSaveActionListener(ClickListener listener);

    public void addCancelActionListener(ClickListener listener);

    public void addDeleteActionListener(ClickListener listener);

    public void setSaveAllowed(boolean enabled);

    public void setDeleteAllowed(boolean enabled);

    public void commit();

    public void setItemDataSource(Item item);

    public Item getItemDataSource();

    public void setCommitErrorMessage(String message);

    public void focus();

    public void refresh();

    public boolean isModified();

}
