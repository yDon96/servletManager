package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IActivityRepository;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IProcedureService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ActivityService implements IActivityService {

    @Autowired
    private IActivityRepository iActivityRepository;

    @Autowired
    private IProcedureService procedureService;

    @Autowired
    private IUserService userService;

    @Override
    public Integer generateActivity(ActivityDao activityDao) {
        log.info("[SERVICE: Activity] Starting generate activity into the database: " + activityDao);
        ActivityDao validatedActivity = this.validate(activityDao);
        if (activityExist(validatedActivity.getId())){
            log.info("[ActivityService] Id Activity exist");
            throw new DuplicateGenerationException("Id Activity exist");
        }
        ActivityDao savedActivity = iActivityRepository.save(validatedActivity);
        log.info("[SERVICE: Activity] Generate activity completed successfully");
        return savedActivity.getId();
    }

    /**
     * Update an existing activity if it exist.
     *
     * @param activityDao the {@literal ActivityDao} to update
     * @throws NotValidTypeException if the given activity is null
     * @throws NotFoundException if the given activity is not present into the database
     */
    @Override
    public void updateActivity(ActivityDao activityDao) {
        log.info("[SERVICE: Activity] Starting update of the given activity: " + activityDao);
        ActivityDao validatedActivity = this.validate(activityDao);
        if (!(activityExist(validatedActivity.getId()))){
            String message = "The given activity is not present into the database";
            log.info("[SERVICE: Activity] " + message);
            throw new NotFoundException(message);
        }
        iActivityRepository.save(validatedActivity);
        log.info("[SERVICE: Activity] Update of the activity completed successfully");
    }

    @Override
    public ActivityDao getActivity(Integer activityId) {
        log.info("[SERVICE: Activity] Starting get activity with ID: " + activityId);
        if (activityId == null) {
            throw new NotValidTypeException("activityId must not be null");
        }
        val activityDao = iActivityRepository.findById(activityId);
        return activityDao.orElseThrow(NotFoundException::new);
    }

    @Override
    public List<ActivityDao> getActivities() {
        log.info("[SERVICE: Activity] Starting getting all the activities");
        return iActivityRepository.findAll();
    }

    /**
     * Returns a list of activities based on the requested week.
     *
     * @param week the {@literal week} filter the list
     * @throws NotValidTypeException if the week value is negative or null
     */
    @Override
    public List<ActivityDao> getActivitiesByWeek(Integer week){
        if (week == null || week < 1 || week > 52){
            log.error("[SERVICE: Activity] the week value is not a valid value.");
            throw new NotValidTypeException("the week value is not a valid value");
        }
        return iActivityRepository.findByWeek(week);
    }

    /**
     * Find all activities related to a specific user in a specific week and day.
     *
     * @param userId a {@literal Integer} value containing a user ID
     * @param week a {@literal Integer} value containing a week
     * @param day a {@literal Integer} value containing a day of a week
     * @return a {@literal List} of {@literal ActivityDao} containing the correspondent activities
     * @throws NotValidTypeException if one or more of the parameters is null
     */
    @Override
    public List<ActivityDao> getUserActivitiesByWeekAndDay(Integer userId, Integer week, Integer day) {
        log.info("[SERVICE: Activity] Starting get activities of user " + userId + " during week " + week + ", day " + day);
        if (userId == null || week == null || day == null) {
            throw new NotValidTypeException("Null parameters not allowed");
        }
        List<ActivityDao> retrievedActivities = this.iActivityRepository.findUserActivitiesByWeekAndDay(userId, week, day);
        log.info("[SERVICE: Activity] " + retrievedActivities.size() + " activities retrieved successfully");
        return retrievedActivities;
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
        ActivityDao validatedActivity = this.validate(activityDao);
        if (userDao == null || !(userDao.isMaintainer())) {
            String message = "The given user is not a maintainer";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        validatedActivity.setMaintainer(userDao);
        this.updateActivity(validatedActivity);
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
        ActivityDao validatedActivity = this.validate(activityDao);
        if (procedureDao == null) {
            String message = "The given procedure is null";
            log.info("[SERVICE: Activity] " + message);
            throw new NotValidTypeException(message);
        }
        validatedActivity.setProcedure(procedureDao);
        this.updateActivity(validatedActivity);
        log.info("[SERVICE: Activity] Assignation of the procedure completed successfully");
    }

    protected boolean activityExist(Integer activityId){
        if (activityId == null) {
            return false;
        }
        val optionalActivity = iActivityRepository.findById(activityId);
        return optionalActivity.isPresent();
    }

    private void checkIntegrity(ActivityDao activity) {
        if (activity == null) {throw new NotValidTypeException("The ActivityDao can't be null");}
        if (activity.getEstimatedTime() == null) {throw new NotValidTypeException("The ActivityDao estimated time can't be null");}
        if (activity.getWeek() == null) {throw new NotValidTypeException("The ActivityDao week can't be null");}
        if (activity.getMaintainer() != null && !activity.getMaintainer().isMaintainer()) {throw new NotValidTypeException("The ActivityDao user can only be a Maintainer");}
        if (!(activity.getWeek() >= 1 && activity.getWeek() <= 52)) {throw new NotValidTypeException("The ActivityDao week number should be in [1, 52]");}
        if (!(activity.getStartingDay() == null || (activity.getStartingDay() >= 1 && activity.getStartingDay() <= 7))) {throw new NotValidTypeException("The ActivityDao day number should be in [1, 7]");}
        if (!(activity.getStartingHour() == null || (activity.getStartingHour() >= 0 && activity.getStartingHour() <= 23))) {throw new NotValidTypeException("The ActivityDao hour number should be in [0, 23]");}
        if (!(activity.getEstimatedTime() > 0)) {throw new NotValidTypeException("The ActivityDao estimated time should be > 0");}
    }

    private ActivityDao validate(ActivityDao activity) {
        this.checkIntegrity(activity);
        // Check procedure existence
        if (activity.getProcedure() != null) {
            this.procedureService.getProcedure(activity.getProcedure().getId());  // If doesn't throw any error, the procedure is present into the db
        }

        // Check user existence
        if (activity.getMaintainer() != null) {
            this.userService.getUser(activity.getMaintainer().getUserId());  // If doesn't throw any error, the user is present into the db
        }
        return activity;
    }

}
