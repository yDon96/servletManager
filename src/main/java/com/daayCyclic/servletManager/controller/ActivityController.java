package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    @PostMapping
    public void PostActivity(){

    }
    @GetMapping
    public ActivityDto GetActivity(){
        return null;

    }
    @GetMapping
    public List<? extends ObjectDto> getActivities(){
        return null;

    }







}
