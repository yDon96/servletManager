package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @ManyToOne
    @JoinColumn(columnDefinition = "id")
    private ProcedureDao procedure;

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id")
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


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ActivityDao)
                && id.equals(((ActivityDao) obj).id)
                && procedure.equals(((ActivityDao) obj).procedure)
                && maintainer.equals(((ActivityDao) obj).maintainer)
                && (Objects.equals(estimatedTime, ((ActivityDao) obj).estimatedTime))
                && (Objects.equals(week, ((ActivityDao) obj).week))
                && (Objects.equals(description, ((ActivityDao) obj).description));
    }


}
