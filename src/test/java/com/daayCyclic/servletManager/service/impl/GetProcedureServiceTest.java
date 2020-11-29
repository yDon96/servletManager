package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetProcedureServiceTest {

    private ProcedureService procedureService;

    @Autowired
    public void setMyService(ProcedureService myService) {
        this.procedureService = myService;
    }

    private List<ProcedureDao> daoProcedures;

    @BeforeEach
    private void init() {
        daoProcedures = new ArrayList<>();
        for(int i = 1; i < 5; i++) {
            ProcedureDao procedureDao = new ProcedureDao();
            procedureDao.setId(i);
            procedureDao.setTitle("myTitle" + i);
            procedureDao.setDescription("myDescription" + i);
            daoProcedures.add(procedureDao);
            procedureService.generateProcedure(procedureDao);
        }
    }

    @Test
    void getProcedure() {
        assertEquals(
                new ProcedureDao(2,"myTitle" + 2,"myDescription" + 2),
                procedureService.getProcedure(2)
        );
    }

    @Test
    void shouldGetAllProcedures() {
        assertEquals(
                daoProcedures,
                procedureService.getProcedures()
        );
    }

    @Test
    void shouldThrowExceptionIfGetProcedureThatDoNotExist() {
        assertThrows(NotFoundException.class,() -> {
            procedureService.getProcedure(10000);
        });
    }

    @Test
    void shouldReturnFalseIfProcedureIdIsNull() {
        assertFalse(procedureService.procedureExist(null));
    }

    @Test
    void shouldReturnFalseIfProcedureIdIndicateNotExistingValue() {
        assertFalse(procedureService.procedureExist(10000000));
    }
}