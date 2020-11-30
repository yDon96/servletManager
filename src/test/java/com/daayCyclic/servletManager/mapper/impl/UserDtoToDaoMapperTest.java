package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDtoToDaoMapperTest extends UserMapperTest {

    private UserDtoToDaoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserDtoToDaoMapper();
    }

    @Test
    void convertToDaoGoodObject() {
        //TODO: Non funziona finché non si implementano i ruoli (lo fa Amos)
        UserDto userDto = new UserDto(5, "Mario", "Rossi", LocalDate.of(1900, 10, 5), "Planner");
        assertDoesNotThrow(() -> {
            UserDao userDao = (UserDao) mapper.convertToDao(userDto);
            checkDtoDaoEquality(userDto, userDao);
        });
    }

    @Test
    void convertToDaoNullObject() {
        assertThrows(NotValidTypeException.class, () -> mapper.convertToDao(null));
    }

    @Test
    void convertToDaoNotValidObject() {
        ProcedureDto procedure = new ProcedureDto();
        assertThrows(NotValidTypeException.class, () -> mapper.convertToDao(procedure));
    }

    @Test
    void convertToDaoBadObjectParameters() {
        UserDto user = new UserDto(5, "Mario", null, LocalDate.of(1900, 10, 5), "Prova");
        assertThrows(NotValidTypeException.class, () -> mapper.convertToDao(user));
    }

}