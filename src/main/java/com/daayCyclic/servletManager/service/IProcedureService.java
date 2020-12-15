package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;

import java.util.List;

public interface IProcedureService {

    void generateProcedure(ProcedureDao procedureDao) throws DuplicateGenerationException;
    ProcedureDao getProcedure(Integer procedureId);
    List<ProcedureDao> getProcedures();
    void editProcedure(Integer procedureId, String description);
    void assignCompetencyToProcedure(CompetencyDao competency, ProcedureDao procedure);

}
