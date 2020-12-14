package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IProcedureService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ActivityController {

    @Autowired
    private IActivityService iActivityService;

    @Autowired
    private IProcedureService procedureService;

    @Autowired
    private IUserService userService;

    @Autowired
    @Qualifier("ActivityToDtoMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    @Autowired
    @Qualifier("ActivityToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    @PostMapping(path = "/activity")
    public void postActivity(@RequestBody ActivityDto activityDto) throws NotValidTypeException {
        log.info("[REST] Post Activity");
        val activityDao = (ActivityDao) iDtoToDaoMapper.convertToDao(activityDto);
        iActivityService.generateActivity(activityDao);
        log.debug("[REST] End Post activity");
    }

    @PutMapping(path = "/activity")
    public void putActivity(@RequestBody ActivityDto activityDto){
        log.info("[REST] Put Activity");
        val activityDao = (ActivityDao) iDtoToDaoMapper.convertToDao(activityDto);
        iActivityService.updateActivity(activityDao);
        log.debug("[REST] End Put activity");
    }

    @GetMapping(path = "/activity")
    public ActivityDto getActivity(@RequestParam Integer activityId) throws NotValidTypeException {
        log.info("[REST] Get Activity");

        if(activityId < 0){
            log.error("[REST] Id activity is negative" );
            throw new NotValidTypeException("Negative Id.");
        }

        val activityDao = iActivityService.getActivity(activityId);
        log.debug("[REST] End Get activity");
        return (ActivityDto) iDaoToDtoMapper.convertToDto(activityDao);
    }

    @GetMapping(path = "/activities")
    public List<ActivityDto> getActivities() throws NotValidTypeException {
        log.info("[REST] Get Activities");
        val activityDao = iActivityService.getActivities();
        log.debug("[REST] End Get activities");
        return (List<ActivityDto>) iDaoToDtoMapper.convertDaoListToDtoList(activityDao);
    }

    /**
     * Assign the procedure corresponding to the given procedureID to the activity corresponding to the given activityID
     *
     * @param procedureID a {@literal int} ID which identifies a procedure
     * @param activityID a {@literal int} ID which identifies an activity
     */
    @PutMapping(path = "/assignProcedure")
    public void assignProcedure(@RequestParam int procedureID, @RequestParam int activityID){
        log.info("[REST] Start assigning procedure " + procedureID + "to " + activityID + "activity");
        ActivityDao activity = iActivityService.getActivity(activityID);
        ProcedureDao procedure = procedureService.getProcedure(procedureID);
        iActivityService.assignProcedure(procedure, activity);
        log.info("[REST] Procedure assigned successfully to the activity");
    }

    /**
     * Assign the maintainer corresponding to the given userID to the activity corresponding to the given activityID
     *
     * @param userID a {@literal int} ID which identifies a user
     * @param activityID a {@literal int} ID which identifies an activity
     */
    @PutMapping(path = "/assignMaintainer")
    public void assignMaintainer(@RequestParam int userID,@RequestParam int activityID){
        log.info("[REST] Start assigning maintainer: " + userID + "to " + activityID + " activity");
        ActivityDao activity = iActivityService.getActivity(activityID);
        UserDao user = userService.getUser(userID);
        iActivityService.assignMaintainer(user, activity);
        log.info("[REST] Maintainer assigned successfully to the activity");
    }

}
