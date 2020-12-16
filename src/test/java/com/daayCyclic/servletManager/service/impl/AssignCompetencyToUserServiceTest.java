package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignCompetencyToUserServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICompetencyService competencyService;

    @BeforeEach
    void setUp() {
        createRolesInDB();
        createUsersInDB();
        createCompetenciesInDB();
    }

    @Test
    @Transactional
    void assignCompetencyToUserCompetencyNotPresent() {
        CompetencyDao newCompetency = new CompetencyDao();
        newCompetency.setName("NEW");
        assertThrows(NotFoundException.class, () -> this.userService.assignCompetencyToUser(newCompetency, this.userService.getUser(6)));
    }

    @Test
    @Transactional
    void assignCompetencyToUserUserNotPresent() {
        UserDao newUser = new UserDao(100, "NEW", "new", LocalDate.now(), this.createRole(6, "Maintainer"));
        assertThrows(NotFoundException.class, () -> this.userService.assignCompetencyToUser(this.competencyService.getCompetency("competency4"), newUser));
    }

    @Test
    void assignCompetencyToUserCompetencyNull() {
        assertThrows(NotValidTypeException.class, () -> this.userService.assignCompetencyToUser(null, this.userService.getUser(6)));
    }

    @Test
    void assignCompetencyToUserUserNull() {
        assertThrows(NotValidTypeException.class, () -> this.userService.assignCompetencyToUser(this.competencyService.getCompetency("competency1"), null));
    }

    @Test
    void assignCompetencyToUserUserNotAMaintainer() {
        assertThrows(NotValidTypeException.class, () -> this.userService.assignCompetencyToUser(this.competencyService.getCompetency("competency1"), this.userService.getUser(2)));
    }

    @Test
    @Transactional
    void assignCompetencyToUserEverythingOk() {
        assertDoesNotThrow(() -> this.userService.assignCompetencyToUser(this.competencyService.getCompetency("competency1"), this.userService.getUser(6)));
    }

    private RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setName(role);
        return roleDao;
    }

    private UserDao createUser(int id, String name, String surname, String role) {
        UserDao user = new UserDao();
        user.setUserId(id);
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
            this.competencyService.generateCompetency(new CompetencyDao(i, "competency" + i));
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
