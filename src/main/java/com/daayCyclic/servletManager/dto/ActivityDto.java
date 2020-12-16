package com.daayCyclic.servletManager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto implements ObjectDto {

    private Integer id;
    private Integer maintainerId;
    private Integer procedureId;
    private Integer week;
    private Integer startingDay;
    private Integer startingHour;
    private boolean isInterruptable;
    private Integer estimatedTime;
    private String description;

    public ActivityDto() {}

    public ActivityDto(Integer id, Integer maintainerId, Integer procedureId, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        this.id = id;
        this.maintainerId = maintainerId;
        this.procedureId = procedureId;
        this.week = week;
        this.isInterruptable = isInterruptable;
        this.estimatedTime = estimatedTime;
        this.description = description;
    }

    public ActivityDto(Integer id, Integer maintainerId, Integer procedureId, Integer week, Integer startingDay, Integer startingHour, boolean isInterruptable, Integer estimatedTime, String description) {
        this.id = id;
        this.maintainerId = maintainerId;
        this.procedureId = procedureId;
        this.week = week;
        this.startingDay = startingDay;
        this.startingHour = startingHour;
        this.isInterruptable = isInterruptable;
        this.estimatedTime = estimatedTime;
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ActivityDto)
                && id.equals(((ActivityDto) obj).id)
                && (Objects.equals(procedureId, ((ActivityDto) obj).procedureId))
                && (Objects.equals(maintainerId, ((ActivityDto) obj).maintainerId))
                && (Objects.equals(estimatedTime, ((ActivityDto) obj).estimatedTime))
                && (Objects.equals(week, ((ActivityDto) obj).week))
                && (Objects.equals(description, ((ActivityDto) obj).description));
    }
}
