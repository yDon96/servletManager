package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IRoleRepository iRoleRepository;

    private List<RoleDao> roleDaoList;

    @BeforeEach
    private void init() {
        roleDaoList = new ArrayList<>();
    }

    @Test
    void shouldGetRoles() throws Exception  {
        for(int i = 1; i < 5; i++) {
            RoleDao roleDao = new RoleDao();
            roleDao.setId(i);
            roleDao.setName("main" + i);
            roleDaoList.add(iRoleRepository.save(roleDao));
        }
        this.mockMvc.perform(get("/roles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(roleDaoList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(roleDaoList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]").value(roleDaoList.get(2).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3]").value(roleDaoList.get(3).getName()));
    }

    @Test
    void shouldGetListEmpty() throws Exception  {
        this.mockMvc.perform(get("/roles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

}
