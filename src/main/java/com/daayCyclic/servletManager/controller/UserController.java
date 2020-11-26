package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.UserDao;
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
    public void postUser(UserDto user) {
        UserDao userDao = userDtoToDaoMapper.convertToDao(user);
        userService.generateUser(userDao);
    }

    @GetMapping(path = "/get")
    public UserDto getUser(int userId) {
        UserDao searchUser = userService.getUser(userId);
        if (searchUser == null) {
            System.out.println("Utente non trovato");
            //throw new
            //LANCIA ERRORE: "Utente non presente nel db"
        } else {
            UserDto user = userDaoToDtoMapper.convertToDto(searchUser);
            return user;
        }
    }

    @GetMapping(path = "/get-many")
    public List<UserDto> getUsers(Optional<List<String>> roles){
        return null;
    }

    public void assignRoleToUser(String role) {

    }

}
