package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;

@Component(value = "UserDtoToDaoMapper")
public class UserDtoToDaoMapper implements IDtoToDaoMapper {

    @Autowired
    IRoleService roleService;

    @Override
    public ObjectDao convertToDao(ObjectDto user) {
        UserDto userDto = (UserDto) user;
        UserDao newDao = new UserDao();
        newDao.setUser_id(userDto.getUser_id());
        newDao.setName(userDto.getName());
        newDao.setSurname(userDto.getSurname());
        newDao.setDateOfBirth(userDto.getDateOfBirth());
        RoleDao tmpRole = null;

        if(userDto.getRole() != null) {
            tmpRole = roleService.getRole(userDto.getRole());
        }

        newDao.setRole(tmpRole);
        return newDao;
    }

}
