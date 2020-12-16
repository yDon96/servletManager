package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.converter.CompetencyConverter;
import com.daayCyclic.servletManager.service.ICompetencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class CompetencyController {

    @Autowired
    private ICompetencyService competencyService;

    private final CompetencyConverter converter = new CompetencyConverter();

    /**
     * Put a new competency into the server.
     *
     * @param competency the {@literal String} representing the Competency to post
     */
    @PostMapping(path = "/competency")
    public void postCompetency(@RequestParam String competency) {
        log.info("[REST] Start posting competency: " + competency);
        this.competencyService.generateCompetency(this.converter.convertFromDto(competency));
        log.info("[REST] Posting competency completed successfully");
    }

    /**
     * Retrieve from the server all the competencies.
     *
     * @return a {@literal List} of {@literal String} containing all the competencies in the server
     */
    @GetMapping(path = "/competencies")
    public List<String> getCompetencies() {
        log.info("[REST] Start retrieving all the competencies from the server");
        ArrayList<String> retrievedCompetencies = new ArrayList<>(this.converter.createFromEntities(this.competencyService.getCompetencies()));
        log.info("[REST] Competencies retrieved successfully");
        return retrievedCompetencies;
    }

    @GetMapping(path = "/competencies/count")
    public int countUserOwnedCompetenciesRequiredFromProcedure(@RequestParam Integer userId, @RequestParam Integer procedureId) {
        log.info("[REST] Starting request to count required competencies of procedure " + procedureId + " owned by user " + userId);
        int result = this.competencyService.countUserOwnedCompetenciesRequiredFromProcedure(userId, procedureId);
        log.info("[REST] Request completed successfully");
        return result;
    }

}
