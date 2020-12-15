package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component(value = "ProcedureMapper")
public class ProcedureDaoToDtoMapper implements IDaoToDtoMapper {


    @Override
    public ProcedureDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {
        log.debug("[ProcedureToDtoMapper] Convert procedureDao to dto object");

        if (!(objectDao instanceof ProcedureDao)){
            log.error("[ProcedureToDtoMapper] Object to convert is not a ProcedureDao.");
            throw new NotValidTypeException();
        }

        val procedureDao = (ProcedureDao) objectDao;

        if (procedureDao.getId() == null) {
            log.error("[ProcedureToDtoMapper] Missing id in ProcedureDao object.");
            throw new NullPointerException();
        }
        if (procedureDao.getCompetencies() == null){
            procedureDao.setCompetencies(new LinkedHashSet<>());
        }

        return new ProcedureDto(procedureDao.getId(),
                procedureDao.getTitle(),
                procedureDao.getDescription(),
                this.convertCompetencyToDto(procedureDao));
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<ProcedureDto> convertDaoListToDtoList(List<? extends ObjectDao> daoProcedures) throws NotValidTypeException {
        log.debug("[ProcedureToDtoMapper] Convert list of procedureDao to list of dto object");
        //TODO: check null list, change this check below
        if (!(!daoProcedures.isEmpty() && daoProcedures.get(0) instanceof ProcedureDao)){
            log.error("[ProcedureToDtoMapper] List type is not a ProcedureDao.");
            throw new NotValidTypeException();
        }

        var procedureDtoList = new ArrayList<ProcedureDto>();
        for (ProcedureDao value : (List<ProcedureDao>) daoProcedures) {
            procedureDtoList.add(this.convertToDto(value));
        }
        return  procedureDtoList;
    }

    private Set<String> convertCompetencyToDto(ProcedureDao procedureDao){

        Set<String> nameCompetency = new LinkedHashSet<>();
        for (CompetencyDao name : procedureDao.getCompetencies()){
            nameCompetency.add(name.getName());
        }
        return nameCompetency;
    }
}
