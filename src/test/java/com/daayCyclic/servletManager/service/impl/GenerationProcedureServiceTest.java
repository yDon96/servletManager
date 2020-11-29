package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenerationProcedureServiceTest {

    private ProcedureService procedureService;

    private ProcedureDao procedureDao;

    @Autowired
    public void setMyService(ProcedureService myService) {
        this.procedureService = myService;
    }

    @BeforeEach
    private void init(){
        procedureDao = new ProcedureDao();
    }

    @Test
    void shouldGenerateProcedureWithoutSetId() {
        setProcedureDao(null,"myTitle","myDescription");
        procedureService.generateProcedure(procedureDao);
    }

    @Test
    void shouldGenerateProcedureWithANotExistingId() {
        setProcedureDao(1,"myTitle","myDescription");
        procedureService.generateProcedure(procedureDao);
    }

    @Test
    void shouldThrowExceptionWhenGenerateProcedureWithAnExistingId() {
        generateProcedureToTestDuplicateEntry();
        assertThrows(DuplicateGenerationException.class, () -> {
            procedureDao = new ProcedureDao();
            setProcedureDao(1,"myNewTitle","myNewDescription");
            procedureService.generateProcedure(procedureDao);
        });
    }

    private void generateProcedureToTestDuplicateEntry(){
        setProcedureDao(1,"myTitle","myDescription");
        procedureService.generateProcedure(procedureDao);
    }

    private void setProcedureDao(Integer id,String title,String description) {
        procedureDao.setId(id);
        procedureDao.setTitle(title);
        procedureDao.setDescription(description);
    }

}