package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IUserRepository repository;

    @Autowired
    IRoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        insertRoles();
        UserDao[] list = new UserDao[5];
        list[0] = createUser(1, "mario", "rossi");
        list[1] = createUser(2, "giacomo", "ciccio");
        list[2] = createUser(3, "maria", "melone");
        list[3] = createUser(4, "Cosimo", "Leone");
        list[3].setRole(createRole(1, "STUB"));
        list[4] = createUser(5, "maria", "maddalena");
        list[4].setRole(createRole(3, "Planner"));
        for (UserDao userDao : list) {
            repository.save(userDao);
        }
    }

    @Test
    void postUserNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFormatted(10, "Domenico", "Barra", "1996-10-07", "System Administrator")))
                    .andDo(print())
                    .andExpect(status().isOk());
        });
    }

    @Test
    void postUserAlreadyPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFormatted(1, "Domenico", "Barra", "1996-10-07", "System Administrator")))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    void postUserNotValid() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    private String getContentFormatted(int id, String name, String surname, String dateOfBirth, String role){
        return "{\"userId\":\"" + id
                + "\",\"name\":\"" + name
                + "\",\"surname\":\"" + surname
                + "\",\"dateOfBirth\":\"" + dateOfBirth
                + "\",\"role\":\"" + role
                + "\"}";
    }

    //Utility Methods
    UserDao createUser(int id, String name, String surname) {
        UserDao user = new UserDao();
        user.setUserId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.of(1968, 1, 1));
        user.setRole(createRole(2, "System Administrator"));
        return user;
    }

    RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setName(role);
        return roleDao;
    }

    void insertRoles() {
        roleRepository.save(createRole(1, "STUB"));
        roleRepository.save(createRole(2, "System Administrator"));
        roleRepository.save(createRole(3, "Planner"));
        roleRepository.save(createRole(4, "Maintainer"));
    }
}
