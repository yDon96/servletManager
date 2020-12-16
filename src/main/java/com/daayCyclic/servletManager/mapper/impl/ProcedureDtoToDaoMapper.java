package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.converter.CompetencyConverter;
import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component("ProcedureDtoToDaoMapper")
public class ProcedureDtoToDaoMapper implements IDtoToDaoMapper {

    private final CompetencyConverter competencyConverter = new CompetencyConverter();

    @Override
    public ProcedureDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {
        log.debug("[ProcedureToDtaMapper] Convert procedureDto to dao object");
        if (!(objectDto instanceof ProcedureDto)){
            log.error("[ProcedureToDtoMapper] Object to convert is not a ProcedureDto.");
            throw new NotValidTypeException();
        }
        val procedureDto = (ProcedureDto) objectDto;
        return new ProcedureDao(procedureDto.getId(),
                procedureDto.getTitle(),
                procedureDto.getDescription(),
                (Set<CompetencyDao>) this.competencyConverter.createFromDtos(procedureDto.getCompetencies()));
    }

}
