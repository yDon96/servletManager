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

    /**
     * Insert (or Update if already present) into the server the {@literal UserDto} passed.
     *
     * @param user the {@literal UserDto} to insert
     * @throws NotValidTypeException if {@literal UserDto} does not pass the integrity check
     */
    @PostMapping(path = "/post")
    public void postUser(@RequestBody UserDto user) throws NotValidTypeException {
        UserDao userDao = (UserDao) userDtoToDaoMapper.convertToDao(user);
        log.info("[REST] Start insert/update of a new user into the database: " + user);
        userService.generateUser(userDao);
        log.info("[REST] Insert/update completed successfully");
    }

    /**
     * Get from the server the user correspondent to {@literal userId}
     *
     * @param userId the id into the server of the user
     * @return a {@literal UserDto} represents the desired user
     * @throws NotFoundException if the desired user is not presente into the server
     */
    @GetMapping(path = "/get")
    public UserDto getUser(@RequestParam int userId) throws NotValidTypeException, NotFoundException {
        log.info("[REST] Start search for user with id: " + userId);
        UserDao searchUser = userService.getUser(userId);
        if (searchUser == null) {
            log.info("[REST] There is no such user");
            throw new NotFoundException("User not found");
        } else {
            log.info("[REST] User found: " + searchUser);
            return (UserDto) userDaoToDtoMapper.convertToDto(searchUser);
        }
    }

    /**
     * Get users from the server according to their roles (if no role is given, get all users)
     *
     * @param roles a {@literal List<String>} of the desired roles
     * @return a {@literal List<UserDto>} containing the desired users
     * @throws NotValidTypeException
     */
    @GetMapping(path = "/get-many")
    public List<UserDto> getUsers(@RequestParam(required = false) List<String> roles) throws NotValidTypeException {
        ArrayList<RoleDao> rolesDao = null;
        if (roles != null) {
            rolesDao = new ArrayList<>();
            for (String role : roles) {
                rolesDao.add(roleService.getRole(role));
            }
        }
        log.info("[REST] Start a research with the given roles as filter: " + roles);
        List<UserDao> foundUsers = userService.getUsers(rolesDao);
        log.info("[REST] Found a total of " + foundUsers.size() + " users");
        return (List<UserDto>) userDaoToDtoMapper.convertDaoListToDtoList(foundUsers);
    }

    public void assignRoleToUser(UserDto user, String role) {
        // TODO: Controllare se esiste il ruolo prima. (Lo fa Amos)
        if (role != null && user != null) {
            RoleDao roleDao = roleService.getRole(role);
            UserDao userDao = userService.getUser(user.getUser_id());
            userService.assignRoleToUser(userDao, roleDao);
        }
    }

}
