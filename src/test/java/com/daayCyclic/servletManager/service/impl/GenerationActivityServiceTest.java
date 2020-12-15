package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

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

    private UserDao maintainer;

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
        maintainer = iUserRepository.save(userDao);
    }

    @Test
    void shouldGenerateActivityWithoutSetId() {
        setActivityDao(null, maintainer, procedure,5,true,50,"ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivityWithoutSetDescription() {
        setActivityDao(1, maintainer, procedure,5,true,50,null);
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivityWithoutSetProcedure() {
        setActivityDao(1, maintainer, null,5,true,50,"ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivityWithoutSetMaintainer() {
        setActivityDao(1, null, procedure,5,true,50,"ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivity() {
        setActivityDao(1, maintainer, procedure,5,true,50,"ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivityWithHourAndDay() {
        setActivityDao(1, maintainer, procedure, 5, 1, 21, true, 50, "ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithWeekNotValid() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, 411, 1, 21, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithWeekNull() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, null, 1, 21, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithWeekNegative() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, -11, 1, 21, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithHourNotValid() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, 51, 1, 33, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithHourNegative() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, 51, 1, -33, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithDayNotValid() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, 51, 41, 23, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithDayNegative() {
        assertThrows(NotValidTypeException.class, () -> {
            setActivityDao(1, maintainer, procedure, 51, -41, 23, true, 50, "ddd");
            activityService.generateActivity(activityDao);
        });
    }

    @Test
    void shouldGenerateActivityWithDayIsNull() {
        setActivityDao(1, maintainer, procedure, 5, null, 21, true, 50, "ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldGenerateActivityWithHourIsNull() {
        setActivityDao(1, maintainer, procedure, 5, 2, null, true, 50, "ddd");
        activityService.generateActivity(activityDao);
    }

    @Test
    void shouldThrowExceptionWhenGenerateActivityWithAnExistingId() {
        Integer id = generateActivityToTestDuplicateEntry();
        assertThrows(DuplicateGenerationException.class, () -> {
            activityDao = new ActivityDao();
            setActivityDao(id, null, null,5,true,50,"ddd");
            activityService.generateActivity(activityDao);
        });
    }

    private Integer generateActivityToTestDuplicateEntry(){
        setActivityDao(1, maintainer, procedure,6,false,60,"ddd");
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

    private void setActivityDao(Integer id, UserDao maintainer, ProcedureDao procedure, Integer week, Integer startingDay, Integer startingHour, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDao.setId(id);
        activityDao.setMaintainer(maintainer);
        activityDao.setProcedure(procedure);
        activityDao.setWeek(week);
        activityDao.setStartingDay(startingDay);
        activityDao.setStartingHour(startingHour);
        activityDao.setInterruptable(isInterruptable);
        activityDao.setEstimatedTime(estimatedTime);
        activityDao.setDescription(description);
    }
}
