package com.daayCyclic.servletManager.dao;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "competencies")
public class CompetencyDao implements ObjectDao{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competencyId;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "competencies")
    private Set<UserDao> users;

    @ManyToMany(mappedBy = "competencies")
    private Set<ProcedureDao> procedures;

    public CompetencyDao() {}

    public CompetencyDao(String name) {
        this.name = name;
    }

    public CompetencyDao(Integer competencyId, String name) {
        this.competencyId = competencyId;
        this.name = name;
    }

    public Set<UserDao> getUsers() {
        return users;
    }

    public Set<ProcedureDao> getProcedures() {
        return procedures;
    }

    public void setProcedures(Set<ProcedureDao> procedures) {
        this.procedures = procedures;
    }

    public Integer getCompetencyId() {
        return competencyId;
    }

    public void setCompetencyId(Integer competencyId) {
        this.competencyId = competencyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<UserDao> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompetencyDao)) return false;
        CompetencyDao that = (CompetencyDao) o;
        return Objects.equals(competencyId, that.competencyId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(competencyId, name);
    }

    @Override
    public String toString() {
        return "CompetencyDao{" +
                "competencyId=" + competencyId +
                ", name='" + name + '\'' +
                '}';
    }
}
