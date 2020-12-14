package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.service.impl.RoleService;
import com.daayCyclic.servletManager.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PutUserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    public void setMyService(UserService myService) {
        this.userService = myService;
    }

    @Autowired
    public void setMyService2(RoleService myService) {
        this.roleService = myService;
    }

    @BeforeEach
    private void init() {
        UserDao userDao1 = new UserDao();
        userDao1.setUser_id(1);
        userDao1.setName("nome");
        userDao1.setSurname("cognome");
        userDao1.setDateOfBirth(LocalDate.of(2000,1,1));
        userService.generateUser(userDao1);
        RoleDao roleDao1 = new RoleDao();
        roleDao1.setId(1);
        roleDao1.setName("manutentore");
        roleService.generateRole(roleDao1);
    }

    @Test
    void shouldPutRole() throws Exception {
        this.mockMvc.perform(put("/user/1/assign-role")
                .param("role", "manutentore"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestPutRoleWithCharAsId() throws Exception {
        this.mockMvc.perform(put("/user/k/assign-role")
                    .param("role", "manutentore"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        this.mockMvc.perform(put("/user/prova/assign-role")
                .param("role", "manutentore"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldRespondBadRequestPutRoleWithNegativeId() throws Exception {
        this.mockMvc.perform(put("/user/-20/assign-role")
                    .param("role", "manutentore"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondNotFoundPutRoleWithAnNotExistingUserId() throws Exception {
        this.mockMvc.perform(put("/user/100/assign-role")
                .param("role", "manutentore"))
                .andDo(print())
                .andExpect(status().isNotFound());

        this.mockMvc.perform(put("/user/10000/assign-role")
                .param("role", "manutentore"))
                .andDo(print())
                .andExpect(status().isNotFound());

        this.mockMvc.perform(put("/user/600/assign-role")
                .param("role", "manutentore"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondNotFoundPutRoleWithAnNotExistingRole() throws Exception {
        this.mockMvc.perform(put("/user/1/assign-role")
                .param("role", "magazziniere"))
                .andDo(print())
                .andExpect(status().isNotFound());

        this.mockMvc.perform(put("/user/1/assign-role")
                .param("role", ""))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
