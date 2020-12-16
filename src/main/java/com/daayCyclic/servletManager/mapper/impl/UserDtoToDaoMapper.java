package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.converter.CompetencyConverter;
import com.daayCyclic.servletManager.converter.RoleConverter;
import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.ICompetencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component(value = "UserDtoToDaoMapper")
public class UserDtoToDaoMapper implements IDtoToDaoMapper {
//TODO Add tests on competencies

    @Autowired
    private ICompetencyService competencyService;

    private final RoleConverter roleConverter = new RoleConverter();
    private final CompetencyConverter competenciesConverter = new CompetencyConverter();

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
        UserDao newDaoUser = new UserDao(userDto.getUserId(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getDateOfBirth(),
                roleConverter.convertFromDto(userDto.getRole()));
        newDaoUser.setCompetencies((Set<CompetencyDao>) competenciesConverter.createFromDtos(userDto.getCompetencies()));
        log.info("[MAPPER: UserDtoToDao] " + userDto + " successfully converted to UserDao");
        return newDaoUser;
    }

}
