package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity(name = "activity_procedure")
@Getter
@Setter
@NoArgsConstructor
public class ProcedureDao implements ObjectDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
}
