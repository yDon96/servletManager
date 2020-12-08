package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.RoleDao;

import java.util.List;


public interface IRoleService {
    void generateRole(RoleDao role);
    RoleDao getRole(String role);
    List<RoleDao> getRoles();
}
