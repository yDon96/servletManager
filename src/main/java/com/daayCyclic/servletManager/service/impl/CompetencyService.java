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
     * Create a new competency into the database.
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

    /**
     * Retrieve a {@literal CompetencyDao} from the database, correspondent to the given {@literal String}.
     *
     * @param competency a {@literal String} representing the competency to find
     * @return a {@literal CompetencyDao} representing the found competency
     */
    @Override
    public CompetencyDao getCompetency(String competency) {
        log.info("[SERVICE: Competency] Start retrieving from the database the competency: " + competency);
        if (competency != null) {
            competency = competency.toUpperCase();
        }
        Optional<CompetencyDao> retrievedCompetency = this.repository.findByName(competency);
        if (retrievedCompetency.isEmpty()) {
            String message = "The given competency doesn't exist into the database";
            log.info("[SERVICE: Competency] " + message);
            throw new NotFoundException(message);
        }
        log.info("[SERVICE: Competency] Competency retrieved successfully");
        return retrievedCompetency.get();
    }

    /**
     * Retrieve from the database all the {@literal CompetencyDao}.
     *
     * @return a {@literal List} of {@literal CompetencyDao} containing all the competencies in the database
     */
    @Override
    public List<CompetencyDao> getCompetencies() {
        log.info("[SERVICE: Competency] Start retrieving all the competencies from the database");
        ArrayList<CompetencyDao> retrievedCompetencies = (ArrayList<CompetencyDao>) this.repository.findAll();
        log.info("[SERVICE: Competency] Competencies retrieved successfully (" + retrievedCompetencies.size() + " elements found)");
        return retrievedCompetencies;
    }

    /**
     * If a competency is present returns {@code true}, otherwise {@code false}.
     *
     * @param competency a {@literal String} representing the competency to find
     * @return a boolean indicating if the competency exist
     */
    private boolean competencyExist(String competency) {
        if (competency == null) {
            return false;
        }
        return this.repository.findByName(competency).isPresent();
    }

}
