package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;

public class UserDaoToDtoMapper implements IDaoToDtoMapper {

    @Override
    public ObjectDto convertToDto(ObjectDao objectDao) {
        if (objectDao instanceof UserDao) {
            UserDao user = (UserDao) objectDao;
            return new UserDto(user.getUser_id(), user.getName(), user.getSurname(), user.getDateOfBirth(), user.getRole());
        } else {
            //throw new Exception();
            //LANCIA ERORRE: "Non è l'oggetto giusto per questa classe"
        }
    }

}
