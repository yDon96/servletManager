package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.service.ICompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "CompetencyService")
public class CompetencyService implements ICompetencyService {

    @Autowired
    private ICompetencyService competencyService;

    @Override
    public void generateCompetency(String competency) {

    }

    @Override
    public CompetencyDao getCompetency(String competency) {
        return null;
    }

    @Override
    public List<CompetencyDao> getCompetencies() {
        return null;
    }

    @Override
    public void updateCompetency(CompetencyDao updatedCompetency) {

    }

}
