package com.daayCyclic.servletManager.dao;

import com.daayCyclic.servletManager.exception.NotValidTypeException;
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
        this.setWeek(week);
        this.procedure = procedure;
        this.maintainer = maintainer;
    }

    public ActivityDao(Integer id, String description, Integer estimatedTime, boolean isInterruptable, Integer week, Integer startingDay, Integer startingHour, ProcedureDao procedure, UserDao maintainer) {
        this.id = id;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.isInterruptable = isInterruptable;
        this.setWeek(week);
        this.setStartingDay(startingDay);
        this.setStartingHour(startingHour);
        this.procedure = procedure;
        this.maintainer = maintainer;
    }

    public void setWeek(Integer week) {
        if (!this.isValidWeek(week)) {
            throw new NotValidTypeException("Week number should be included between 1 and 52");
        }
        this.week = week;
    }

    public void setStartingDay(Integer startingDay) {
        if (!this.isValidDay(startingDay)) {
            throw new NotValidTypeException("Day number should be included between 1 and 7");
        }
        this.startingDay = startingDay;
    }

    public void setStartingHour(Integer startingHour) {
        if (!this.isValidHour(startingHour)) {
            throw new NotValidTypeException("Hour number should be included between 0 and 23");
        }
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

    private boolean isValidWeek(Integer week) {
        return week != null && (week > 0 && week < 53);
    }

    private boolean isValidDay(Integer day) {
        return day == null || (day > 0 && day < 8);
    }

    private boolean isValidHour(Integer hour) {
        return hour == null || (hour >= 0 && hour < 24);
    }
}
