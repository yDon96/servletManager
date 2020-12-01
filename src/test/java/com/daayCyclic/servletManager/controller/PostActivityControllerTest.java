package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
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

    private ProcedureDao procedure;

    private UserDao maintainer;

    @BeforeEach
    void init(){
        ProcedureDao procedureDao = new ProcedureDao();
        UserDao userDao = new UserDao();
        procedureDao.setTitle("ttt");
        userDao.setName("aaa");
        userDao.setSurname("sss");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        procedure =  iProcedureRepository.save(procedureDao);
        maintainer = iUserRepository.save(userDao);
    }

    @Test
    void shouldPostActivity() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", 50, true, 5, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeId() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(-225,"ddd", 50, true, 5, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeWeek() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, -1, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeProcedure() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, -procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeMaintainer() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"ddd", 50, true, 5, procedure.getId(), -maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPostActivityWithoutAnId() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(null,"ddd",50,true,5, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostActivityWithoutADescription() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, null, 50, true, 5, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostActivityWithoutAWeek() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", 50, true, null, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostActivityWithoutAEstimateTime() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", null, true, 5, procedure.getId(), maintainer.getUser_id())))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    void shouldPostActivityWithoutAProcedureId() throws Exception {
//        this.mockMvc.perform(post("/activity")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(getContentFormatted(1, "ddd", 50, true, 5, null, maintainer.getUser_id())))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldPostActivityWithoutAMaintainerId() throws Exception {
//        this.mockMvc.perform(post("/activity")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(getContentFormatted(1, "ddd", 50, true, 5, procedure.getId(), null)))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    void shouldRespondBadRequestPostActivityWithNegativeEstimateTime() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "ddd", -1, true, 5, procedure.getId(), maintainer.getUser_id())))
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
    void shouldRespondeBadRequestIfPostActivityWithWrongBody() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"a_id\":\"1\"}"))
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

}
