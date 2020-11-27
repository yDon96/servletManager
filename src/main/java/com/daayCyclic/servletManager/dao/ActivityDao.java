package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.*;
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

    private ProcedureDao procedure;

    private UserDao maintainer;

    private LocalDateTime lastMod;

    private  LocalDateTime timestamp;


}
