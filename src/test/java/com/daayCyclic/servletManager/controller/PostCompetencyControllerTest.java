package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.service.ICompetencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostCompetencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICompetencyService service;

    @BeforeEach
    void setUp() {
        service.generateCompetency("ECDL");
    }

    @Test
    void postCompetencyEmpty() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/postCompetency").param("competency", ""))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    void postCompetencyAlreadyPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/postCompetency").param("competency", "ECDL"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    void postCompetencyNullAlreadyPresentLowerCase() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/postCompetency").param("competency", "ecdl"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    void postCompetencyNullEverythingGood() {
        assertDoesNotThrow(() -> {
            String newCompetency = "competency1";
            mockMvc.perform(post("/postCompetency").param("competency", newCompetency))
                    .andDo(print())
                    .andExpect(status().isOk());
            assertEquals(newCompetency, service.getCompetency(newCompetency).getName());
        });
    }

}
