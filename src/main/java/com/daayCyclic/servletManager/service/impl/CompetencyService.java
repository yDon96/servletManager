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
     * @throws NotValidTypeException if the {@literal String} is not valid (null or empty)
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
     * Update a competency into the database with a given one.
     *
     * @param updatedCompetency a {@literal CompetencyDao} containing the new, updated, competency
     * @throws NotFoundException if the given competency is not present into the database
     * @throws NotValidTypeException if the {@literal CompetencyDao} is not valid (null or empty)
     */
    @Override
    public void updateCompetency(CompetencyDao updatedCompetency) {
        log.info("[SERVICE: Competency] Starting update competency");
        if (updatedCompetency == null || updatedCompetency.getName().equals("")) {
            String message = "The given competency is null or empty";
            log.info("[SERVICE: Competency] " + message);
            throw new NotValidTypeException(message);
        }
        updatedCompetency.setName(updatedCompetency.getName().toUpperCase());
        Optional<CompetencyDao> competencyDao = this.findByName(updatedCompetency.getName());
        if (competencyDao.isEmpty()) {
            String message = "The given competency doesn't exist";
            log.info("[SERVICE: Competency] " + message);
            throw new NotFoundException(message);
        }
        updatedCompetency.setCompetencyId(competencyDao.get().getCompetencyId());  // Set the ID to the one into the DB (if the ID is different the save would create a new record otherwise)
        log.info("[SERVICE: Competency] Update completed successfully");
        this.repository.save(updatedCompetency);
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
        Optional<CompetencyDao> retrievedCompetency = this.findByName(competency);
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
        //TODO: Possibile miglioramento, se questa torna l'id, l'update si può fare tramite la generate
        if (competency == null) {
            return false;
        }
        return this.findByName(competency).isPresent();
    }

    /**
     * Find a competency with is name.
     *
     * @param competency a {@literal String} representing the competency
     * @return an {@literal Optional} containing a {@literal CompetencyDao} if the competency was found, nothing otherwise
     */
    private Optional<CompetencyDao> findByName(String competency) {
        return this.repository.findByName(competency);
    }

}
