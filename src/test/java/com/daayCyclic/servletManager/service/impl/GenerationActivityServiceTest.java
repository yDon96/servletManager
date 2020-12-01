package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenerationActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    private ActivityDao activityDao;


    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    private ProcedureDao procedure;

    private UserDao mantanier;

    @BeforeEach
    void init(){
        activityDao = new ActivityDao();
        ProcedureDao procedureDao = new ProcedureDao();
        UserDao userDao = new UserDao();
        procedureDao.setTitle("t");
        userDao.setName("a");
        userDao.setSurname("s");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        procedure =  iProcedureRepository.save(procedureDao);
        mantanier = iUserRepository.save(userDao);
    }

    @Test
    void shouldGenerateActivityWithoutSetId() {
        setActivityDao(null, mantanier, procedure,5,true,50,"description");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivity() {
        setActivityDao(1, mantanier, procedure,5,true,50,"description");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithAnExistingId() {
        Integer id = generateActivityToTestDuplicateEntry();
        assertThrows(DuplicateGenerationException.class, () -> {
            activityDao = new ActivityDao();
            setActivityDao(id, null, null,5,true,50,"definition");
            activityService.generateActivity(activityDao);
        });
    }

    private Integer generateActivityToTestDuplicateEntry(){
        setActivityDao(1, mantanier, procedure,6,false,60,"description1");
        return activityService.generateActivity(activityDao);
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
