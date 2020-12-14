package com.daayCyclic.servletManager.service.impl;


import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(value = "RoleService")
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository repository;

    @Override
    public RoleDao getRole(String roleName) {
        log.debug("[RoleService] Get role with name: " + roleName);
        val roleDao = repository.findByName(roleName);
        return roleDao.orElseThrow(() -> new NotFoundException("Not Found."));
    }

    @Override
    public List<RoleDao> getRoles() {
        log.info("[RoleService] Get all roles.");
        return repository.findAll();
    }

    @Override
    public void generateRole(RoleDao role) {
        log.debug("[RoleService] Generating role");
        if(role.getName() == null || role.getName() == ""){
            log.error("[RoleService] String void");
            throw new NotValidTypeException("String void");
        }
        val roleDao = repository.findByName(role.getName());
        if(roleDao.isPresent()){
            log.error("[RoleService] Trying to create a role that already exist with name: " + role.getName());
            throw new DuplicateGenerationException();
        }
        repository.save(role);
    }

}
