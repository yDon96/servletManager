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

    /**
     * Convert a {@literal ActivityDao} to a {@literal ActivityDto}
     *
     * @param objectDao the {@literal ObjectDao} object to convert.
     * @return a {@literal ObjectDto} that is a conversion of the given object.
     * @throws NotValidTypeException if ID is negative or the user is not a maintainer.
     */
    @Override
    public ActivityDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {
        log.info("[ActivityMapperToDto] Initialization of activity conversion in Dto.");
        if (!(objectDao instanceof ActivityDao)){
            log.error("[ActivityMapperToDto] The given ObjectDao is not an instance of ActivityDao.");
            throw new NotValidTypeException("[ActivityMapperToDto] The given ObjectDao is not an instance of ActivityDao.");
        }

        val activityDao = (ActivityDao) objectDao;

        UserDao userDao = activityDao.getMaintainer();
        Integer userId = null;
        if (userDao != null) {
            if (!userDao.isMaintainer()) {
                log.error("[ActivityMapperToDto] The user is not a maintainer.");
                throw new NotValidTypeException("The user is not a maintainer.");
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
            throw new NotValidTypeException("invalid id.");
        }

        log.info("[ActivityMapperToDto] Finished activity conversion into Dto.");
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

    /**
     * converts a activity list of {@literal ActivityDao} to Dto.
     *
     * @param daoActivities activity list {@literal ActivityDao}.
     * @return a {@literal List} of {@literal ActivityDto}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoActivities) throws NotValidTypeException {
        log.info("[ActivityMapperToDto] Initialization to convert activity list to Dto.");
        List<ActivityDto> activityDtoList = null;
        if(daoActivities != null){
            activityDtoList = new ArrayList<>();
            for (ActivityDao activityDao :(List<ActivityDao>) daoActivities){
                activityDtoList.add(this.convertToDto(activityDao));
            }
        }
        log.info("[ActivityMapperToDto] List conversion successfully.");
        return activityDtoList;
    }

}
