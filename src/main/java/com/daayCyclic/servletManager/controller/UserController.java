package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired private IUserService userService;
    private IDaoToDtoMapper userDaoToDtoMapper;
    private IDtoToDaoMapper userDtoToDaoMapper;

    @PostMapping(path = "/post")
    public void postUser(UserDto user) {}

    @GetMapping(path = "/get")
    public UserDto getUser(UserDto user) { return null; }

    @GetMapping(path = "/get-many")
    public List<UserDto> getUsers(Optional<List<String>> roles){ return null; }

    public void assignRoleToUser(String role) {}

}
