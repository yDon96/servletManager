package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.ICompetencyService;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component(value = "UserDtoToDaoMapper")
public class UserDtoToDaoMapper implements IDtoToDaoMapper {
//TODO Add tests on competencies

    @Autowired
    IRoleService roleService;

    @Autowired
    ICompetencyService competencyService;

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
        log.info("[MAPPER: UserDtoToDao] Start conversion from UserDto to UserDao");
        if (!(user instanceof UserDto)) {
            log.info("[MAPPER: UserDtoToDao] The given object is not an instance of UserDto");
            throw new NotValidTypeException("The given object is not an UserDto instance.");
        }
        UserDto userDto = (UserDto) user;
        if (!(checkDataIntegrityDto(userDto))) {
            log.info("[MAPPER: UserDtoToDao] The given object did not pass data integrity violation check");
            throw new NotValidTypeException("The given object has one or more 'null' attributes who would violate data integrity.");
        }
        UserDao newDaoUser = new UserDao(userDto.getUser_id(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getDateOfBirth(),
                this.convertRoleToDao(userDto.getRole()));
        newDaoUser.setCompetencies(this.convertCompetenciesToDao(userDto.getCompetencies()));
        log.info("[MAPPER: UserDtoToDao] " + userDto + " successfully converted to UserDao");
        return newDaoUser;
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


    //TODO: Extract this methods logic from here
    private RoleDao convertRoleToDao(String role) {
        if (role != null && !role.equals("")) {
            try {
                return roleService.getRole(role);
            } catch (NotFoundException e) {
                log.info("[MAPPER: UserDtoToDao] The given object contains a role that is not present into the server, conversion impossible");
                throw new NotValidTypeException("The given object contains a role that is not present into the server");
            }
        }
        return null;
    }

    private CompetencyDao convertCompetencyToDao(String competency) {
        if (competency != null && !competency.equals("")) {
            try {
                return competencyService.getCompetency(competency);
            } catch (NotFoundException e) {
                log.info("[MAPPER: UserDtoToDao] The given object contains a competency that is not present into the server, conversion impossible");
                throw new NotValidTypeException("The given object contains a competency that is not present into the server");
            }
        }
        return null;
    }

    private Set<CompetencyDao> convertCompetenciesToDao(Set<String> competencies) {
        Set<CompetencyDao> convertedCompetencies = null;
        if (competencies != null) {
            convertedCompetencies = new HashSet<>();
            for (String competency : competencies) {
                convertedCompetencies.add(this.convertCompetencyToDao(competency));
            }
        }
        return convertedCompetencies;
    }

}
