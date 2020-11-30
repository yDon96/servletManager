package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "UserDtoToDaoMapper")
public class UserDtoToDaoMapper implements IDtoToDaoMapper {

    @Autowired
    IRoleService roleService;

    /**
     * Convert a {@literal UserDto} to a {@literal UserDao}.
     *
     * @param user the {@literal ObjectDto} object to convert
     * @return a {@literal ObjectDao} that is a conversion of the given object
     * @throws NotValidTypeException if the given {@literal ObjectDto} is not a {@literal UserDto}
     *  or it doesn't respect integrity check
     */
    @Override
    public ObjectDao convertToDao(ObjectDto user) throws NotValidTypeException {
        log.info("[MAPPER] Start conversion from UserDto to UserDao");
        if (!(user instanceof UserDto)) {
            log.info("[MAPPER] The given object is not an instance of UserDto");
            throw new NotValidTypeException("The given object is not an UserDto instance.");
        } else {
            UserDto userDto = (UserDto) user;
            if (!(checkDataIntegrityDto(userDto))) {
                log.info("[MAPPER] The given object did not pass data integrity violation check");
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
                // TODO: da eliminare (nella realtà, deve lanciare un errore se non c'è il ruolo,
                //  messo solo perché non c'è ancora un'implementazione di "getRole"). (Lo fa Amos)
                tmpRole = new RoleDao();
                tmpRole.setId(0);
                tmpRole.setName("STUB");
            }
            // End testing
            newDao.setRole(tmpRole);
            log.info("[MAPPER] " + userDto + " successfully converted to UserDao");
            return newDao;
        }
    }

    /**
     * Checks if the given user respects integrity constraints.
     *
     * @param user a {@literal UserDto} represents a Users entity
     * @return {@literal true} if user respects all constraints, {@literal false} otherwise
     */
    private boolean checkDataIntegrityDto(UserDto user) {
        return !(user.getUser_id() == null ||
                user.getName() == null ||
                user.getSurname() == null ||
                user.getDateOfBirth() == null);
    }

}
