package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IProcedureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignProcedureActivityServiceTest {

    @Autowired private IActivityService activityService;
    @Autowired private IProcedureService procedureService;

    @BeforeEach
    void setUp() {
        addActivities();
        addProcedures();
    }

    @Test
    void assignProcedureProcedureNotPresent() {
        assertThrows(NotFoundException.class, () -> {
            ProcedureDao procedure = new ProcedureDao(100, "test", "test");
            activityService.assignProcedure(procedure, activityService.getActivity(1));
        });
    }

    @Test
    void assignProcedureActivityNotPresent() {
        assertThrows(NotFoundException.class, () -> {
            ActivityDao activity = new ActivityDao();
            activity.setId(100);
            activity.setEstimatedTime(15);
            activity.setWeek(1);
            activityService.assignProcedure(procedureService.getProcedure(6), activity);
        });
    }

    @Test
    void assignProcedureNullProcedure() {
        assertThrows(NotValidTypeException.class, () -> {
           activityService.assignProcedure(null, activityService.getActivity(1));
        });
    }

    @Test
    void assignProcedureNullActivity() {
        assertThrows(NotValidTypeException.class, () -> {
            activityService.assignProcedure(procedureService.getProcedure(6), null);
        });
    }

    @Test
    void assignProcedureEverythingGood() {
        int chosenActivityID = 1;
        ProcedureDao chosenProcedure = procedureService.getProcedure(6);
        activityService.assignProcedure(
                chosenProcedure,
                activityService.getActivity(chosenActivityID)
        );
        ActivityDao retrievedActivity = activityService.getActivity(chosenActivityID);
        assertEquals(chosenProcedure, retrievedActivity.getProcedure());
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
                    i,
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
