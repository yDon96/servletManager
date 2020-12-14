package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UpdateCompetencyServiceTest {

    @Autowired
    private ICompetencyService competencyService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICompetencyRepository repository;

    @BeforeEach
    void setUp() {
        createRolesInDB();
        createUsersInDB();
        createCompetenciesInDB();
    }

    @Test
    void updateCompetencyNotExist() {
        assertThrows(NotFoundException.class, () -> this.competencyService.updateCompetency(new CompetencyDao(100, "competency100")));
    }

    @Test
    void updateCompetencyEmpty() {
        assertThrows(NotValidTypeException.class, () -> this.competencyService.updateCompetency(new CompetencyDao(1, "")));
    }

    @Test
    void updateCompetencyNull() {
        assertThrows(NotValidTypeException.class, () -> this.competencyService.updateCompetency(null));
    }

    @Test
    @Transactional
    void updateCompetencyLowerCase() {
        CompetencyDao updatedComp = new CompetencyDao();
        updatedComp.setName("competency1");
        updatedComp.setUsers(new HashSet<>());
        UserDao newUser = this.userService.getUser(1);
        updatedComp.getUsers().add(newUser);
        assertDoesNotThrow(() -> this.competencyService.updateCompetency(updatedComp));

        CompetencyDao retrievedCompetency = this.competencyService.getCompetency("competency1");
        assertEquals(1, retrievedCompetency.getUsers().size());
        assertTrue(retrievedCompetency.getUsers().contains(newUser));
    }

    @Test
    @Transactional
    void updateCompetencyEverythingOk() {
        CompetencyDao updatedComp = new CompetencyDao();
        updatedComp.setName("COMPETENCY1");
        updatedComp.setUsers(new HashSet<>());
        UserDao newUser = this.userService.getUser(1);
        updatedComp.getUsers().add(newUser);
        assertDoesNotThrow(() -> this.competencyService.updateCompetency(updatedComp));

        CompetencyDao retrievedCompetency = this.competencyService.getCompetency("COMPETENCY1");
        assertEquals(1, retrievedCompetency.getUsers().size());
        assertTrue(retrievedCompetency.getUsers().contains(newUser));
    }


    private RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setName(role);
        return roleDao;
    }

    private UserDao createUser(int id, String name, String surname, String role) {
        UserDao user = new UserDao();
        user.setUser_id(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.now());
        user.setRole(createRole(id, role));
        return user;
    }

    private void createRolesInDB() {
        for (int i = 1; i < 6; i++) {
            this.roleService.generateRole(this.createRole(i,"role" + i));
        }
        this.roleService.generateRole(this.createRole(6, "Maintainer"));
    }

    private void createCompetenciesInDB() {
        for (int i = 1; i < 6; i++) {
            this.competencyService.generateCompetency("competency" + i);
        }
    }

    private void createUsersInDB() {
        for (int i = 1; i < 6; i++) {
            UserDao newUser = createUser(
                    i,
                    "name" + i,
                    "surname" + i,
                    "role" + i);
            this.userService.generateUser(newUser);
        }
        this.userService.generateUser(this.createUser(6, "good", "good", "Maintainer"));
    }
}
