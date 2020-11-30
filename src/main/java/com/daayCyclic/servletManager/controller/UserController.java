package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import com.daayCyclic.servletManager.service.impl.ProcedureService;
import com.daayCyclic.servletManager.service.impl.UserService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired @Qualifier(value = "UserService")
    private IUserService userService;

    @Autowired @Qualifier(value = "RoleService")
    private IRoleService roleService;

    @Autowired @Qualifier(value = "UserDaoToDtoMapper")
    private IDaoToDtoMapper userDaoToDtoMapper;

    @Autowired @Qualifier(value = "UserDtoToDaoMapper")
    private IDtoToDaoMapper userDtoToDaoMapper;

    @PostMapping(path = "/post")
    public void postUser(@RequestBody UserDto user) throws NotValidTypeException {
        log.info("Start insert/update of a new user into the database: " + user);
        UserDao userDao = (UserDao) userDtoToDaoMapper.convertToDao(user);
        userService.generateUser(userDao);
        log.info("Insert/update completed successful");
    }

    @GetMapping(path = "/get")
    public UserDto getUser(@RequestParam int userId) throws NotValidTypeException, NotFoundException {
        UserDao searchUser = userService.getUser(userId);
        if (searchUser == null) {
            throw new NotFoundException("Utente non trovato");
        } else {
            return (UserDto) userDaoToDtoMapper.convertToDto(searchUser);
        }
    }

    @GetMapping(path = "/get-many")
    public List<UserDto> getUsers(@RequestParam(required = false) List<String> roles) throws NotValidTypeException {
        log.info("Get Users with roles: " + roles);
        ArrayList<RoleDao> rolesDao = null;
        if (roles != null) {
            rolesDao = new ArrayList<>();
            for (String role : roles) {
                rolesDao.add(roleService.getRole(role));
            }
        }
        return (List<UserDto>) userDaoToDtoMapper.convertDaoListToDtoList(userService.getUsers(rolesDao));
    }



    @PutMapping()
    public void assignRoleToUser(@RequestParam Integer id, @RequestParam String role) {
        log.info("[REST] Get role " + role + "to user with id: " + id);
        if (role != null && id != null) {

            if(id<0){
                log.error("[REST] User with id " + id + "is not valid.");
                throw new NotValidTypeException("Invalid parameter.");
            }

            RoleDao roleDao = roleService.getRole(role);
            UserDao userDao = userService.getUser(id);
            userService.assignRoleToUser(userDao, roleDao);
        }
    }



}
