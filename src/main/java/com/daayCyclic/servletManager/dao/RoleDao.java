package com.daayCyclic.servletManager.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "role")
@Getter
@Setter
@NoArgsConstructor
public class RoleDao implements ObjectDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable=false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDao)) return false;
        RoleDao roleDao = (RoleDao) o;
        return Objects.equals(id, roleDao.id) &&
                Objects.equals(name, roleDao.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
