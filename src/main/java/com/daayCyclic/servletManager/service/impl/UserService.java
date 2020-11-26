package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Override
    public void generateUser(UserDao user) {
    }

    @Override
    public UserDao getUser(int id) {
        return null;
    }

    @Override
    public List<UserDao> getUsers(Optional<List<String>> rolesList) {
        return null;
    }

    @Override
    public void assignRoleToUser(UserDao user, RoleDao role) {

    }
}
