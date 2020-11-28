package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "activity")
@Getter
@Setter
@NoArgsConstructor
public class ActivityDao implements ObjectDao{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

}
