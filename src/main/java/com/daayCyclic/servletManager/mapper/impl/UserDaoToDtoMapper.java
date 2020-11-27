package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "UserDaoToDtoMapper")
public class UserDaoToDtoMapper implements IDaoToDtoMapper {

    @Override
    public ObjectDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {
        if (!(objectDao instanceof UserDao)) {
            throw new NotValidTypeException("L'oggetto passato non è un'istanza di UserDao.");
        } else {
            UserDao user = (UserDao) objectDao;
            return new UserDto(user.getUser_id(), user.getName(), user.getSurname(), user.getDateOfBirth(), user.getRole().getRole());
        }
    }

    @Override
    public List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoObjects) throws NotValidTypeException {
        ArrayList<UserDto> userList = null;
        if (daoObjects != null) {
            userList = new ArrayList<>();
            for (ObjectDao user : daoObjects) {
                UserDao tmpUser = (UserDao) user;
                UserDto convertedUser = (UserDto) convertToDto(tmpUser);
                if (convertedUser != null) {
                    userList.add(convertedUser);
                }
            }
        }
        return userList;
    }

}
