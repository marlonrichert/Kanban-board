// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vaadin.kanban.web.ui;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Table;
import java.lang.Class;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.util.List;
import org.vaadin.kanban.domain.Card;
import org.vaadin.kanban.web.EntityTableColumnGenerator;

privileged aspect CardView_Roo_VaadinEntityView {
    
    public String CardView.getEntityName() {
        return "Card";
    }
    
    public Class<Card> CardView.getEntityClass() {
        return Card.class;
    }
    
    public boolean CardView.isCreateAllowed() {
        return true;
    }
    
    public boolean CardView.isUpdateAllowed() {
        return true;
    }
    
    public boolean CardView.isDeleteAllowed() {
        return true;
    }
    
    public List<Card> CardView.listEntities() {
        List<Card> list = Card.findAllCards();
        return list;
    }
    
    public Card CardView.saveEntity(Card entity) {
        if (entity == null) {
            return null;
        }
        return (Card) entity.merge();
    }
    
    public void CardView.deleteEntity(Card entity) {
        if (entity != null) {
            entity.remove();
        }
    }
    
    public boolean CardView.isNewEntity(Card entity) {
        return (entity != null && getIdForEntity(entity) == null);
    }
    
    public String CardView.getIdProperty() {
        return "id";
    }
    
    public String CardView.getVersionProperty() {
        return "version";
    }
    
    public Card CardView.createEntityInstance() {
        return new Card();
    }
    
    public BeanContainer<Long, Card> CardView.getTableContainer() {
        BeanContainer<Long, Card> container = new BeanContainer<Long, Card>(Card.class);
        container.setBeanIdProperty("id");
        for (Card entity : Card.findAllCards()) {
            container.addBean(entity);
        }
        return container;
    }
    
    public Item CardView.getItemForEntity(Card entity) {
        Item item = getTable().getItem(entity.getId());
        if (item == null) {
            item = new BeanItem<Card>(entity);
        }
        return item;
    }
    
    public Card CardView.getEntityForItem(Item item) {
        if (item != null) {
            return ((BeanItem<Card>) item).getBean();
        } else {
            return null;
        }
    }
    
    public Object CardView.getIdForEntity(Card entity) {
        return entity != null ? entity.getId() : null;
    }
    
    public void CardView.setupGeneratedColumns(Table table) {
        table.removeGeneratedColumn("stateColumn");
        table.addGeneratedColumn("stateColumn", new EntityTableColumnGenerator((String) getStateColumnCaptionPropertyId()));
    }
    
    public Object CardView.getStateColumnCaptionPropertyId() {
        return "name";
    }
    
}
