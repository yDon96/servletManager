package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.ICompetencyService;
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
    private ICompetencyService iCompetencyService;

    @Autowired
    @Qualifier("ProcedureMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    @Autowired
    @Qualifier("ProcedureDtoToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    /**
     * Put a new procedure into the server.
     *
     * @param procedureDto the {@literal ProcedureDto} representing the procedure to post.
     * @throws NotValidTypeException if one or more of the parameters is null.
     * @throws DuplicateGenerationException if the procedure is already present.
     */
    @PostMapping(path = "/procedure")
    public void postProcedure(@RequestBody ProcedureDto procedureDto) throws NotValidTypeException, DuplicateGenerationException {
        log.info("[REST] Post procedure with title" + procedureDto.getTitle());
        val procedureDao = (ProcedureDao) iDtoToDaoMapper.convertToDao(procedureDto);
        iProcedureService.generateProcedure(procedureDao);
        log.debug("[REST] End post procedure.");
    }

    /**
     * Retrieve into the server the procedure passed through {@literal Integer} ID.
     *
     * @param id the {@literal Integer} to retrieve.
     * @throws NotValidTypeException if {@literal Integer} is negative.
     */
    @GetMapping(path = "/procedure/{id}")
    public ProcedureDto getProcedure(@PathVariable("id") Integer id) throws NotValidTypeException {
        log.info("[REST] Get procedure with id" + id.toString());

        if(id < 0){
            log.error("[REST] Required a product with negative id." );
            throw new NotValidTypeException("Invalid parameter.");
        }

        val procedureDao = iProcedureService.getProcedure(id);
        val procedure =(ProcedureDto) iDaoToDtoMapper.convertToDto(procedureDao);
        log.debug("[REST] End get procedure.");
        return procedure;
    }

    /**
     * Retrieve from the server all the procedures.
     *
     * @return a {@literal List} of {@literal ProcedureDto} containing all the procedures in the server.
     * @throws NotValidTypeException if one or more of the parameters is null
     */
    @GetMapping(path = "/procedures")
    @SuppressWarnings( "unchecked" )
    public List<ProcedureDto> getProcedures() throws NotValidTypeException {
        log.info("[REST] Get procedures.");
        val procedureDao = iProcedureService.getProcedures();
        val procedures =(List<ProcedureDto>) iDaoToDtoMapper.convertDaoListToDtoList(procedureDao);
        log.debug("[REST] End get procedures.");
        return procedures;
    }

    /**
     * Assign the competency corresponding to the given {@literal String}
     * to the procedure corresponding to the given procedureId.
     *
     * @param procedureId a {@literal Integer} ID which identifies a procedure.
     * @param competency a {@literal String} which identifies a competency.
     */
    @PutMapping (path = "/procedure/{procedureId}/assign-competency")
    public void assignCompetencyToProcedure(@PathVariable("procedureId") Integer procedureId, @RequestParam String competency) {
        log.info("[REST] Start assign competency to procedure.");
        val competencyDao = iCompetencyService.getCompetency(competency);
        val procedureDao = iProcedureService.getProcedure(procedureId);
        iProcedureService.assignCompetencyToProcedure(competencyDao, procedureDao);
        log.info("[REST] Assignment of competence was successful.");
    }

}
