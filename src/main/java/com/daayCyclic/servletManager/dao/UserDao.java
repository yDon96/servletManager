package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserDao implements ObjectDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

}
