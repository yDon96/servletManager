package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
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

    private ActivityService activityService;

    @Autowired
    public void setMyService(ActivityService myService) {
        this.activityService = myService;
    }

    @BeforeEach
    private void init() {
        for(int i = 1; i < 5; i++) {
            ActivityDao activityDao = new ActivityDao();
            UserDao mantainer = new UserDao();
            ProcedureDao procedure = new ProcedureDao();
            activityDao.setId(i);
            mantainer.setUser_id(22 + i);
            activityDao.setMaintainer(mantainer);
            procedure.setId(56 + i);
            activityDao.setProcedure(procedure);
            activityDao.setWeek(5+i);
            activityDao.setInterruptable(i % 2 == 0 ? true : false);
            activityDao.setEstimatedTime(60 + i*10);
            activityDao.setDescription("Work" + i);
        }
    }

    @Test
    void shouldGetActivityFindIt() throws Exception {
        this.mockMvc.perform(get("/activity").param("id","2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mantainer").value("24"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.procedure").value("58"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.week").value("7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isInterruptable").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedTime").value("70"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("work2"));
    }
}
