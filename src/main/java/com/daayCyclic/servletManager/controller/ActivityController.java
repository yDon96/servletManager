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
        val activityDao = (ActivityDao) iDtoToDaoMapper.convertToDao(activityDto);
        iActivityService.generateActivity(activityDao);
    }

    @PutMapping(path = "/activity")
    public void putActivity(ActivityDto activityDto){
    }

    @GetMapping(path = "/activity")
    public ActivityDto getActivity(@RequestParam Integer activityId) throws NotValidTypeException {
        log.info("");
        val activityDao = iActivityService.getActivity(activityId);
        return (ActivityDto) iDaoToDtoMapper.convertToDto(activityDao);

    }

    @GetMapping(path = "/activities")
    public List<ActivityDto> getActivities() throws NotValidTypeException {
        val activityDao = iActivityService.getActivities();
        return (List<ActivityDto>) iDaoToDtoMapper.convertDaoListToDtoList(activityDao);

    }
    //@?
    public void assignProcedure(){

    }
    //@?
    public void assignMaintainer(){
    }

}
