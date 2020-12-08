package com.daayCyclic.servletManager.dao;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "competencies")
public class CompetencyDao implements ObjectDao{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competencyId;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "competencies")
    private Set<UserDao> users;

    @ManyToMany(mappedBy = "competencies")
    private Set<ProcedureDao> procedures;

    public CompetencyDao(Integer competencyId, String name) {
        this.competencyId = competencyId;
        this.name = name;
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

}
