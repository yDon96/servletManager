package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import lombok.val;
import org.springframework.stereotype.Component;

@Component("ProcedureDtoToDaoMapper")
public class ProcedureDtoToDaoMapper implements IDtoToDaoMapper {


    @Override
    public ProcedureDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {

        if (!(objectDto instanceof ProcedureDto)){
            throw new NotValidTypeException("Not Valid Type.");
        }

        val procedureDto = (ProcedureDto) objectDto;

        if (procedureDto.isAllNull()){
            throw new NotValidTypeException("Object to convert has all empty field.");
        }

        return new ProcedureDao(procedureDto.id,procedureDto.title,procedureDto.description);

    }
}
