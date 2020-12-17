package com.daayCyclic.servletManager.controller;

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
class PostProcedureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldPostProcedure() throws Exception {
        this.mockMvc.perform(post("/procedure")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getContentFormatted(1,"title","description")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostProcedureWithoutAnId() throws Exception {
        this.mockMvc.perform(post("/procedure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(null,"title","description")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPostProcedureWithoutADescription() throws Exception {
        this.mockMvc.perform(post("/procedure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,"title",null)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondBadRequestIfPostProcedureWithoutBody() throws Exception {
        this.mockMvc.perform(post("/procedure"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestPostProcedureWithoutATitle() throws Exception {
        this.mockMvc.perform(post("/procedure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFormatted(1,null,"description")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfPostProcedureWithWrongBody() throws Exception {
        this.mockMvc.perform(post("/procedure")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"p_id\":\"1\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String getContentFormatted(Integer id, String title, String description){
        String json = "{";
        int numberOfContent = 0;
        if (id != null){
            json += "\"id\":\"" + id + "\"";
            numberOfContent += 1;
        }

        if (title != null){
            if (numberOfContent > 0){
                json += ",";
            }
            json += "\"title\":\"" + title + "\"";
            numberOfContent += 1;
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