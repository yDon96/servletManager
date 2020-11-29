package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;

public interface IRoleService {

    public RoleDao getRole(String role);

    public void generateRole(RoleDao role);

}
