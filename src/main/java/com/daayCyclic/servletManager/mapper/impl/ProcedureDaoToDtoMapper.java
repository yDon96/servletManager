package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "ProcedureMapper")
public class ProcedureDaoToDtoMapper implements IDaoToDtoMapper {


    @Override
    public ProcedureDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {

        if (!(objectDao instanceof ProcedureDao)){
            throw new NotValidTypeException("Not Valid Type.");
        }

        val procedureDao = (ProcedureDao) objectDao;

        if (procedureDao.getId() == null) {
            throw new NullPointerException();
        }

        return new ProcedureDto(procedureDao.getId(),procedureDao.getTitle(),procedureDao.getDescription());
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<ProcedureDto> convertDaoListToDtoList(List<? extends ObjectDao> daoProcedures) throws NotValidTypeException {

        if (!(!daoProcedures.isEmpty() && daoProcedures.get(0) instanceof ProcedureDao)){
            throw new NotValidTypeException("Not Valid Type.");
        }

        var procedureDtoList = new ArrayList<ProcedureDto>();
        for (ProcedureDao value : (List<ProcedureDao>) daoProcedures) {
            procedureDtoList.add(this.convertToDto(value));
        }
        return  procedureDtoList;
    }
}
