package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.converter.RoleConverter;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    private final RoleConverter converter = new RoleConverter();

    /**
     * Put a new role into the server.
     *
     * @param role the {@literal String} representing the new role to post.
     */
    @PostMapping(path = "/role")
    public void postRole(@RequestParam String role) throws NotValidTypeException {
        log.info("[REST] Starting post of role: " + role);
        iRoleService.generateRole(converter.convertFromDto(role));
        log.debug("[REST] Posting role completed successfully.");
    }

    /**
     * Retrieve from the server all the roles.
     *
     * @return a {@literal List} of {@literal ActivityDto} containing all the activities in the server.
     * @throws NotValidTypeException if the role ID or name is null
     */
    @GetMapping(path = "/roles")
    public List<String> getRoles() throws NotValidTypeException {
        log.info("[REST] Starting retrieving all roles from server.");
        val rolesDao = iRoleService.getRoles();
        List<String> roles = new ArrayList<>(converter.createFromEntities(rolesDao));
        log.debug("[REST] Roles retrieved successfully.");
        return roles;
    }

}
