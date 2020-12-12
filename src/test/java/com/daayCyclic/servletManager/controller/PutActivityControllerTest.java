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
        createActivityDB();
    }

    @Test
    void shouldPutActivity() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(56,maintainer.getUser_id(),procedure.getId(),5,true,50,"d 47")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondNotFoundIdActivityNotPresent() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(85,maintainer.getUser_id(),procedure.getId(),5,true,50,"d 47")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondBadRequestIdActivityNull() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(null,maintainer.getUser_id(),procedure.getId(),5,true,50,"d 47")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private void createActivityDB(){
        ActivityDao activityDao = new ActivityDao();
        activityDao.setId(56);
        activityDao.setDescription("d 56");
        activityDao.setEstimatedTime(45);
        activityDao.setInterruptable(true);
        activityDao.setWeek(25);
        activityDao.setMaintainer(createMaintainerDB());
        activityDao.setProcedure(createProcedureDB());
        iActivityService.generateActivity(activityDao);
    }

    private ProcedureDao createProcedureDB(){
        ProcedureDao procedureDao = new ProcedureDao(41, "t 41", "d 41");
        procedure =  iProcedureRepository.save(procedureDao);
        return procedureDao;
    }

    private UserDao createMaintainerDB(){
        UserDao userDao = new UserDao(78,"n 78", "s 78", LocalDate.of(1999, 11, 11), null);
        maintainer = iUserRepository.save(userDao);
        return userDao;
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
        json += "interruptable\":\"" + isInterruptable + "\"";

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
