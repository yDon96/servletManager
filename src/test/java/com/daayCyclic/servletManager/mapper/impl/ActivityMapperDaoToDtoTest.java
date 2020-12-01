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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class ActivityMapperDaoToDtoTest {


    @Autowired
    @Qualifier("ActivityToDtoMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    private ActivityDao activityDao;

    private ProcedureDao procedure;

    private UserDao maintainer;

    @BeforeEach
    private void init(){
        activityDao = new ActivityDao();
        ProcedureDao procedureDao = new ProcedureDao();
        UserDao userDao = new UserDao();
        procedureDao.setTitle("t");
        userDao.setName("a");
        userDao.setSurname("s");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        procedure =  procedureDao;
        maintainer = userDao;
    }

    @Test
    void shouldConvertToDto() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUser_id(), procedure.getId(), 5, true, 50, "ddd"), iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdIsNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingDescription() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, null);
        assertEquals(new ActivityDto(1, maintainer.getUser_id(), procedure.getId(), 5, true, 50, null),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndDescriptionNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, null);
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingMaintainer() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, null, procedure.getId(), 5, true, 50, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndMaintainerNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingProcedure() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUser_id(), null, 5, true, 50, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndProcedureNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoWithOnlyIdAndInter() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, null, true, null, null);
        assertEquals(new ActivityDto(1, null, null, null, true, null, null),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndAllFieldsAreNotSetExcludingInter() {
        setActivityDao(null, maintainer, procedure, null, true, null, null);
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingWeek() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, null, true, 50, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUser_id(), procedure.getId(), null, true, 50, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndWeekNotSet() {
        setActivityDao(null, maintainer, procedure, null, true, 50, "ddd");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingEstimateTime() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, null, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUser_id(), procedure.getId(), 5, true, null, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndEstimateTimeNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, null, "ddd");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoWithIdNegative() {
        setActivityDao(-7, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfDifferentType() {
        ProcedureDao procedureDao = new ProcedureDao();
        procedureDao.setId(1);
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(procedureDao);
        });
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
