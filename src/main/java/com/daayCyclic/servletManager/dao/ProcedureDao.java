package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(name = "activity_procedure")
@Getter
@Setter
@NoArgsConstructor
public class ProcedureDao implements ObjectDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "procedures_competencies",
            joinColumns = @JoinColumn(name = "procedure_id"),
            inverseJoinColumns = @JoinColumn(name = "competency_id"))
    private Set<CompetencyDao> competencies;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @UpdateTimestamp
    private LocalDateTime lastMod;

    public ProcedureDao(Integer productId, String title, String description, Set<CompetencyDao> competencies) {
        this.id = productId;
        this.title = title;
        this.description = description;
        this.competencies = competencies;
    }

    public ProcedureDao(Integer productId, String title, String description) {
        this.id = productId;
        this.title = title;
        this.description = description;
        this.competencies = new LinkedHashSet<>();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProcedureDao)
                && id.equals(((ProcedureDao) obj).id)
                && (Objects.equals(title, ((ProcedureDao) obj).title))
                && (Objects.equals(description, ((ProcedureDao) obj).description));
    }
}
