package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IActivityRepository;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.impl.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
        ;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IActivityRepository iActivityRepository;

    private List<ActivityDao> activityDaoList;

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
        activityDaoList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            ActivityDao activityDao = new ActivityDao();
            activityDao.setWeek(5 + i);
            activityDao.setInterruptable(i % 2 == 0 ? true : false);
            activityDao.setEstimatedTime(60 + i * 10);
            activityDao.setDescription("Work" + i);
            activityDao.setProcedure(procedure);
            activityDao.setMaintainer(maintainer);
            activityDaoList.add(iActivityRepository.save(activityDao));
        }
    }

    @Test
    void shouldGetActivityFindIt() throws Exception {
        this.mockMvc.perform(get("/activity").param("activityId",String.valueOf(activityDaoList.get(2).getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(activityDaoList.get(2).getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.procedureId").value(procedure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maintainerId").value(maintainer.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.week").value(activityDaoList.get(2).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.interruptable").value(activityDaoList.get(2).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedTime").value(activityDaoList.get(2).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(activityDaoList.get(2).getDescription()));
    }

    @Test
    void shouldGetActivitiesByWeek() throws Exception {
        this.mockMvc.perform(get("/activitiesWeek").param("week",String.valueOf(activityDaoList.get(0).getWeek())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(activityDaoList.get(0).getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].procedureId").value(procedure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maintainerId").value(maintainer.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].week").value(activityDaoList.get(0).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].interruptable").value(activityDaoList.get(0).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].estimatedTime").value(activityDaoList.get(0).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(activityDaoList.get(0).getDescription()));
    }

    @Test
    void shouldGetActivities() throws Exception  {
        this.mockMvc.perform(get("/activities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(activityDaoList.get(0).getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].procedureId").value(procedure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maintainerId").value(maintainer.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].week").value(activityDaoList.get(0).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].interruptable").value(activityDaoList.get(0).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].estimatedTime").value(activityDaoList.get(0).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(activityDaoList.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(activityDaoList.get(1).getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].procedureId").value(procedure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].maintainerId").value(maintainer.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].week").value(activityDaoList.get(1).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].interruptable").value(activityDaoList.get(1).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].estimatedTime").value(activityDaoList.get(1).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(activityDaoList.get(1).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(activityDaoList.get(2).getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].procedureId").value(procedure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].maintainerId").value(maintainer.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].week").value(activityDaoList.get(2).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].interruptable").value(activityDaoList.get(2).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].estimatedTime").value(activityDaoList.get(2).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(activityDaoList.get(2).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(activityDaoList.get(3).getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].procedureId").value(procedure.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].maintainerId").value(maintainer.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].week").value(activityDaoList.get(3).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].interruptable").value(activityDaoList.get(3).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].estimatedTime").value(activityDaoList.get(3).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].description").value(activityDaoList.get(3).getDescription()));
    }

    @Test
    void shouldRespondBadRequestIfGetActivityWithoutParameter() throws Exception {
        this.mockMvc.perform(get("/activity")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfGetActivityWithWrongTypeParameterValue() throws Exception {
        this.mockMvc.perform(get("/activity").param("activityId","i")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfGetActivityWithNegativeId() throws Exception {
        this.mockMvc.perform(get("/activity").param("activityId","-222")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondNotFoundIfGetActivityThatNotExist() throws Exception {
        this.mockMvc.perform(get("/activity").param("activityId","100000")).andDo(print()).andExpect(status().isNotFound());
    }

}
