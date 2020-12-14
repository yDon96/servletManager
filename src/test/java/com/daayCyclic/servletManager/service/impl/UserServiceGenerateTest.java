package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceGenerateTest {

    @Autowired
    IUserRepository repository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired @Qualifier(value = "UserService")
    IUserService userService;

    @BeforeEach
    void setUp() {
        insertRoles();
    }

    @Test
    void generateUserNotPresent(){
        UserDao newUser = createUser(1, "Mario", "Rossi");
        userService.generateUser(newUser);
        Optional<UserDao> foundUser = repository.findById(1);
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), newUser);
    }

    @Test
    void generateUserNullName(){
        UserDao newUser = createUser(1, null, "Rossi");
        assertThrows(DataIntegrityViolationException.class, () -> userService.generateUser(newUser));
    }

    @Test
    void generateUserAlreadyPresent(){
        UserDao newUser = createUser(1, "Marii", "Rossi");
        userService.generateUser(newUser);
        newUser = createUser(1, "Mario", "Rossi");
        userService.generateUser(newUser);
        Optional<UserDao> foundUser = repository.findById(1);
        assertTrue(foundUser.isPresent());
        assertEquals(newUser, foundUser.get());
    }

    @Test
    void generateUserNullParameter(){
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userService.generateUser(null));
    }

    // BEGIN Utilities methods
    RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setName(role);
        return roleDao;
    }

    UserDao createUser(int id, String name, String surname) {
        UserDao user = new UserDao();
        user.setUser_id(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.of(1968, 1, 1));
        user.setRole(createRole(2, "System Administrator"));
        return user;
    }

    UserDao[] populateDB() {
        UserDao[] list = new UserDao[4];
        list[0] = createUser(1, "mario", "rossi");
        list[1] = createUser(2, "giacomo", "ciccio");
        list[2] = createUser(3, "maria", "melone");
        list[3] = createUser(4, "Cosimo", "Leone");
        list[3].setRole(createRole(1, "STUB"));
        for (UserDao userDao : list) {
            repository.save(userDao);
        }
        return list;
    }

    void insertRoles() {
        roleRepository.save(createRole(1, "STUB"));
        roleRepository.save(createRole(2, "System Administrator"));
        roleRepository.save(createRole(3, "Planner"));
        roleRepository.save(createRole(4, "Maintainer"));
    }
    // END Utilities methods

}
