package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenerateCompetencyServiceTest {

    @Autowired
    private ICompetencyRepository repository;

    @Autowired
    private ICompetencyService service;

    @BeforeEach
    void setUp() {
        CompetencyDao newCompetency = new CompetencyDao(0, "ECDL");
        repository.save(newCompetency);
    }

    @Test
    void generateCompetencyNull() {
        assertThrows(NotValidTypeException.class, () -> service.generateCompetency(null));
    }

    @Test
    void generateCompetencyEmptyString() {
        assertThrows(NotValidTypeException.class, () -> service.generateCompetency(""));
    }

    @Test
    void generateCompetencyAlreadyPresent() {
        assertThrows(DuplicateGenerationException.class, () -> service.generateCompetency("ECDL"));
    }

    @Test
    void generateCompetencyAlreadyPresentLowerCase() {
        assertThrows(DuplicateGenerationException.class, () -> service.generateCompetency("ecdl"));
    }

    @Test
    void generateCompetencyEverythingGood() {
        String newCompetency = "iOS Academy";
        service.generateCompetency(newCompetency);
        String searchedCompetency = newCompetency.toUpperCase();
        assertEquals(searchedCompetency, repository.findByName(searchedCompetency).get().getName());
    }

}
