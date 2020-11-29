package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

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





    private void setActivityDto(Integer id, Integer maintainerId, Integer procedureId, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {

        activityDto.setId(id);
        activityDto.setWeek(week);
        activityDto.setInterruptable(isInterruptable);
        activityDto.setEstimatedTime(estimatedTime);
        activityDto.setDescription(description);
    }
}
