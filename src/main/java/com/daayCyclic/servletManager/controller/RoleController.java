package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class RoleController {

    @Autowired
    IRoleService iRoleService;


    @PostMapping(path = "/postRole")
    public void postRole(@RequestParam String role) throws NotValidTypeException {
        /**
         * Insert (or Update if already present) into the server the the role.
         */
        log.info("[REST] Post Role");
        RoleDao roleDao = new RoleDao();
        roleDao.setName(role);
        iRoleService.generateRole(roleDao);
        log.debug("[REST] End Post role");
    }

    @GetMapping(path = "/getRoles")
    public List<String> getRoles() throws NotValidTypeException {
        /**
         * get all the roles that are in the database.
         */
        log.info("[REST] Get Roles");
        val roleDao = iRoleService.getRoles();
        List<String> roles = new ArrayList<>();
        for (int i = 0; i < roleDao.size(); i++){
            roles.add(roleDao.get(i).getName());
        }
        log.debug("[REST] End Get roles");
        return roles;
    }

}
