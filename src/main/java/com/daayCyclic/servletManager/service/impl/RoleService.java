package com.daayCyclic.servletManager.service.impl;


import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "RoleService")
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository repository;

    @Override
    public RoleDao getRole(String role) {
        val roleDao = repository.findByName(role);
        return roleDao.orElseThrow(() -> new NotFoundException("Not Found."));
    }

    @Override
    public void generateRole(RoleDao role) {
        repository.save(role);
    }



}
