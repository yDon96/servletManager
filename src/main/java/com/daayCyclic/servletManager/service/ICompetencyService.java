package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.CompetencyDao;

import java.util.List;

public interface ICompetencyService {
    void generateCompetency(String competency);
    void updateCompetency(CompetencyDao updatedCompetency);
    CompetencyDao getCompetency(String competency);
    List<CompetencyDao> getCompetencies();
}
