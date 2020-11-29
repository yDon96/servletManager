package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ActivityMapperDaoToDtoTest {


    @Autowired
    @Qualifier("ActivityToDtoMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    private ActivityDao activityDao;

    @BeforeEach
    private void init(){
        activityDao = new ActivityDao();
    }

    @Test
    void shouldConvertToDto() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setId(23);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, 5, true, 50, "definition");
        assertEquals(new ActivityDto(1, 23, 43, 5, true, 50, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldConvertToDtoNull() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setId(23);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, 5, true, 50, "definition");
        assertEquals(new ActivityDto(1, 23, 43, 5, true, 50, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    private void setActivityDao(Integer id, UserDao maintainer, ProcedureDao procedure, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDao.setId(id);
        activityDao.setMaintainer(maintainer);
        activityDao.setProcedure(procedure);
        activityDao.setWeek(week);
        activityDao.setInterruptable(isInterruptable);
        activityDao.setEstimatedTime(estimatedTime);
        activityDao.setDescription(description);
    }
}
