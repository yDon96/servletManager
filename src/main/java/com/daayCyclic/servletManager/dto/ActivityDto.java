package com.daayCyclic.servletManager.dto;

import com.daayCyclic.servletManager.dao.UserDao;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto implements ObjectDto {

    @NonNull
    private java.lang.Integer id;

    private Integer maintainerId;

    private Integer procedureId;

    private java.lang.Integer week;

    private boolean isInterruptable;

    private java.lang.Integer estimatedTime;

    private String description;

    public ActivityDto(@NonNull java.lang.Integer id, Integer maintainerId, Integer procedureId, java.lang.Integer week, boolean isInterruptable, java.lang.Integer estimatedTime, String description) {
        this.id = id;
        this.maintainerId = maintainerId;
        this.procedureId = procedureId;
        this.week = week;
        this.isInterruptable = isInterruptable;
        this.estimatedTime = estimatedTime;
        this.description = description;
    }

}
