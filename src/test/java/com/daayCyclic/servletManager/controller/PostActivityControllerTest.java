package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void shouldPostActivity() throws Exception {
        this.mockMvc.perform(post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1, "definition", 50, true, 5, new ProcedureDao(23, "title", "description"), new UserDao())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String getContentFormatted(Integer id, String description, Integer estimatedTime, boolean isInterruptable, Integer week, ProcedureDao procedure, UserDao maintainer){
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

        if (procedure != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"procedure\":\"" + procedure + "\"";
        }

        if (maintainer != null){
            if (numberOfContent > 0) {
                json += ",";
            }
            json += "\"maintainer\":\"" + maintainer + "\"";
        }

        json += "}";

        return json;
    }

}
