package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
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
    public void generateProcedure(ProcedureDao procedureDao) throws DuplicateGenerationException {
        if (procedureExist(procedureDao.getId())){
            throw new DuplicateGenerationException();
        }
        iProcedureRepository.save(procedureDao);
    }

    @Override
    public ProcedureDao getProcedure(Integer procedureId) {
        val procedureDao = iProcedureRepository.findById(procedureId);
        return procedureDao.orElseThrow(NotFoundException::new);
    }

    @Override
    public List<ProcedureDao> getProcedures() {
        return iProcedureRepository.findAll();
    }

    protected boolean procedureExist(Integer procedureId){
        if (procedureId == null) {
            return false;
        }
        val optionalProcedure = iProcedureRepository.findById(procedureId);
        return optionalProcedure.isPresent();
    }
}
