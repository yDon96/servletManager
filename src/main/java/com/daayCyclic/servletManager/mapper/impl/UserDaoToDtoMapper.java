package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component(value = "UserDaoToDtoMapper")
public class UserDaoToDtoMapper implements IDaoToDtoMapper {

    /**
     * Convert a {@literal UserDao} to a {@literal UserDto}
     *
     * @param objectDao the {@literal ObjectDao} object to convert
     * @return a {@literal ObjectDto} that is a conversion of the given object
     * @throws NotValidTypeException if the given {@literal ObjectDao} is not a {@literal UserDao}
     */
    @Override
    public ObjectDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {
        log.info("[MAPPER] Start conversion from UserDao to UserDto");
        if (!(objectDao instanceof UserDao)) {
            log.info("[MAPPER] The given object is not an instance of UserDao");
            throw new NotValidTypeException("The given object is not an UserDao instance.");
        } else {
            UserDao userDao = (UserDao) objectDao;
            UserDto userDto = new UserDto(userDao.getUser_id(), userDao.getName(), userDao.getSurname(), userDao.getDateOfBirth(), userDao.getRole().getName());
            log.info("[MAPPER] " + userDao + " successfully converted to UserDto");
            return userDto;
        }
    }

    /**
     * Convert a list of {@literal UserDao} to a list of {@literal UserDto}
     * (empty or null list will be converted in the equivalent empty or null list)
     *
     * @param daoObjects a list of {@literal ObjectDao} to convert
     * @return a list of {@literal ObjectDto} that are conversions of the given ones
     * @throws NotValidTypeException if one of the given {@literal ObjectDao} in the list is not a {@literal UserDao}
     */
    @Override
    public List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoObjects) throws NotValidTypeException {
        log.info("[MAPPER] Start batch UserDao to UserDto conversion");
        ArrayList<UserDto> userList = null;
        if (daoObjects != null) {
            userList = new ArrayList<>();
            for (ObjectDao user : daoObjects) {
                UserDto convertedUser = (UserDto) convertToDto(user);
              
                if (convertedUser != null) {
                    userList.add(convertedUser);
                }
            }
        }
        log.info("[MAPPER] Batch conversion completed successfully");
        return userList;
    }
}
