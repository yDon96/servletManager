package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

}
