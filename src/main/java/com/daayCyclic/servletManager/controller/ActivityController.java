package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import lombok.*;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IActivityService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Slf4j
public class ActivityController {

    @Autowired
    private IActivityService iActivityService;

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
    public void putActivity(ActivityDto activityDto){
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
    //@?
    public void assignProcedure(){

    }
    //@?
    public void assignMaintainer(){
    }

}
