package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
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

    public void assignRoleToUser(UserDto user, String role) {
        if (role != null && user != null) {
            RoleDao roleDao = roleService.getRole(role);
            UserDao userDao = userService.getUser(user.getUser_id());
            userService.assignRoleToUser(userDao, roleDao);
        }
    }

}
