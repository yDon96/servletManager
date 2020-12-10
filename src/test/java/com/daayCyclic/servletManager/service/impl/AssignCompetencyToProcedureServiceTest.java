package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.*;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
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

    private CompetencyDao competency;

    private ProcedureDao procedureDao;

    private List<CompetencyDao> competencyDaoList;

    @BeforeEach
    private void init() {
        addProcedure();
        addCompetency();
    }

    @Test
    void assignCompetencyNotPresent() {
        assertThrows(NotFoundException.class, () -> {
            CompetencyDao competencyDao = new CompetencyDao(121,"competency");
            iProcedureService.assignCompetencyToProcedure(competencyDao,procedureDao);
        });
    }

    @Test
    void assignCompetencyWithNullProcedure() {
        assertThrows(NotValidTypeException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(competencyDaoList.get(2), null);
        });
    }

    @Test
    void assignCompetencyWithNullCompetencyField() {
        assertThrows(NotValidTypeException.class, () -> {
            iProcedureService.assignCompetencyToProcedure(null, procedureDao);
        });
    }

    @Test
    void containCompetency() {
        CompetencyDao competencyDao = competencyDaoList.get(1);
        iProcedureService.assignCompetencyToProcedure(competencyDao,procedureDao);
        Set<CompetencyDao> set = new LinkedHashSet<CompetencyDao>();
        set.addAll(procedureDao.getCompetencies());
        assertTrue(set.contains(competencyDaoList.get(1)));
    }

    private void addProcedure(){
        procedureDao = new ProcedureDao();
        procedureDao.setId(1);
        procedureDao.setTitle("t");
        procedureDao.setDescription("d");
    }

    private void addCompetency(){
        competencyDaoList = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            CompetencyDao competencyDao = new CompetencyDao();
            competencyDao.setCompetencyId(i);
            competencyDao.setName("competency n°" + i);
            competencyDaoList.add(iCompetencyRepository.save(competencyDao));
        }
    }
}
