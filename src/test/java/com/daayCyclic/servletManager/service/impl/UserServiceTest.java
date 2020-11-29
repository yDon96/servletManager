package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@SpringBootTest
public class UserServiceTest {

    // BEGIN Attributes
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    IUserRepository repository;

    @Autowired @Qualifier(value = "UserService")
    IUserService userService;

    @Autowired
    private PlatformTransactionManager transactionManager;
    // END Attributes

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
            transactionStatus.flush();
        });
    }

    // BEGIN Utilities methods
    RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setRole(role);
        return roleDao;
    }

    UserDao createUser(int id, String name, String surname) {
        UserDao user = new UserDao();
        user.setUser_id(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.of(1968, 1, 1));
        user.setRole(createRole(1, "System Administrator"));
        return user;
    }

    UserDao[] populateDB() {
        UserDao[] list = new UserDao[4];
        list[0] = createUser(1, "mario", "rossi");
        list[1] = createUser(2, "giacomo", "ciccio");
        list[2] = createUser(3, "maria", "melone");
        list[3] = createUser(4, "Cosimo", "Leone");
        list[3].setRole(createRole(0, "STUB"));
        for (UserDao userDao : list) {
            repository.save(userDao);
        }
        return list;
    }
    // END Utilities methods

}
