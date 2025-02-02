package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private IRoleService roleService;

    private ProcedureDao procedure;

    private UserDao maintainer;

    @BeforeEach
    void init(){
        ProcedureDao procedureDao = new ProcedureDao();
        procedureDao.setTitle("ttt");
        procedure =  iProcedureRepository.save(procedureDao);

        RoleDao newRole = new RoleDao();
        newRole.setId(1);
        newRole.setName("Maintainer");
        this.roleService.generateRole(newRole);

        UserDao userDao = new UserDao();
        userDao.setName("aaa");
        userDao.setSurname("sss");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        userDao.setRole(newRole);
        maintainer = iUserRepository.save(userDao);
    }

    @Test
    void shouldPostActivity() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", 50, true, 5, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostActivityWithDayAndHour() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, 7,21,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithDayNotCorrect() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, 9,17,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithHourNotCorrect() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, 2,29,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithWeekNotCorrect() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 60, 4,11,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithDayNegative() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 60, -4,11,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithHourNegative() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 60, 4,-11,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithHourNull() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 60, 4,null,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithDayNull() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 60, null,14,procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPostActivityWithNegativeId() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(-225,"ddd", 50, true, 5, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeWeek() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, -1, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondNotFoundPostActivityWithNegativeProcedure() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, -procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondNotFoundPostActivityWithNegativeMaintainer() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, procedure.getId(), -maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostActivityWithoutAnId() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(null,"ddd",50,true,5, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostActivityWithoutADescription() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, null, 50, true, 5, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithoutAWeek() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", 50, true, null, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithoutAEstimateTime() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", null, true, 5, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPostActivityWithoutAProcedureId() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", 50, true, 5, null, maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostActivityWithoutAMaintainerId() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", 50, true, 5, procedure.getId(), null)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeEstimateTime() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", -1, true, 5, procedure.getId(), maintainer.getUserId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfPostActivityWithoutBody() throws Exception {
        this.mockMvc.perform(post("/activity"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfPostActivityWithWrongBody() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"aaa_id\":\"1\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String getContentFormatted(Integer id, String description, Integer estimatedTime, boolean isInterruptable, Integer week, Integer procedureId, Integer maintainerId){
        String json = "{";
        int numberOfContent = 0;
        if (id != null){
            json += "\"id\":\"" + id + "\"";
            numberOfContent += 1;
        }

        if (description != null){
            if (numberOfContent > 0){
                json += ",";
            }
            json += "\"description\":\"" + description + "\"";
            numberOfContent += 1;
        }

        if (estimatedTime != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"estimatedTime\":\"" + estimatedTime + "\"";
        }

        if (numberOfContent > 0) {
            json += ",";
        }
        json += "\"isInterruptable\":\"" + isInterruptable + "\"";

        if (week != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"week\":\"" + week + "\"";
        }

        if (procedureId != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"procedureId\":\"" + procedureId + "\"";
        }

        if (maintainerId != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"maintainerId\":\"" + maintainerId + "\"";
        }

        json += "}";

        return json;
    }

    private String getContentFormatted(Integer id, String description, Integer estimatedTime, boolean isInterruptable, Integer week, Integer startingDay, Integer startingHour, Integer procedureId, Integer maintainerId){
        String json = "{";
        int numberOfContent = 0;
        if (id != null){
            json += "\"id\":\"" + id + "\"";
            numberOfContent += 1;
        }

        if (description != null){
            if (numberOfContent > 0){
                json += ",";
            }
            json += "\"description\":\"" + description + "\"";
            numberOfContent += 1;
        }

        if (estimatedTime != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"estimatedTime\":\"" + estimatedTime + "\"";
        }

        if (numberOfContent > 0) {
            json += ",";
        }
        json += "\"isInterruptable\":\"" + isInterruptable + "\"";

        if (week != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"week\":\"" + week + "\"";
        }

        if (startingDay != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"startingDay\":\"" + startingDay + "\"";
        }

        if (startingHour != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"startingHour\":\"" + startingHour + "\"";
        }

        if (procedureId != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"procedureId\":\"" + procedureId + "\"";
        }

        if (maintainerId != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"maintainerId\":\"" + maintainerId + "\"";
        }

        json += "}";

        return json;
    }
}


