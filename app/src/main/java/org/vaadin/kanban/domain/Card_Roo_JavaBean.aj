// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vaadin.kanban.domain;

import java.lang.String;
import java.util.Date;
import org.vaadin.kanban.domain.StateColumn;

privileged aspect Card_Roo_JavaBean {
    
    public String Card.getDescription() {
        return this.description;
    }
    
    public void Card.setDescription(String description) {
        this.description = description;
    }
    
    public StateColumn Card.getStateColumn() {
        return this.stateColumn;
    }
    
    public void Card.setStateColumn(StateColumn stateColumn) {
        this.stateColumn = stateColumn;
    }
    
    public int Card.getSortOrder() {
        return this.sortOrder;
    }
    
    public void Card.setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String Card.getOwner() {
        return this.owner;
    }
    
    public void Card.setOwner(String owner) {
        this.owner = owner;
    }
    
    public Date Card.getStartDate() {
        return this.startDate;
    }
    
    public void Card.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date Card.getEndDate() {
        return this.endDate;
    }
    
    public void Card.setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}