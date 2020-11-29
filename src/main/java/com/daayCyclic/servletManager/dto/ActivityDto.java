package com.daayCyclic.servletManager.dto;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto implements ObjectDto {

    @NonNull
    private Integer id;

    private Integer maintainerId;

    private Integer procedureId;

    private Integer week;

    private boolean isInterruptable;

    private Integer estimatedTime;

    private String description;

    public ActivityDto() {
    }

    public ActivityDto(@NonNull Integer id, Integer maintainerId, Integer procedureId, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        this.id = id;
        this.maintainerId = maintainerId;
        this.procedureId = procedureId;
        this.week = week;
        this.isInterruptable = isInterruptable;
        this.estimatedTime = estimatedTime;
        this.description = description;
    }



    //TODO : equals maintainerId and procedureId
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ActivityDto)
                && id.equals(((ActivityDto) obj).id)
                && (Objects.equals(estimatedTime, ((ActivityDto) obj).estimatedTime))
                && (Objects.equals(week, ((ActivityDto) obj).week))
                && (Objects.equals(description, ((ActivityDto) obj).description));
    }

    public boolean isAllNull() {
        return id == null && maintainerId == null && procedureId == null && week == null && estimatedTime == null && description == null;
    }
    public boolean isAlLFieldsNegative() {
        return id < 0 || maintainerId < 0 || procedureId < 0 || week < 0 || estimatedTime < 0;
    }
}
