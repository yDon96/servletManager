package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.repository.IActivityRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetActivityServiceTest {

    private ActivityService activityService;

    private List<ActivityDao> daoActivities;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    public void setMyService(ActivityService myService) {
        this.activityService = myService;
    }

    @Autowired
    private IActivityRepository iActivityRepository;

    private ProcedureDao procedure;

    private UserDao maintainer;

    @BeforeEach
    private void init() {
        ProcedureDao procedureDao = new ProcedureDao();
        UserDao userDao = new UserDao();
        procedureDao.setTitle("t");
        userDao.setName("a");
        userDao.setSurname("s");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        procedure =  iProcedureRepository.save(procedureDao);
        maintainer = iUserRepository.save(userDao);
        daoActivities = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            ActivityDao activityDao = new ActivityDao();
            activityDao.setWeek(5 + i);
            activityDao.setInterruptable(i % 2 == 0 ? true : false);
            activityDao.setEstimatedTime(60 + i * 10);
            activityDao.setDescription("Work" + i);
            activityDao.setProcedure(procedure);
            activityDao.setMaintainer(maintainer);
            daoActivities.add(iActivityRepository.save(activityDao));
        }
    }

    @Test
    void getActivity() {
        assertEquals(
                new ActivityDao(daoActivities.get(2).getId(),
                        daoActivities.get(2).getDescription(),
                        daoActivities.get(2).getEstimatedTime(),
                        daoActivities.get(2).isInterruptable(),
                        daoActivities.get(2).getWeek(),
                        daoActivities.get(2).getProcedure(),
                        daoActivities.get(2).getMaintainer()),
                activityService.getActivity(daoActivities.get(2).getId()));
    }

    @Test
    void shouldGetAllActivities() {
        assertEquals(
                daoActivities,
                activityService.getActivities()
        );
    }

    @Test
    void shouldThrowExceptionIfGetActivityThatDoNotExist() {
        assertThrows(NotFoundException.class,() -> {
            activityService.getActivity(644553);
        });
    }

    @Test
    void shouldReturnFalseIfActivityIdIsNull() {
        assertFalse(activityService.activityExist(null));
    }

    @Test
    void shouldReturnTrueIfActivityIdExist() {
        assertTrue(activityService.activityExist(daoActivities.get(1).getId()));
    }

    @Test
    void shouldReturnFalseIfActivityIdIndicateNotExistingValue() {
        assertFalse(activityService.activityExist(376635));
    }
}
