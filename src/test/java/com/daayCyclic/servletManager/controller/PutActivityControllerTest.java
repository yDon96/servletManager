package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PutActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IActivityService iActivityService;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private IUserRepository iUserRepository;

    private ProcedureDao procedure;

    private UserDao maintainer;

    @BeforeEach
    private void init() {
        ActivityDao activityDao = new ActivityDao();
        ProcedureDao procedureDao = new ProcedureDao(1, "t", "dd");
        UserDao userDao = new UserDao("n", "s", LocalDate.of(2000, 1, 1), null);
        activityDao.setId(1);
        activityDao.setDescription("d");
        activityDao.setEstimatedTime(50);
        activityDao.setInterruptable(true);
        activityDao.setWeek(5);
        procedure = iProcedureRepository.save(procedureDao);
        maintainer = iUserRepository.save(userDao);
        activityDao.setMaintainer(maintainer);
        activityDao.setProcedure(procedure);
        iActivityService.generateActivity(activityDao);
    }
    @Test
    void shouldPutActivity() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,maintainer.getUser_id(),procedure.getId(),5,true,50,"d")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String getContentFormatted(Integer id, Integer maintainerId, Integer procedureId, Integer week, boolean isInterruptable, Integer estimatedTime, String description){
        String json = "{";
        int numberOfContent = 0;
        if (id != null){
            json += "\"id\":\"" + id + "\"";
            numberOfContent += 1;
        }

        if (maintainerId != null){
            if (numberOfContent > 0){
                json += ",";
            }
            json += "\"maintainerId\":\"" + maintainerId + "\"";
            numberOfContent += 1;
        }

        if (procedureId != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"procedureId\":\"" + procedureId + "\"";
        }

        if (week != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"week\":\"" + week + "\"";
        }

        if (numberOfContent > 0) {
            json += ",";
        }
        json += "\"isInterruptable\":\"" + isInterruptable + "\"";

        if (estimatedTime != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"estimatedTime\":\"" + estimatedTime + "\"";
        }

        if (description != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"description\":\"" + description + "\"";
        }

        json += "}";

        return json;
    }
}
