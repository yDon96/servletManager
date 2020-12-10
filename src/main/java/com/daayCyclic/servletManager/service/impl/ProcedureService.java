package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.ICompetencyRepository;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.service.IProcedureService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ProcedureService implements IProcedureService {

    @Autowired
    private IProcedureRepository iProcedureRepository;
    @Autowired
    private ICompetencyRepository iCompetencyRepository;


    @Override
    public void generateProcedure(ProcedureDao procedureDao) throws DuplicateGenerationException {
        log.debug("[ProcedureService] Generating procedure");
        if (procedureExist(procedureDao.getId())){
            log.error("[ProcedureService] Trying to create a procedure that already exist with id: " + procedureDao.getId().toString());
            throw new DuplicateGenerationException();
        }
        iProcedureRepository.save(procedureDao);
    }

    @Override
    public ProcedureDao getProcedure(Integer procedureId) {
        log.debug("[ProcedureService] Get procedure with id: " + procedureId.toString());
        val procedureDao = iProcedureRepository.findById(procedureId);
        return procedureDao.orElseThrow(NotFoundException::new);
    }

    @Override
    public List<ProcedureDao> getProcedures() {
        log.debug("[ProcedureService] Get all procedures");
        return iProcedureRepository.findAll();
    }

    @Override
    public void assignCompetencyToProcedure(CompetencyDao competency, ProcedureDao procedure) {
        /**
         * Assign the given competency to the given procedure
         */
        log.info("[SERVICE procedure] start assign competency to procedure");
        if (procedure == null) {
            log.error("[SERVICE procedure] procedure is null");
            throw new NotValidTypeException("procedure is null");
        }

        if (competency == null) {
            log.error("[SERVICE procedure] competency is null");
            throw new NotValidTypeException("competency is null");
        }

        if (!(iCompetencyRepository.existsById(competency.getCompetencyId()))){
            log.error("[SERVICE procedure] competency is no present into Database");
            throw new NotFoundException("competency is no present into Database");
        }
        Set<CompetencyDao> competencyDaoSet;
        competencyDaoSet = procedure.getCompetencies();
        competencyDaoSet.add(competency);
        procedure.setCompetencies(competencyDaoSet);
        log.info("[SERVICE procedure] assignment successfully");
    }

    protected boolean procedureExist(Integer procedureId){
        if (procedureId == null) {
            return false;
        }
        val optionalProcedure = iProcedureRepository.findById(procedureId);
        return optionalProcedure.isPresent();
    }
}
