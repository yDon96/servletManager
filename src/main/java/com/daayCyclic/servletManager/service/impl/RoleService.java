package com.daayCyclic.servletManager.service.impl;


import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
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
    public RoleDao getRole(String roleName) {
        val roleDao = repository.findByName(roleName);
        return roleDao.orElseThrow(() -> new NotFoundException("Not Found."));
    }

    @Override
    public void generateRole(RoleDao role) {
        val roleDao = repository.findByName(role.getName());
        if(roleDao.isPresent()){
            throw new DuplicateGenerationException();
        }
        repository.save(role);
    }



}
