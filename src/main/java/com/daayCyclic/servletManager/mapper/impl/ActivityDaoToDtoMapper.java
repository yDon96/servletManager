package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
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
            throw new NotValidTypeException("[Activity MapperToDto] The given ObjectDao is not an instance of ActivityDao");
        }

        val activityDao = (ActivityDao) objectDao;

        UserDao userDao = activityDao.getMaintainer();
        Integer userId = null;
        if (userDao != null) {
            if (!userDao.isMaintainer()) {
                log.error("[Activity MapperToDto] The user is not a maintainer");
                throw new NotValidTypeException("The user is not a maintainer");
            }
            userId = userDao.getUserId();
        }

        ProcedureDao procedureDao = activityDao.getProcedure();
        Integer procedureId = null;
        if (procedureDao != null) {
            procedureId = procedureDao.getId();
        }

        if (activityDao.getId() == null || activityDao.getId() <= 0) {
            log.error("[ActivityToDtoMapper] invalid id.");
            throw new NotValidTypeException();
        }

        return new ActivityDto(activityDao.getId(),
                userId,
                procedureId,
                activityDao.getWeek(),
                activityDao.getStartingDay(),
                activityDao.getStartingHour(),
                activityDao.isInterruptable(),
                activityDao.getEstimatedTime(),
                activityDao.getDescription());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoActivities) throws NotValidTypeException {
        if(daoActivities == null){
            throw new NotValidTypeException("Not valid type. (convertToDtoList)");
        }
        var activityDtoList = new ArrayList<ActivityDto>();
        for (ActivityDao activityDao :(List<ActivityDao>) daoActivities){
            activityDtoList.add(this.convertToDto(activityDao));
        }
        return activityDtoList;
    }

}
