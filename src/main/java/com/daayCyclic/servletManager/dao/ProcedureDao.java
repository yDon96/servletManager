package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "activity_procedure")
@Getter
@Setter
@NoArgsConstructor
public class ProcedureDao implements ObjectDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private String description;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @UpdateTimestamp
    private LocalDateTime lastMod;

    public ProcedureDao(Integer productId, String title, String description) {
        this.id = productId;
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ProcedureDao)
                && id.equals(((ProcedureDao) obj).id)
                && (Objects.equals(title, ((ProcedureDao) obj).title))
                && (Objects.equals(description, ((ProcedureDao) obj).description));
    }
}
