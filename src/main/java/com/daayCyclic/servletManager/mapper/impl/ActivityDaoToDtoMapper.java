package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("ActivityToDtoMapper")
public class ActivityDaoToDtoMapper implements IDaoToDtoMapper {

    @Override
    public ActivityDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {

        if (!(objectDao instanceof ActivityDao)){
            throw new NotValidTypeException("Not valid type. (ActivityToDtoMapper)");
        }

        val activityDao = (ActivityDao) objectDao;

        if (activityDao.getId() == null || activityDao.getId() <= 0) {
            log.error("[ActivityToDtoMapper] invalid id.");
            throw new NullPointerException();
        }

        return new ActivityDto(activityDao.getId(),
                activityDao.getMaintainer().getUser_id(),
                activityDao.getProcedure().getId(),
                activityDao.getWeek(),
                activityDao.isInterruptable(),
                activityDao.getEstimatedTime(),
                activityDao.getDescription());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoActivities) throws NotValidTypeException {
        if(!(!daoActivities.isEmpty() && daoActivities.get(0) instanceof ActivityDao)){
            throw new NotValidTypeException("Not valid type. (convertToDtoList)");
        }

        var activityDtoList = new ArrayList<ActivityDto>();

        for (ActivityDao activityDao :(List<ActivityDao>) daoActivities){
            activityDtoList.add(this.convertToDto(activityDao));
        }

        return activityDtoList;
    }


}
