package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(value = "CompetencyService")
public class CompetencyService implements ICompetencyService {

    @Autowired
    private ICompetencyRepository repository;

    @Override
    public void generateCompetency(String competency) {
        log.info("[SERVICE: Competency] Start inserting into the database of the competency: " + competency);
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
        return null;
    }

    @Override
    public List<CompetencyDao> getCompetencies() {
        return null;
    }

    private boolean competencyExist(String competency) {
        if (competency == null) {
            return false;
        }
        return this.repository.findByName(competency).isPresent();
    }

}
