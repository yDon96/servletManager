package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IProcedureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignProcedureActivityControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private IActivityService activityService;
    @Autowired private IProcedureService procedureService;

    @BeforeEach
    void setUp() {
        addActivities();
        addProcedures();
    }

    @Test
    void assignProcedureProcedureNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignProcedure")
                    .param("procedureID", "100")
                    .param("activityID", "1"))
                        .andDo(print())
                        .andExpect(status().isNotFound());
        });
    }

    @Test
    void assignProcedureActivityNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignProcedure")
                    .param("procedureID", "6")
                    .param("activityID", "100"))
                        .andDo(print())
                        .andExpect(status().isNotFound());
        });
    }

    @Test
    void assignProcedureEverythingGood() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignProcedure")
                    .param("procedureID", "6")
                    .param("activityID", "1"))
                        .andDo(print())
                        .andExpect(status().isOk());
            assertEquals(procedureService.getProcedure(6), activityService.getActivity(1).getProcedure());
        });
    }

    /**
     * Add some Activities into the database
     */
    void addActivities() {
        for (int i = 1; i < 6; i++) {
            ActivityDao newActivity = new ActivityDao(
                    i,
                    "description" + i,
                    10 + i,
                    true,
                    i * 2,
                    null,
                    null);
            activityService.generateActivity(newActivity);
        }
    }

    /**
     * Add some Procedures into the database
     */
    void addProcedures() {
        for (int i = 1; i < 6; i++) {
            ProcedureDao newProcedure = new ProcedureDao(
                    i,
                    "procedure" + i,
                    "description" + i
            );
            procedureService.generateProcedure(newProcedure);
        }
    }

}
