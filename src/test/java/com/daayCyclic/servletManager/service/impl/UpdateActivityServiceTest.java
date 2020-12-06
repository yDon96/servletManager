package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.service.IActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UpdateActivityServiceTest {

    @Autowired private IActivityService activityService;

    @BeforeEach
    void setUp() {
        addActivities();
    }

    @Test
    void updateActivityNullActivity() {
        assertThrows(NotValidTypeException.class, () -> {
            activityService.updateActivity(null);
        });
    }

    @Test
    void updateActivityActivityNotPresent() {
        assertThrows(NotFoundException.class, () -> {
            ActivityDao changedActivity = new ActivityDao(
                    100,
                    "description",
                    1,
                    true,
                    2,
                    null,
                    null);
            activityService.updateActivity(changedActivity);
        });
    }

    @Test
    void updateActivityEverythingGood() {
        ActivityDao changedActivity = new ActivityDao(
                1,
                "changedDescription",
                1,
                false,
                25,
                null,
                null);
        ActivityDao oldActivity = activityService.getActivity(1);
        activityService.updateActivity(changedActivity);
        ActivityDao updatedActivity = activityService.getActivity(1);
        assertNotEquals(oldActivity, updatedActivity);
        assertEquals(changedActivity, updatedActivity);
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

}
