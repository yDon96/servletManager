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

    private UserDao mantanier;

    @BeforeEach
    private void init() {
        ProcedureDao procedureDao = new ProcedureDao();
        UserDao userDao = new UserDao();
        procedureDao.setTitle("t");
        userDao.setName("a");
        userDao.setSurname("s");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        procedure =  iProcedureRepository.save(procedureDao);
        mantanier = iUserRepository.save(userDao);
        activityDaoList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            ActivityDao activityDao = new ActivityDao();
            activityDao.setWeek(5 + i);
            activityDao.setInterruptable(i % 2 == 0 ? true : false);
            activityDao.setEstimatedTime(60 + i * 10);
            activityDao.setDescription("Work" + i);
            activityDao.setProcedure(procedure);
            activityDao.setMaintainer(mantanier);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mantainerId").value(mantanier.getUser_id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.week").value(activityDaoList.get(2).getWeek().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.interruptable").value(activityDaoList.get(2).isInterruptable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedTime").value(activityDaoList.get(2).getEstimatedTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(activityDaoList.get(2).getDescription()));
    }

    @Test
    void shouldRespondNotFoundIfGetActivityThatNotExist() throws Exception {
        this.mockMvc.perform(get("/activity").param("activityId","100000")).andDo(print()).andExpect(status().isNotFound());
    }


}
