package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.ICompetencyService;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICompetencyService competencyService;

    @Autowired @Qualifier(value = "UserDaoToDtoMapper")
    private IDaoToDtoMapper userDaoToDtoMapper;

    @Autowired @Qualifier(value = "UserDtoToDaoMapper")
    private IDtoToDaoMapper userDtoToDaoMapper;

    /**
     * Insert into the server the {@literal UserDto} passed.
     *
     * @param user the {@literal UserDto} to insert.
     */
    @PostMapping(path = "/user")
    public void postUser(@RequestBody UserDto user) {
        log.info("[REST] Starting a postUser request.");
        UserDao userDao = (UserDao) userDtoToDaoMapper.convertToDao(user);
        log.info("[REST] Start insert of a new user into the database: " + user);
        userService.generateUser(userDao);
        log.info("[REST] Insert completed successfully.");
    }

    /**
     * Update into the server the {@literal UserDto} passed.
     *
     * @param user the {@literal UserDto} to update.
     */
    @PutMapping(path = "/user")
    public void putUser(@RequestBody UserDto user) {
        log.info("[REST] Starting a putUser request.");
        UserDao userDao = (UserDao) userDtoToDaoMapper.convertToDao(user);
        log.info("[REST] Start update of a user into the database: " + user);
        userService.updateUser(userDao);
        log.info("[REST] Update completed successfully.");
    }

    /**
     * Get from the server the user correspondent to {@literal userId}.
     *
     * @param userId the id into the server of the user.
     * @return a {@literal UserDto} represents the desired user.
     */
    @GetMapping(path = "/user/{userId}")
    public UserDto getUser(@PathVariable("userId") int userId) {
        log.info("[REST] Start search for user with id: " + userId);
        UserDao searchUser = userService.getUser(userId);
        log.info("[REST] User found: " + searchUser);
        return (UserDto) userDaoToDtoMapper.convertToDto(searchUser);
    }

    /**
     * Get users from the server according to their roles, if no role is given, get all users.
     * (if some of the roles in the list does not exist, will be skipped)
     *
     * @param roles a {@literal List<String>} of the desired roles.
     * @return a {@literal List<UserDto>} containing the desired users.
     */
    @GetMapping(path = "/users")
    @SuppressWarnings("unchecked")
    public List<UserDto> getUsers(@RequestParam(required = false) List<String> roles) {
        log.info("[REST] Starting a getUsers with the given roles.");
        ArrayList<RoleDao> rolesDao = null;
        if (roles != null) {
            rolesDao = new ArrayList<>();
            for (String role : roles) {
                try {
                    RoleDao foundRole = roleService.getRole(role);
                    rolesDao.add(foundRole);
                } catch (NotFoundException e) {
                    log.info("[REST] The role '" + role + "' does not exist inside the server, will be skipped.");
                }
            }
        }
        log.info("[REST] Start a research with the given roles as filter: " + roles);
        List<UserDao> foundUsers = userService.getUsers(rolesDao);
        log.info("[REST] Found a total of " + foundUsers.size() + " users.");
        return (List<UserDto>) userDaoToDtoMapper.convertDaoListToDtoList(foundUsers);
    }

    /**
     * Assign the role corresponding to the given {@literal String} role
     * to the user corresponding to the given id.
     *
     * @param id a {@literal Integer} ID which identifies a user.
     * @param role a {@literal String} which identifies an role.
     */
    @PutMapping(path = "/user/{id}/assign-role")
    public void assignRoleToUser(@PathVariable("id") Integer id, @RequestParam String role) {
        log.info("[REST] Get role " + role + "to user with id: " + id);
        if (role != null && id != null) {

            if(id<0){
                log.error("[REST] User with id " + id + "is not valid.");
                throw new NotValidTypeException("Invalid parameter.");
            }

            RoleDao roleDao = roleService.getRole(role);
            UserDao userDao = userService.getUser(id);
            userService.assignRoleToUser(userDao, roleDao);
            log.info("[REST] User role assignment was successful.");
        }
    }

    @PutMapping(path = "/user/{userId}/assign-competencies")
    public void assignMultipleCompetencyToUser(@PathVariable("userId") Integer userId,@RequestParam List<String> competencies) {
        log.info("[REST] Starting assign competency: " + competencies + " to user: " + userId);
        competencies.forEach(competency -> {
            assignCompetencyToUser(userId,competency);
        });
        log.info("[REST] Competency assigned successfully");
    }

    /**
     * Assign the competency corresponding to the given {@literal String} competency
     * to the user corresponding to the given userId.
     *
     * @param userId a {@literal Integer} ID which identifies a user.
     * @param competency a {@literal String} which identifies an competency.
     */
    @PutMapping(path = "/user/{userId}/assign-competency")
    public void assignCompetencyToUser(@PathVariable("userId") Integer userId, @RequestParam String competency) {
        log.info("[REST] Starting assign competency: " + competency + " to user: " + userId);
        UserDao userDao = userService.getUser(userId);
        CompetencyDao competencyDao = competencyService.getCompetency(competency);
        userService.assignCompetencyToUser(competencyDao, userDao);
        log.info("[REST] Competency assigned successfully.");
    }

}
