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
import java.util.List;

@RestController
@Slf4j
public class RoleController {

    @Autowired
    IRoleService iRoleService;


    @PostMapping(path = "/postRole")
    public void postRole(@RequestBody String role) throws NotValidTypeException {
        /**
         * Insert (or Update if already present) into the server the the role.
         * @param role the to insert
         * @throws NotValidTypeException if does not pass the integrity check
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
         * @return a list of roles.
         * @throws NotValidTypeException if does not pass the integrity check.
         */
        log.info("[REST] Get Roles");
        val roleDao = iRoleService.getRoles();
        List<String> roles = new ArrayList<>();
        for (int i = 0; i < roleDao.size(); i++){
            roles.add(roleDao.get(0).getName());
        }
        log.debug("[REST] End Get roles");
        return roles;
    }

}
