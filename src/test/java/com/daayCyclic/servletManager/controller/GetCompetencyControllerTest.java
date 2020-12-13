package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.service.ICompetencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetCompetencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICompetencyService service;

    @Test
    void getCompetenciesEmptyDB() {
        assertDoesNotThrow(() -> {
            this.mockMvc.perform(get("/competencies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
        });
    }

    @Test
    void getCompetenciesNotEmptyDB() {
        int competencyNr = 5;
        this.populateCompetencies(competencyNr);
        assertDoesNotThrow(() -> {
            ResultActions results = this.mockMvc.perform(get("/competencies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(competencyNr)));

            // Check if every entry is present
            for (int i = 0; i < competencyNr; i++) {
                results.andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "]").value("COMPETENCY" + i));
            }
        });
    }

    private void populateCompetencies(int n) {
        for (int i = 0; i < n; i++) {
            this.service.generateCompetency("competency" + i);
        }
    }

}
