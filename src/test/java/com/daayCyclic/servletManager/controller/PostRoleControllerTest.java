package com.daayCyclic.servletManager.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldPostRole() throws Exception {
        this.mockMvc.perform(post("/postRole")
                .param("role","main"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestWithoutRole() throws Exception {
        this.mockMvc.perform(post("/postRole")
                .param("role",""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestDuplicateRole() throws Exception {
        this.mockMvc.perform(post("/postRole")
                .param("role","main"));
        this.mockMvc.perform(post("/postRole")
                .param("role","main"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
