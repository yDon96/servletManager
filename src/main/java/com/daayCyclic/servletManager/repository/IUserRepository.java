package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserDao,Integer> {

    List<UserDao> findByRole(RoleDao role);

}
