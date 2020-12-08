package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.service.ICompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompetencyController {

    @Autowired
    ICompetencyService competencyService;

    @PostMapping(path = "/postCompetency")
    public void postCompetency(String competency) {}

    @GetMapping(path = "/getCompetencies")
    public List<String> getCompetencies() {
        return null;
    }

}
