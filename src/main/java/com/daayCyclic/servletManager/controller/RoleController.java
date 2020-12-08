package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    IRoleService roleService;

    @PostMapping(path = "/postRole")
    public void postRole(String role) {}

    @GetMapping(path = "/getRoles")
    public List<String> getRoles() {
        return null;
    }

}
