package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import lombok.*;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Component("ActivityToDtoMapper")
public class ActivityDaoToDtoMapper implements IDaoToDtoMapper {

    @Override
    public ActivityDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {

        if (!(objectDao instanceof ActivityDao)){
            throw new NotValidTypeException("Not valid type. (Activity)");
        }

        val activityDao = (ActivityDao) objectDao;

        return new ActivityDto(activityDao.getId(),
                activityDao.getMaintainer().getId(),
                activityDao.getProcedure().getId(),
                activityDao.getWeek(),
                activityDao.isInterruptable(),
                activityDao.getEstimatedTime(),
                activityDao.getDescription());
    }

    @Override
    public List<ActivityDto> convertDaoListToDtoList(List<ActivityDao> daoActivities) throws NotValidTypeException {

        if(!(!daoActivities.isEmpty() && daoActivities.get(0) instanceof ActivityDao)){
            throw new NotValidTypeException("Not valid type. (convertToDtoList)");
        }

        var activityDtoList = new ArrayList<ActivityDto>();

        for (ActivityDao activityDao : daoActivities){
            activityDtoList.add(this.convertToDto(activityDao));
        }

        return activityDtoList;
    }


}
