package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "UserDtoToDaoMapper")
public class UserDtoToDaoMapper implements IDtoToDaoMapper {

    @Autowired
    IRoleService roleService;

    @Override
    public ObjectDao convertToDao(ObjectDto user) throws NotValidTypeException {
        if (!(user instanceof UserDto)) {
            throw new NotValidTypeException("The given object is not an UserDto instance.");
        } else {
            UserDto userDto = (UserDto) user;
            if (!(checkDataIntegrityDto(userDto))) {
                throw new NotValidTypeException("The given object has one or more 'null' attributes who would violate data integrity.");
            }
            UserDao newDao = new UserDao();
            newDao.setUser_id(userDto.getUser_id());
            newDao.setName(userDto.getName());
            newDao.setSurname(userDto.getSurname());
            newDao.setDateOfBirth(userDto.getDateOfBirth());
            RoleDao tmpRole = roleService.getRole(userDto.getRole());
            // Start testing
            if (tmpRole == null) {
                // TODO: da eliminare (nella realtà, deve lanciare un errore se non c'è il ruolo)
                tmpRole = new RoleDao();
                tmpRole.setId(0);
                tmpRole.setRole("STUB");
            }
            // End testing
            newDao.setRole(tmpRole);
            return newDao;
        }
    }

    private boolean checkDataIntegrityDto(UserDto user) {
        return user != null &&
                !(user.getUser_id() == null ||
                  user.getName() == null ||
                  user.getSurname() == null ||
                  user.getDateOfBirth() == null);
    }

}
