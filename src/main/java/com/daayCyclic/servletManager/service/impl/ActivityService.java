package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IActivityRepository;
import com.daayCyclic.servletManager.service.IActivityService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DuplicateFormatFlagsException;
import java.util.List;

@Slf4j
@Service
public class ActivityService implements IActivityService {

    @Autowired
    private IActivityRepository iActivityRepository;

    @Override
    public Integer generateActivity(ActivityDao activityDao) throws DuplicateFormatFlagsException {
        if (activityExist(activityDao.getId())){
            log.error("[ActivityService] Id Activity exist");
            throw new DuplicateGenerationException("Id Activity exist");
        }
        ActivityDao savedActivity = iActivityRepository.save(activityDao);
        return savedActivity.getId();
    }

    @Override
    public ActivityDao getActivity(Integer activityId) {
        val activityDao = iActivityRepository.findById(activityId);
        return activityDao.orElseThrow(NotFoundException::new);
    }

    /**
     * Update an existing activity if it exist.
     *
     * @param activityDao the {@literal ActivityDao} to update
     * @throws NotValidTypeException if the given activity is null
     * @throws NotFoundException if the given activity is not present into the database
     */
    //TODO: decidere se tenere questo o fare l'update con la generate
    @Override
    public void updateActivity(ActivityDao activityDao) {
        log.info("[SERVICE: Activity] Starting update of the given activity: " + activityDao);
        if (activityDao == null) {
            String message = "The given activity is null";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        if (!(activityExist(activityDao.getId()))){
            String message = "The given activity is not present into the database";
            log.info("[SERVICE: Activity] " + message);
            throw new NotFoundException(message);
        }
        iActivityRepository.save(activityDao);
        log.info("[SERVICE: Activity] Update of the activity completed successfully");
    }

    @Override
    public List<ActivityDao> getActivities() {
        return iActivityRepository.findAll();
    }

    //TODO
    @Override
    public void assignMaintainer(UserDao userDao, ActivityDao activityDao) {

    }

    //TODO
    @Override
    public void assignProcedures(ProcedureDao procedureDao, ActivityDao activityDao) {

    }

    protected boolean activityExist(Integer activityId){
        if (activityId == null) {
            return false;
        }
        val optionalActivity = iActivityRepository.findById(activityId);
        return optionalActivity.isPresent();
    }
}
