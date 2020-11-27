package com.daayCyclic.servletManager.dto;

import com.daayCyclic.servletManager.dao.UserDao;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto implements ObjectDto {

    @NonNull
    private Integer id;

    private UserDao maintainer;

    private ProcedureDto procedure;

    private Integer week;

    private boolean isInterruptable;

    private Integer estimatedTime;

    private String description;

    public ActivityDto(@NonNull Integer id, UserDao maintainer, ProcedureDto procedure, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        this.id = id;
        this.maintainer = maintainer;
        this.procedure = procedure;
        this.week = week;
        this.isInterruptable = isInterruptable;
        this.estimatedTime = estimatedTime;
        this.description = description;
    }

}
