package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDtoToDaoMapperTest {

    private UserDtoToDaoMapper mapper;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    ICompetencyRepository iCompetencyRepository;

    @BeforeEach
    void setUp() {
        mapper = new UserDtoToDaoMapper();
    }

//    @Disabled("Bisogna capire perché non va prima, penso che il problema sia legato in qualche modo a Spring. " +
//            "Da quanto si legge nel log, non inizializza un RoleService quando avvio i test")
    @Test
    void convertToDaoGoodObject() {
        roleRepository.save(createRole(1, "Planner"));
        UserDto userDto = new UserDto(5, "Mario", "Rossi", LocalDate.of(1900, 10, 5), "Planner");
        assertDoesNotThrow(() -> {
            UserDao userDao = (UserDao) mapper.convertToDao(userDto);
            checkDtoDaoEquality(userDto, userDao);
        });
    }

    @Test
    void convertToDaoWithAddedCompetency() {
        RoleDao roleDao = new RoleDao();
        roleDao.setName("Planner");
        roleRepository.save(roleDao);
        CompetencyDao competencyDao = new CompetencyDao();
        competencyDao.setUsers(new LinkedHashSet<>());
        competencyDao.setName("competency");
        competencyDao.getUsers().add(new UserDao(5, "Mario", "Rossi", LocalDate.of(1900, 10, 5), roleDao));
        iCompetencyRepository.save(competencyDao);
        roleRepository.save(createRole(1, "Planner"));
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

    void checkDtoDaoEquality(UserDto userDto, UserDao userDao) {
        assertEquals(userDto.getUser_id(), userDao.getUser_id());
        assertEquals(userDto.getName(), userDao.getName());
        assertEquals(userDto.getSurname(), userDao.getSurname());
        assertEquals(userDto.getDateOfBirth(), userDao.getDateOfBirth());
        assertEquals(userDto.getRole(), userDao.getRole().getName());
    }

    RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setName(role);
        return roleDao;
    }
}