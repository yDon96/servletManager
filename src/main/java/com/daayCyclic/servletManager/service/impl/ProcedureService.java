package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.service.IProcedureService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcedureService implements IProcedureService {

    @Autowired
    private IProcedureRepository iProcedureRepository;


    @Override
    public void generateProcedure(ProcedureDao procedureDao) {
        iProcedureRepository.save(procedureDao);
    }

    @Override
    public ProcedureDao getProcedure(Integer procedureId) {
        val procedureDao = iProcedureRepository.findById(procedureId);
        return procedureDao.orElseThrow(() -> new RuntimeException("Not Found."));
    }

    @Override
    public List<ProcedureDao> getProcedures() {
        return iProcedureRepository.findAll();
    }
}
