package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceGenerateTest extends UserServiceTest {

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

}
