package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.service.impl.ProcedureService;
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

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetProcedureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ProcedureService procedureService;

    @Autowired
    public void setMyService(ProcedureService myService) {
        this.procedureService = myService;
    }

    @BeforeEach
    private void init() {
        for(int i = 1; i < 5; i++) {
            ProcedureDao procedureDao = new ProcedureDao();
            procedureDao.setId(i);
            procedureDao.setTitle("myTitle" + i);
            procedureDao.setDescription("myDescription" + i);
            procedureService.generateProcedure(procedureDao);
        }
    }

    @Test
    void shouldGetProcedureFindIt() throws Exception {
        this.mockMvc.perform(get("/procedure").param("id","2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("myTitle2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("myDescription2"));
    }

    @Test
    void shouldGetProcedures() throws Exception  {
        this.mockMvc.perform(get("/procedures"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("myTitle1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("myDescription1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("myTitle2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("myDescription2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("myTitle3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value("myDescription3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].title").value("myTitle4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].description").value("myDescription4"));
    }

    @Test
    void shouldRespondBadRequestIfGetProcedureWithoutParameter() throws Exception {
        this.mockMvc.perform(get("/procedure")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfGetProcedureWithWrongTypeParameterValue() throws Exception {
        this.mockMvc.perform(get("/procedure").param("id","aaa")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondBadRequestIfGetProcedureWithNegativeId() throws Exception {
        this.mockMvc.perform(get("/procedure").param("id","-222")).andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    void shouldRespondNotFoundIfGetProcedureThatNotExist() throws Exception {
        this.mockMvc.perform(get("/procedure").param("id","100000")).andDo(print()).andExpect(status().isNotFound());
    }

}