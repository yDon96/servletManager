package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignCompetencyToProcedureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICompetencyService iCompetencyService;

    @Autowired
    private ICompetencyRepository iCompetencyRepository;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    private ProcedureDao procedure;

    private CompetencyDao competency;

    private Set<CompetencyDao> competencyDaoSet;

    private List<CompetencyDao> competencyDaos;

    private List<ProcedureDao> procedureDaos;

    @BeforeEach
    private void init() {
        createCompetencyDB();
        createProcedureDB();
    }

    @Test
    void assignCompetencyToProcedureNotPresentCompetency(){
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/procedure/14/assign-competency")
                    .param("competency", "competency n°1212"))
                    .andDo(print())
                    .andExpect(status().isNotFound()
                    );
        });
    }

    @Test
    void assignCompetencyToProcedureNotPresentProcedure() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/procedure/100/assign-competency")
                    .param("competency", "competency n°12"))
                    .andDo(print())
                    .andExpect(status().isNotFound()
                    );
        });
    }


    @Test
    void assignCompetencyToProcedureEverythingOk() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/procedure/" + procedureDaos.get(0).getId() + "/assign-competency")
                    .param("competency", "COMPETENCY N°12"))
                    .andDo(print())
                    .andExpect(status().isOk()
                    );
        });
    }


    private void createCompetencyDB(){
        competencyDaos = new LinkedList<>();
        for(int i = 10; i < 16; i++) {
            CompetencyDao competencyDao = new CompetencyDao();
            competencyDao.setCompetencyId(i);
            competencyDao.setName("COMPETENCY N°" + i);
            competencyDaos.add(iCompetencyRepository.save(competencyDao));
        }
    }

    private void createProcedureDB(){
        procedureDaos = new LinkedList<>();
        for(int i = 10; i < 15; i++) {
            ProcedureDao procedureDao = new ProcedureDao();
            procedureDao.setId(i);
            procedureDao.setTitle("t " + i);
            procedureDao.setDescription("d " + i);
            procedureDaos.add(iProcedureRepository.save(procedureDao));
        }
    }

}
