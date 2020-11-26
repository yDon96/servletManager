package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IProcedureService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


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
    public void postProcedure(@RequestBody ProcedureDto procedureDto) throws NotValidTypeException {
        val procedureDao = (ProcedureDao) iDtoToDaoMapper.convertToDao(procedureDto);
        iProcedureService.generateProcedure(procedureDao);
    }
}
