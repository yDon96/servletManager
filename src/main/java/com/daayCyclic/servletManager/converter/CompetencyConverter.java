package com.daayCyclic.servletManager.converter;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import org.springframework.stereotype.Component;

@Component("CompetencyConverter")
public class CompetencyConverter extends Converter<String, CompetencyDao>{

    public CompetencyConverter() {
        super(CompetencyConverter::convertToEntity, CompetencyConverter::convertToDto);
    }

    private static String convertToDto(CompetencyDao competency) {
        if (competency != null && !competency.getName().equals("")) {
            return competency.getName();
        }
        return null;
    }

    private static CompetencyDao convertToEntity(String competency) {
        if (competency != null && !competency.equals("")) {
            CompetencyDao newDao = new CompetencyDao();
            newDao.setName(competency);
            return newDao;
        }
        return null;
    }

}
