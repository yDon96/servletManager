package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IUserRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    PlatformTransactionManager transactionManager;


    @BeforeEach
    void setUp() {
        UserDao[] list = new UserDao[5];
        list[0] = createUser(1, "mario", "rossi");
        list[1] = createUser(2, "giacomo", "ciccio");
        list[2] = createUser(3, "maria", "melone");
        list[3] = createUser(4, "Cosimo", "Leone");
        list[3].setRole(createRole(0, "STUB"));
        list[4] = createUser(5, "maria", "maddalena");
        list[4].setRole(createRole(2, "Planner"));
        for (UserDao userDao : list) {
            repository.save(userDao);
        }
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
            transactionStatus.flush();
        });
    }

    //Utility Methods
    UserDao createUser(int id, String name, String surname) {
        UserDao user = new UserDao();
        user.setUser_id(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.of(1968, 1, 1));
        user.setRole(createRole(1, "System Administrator"));
        return user;
    }

    RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setRole(role);
        return roleDao;
    }

}