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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private List<ProcedureDao> procedureDaos;

    private List<CompetencyDao> competencyDaos;

    @BeforeEach
    private void init() {
        createCompetencyDB();
        createProcedureDB();
    }

    @Test
    void assignCompetencyNotPresentCompetency() {
        assertThrows(NotFoundException.class, () -> {
            CompetencyDao competencyDao = new CompetencyDao(121,"competency");
            iProcedureService.assignCompetencyToProcedure(competencyDao,procedureDaos.get(0));
        });
    }

    @Test
    void assignCompetencyNotPresentProcedure() {
        assertThrows(NotFoundException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(competencyDaos.get(4),
                    iProcedureService.getProcedure(52));
        });
    }

    @Test
    void assignCompetencyWithNullProcedure() {
        assertThrows(NotValidTypeException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(competencyDaos.get(2), null);
        });
    }

    @Test
    void assignCompetencyWithNullCompetencyField() {
        assertThrows(NotValidTypeException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(null, procedureDaos.get(1));
        });
    }

    @Test
    void assignCompetencyToProcedureOk() {
        assertDoesNotThrow(() ->
                iProcedureService.assignCompetencyToProcedure(competencyDaos.get(0),procedureDaos.get(2)));
    }

    @Transactional
    private void createCompetencyDB(){
        competencyDaos = new LinkedList<>();
        for(int i = 25; i < 30; i++) {
            CompetencyDao competencyDao = new CompetencyDao();
            competencyDao.setCompetencyId(i);
            competencyDao.setName("competency n°" + i);
            competencyDaos.add(iCompetencyRepository.save(competencyDao));
        }
    }

    @Transactional
    private void createProcedureDB(){
        procedureDaos = new LinkedList<>();
        for(int i = 17; i < 23; i++) {
            ProcedureDao procedureDao = new ProcedureDao();
            procedureDao.setId(i);
            procedureDao.setTitle("t " + i);
            procedureDao.setDescription("d " + i);
            procedureDaos.add(iProcedureRepository.save(procedureDao));
            ;
        }
    }
}
