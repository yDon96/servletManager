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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerGetTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IUserRepository userRepository;

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
            userRepository.save(userDao);
        }
    }

    @Test
    void getUserPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get").param("userId", "3"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value("3"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("melone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("System Administrator"));
        });
    }

    @Test
    void getUserNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get").param("userId", "100"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        });
    }

    @Test
    void getUsersEmptyRolesList() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get-many").param("roles", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].user_id").value("1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("mario"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("rossi"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("System Administrator"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].user_id").value("2"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("giacomo"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("ciccio"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value("System Administrator"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].user_id").value("3"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].surname").value("melone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].role").value("System Administrator"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].user_id").value("4"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Cosimo"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].surname").value("Leone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].role").value("STUB"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].user_id").value("5"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].surname").value("maddalena"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].role").value("Planner")
                    );
        });
    }

    @Test
    void getUsersRoleNotExistingInList() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get-many").param("roles", "Pippo"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5))
                    );
        });
    }

    @Test
    void getUsersGoodRolesList() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get-many").param("roles", "Planner,STUB"))
                    .andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].user_id").value("4"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Cosimo"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("Leone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value("STUB"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].user_id").value("5"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("maddalena"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("Planner")
                    );
        });
    }

    //Utility Methods
    UserDao createUser(int id, String name, String surname) {
        UserDao user = new UserDao();
        user.setUser_id(id);
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
