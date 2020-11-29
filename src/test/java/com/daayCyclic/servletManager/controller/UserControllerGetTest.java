package com.daayCyclic.servletManager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerGetTest extends UserControllerTest {

    @Test
    void getUserPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get").param("userId", "3"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value("3"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("melone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("System Administrator"));
        });
    }

    @Test
    void getUserNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get").param("userId", "100"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        });
    }

    @Test
    void getUsersEmptyRolesList() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get-many").param("roles", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].user_id").value("1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("mario"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("rossi"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("System Administrator"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].user_id").value("2"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("giacomo"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("ciccio"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value("System Administrator"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].user_id").value("3"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].surname").value("melone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].role").value("System Administrator"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].user_id").value("4"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Cosimo"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].surname").value("Leone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].role").value("STUB"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].user_id").value("5"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].surname").value("maddalena"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[4].role").value("Planner")
                    );
        });
    }

    @Test
    void getUsersRoleNotExistingInList() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get-many").param("roles", "Pippo"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0))
                    );
        });
    }

    @Test
    void getUsersGoodRolesList() {
        //TODO: Non funziona finché non si implementano i ruoli (lo fa Amos)
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/user/get-many").param("roles", "Planner,STUB"))
                    .andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].user_id").value("4"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Cosimo"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Leone"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("STUB"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].user_id").value("5"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("maria"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("maddalena"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth").value("1968-01-01"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value("Planner")
                    );
        });
    }

}
