package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IProcedureService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ActivityToDaoMapper")
public class ActivityDtoToDaoMapper implements IDtoToDaoMapper {

    @Autowired
    private IProcedureService iProcedureService;

    @Autowired
    private IUserService iUserService;

    @Override
    public ActivityDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {

        if (!(objectDto instanceof ActivityDto)){
            throw new NotValidTypeException("Not valid type. (ActivityToDaoMapper)");
        }

        val activityDto = (ActivityDto) objectDto;
        ProcedureDao procedure = null;
        UserDao maintainer = null;

        if (activityDto.getMaintainerId() != null){
            maintainer = iUserService.getUser(activityDto.getMaintainerId());
        }
        if (activityDto.getProcedureId() != null){
            procedure = iProcedureService.getProcedure(activityDto.getProcedureId());
        }

        return new ActivityDao(activityDto.getId(),
                activityDto.getDescription(),
                activityDto.getEstimatedTime(),
                activityDto.isInterruptable(),
                activityDto.getWeek(),
                activityDto.getStartingDay(),
                activityDto.getStartingHour(),
                procedure,
                maintainer);
    }

}
