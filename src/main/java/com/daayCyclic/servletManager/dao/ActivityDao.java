package com.daayCyclic.servletManager.dao;

import com.daayCyclic.servletManager.dto.ActivityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.*;
import java.util.*;
import javax.persistence.*;

@Entity(name = "activity")
@Getter
@Setter
@NoArgsConstructor

public class ActivityDao implements ObjectDao{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String description;

    private Integer estimatedTime;

    private boolean isInterruptable;

    private Integer week;

    @OneToOne
    @MapsId
    private ProcedureDao procedure;

    @OneToOne
    @MapsId
    private UserDao maintainer;

    @UpdateTimestamp
    private LocalDateTime lastMod;

    @CreationTimestamp
    private  LocalDateTime timestamp;

    public ActivityDao(Integer id, String description, Integer estimatedTime, boolean isInterruptable, Integer week, ProcedureDao procedure, UserDao maintainer) {
        this.id = id;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.isInterruptable = isInterruptable;
        this.week = week;
        this.procedure = procedure;
        this.maintainer = maintainer;
    }

    //TODO : equals maintainerId and procedureId
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ActivityDao)
                && id.equals(((ActivityDao) obj).id)
                && (Objects.equals(estimatedTime, ((ActivityDao) obj).estimatedTime))
                && (Objects.equals(week, ((ActivityDao) obj).week))
                && (Objects.equals(description, ((ActivityDao) obj).description));
    }


}
