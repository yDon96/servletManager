package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceGetTest {

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
    void getUserPresent() {
        UserDao newUser = createUser(1, "Mario", "Rossi");
        repository.save(newUser);
        assertEquals(newUser, userService.getUser(1));
    }

    @Test
    void getUserNotPresent() {
        assertThrows(NotFoundException.class, () -> userService.getUser(2000));
    }

    @Test
    void getUserEmptyDB() {
        assertThrows(NotFoundException.class, () -> userService.getUser(2000));
    }

    @Test
    void getAllUsers() {
        UserDao[] list = populateDB();
        ArrayList<UserDao> getList = (ArrayList<UserDao>) userService.getUsers(null);
        assertEquals(list.length, getList.size());
        for (UserDao userDao : list) {
            assertTrue(getList.contains(userDao));
        }
    }

    @Test
    void getUsersSelectedRoles() {
        populateDB();
        ArrayList<RoleDao> rolesList = new ArrayList<>();
        rolesList.add(createRole(1, "STUB"));
        ArrayList<UserDao> foundUsers = (ArrayList<UserDao>) userService.getUsers(rolesList);
        assertEquals(rolesList.size(), foundUsers.size());
        assertEquals("STUB", foundUsers.get(0).getRole().getName());
    }

    @Test
    void getUsersRoleNotExisting() {
        populateDB();
        ArrayList<RoleDao> roles = new ArrayList<>();
        roles.add(createRole(15, "Account"));
        ArrayList<UserDao> getList = (ArrayList<UserDao>) userService.getUsers(roles);
        assertEquals(0, getList.size());
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