package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.service.ICompetencyService;
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
public class GetCompetencyServiceTest {

    @Autowired
    ICompetencyService service;

    @Test
    void getCompetencyEmptyDB() {
        assertThrows(NotFoundException.class, () -> this.service.getCompetency("ECDL"));
    }

    @Test
    void getCompetencyNotPresent() {
        this.service.generateCompetency("ECDL");
        assertThrows(NotFoundException.class, () -> this.service.getCompetency("competency"));
    }

    @Test
    void getCompetencyNull() {
        assertThrows(NotFoundException.class, () -> this.service.getCompetency(null));
    }

    @Test
    void getCompetencyEmpty() {
        assertThrows(NotFoundException.class, () -> this.service.getCompetency(""));
    }

    @Test
    void getCompetencyPresentLowerCase() {
        String chosenCompetency = "competency1";
        this.service.generateCompetency("ECDL");
        this.service.generateCompetency(chosenCompetency);
        chosenCompetency = chosenCompetency.toUpperCase();
        assertEquals(chosenCompetency, this.service.getCompetency(chosenCompetency).getName());
    }

    @Test
    void getCompetencyPresent() {
        String chosenActivity = "ECDL";
        this.service.generateCompetency(chosenActivity);
        assertEquals(chosenActivity, this.service.getCompetency(chosenActivity).getName());
    }

    @Test
    void getCompetenciesEmptyDB() {
        assertEquals(0, this.service.getCompetencies().size());
    }

    @Test
    void getCompetenciesPresents() {
        for (int i = 0; i < 5; i++) {
            this.service.generateCompetency("competency" + i);
        }
        assertEquals(5, this.service.getCompetencies().size());
    }

}
