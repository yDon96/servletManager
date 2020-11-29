package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ProcedureDtoToDaoMapper")
public class ProcedureDtoToDaoMapper implements IDtoToDaoMapper {


    @Override
    public ProcedureDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {
        log.debug("[ProcedureToDtaMapper] Convert procedureDto to dao object");

        if (!(objectDto instanceof ProcedureDto)){
            log.error("[ProcedureToDtoMapper] Object to convert is not a ProcedureDto.");
            throw new NotValidTypeException();
        }

        val procedureDto = (ProcedureDto) objectDto;

        checkDtoConsistency(procedureDto);

        return new ProcedureDao(procedureDto.id,procedureDto.title,procedureDto.description);

    }

    private void checkDtoConsistency(ProcedureDto procedureDto){
        if (procedureDto.isAllNull()){
            log.error("[ProcedureToDtoMapper] Object to convert has all empty field.");
            throw new NotValidTypeException("Object to convert has all empty field.");
        }

        if (!procedureDto.containsAllRequiredValue()){
            log.error("[ProcedureToDtoMapper] Object to convert has all empty required field.");
            throw new NotValidTypeException("Object to convert has all empty required field.");
        }

        if (!procedureDto.isIdValid()){
            log.error("[ProcedureToDtoMapper] Object to convert has invalid id field: " + procedureDto.id );
            throw new NotValidTypeException("Object to convert has an invalid field.");
        }
    }
}
