package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IActivityRepository;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
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

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProcedureRepository procedureRepository;

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

    //TODO
    @Override
    public void updateActivity(ActivityDao activityDao) {
    }

    @Override
    public List<ActivityDao> getActivities() {
        return iActivityRepository.findAll();
    }

    /**
     * Assign the given user to the given activity (if they aren't null and the given user is a Maintainer).
     *
     * @param userDao the {@literal UserDao} to assign
     * @param activityDao the {@literal ActivityDao} to assign the user to
     * @throws NotValidTypeException if the given user is either null or not a maintainer, or if the given activity is null
     */
    @Override
    public void assignMaintainer(UserDao userDao, ActivityDao activityDao) {
        log.info("[SERVICE: Activity] Starting assignation of " + userDao + " to " + activityDao);
        if (activityDao == null) { //TODO: controllare se ci sta gia un maintainer che la fa?
            String message = "The given activity is null";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        if (userDao == null || !(userDao.isMaintainer())) {
            String message = "The given user is not a maintainer";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        if (!(userRepository.existsById(userDao.getUser_id()))) { //If the user is not into the DB
            String message = "The given user is not present into the database";
            log.info("[SERVICE: Activity] " + message);
            throw new NotFoundException(message);
        }
        activityDao.setMaintainer(userDao);
        this.updateActivity(activityDao); //this.generateActivity(activityDao); //se cambiamo
        log.info("[SERVICE: Activity] Assignation of the maintainer completed successfully");
    }

    /**
     * Assign the given procedure to the given activity (if they aren't null)
     *
     * @param procedureDao the {@literal ProcedureDao} to assign
     * @param activityDao the {@literal ActivityDao} to assign the procedure to
     * @throws NotValidTypeException if either the given procedure or activity is null
     */
    @Override
    public void assignProcedure(ProcedureDao procedureDao, ActivityDao activityDao) {
        log.info("[SERVICE: Activity] Starting assignation of " + procedureDao + " to " + activityDao);
        if (activityDao == null) {
            String message = "The given activity is null";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        if (procedureDao == null) {
            String message = "The given procedure is null";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        if (!(procedureRepository.existsById(procedureDao.getId()))) { //If the procedure is not into the DB
            String message = "The given procedure is not present into the database";
            log.info("[SERVICE: Activity] " + message);
            throw new NotFoundException(message);
        }
        activityDao.setProcedure(procedureDao);
        this.updateActivity(activityDao); //this.generateActivity(activityDao); //se cambiamo
        log.info("[SERVICE: Activity] Assignation of the procedure completed successfully");
    }

    protected boolean activityExist(Integer activityId){
        if (activityId == null) {
            return false;
        }
        val optionalActivity = iActivityRepository.findById(activityId);
        return optionalActivity.isPresent();
    }
}
