package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IProcedureService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ProcedureController {

    @Autowired
    private IProcedureService iProcedureService;

    @Autowired
    @Qualifier("ProcedureMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    @Autowired
    @Qualifier("ProcedureDtoToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    @PostMapping(path = "/procedure")
    public void postProcedure(@RequestBody ProcedureDto procedureDto) throws NotValidTypeException, DuplicateGenerationException {
        log.info("[REST] Post procedure with title" + procedureDto.title);
        val procedureDao = (ProcedureDao) iDtoToDaoMapper.convertToDao(procedureDto);
        iProcedureService.generateProcedure(procedureDao);
        log.debug("[REST] End post procedure");
    }

    @GetMapping(path = "/procedure")
    public ProcedureDto getProcedure(@RequestParam Integer id) throws NotValidTypeException {
        log.info("[REST] Get procedure with id" + id.toString());

        if(id < 0){
            log.error("[REST] Required a product with negative id." );
            throw new NotValidTypeException("Invalid parameter.");
        }

        val procedureDao = iProcedureService.getProcedure(id);
        val procedure =(ProcedureDto) iDaoToDtoMapper.convertToDto(procedureDao);
        log.debug("[REST] End get procedure");
        return procedure;
    }

    @GetMapping(path = "/procedures")
    @SuppressWarnings( "unchecked" )
    public List<ProcedureDto> getProcedures() throws NotValidTypeException {
        log.info("[REST] Get procedures");
        val procedureDao = iProcedureService.getProcedures();
        val procedures =(List<ProcedureDto>) iDaoToDtoMapper.convertDaoListToDtoList(procedureDao);
        log.debug("[REST] End get procedures");
        return procedures;
    }

}
