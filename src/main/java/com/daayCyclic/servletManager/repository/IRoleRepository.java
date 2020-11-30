package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.RoleDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleDao,Integer> {

    Optional<RoleDao> findByName(String name);
}
