package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.RoleDao;


public interface IRoleService {

    public RoleDao getRole(String role);

    public void generateRole(RoleDao role);
}
