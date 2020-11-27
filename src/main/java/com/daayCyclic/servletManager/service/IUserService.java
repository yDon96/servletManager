package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;

import java.util.List;

public interface IUserService {

    void generateUser(UserDao user);
    UserDao getUser(int id);
    List<UserDao> getUsers(List<String> rolesList);
    void assignRoleToUser(UserDao user, RoleDao role);

}
