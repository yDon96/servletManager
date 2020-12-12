package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.*;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import com.daayCyclic.servletManager.service.IProcedureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignCompetencyToProcedureServiceTest {

    @Autowired
    private ICompetencyService iCompetencyService;

    @Autowired
    private ICompetencyRepository iCompetencyRepository;

    @Autowired
    private IProcedureService iProcedureService;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    private CompetencyDao competency;

    private ProcedureDao procedureDao;

    private List<CompetencyDao> competencyDaoList;

    @BeforeEach
    private void init() {
        createCompetencyDB();
        createProcedureDB();
    }

    @Test
    void assignCompetencyNotPresentCompetency() {
        assertThrows(NotFoundException.class, () -> {
            CompetencyDao competencyDao = new CompetencyDao(121,"competency");
            iProcedureService.assignCompetencyToProcedure(competencyDao,iProcedureService.getProcedure(17));
        });
    }

    @Test
    void assignCompetencyNotPresentProcedure() {
        assertThrows(NotFoundException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(iCompetencyService.getCompetency("competency n°29"),
                    iProcedureService.getProcedure(52));
        });
    }

    @Test
    void assignCompetencyWithNullProcedure() {
        assertThrows(NotValidTypeException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(iCompetencyService.getCompetency("competency n°27"), null);
        });
    }

    //TODO : non trova nessuna procedura
    @Test
    void assignCompetencyWithNullCompetencyField() {
        assertThrows(NotValidTypeException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(null, iProcedureService.getProcedure(17));
        });
    }

    @Test
    void assignCompetencyToProcedureOk() {
        assertDoesNotThrow(() ->
                iProcedureService.assignCompetencyToProcedure(iCompetencyService.getCompetency("competency n°25"),
                        iProcedureService.getProcedure(18)));
    }

    private void createCompetencyDB(){
        for(int i = 25; i < 30; i++) {
            CompetencyDao competencyDao = new CompetencyDao();
            competencyDao.setCompetencyId(i);
            competencyDao.setName("competency n°" + i);
            iCompetencyRepository.save(competencyDao);
        }
    }

    private void createProcedureDB(){
        for(int i = 17; i < 23; i++) {
            ProcedureDao procedureDao = new ProcedureDao();
            procedureDao.setId(i);
            procedureDao.setTitle("t " + i);
            procedureDao.setDescription("d " + i);
            iProcedureRepository.save(procedureDao);
        }
    }
}
