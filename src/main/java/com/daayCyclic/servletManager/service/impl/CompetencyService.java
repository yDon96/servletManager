package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(value = "CompetencyService")
public class CompetencyService implements ICompetencyService {

    @Autowired
    private ICompetencyRepository repository;

    /**
     * Create a new competency into the database
     *
     * @param competency the {@literal String} representing the Competency to create
     * @throws NotValidTypeException if the {@literal string} is not valid (null or empty)
     * @throws DuplicateGenerationException if the Competency is already present
     */
    @Override
    public void generateCompetency(String competency) {
        log.info("[SERVICE: Competency] Start inserting into the database the competency: " + competency);
        if (competency == null || competency.equalsIgnoreCase("")) {
            String message = "The given competency is null or empty";
            log.info("[SERVICE: Competency] " + message);
            throw new NotValidTypeException(message);
        }
        competency = competency.toUpperCase();  // Make every string inserted into the DB upper case (to avoid inconsistency)
        if (this.competencyExist(competency)) {
            String message = "The given competency already exist into the database";
            log.info("[SERVICE: Competency] " + message);
            throw new DuplicateGenerationException(message);
        }
        CompetencyDao newCompetency = new CompetencyDao(0, competency);
        repository.save(newCompetency);
        log.info("[SERVICE: Competency] Insert of the new competency completed successfully");
    }

    @Override
    public CompetencyDao getCompetency(String competency) {
        log.info("[SERVICE: Competency] Start retrieving from the database the competency: " + competency);
//        if (competency == null) {
//            String message = "The given competency is null";
//            log.info("[SERVICE: Competency] " + message);
//            throw new NotValidTypeException(message);
//        }
//        competency = competency.toUpperCase();
//        Che succede se non controllo se è null??
        Optional<CompetencyDao> retrievedCompetency = this.repository.findByName(competency);
        if (retrievedCompetency.isEmpty()) {
            String message = "The given competency doesn't exist into the database";
            log.info("[SERVICE: Competency] " + message);
            throw new NotFoundException(message);
        }
        log.info("[SERVICE: Competency] Competency retrieved successfully");
        return retrievedCompetency.get();
    }

    @Override
    public List<CompetencyDao> getCompetencies() {
        log.info("[SERVICE: Competency] Start retrieving all the competencies from the database");
        ArrayList<CompetencyDao> retrievedCompetencies = (ArrayList<CompetencyDao>) this.repository.findAll();
        log.info("[SERVICE: Competency] Competencies retrieved successfully (" + retrievedCompetencies.size() + " elements found)");
        return retrievedCompetencies;
    }

    private boolean competencyExist(String competency) {
        if (competency == null) {
            return false;
        }
        return this.repository.findByName(competency).isPresent();
    }

}
