package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.ICompetencyService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Component("ProcedureDtoToDaoMapper")
public class ProcedureDtoToDaoMapper implements IDtoToDaoMapper {

    @Autowired
    ICompetencyService iCompetencyService;

    @Override
    public ProcedureDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {
        log.debug("[ProcedureToDtaMapper] Convert procedureDto to dao object");

        if (!(objectDto instanceof ProcedureDto)){
            log.error("[ProcedureToDtoMapper] Object to convert is not a ProcedureDto.");
            throw new NotValidTypeException();
        }

        val procedureDto = (ProcedureDto) objectDto;

        checkDtoConsistency(procedureDto);

        return new ProcedureDao(procedureDto.getId(),
                procedureDto.getTitle(),
                procedureDto.getDescription(),
                this.convertCompetencyDtoToDao(procedureDto));

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
            log.error("[ProcedureToDtoMapper] Object to convert has invalid id field: " + procedureDto.getId() );
            throw new NotValidTypeException("Object to convert has an invalid field.");
        }
        if (procedureDto.getCompetencies() == null){
            log.info("[ProcedureToDtoMapper] initialization Set Competencies");
            procedureDto.setCompetencies(new LinkedHashSet<>());
        }
    }

    private Set<CompetencyDao> convertCompetencyDtoToDao(ProcedureDto procedureDto){
        Set<CompetencyDao> competencyDaoSet = new LinkedHashSet<>();
        for (String name : procedureDto.getCompetencies()){
            try {
                competencyDaoSet.add(iCompetencyService.getCompetency(name));
            } catch (NotFoundException e){
                log.error("[MapperProcedureDtoToDao] element null. (procedureDto.getCompetencies() -> " + iCompetencyService.getCompetency(name) + ")");
                throw new NotFoundException("element null. (procedureDto.getCompetencies() -> " + iCompetencyService.getCompetency(name) + ")");
            }
        }
        return competencyDaoSet;
    }
}
