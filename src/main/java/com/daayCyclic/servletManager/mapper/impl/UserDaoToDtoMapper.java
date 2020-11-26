package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;

public class UserDaoToDtoMapper implements IDaoToDtoMapper {

    @Override
    public ObjectDto convertToDto(ObjectDao objectDao) {
        if (!(objectDao instanceof UserDao)) {
            System.out.println("Errore, oggetto di una classe sbagliata per il mapping.");
            //throw new Exception();
            //LANCIA ERORRE: "Non è l'oggetto giusto per questa classe"
        } else {
            UserDao user = (UserDao) objectDao;
            return new UserDto(user.getUser_id(), user.getName(), user.getSurname(), user.getDateOfBirth(), user.getRole());
        }
    }

}
