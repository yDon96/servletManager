package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "UserService")
public class UserService implements IUserService{

    @Autowired
    private IUserRepository repository;

    @Override
    public void generateUser(UserDao user) {
        repository.save(user);
    }

    @Override
    public UserDao getUser(int id) {
        return repository.findById(id);
    }

    @Override
    public List<UserDao> getUsers(List<RoleDao> rolesList) {
        ArrayList<UserDao> list = new ArrayList<>();
        if (rolesList == null) {
            list.addAll(repository.findAll());
        } else {
            for (RoleDao role : rolesList) {
                list.addAll(repository.findByRole(role));
            }
        }
        return list;
    }

    @Override
    public void assignRoleToUser(UserDao user, RoleDao role) {
        user.setRole(role);
        repository.save(user);
    }

}
