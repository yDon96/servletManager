package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ActivityMapperDtoToDaoTest {

    @Autowired
    @Qualifier("ActivityToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    private ActivityDto activityDto;

    @BeforeEach
    private void init() {
        activityDto = new ActivityDto();
    }

    @Test
    void shouldConvertToDaoIfIdIsNotNull() {
        setActivityDto(1,23,43,5,true,50,"definition");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test
    void shouldConvertToDaoIfDescriptionIsNull() throws NotValidTypeException {
        UserDao mantainer = new UserDao();
        ProcedureDao procedure = new ProcedureDao();
        setActivityDto(1,23,43,5,true,50,null);
        assertEquals(new ActivityDao(1, null, 50, true, 5, procedure, mantainer), iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test()
    void shouldThrowExceptionConvertToDaoIfDifferentType() {
        ProcedureDto procedureDto = new ProcedureDto();
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(procedureDto);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDaoIfWeekNull() {
        setActivityDto(1,23,43,5,true,50,"definition");
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDaoIfMaintainerIdNull() {
        setActivityDto(1,null,43,5,true,50,"definition");
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDaoIfProcedureIdNull() {
        setActivityDto(1,23,null,5,true,50,"definition");
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDaoIfEstimateTimeNull() {
        setActivityDto(1,23,43,5,true,null,"definition");
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
    }

    private void setActivityDto(Integer id, Integer maintainerId, Integer procedureId, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDto.setId(id);
        activityDto.setMaintainerId(maintainerId);
        activityDto.setProcedureId(procedureId);
        activityDto.setWeek(week);
        activityDto.setInterruptable(isInterruptable);
        activityDto.setEstimatedTime(estimatedTime);
        activityDto.setDescription(description);
    }
}
