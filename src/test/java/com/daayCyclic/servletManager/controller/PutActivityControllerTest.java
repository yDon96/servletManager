package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IActivityRepository;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IRoleService;
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

import javax.transaction.Transactional;
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
    private IActivityRepository iActivityRepository;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleService roleService;

    private ProcedureDao procedure;

    private UserDao maintainer;

    private ActivityDao activityDao;

    @BeforeEach
    private void init() {
        createActivityDB();
    }

    @Test
    void shouldPutActivity() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(activityDao.getId(),maintainer.getUserId(),procedure.getId(),5,true,50,"d 47")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondNotFoundIdActivityNotPresent() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(85,maintainer.getUserId(),procedure.getId(),5,true,50,"d 47")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondNotFoundWithIdActivityNull() throws Exception {
        this.mockMvc.perform(put("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(null,maintainer.getUserId(),procedure.getId(),5,true,50,"d 47")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Transactional
    private void createActivityDB(){
        ActivityDao activity = new ActivityDao();
        activity.setDescription("d 56");
        activity.setEstimatedTime(45);
        activity.setInterruptable(true);
        activity.setWeek(25);
        activity.setMaintainer(createMaintainerDB());
        activity.setProcedure(createProcedureDB());
        activityDao = iActivityRepository.save(activity);
    }

    @Transactional
    private ProcedureDao createProcedureDB(){
        ProcedureDao procedureDao = new ProcedureDao(41, "t 41", "d 41");
        procedure =  iProcedureRepository.save(procedureDao);
        return procedure;
    }

    @Transactional
    private UserDao createMaintainerDB(){
        RoleDao newRole = new RoleDao();
        newRole.setId(1);
        newRole.setName("Maintainer");
        this.roleService.generateRole(newRole);
        UserDao userDao = new UserDao(78,"n 78", "s 78", LocalDate.of(1999, 11, 11), newRole);
        maintainer = iUserRepository.save(userDao);
        return maintainer;
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
        json += "\"interruptable\":\"" + isInterruptable + "\"";

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
