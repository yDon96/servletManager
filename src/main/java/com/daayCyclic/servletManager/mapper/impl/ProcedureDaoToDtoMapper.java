package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component(value = "ProcedureMapper")
public class ProcedureDaoToDtoMapper implements IDaoToDtoMapper {


    @Override
    public ProcedureDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {
        log.debug("[ProcedureToDtoMapper] Convert procedureDao to dto object");

        if (!(objectDao instanceof ProcedureDao)){
            throw new NotValidTypeException("Not Valid Type.");
            log.error("[ProcedureToDtoMapper] Object to convert is not a ProcedureDao.");
        }

        val procedureDao = (ProcedureDao) objectDao;

        if (procedureDao.getId() == null) {
            log.error("[ProcedureToDtoMapper] Missing id in ProcedureDao object.");
            throw new NullPointerException();
        }

        return new ProcedureDto(procedureDao.getId(),procedureDao.getTitle(),procedureDao.getDescription());
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<ProcedureDto> convertDaoListToDtoList(List<? extends ObjectDao> daoProcedures) throws NotValidTypeException {
        log.debug("[ProcedureToDtoMapper] Convert list of procedureDao to list of dto object");
        if (!(!daoProcedures.isEmpty() && daoProcedures.get(0) instanceof ProcedureDao)){
            throw new NotValidTypeException("Not Valid Type.");
            log.error("[ProcedureToDtoMapper] List type is not a ProcedureDao.");
        }

        var procedureDtoList = new ArrayList<ProcedureDto>();
        for (ProcedureDao value : (List<ProcedureDao>) daoProcedures) {
            procedureDtoList.add(this.convertToDto(value));
        }
        return  procedureDtoList;
    }
}
