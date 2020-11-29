package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Change DB, with the test one (in all tests)
class UserServiceGetTest extends UserServiceTest{

    @Test
    void getUserPresent() {
        UserDao newUser = createUser(1, "Mario", "Rossi");
        repository.save(newUser);
        assertEquals(newUser, userService.getUser(1));
    }

    @Test
    void getUserNotPresent() {
        assertThrows(NotFoundException.class, () -> userService.getUser(1));
    }

    @Test
    void getUserEmptyDB() {
        assertThrows(NotFoundException.class, () -> userService.getUser(1));
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
        rolesList.add(createRole(0, "STUB"));
        ArrayList<UserDao> foundUsers = (ArrayList<UserDao>) userService.getUsers(rolesList);
        assertEquals(rolesList.size(), foundUsers.size());
        assertEquals("STUB", foundUsers.get(0).getRole().getRole());
    }

    @Test
    void getUsersRoleNotExisting() {
        populateDB();
        ArrayList<RoleDao> roles = new ArrayList<>();
        roles.add(createRole(15, "Account"));
        ArrayList<UserDao> getList = (ArrayList<UserDao>) userService.getUsers(roles);
        assertEquals(0, getList.size());
    }

}