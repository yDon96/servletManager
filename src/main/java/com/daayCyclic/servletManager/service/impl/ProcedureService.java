package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import com.daayCyclic.servletManager.service.IProcedureService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ProcedureService implements IProcedureService {

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private ICompetencyService iCompetencyService;

    /**
     * Create a new procedure into the database.
     *
     * @param procedureDao the {@literal ProcedureDao} to save into the database.
     * @throws DuplicateGenerationException if a procedure already exists is generated.
     */
    @Override
    public void generateProcedure(ProcedureDao procedureDao) throws DuplicateGenerationException {
        log.debug("[ProcedureService] Generating procedure.");
        if (procedureExist(procedureDao.getId())){
            log.error("[ProcedureService] Trying to create a procedure that already exist with id: " + procedureDao.getId().toString());
            throw new DuplicateGenerationException();
        }
        iProcedureRepository.save(this.validate(procedureDao));
    }

    /**
     * Update an existing procedure if it exist.
     *
     * @param procedureDao the {@literal ProcedureDao} to update.
     * @throws NotFoundException if the given procedure is not present into the database.
     */
    public void updateProcedure(ProcedureDao procedureDao){
        log.info("[SERVICE: Procedure] Starting update of the given procedure: " + procedureDao);
        ProcedureDao validatedProcedure = this.validate(procedureDao);
        if (!(procedureExist(validatedProcedure.getId()))){
            String message = "The given procedure is not present into the database";
            log.info("[SERVICE: Procedure] " + message);
            throw new NotFoundException(message);
        }
        iProcedureRepository.save(validatedProcedure);
        log.info("[SERVICE: Procedure] Update of the procedure completed successfully.");
    }

    /**
     * Retrieve a {@literal ProcedureDao} from the database, correspondent to the given {@literal Integer}.
     *
     * @param procedureId a {@literal Integer} representing the id of the procedure.
     * @return a {@literal ProcedureDao} representing the found procedure.
     * @throws NotFoundException if the procedure is not present.
     */
    @Override
    public ProcedureDao getProcedure(Integer procedureId) {
        log.debug("[ProcedureService] Get procedure with id: " + procedureId.toString());
        val procedureDao = iProcedureRepository.findById(procedureId);
        return procedureDao.orElseThrow(NotFoundException::new);
    }

    /**
     * Retrieve from the database all the {@literal ProcedureDao}.
     *
     * @return a {@literal List} of {@literal ProcedureDao} containing all the procedures in the database.
     */
    @Override
    public List<ProcedureDao> getProcedures() {
        log.debug("[ProcedureService] Get all procedures.");
        return iProcedureRepository.findAll();
    }

    @Override
    public void editProcedure(Integer procedureId,String description) {
        log.debug("[ProcedureService] Edit procedure with id: " + procedureId);
        val procedureDao = getProcedure(procedureId);
        procedureDao.setDescription(description);
        iProcedureRepository.save(procedureDao);
    }

    /**
     * Assign the given competency to the given procedure.
     *
     * @param competency {@literal CompetencyDao} to assign.
     * @param procedure {@literal ProcedureDao} receives the assigned competence.
     * @throws NullPointerException if one or more fields is null.
     * @throws NotValidTypeException if competency {@literal CompetencyDao} is null.
     */
    @Override
    public void assignCompetencyToProcedure(CompetencyDao competency, ProcedureDao procedure) throws NullPointerException {
        log.info("[SERVICE procedure] Starting assign of competency " + competency + " to procedure " + procedure);
        ProcedureDao validatedProcedure = this.validate(procedure);
        if (competency == null) {
            log.error("[SERVICE procedure] Competency can't be null");
            throw new NotValidTypeException("competency is null");
        }

        if (competency.getProcedures() == null){
            competency.setProcedures(new LinkedHashSet<>());
        }
        competency.getProcedures().add(validatedProcedure);

        if (validatedProcedure.getCompetencies() == null){
            validatedProcedure.setCompetencies(new LinkedHashSet<>());
        }
        validatedProcedure.getCompetencies().add(competency);

        this.updateProcedure(this.validate(validatedProcedure));
        this.iCompetencyService.updateCompetency(competency);
        log.info("[SERVICE procedure] Assigning procedure completed successfully.");
    }

    protected boolean procedureExist(Integer procedureId){
        if (procedureId == null) {
            return false;
        }
        val optionalProcedure = iProcedureRepository.findById(procedureId);
        return optionalProcedure.isPresent();
    }

    private void checkIntegrity(ProcedureDao procedure) {
        if (procedure == null) { throw new NotValidTypeException("The ProcedureDao can't be null."); }
        if (procedure.getTitle() == null || procedure.getTitle().equals("")) { throw new NotValidTypeException("The ProcedureDao title can't be null or empty."); }
    }

    private ProcedureDao validate(ProcedureDao procedure) {
        this.checkIntegrity(procedure);
        Set<CompetencyDao> savedCompetencies = procedure.getCompetencies();
        if (savedCompetencies != null) {
            Set<CompetencyDao> checkedCompetencies = new LinkedHashSet<>();
            for (CompetencyDao competency : savedCompetencies) {
                if (competency.getName() != null)
                    checkedCompetencies.add(this.iCompetencyService.getCompetency(competency.getName()));
            }
            procedure.setCompetencies(checkedCompetencies);
        }
        return procedure;
    }

}
