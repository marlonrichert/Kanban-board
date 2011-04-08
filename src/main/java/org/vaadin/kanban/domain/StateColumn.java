package org.vaadin.kanban.domain;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class StateColumn {

    @NotNull
    @Column(unique = true)
    @Size(min = 2)
    private String name;

    @Min(0L)
    private int workInProgressLimit;

    @NotNull
    private String definitionOfDone;

    @NotNull
    @Column(unique = true)
    @Min(0L)
    private int sortOrder;
}
