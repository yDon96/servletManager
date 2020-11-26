package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.ProcedureDao;

import java.util.List;

public interface IProcedureService {

    void generateProcedure(ProcedureDao procedureDao);

    ProcedureDao getProcedure(Integer procedureId);

    List<ProcedureDao> getProcedures();
}
