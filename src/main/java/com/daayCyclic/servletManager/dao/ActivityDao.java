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

    @Column(nullable = false)
    private Integer estimatedTime;

    private boolean isInterruptable;

    @Column(nullable = false)
    private Integer week;

    private Integer startingDay;
    private Integer startingHour;

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

    public ActivityDao(Integer id, String description, Integer estimatedTime, boolean isInterruptable, Integer week, Integer startingDay, Integer startingHour, ProcedureDao procedure, UserDao maintainer) {
        this.id = id;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.isInterruptable = isInterruptable;
        this.week = week;
        this.startingDay = startingDay;
        this.startingHour = startingHour;
        this.procedure = procedure;
        this.maintainer = maintainer;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public void setStartingDay(Integer startingDay) {
        this.startingDay = startingDay;
    }

    public void setStartingHour(Integer startingHour) {
        this.startingHour = startingHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityDao)) return false;
        ActivityDao that = (ActivityDao) o;
        return isInterruptable == that.isInterruptable &&
                Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(estimatedTime, that.estimatedTime) &&
                Objects.equals(week, that.week) &&
                Objects.equals(procedure, that.procedure) &&
                Objects.equals(maintainer, that.maintainer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, estimatedTime, isInterruptable, week, procedure, maintainer);
    }

}
