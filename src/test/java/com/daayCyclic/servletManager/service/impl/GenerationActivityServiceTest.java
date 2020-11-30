package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenerationActivityServiceTest {

    private ActivityService activityService;

    private ActivityDao activityDao;

    @Autowired
    public void setMyService(ActivityService myService) {
        this.activityService = myService;
    }

    @BeforeEach
    private void init(){
        activityDao = new ActivityDao();
    }

    @Test
    void shouldGenerateActivityWithoutSetId() {
        setActivityDao(null, new UserDao(), new ProcedureDao(),5,true,50,"definition");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivity() {
        setActivityDao(1, new UserDao(), new ProcedureDao(),5,true,50,"definition");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithAnExistingId() {
        generateActivityToTestDuplicateEntry();
        assertThrows(DuplicateGenerationException.class, () -> {
            activityDao = new ActivityDao();
            setActivityDao(1, new UserDao(), new ProcedureDao(1,"title", "description"),5,true,50,"definition");
            activityService.generateActivity(activityDao);
        });
    }

    private void generateActivityToTestDuplicateEntry(){
        setActivityDao(1, new UserDao(), new ProcedureDao(),6,false,60,"definition1");
        activityService.generateActivity(activityDao);
    }



    private void setActivityDao(Integer id, UserDao maintainer, ProcedureDao procedure, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDao.setId(id);
        activityDao.setMaintainer(maintainer);
        activityDao.setProcedure(procedure);
        activityDao.setWeek(week);
        activityDao.setInterruptable(isInterruptable);
        activityDao.setEstimatedTime(estimatedTime);
        activityDao.setDescription(description);
    }
}
