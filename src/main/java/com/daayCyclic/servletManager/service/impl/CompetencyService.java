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
     * @param competency the {@literal CompetencyDao} to save into the database
     * @throws NotValidTypeException if the {@literal CompetencyDao} is not valid (null or empty)
     * @throws DuplicateGenerationException if the Competency is already present
     */
    @Override
    public void generateCompetency(CompetencyDao competency) {
        log.info("[SERVICE: Competency] Start inserting into the database the competency: " + competency);
        CompetencyDao validatedCompetency = this.validate(competency);
        if (this.competencyExist(validatedCompetency.getName())) {
            String message = "The given competency already exist into the database";
            log.info("[SERVICE: Competency] " + message);
            throw new DuplicateGenerationException(message);
        }
        repository.save(validatedCompetency);
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
        CompetencyDao validatedCompetency = this.validate(updatedCompetency);
        CompetencyDao retrievedDao = this.getCompetency(validatedCompetency.getName());
        validatedCompetency.setCompetencyId(retrievedDao.getCompetencyId());  // Set the ID to the one into the DB (if the ID is different the save would create a new record)
        log.info("[SERVICE: Competency] Update completed successfully");
        this.repository.save(validatedCompetency);
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
        CompetencyDao tmpDao = this.validate(new CompetencyDao(competency));
        CompetencyDao retrievedDao = this.retrieve(tmpDao.getName());
        log.info("[SERVICE: Competency] Competency retrieved successfully");
        return retrievedDao;
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

    @Override
    public Integer countUserOwnedCompetenciesRequiredFromProcedure(Integer userId, Integer procedureId) {
        log.info("[SERVICE: Competency] Start counting required competencies of procedure " + procedureId + " owned by user " + userId);
        if (userId == null || procedureId == null) {
            String message = "userId and procedureId can't be null";
            log.error("[SERVICE: Competency] " + message);
            throw new NotValidTypeException(message);
        }
        Integer result = this.repository.countUserOwnedCompetenciesRequiredFromProcedure(userId, procedureId);
        log.info("[SERVICE: Competency] Count completed successfully");
        return result;
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

    private CompetencyDao retrieve(String competency) {
        Optional<CompetencyDao> retrievedCompetency = this.repository.findByName(competency);
        if (retrievedCompetency.isEmpty()) {
            String message = "The given competency doesn't exist into the database";
            log.info("[SERVICE: Competency] " + message);
            throw new NotFoundException(message);
        }
        return retrievedCompetency.get();
    }

    private void checkConsistency(CompetencyDao competency) {
        if (competency == null) {throw new NotValidTypeException("The CompetencyDao can't be null");}
        if (competency.getName() == null || competency.getName().equals("")) {throw new NotValidTypeException("The CompetencyDao name can't be null or empty");}
    }

    private CompetencyDao validate(CompetencyDao competency) {
        this.checkConsistency(competency);
        competency.setName(competency.getName().toUpperCase()); // Make every string inserted into the DB upper case (to avoid inconsistency)
        return competency;
    }

}
