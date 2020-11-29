package com.daayCyclic.servletManager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerPostTest extends UserControllerTest {

    @Test
    void postUserNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/user/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFormatted(10, "Domenico", "Barra", "1996-10-07", "System Administrator")))
                    .andDo(print())
                    .andExpect(status().isOk());
        });
    }

    @Test
    void postUserAlreadyPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/user/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFormatted(1, "Domenico", "Barra", "1996-10-07", "System Administrator")))
                    .andDo(print())
                    .andExpect(status().isOk());
        });
    }

    @Test
    void postUserNotValid() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/user/post")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    private String getContentFormatted(int id, String name, String surname, String dateOfBirth, String role){
        return "{\"user_id\":\"" + id
                + "\",\"name\":\"" + name
                + "\",\"surname\":\"" + surname
                + "\",\"dateOfBirth\":\"" + dateOfBirth
                + "\",\"role\":\"" + role
                + "\"}";
    }

}
