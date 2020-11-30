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
import static org.junit.jupiter.api.Assertions.assertThrows;


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
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, 5, true, 50, "definition");
        assertEquals(new ActivityDto(1, 23, 43, 5, true, 50, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdIsNotSet() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(null, mantainer, procedure, 5, true, 50, "definition");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingDescription() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, 5, true, 50, null);
        assertEquals(new ActivityDto(1, 23, 43, 5, true, 50, null),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndDescriptionNotSet() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(null, mantainer, procedure, 5, true, 50, null);
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingMantainer() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(null);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, 5, true, 50, "definition");
        assertEquals(new ActivityDto(1, null, 43, 5, true, 50, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndMantainerNotSet() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(null);
        procedure.setId(43);
        setActivityDao(null, mantainer, procedure, 5, true, 50, "definition");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingProcedure() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(null);
        setActivityDao(1, mantainer, procedure, 5, true, 50, "definition");
        assertEquals(new ActivityDto(1, 23, null, 5, true, 50, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndProcedureNotSet() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(null);
        setActivityDao(null, mantainer, procedure, 5, true, 50, "definition");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoWithOnlyIdAndInterruptable() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(null);
        procedure.setId(null);
        setActivityDao(1, mantainer, procedure, null, true, null, null);
        assertEquals(new ActivityDto(1, null, null, null, true, null, null),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndAllFieldsAreNotSetExcludingInterr() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(null);
        procedure.setId(null);
        setActivityDao(null, mantainer, procedure, null, true, null, null);
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingWeek() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, null, true, 50, "definition");
        assertEquals(new ActivityDto(1, 23, 43, null, true, 50, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndWeekNotSet() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(null, mantainer, procedure, null, true, 50, "definition");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingEstimateTime() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(1, mantainer, procedure, 5, true, null, "definition");
        assertEquals(new ActivityDto(1, 23, 43, 5, true, null, "definition"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndEstimateTimeNotSet() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(null, mantainer, procedure, 5, true, null, "definition");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoWithIdNegative() {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        mantainer.setUser_id(23);
        procedure.setId(43);
        setActivityDao(-7, mantainer, procedure, 5, true, 50, "definition");
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
