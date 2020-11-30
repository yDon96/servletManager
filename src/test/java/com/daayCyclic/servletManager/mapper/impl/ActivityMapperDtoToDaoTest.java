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
    void shouldConvertToDao() {
        new ActivityDao(1, "definition", 50, true, 5, new ProcedureDao(), new UserDao());
        assertEquals(new ActivityDao(1, "definition", 50, true, 5, new ProcedureDao(), new UserDao()),iDtoToDaoMapper.convertToDao(activityDto));
    }


    private void setActivityDto(Integer id, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDto.setId(id);
        activityDto.setWeek(week);
        activityDto.setInterruptable(isInterruptable);
        activityDto.setEstimatedTime(estimatedTime);
        activityDto.setDescription(description);
    }
}
