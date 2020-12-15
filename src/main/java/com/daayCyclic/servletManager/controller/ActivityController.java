package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IProcedureService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ActivityController {

    @Autowired
    private IActivityService iActivityService;

    @Autowired
    private IProcedureService procedureService;

    @Autowired
    private IUserService userService;

    @Autowired
    @Qualifier("ActivityToDtoMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    @Autowired
    @Qualifier("ActivityToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    @PostMapping(path = "/activity")
    public void postActivity(@RequestBody ActivityDto activityDto) throws NotValidTypeException {
        log.info("[REST] Post Activity");
        val activityDao = (ActivityDao) iDtoToDaoMapper.convertToDao(activityDto);
        iActivityService.generateActivity(activityDao);
        log.debug("[REST] End Post activity");
    }

    @PutMapping(path = "/activity")
    public void putActivity(@RequestBody ActivityDto activityDto){
        log.info("[REST] Put Activity");
        val activityDao = (ActivityDao) iDtoToDaoMapper.convertToDao(activityDto);
        iActivityService.updateActivity(activityDao);
        log.debug("[REST] End Put activity");
    }

    @GetMapping(path = "/activity/{activityId}")
    public ActivityDto getActivity(@PathVariable("activityId") Integer activityId) throws NotValidTypeException {
        log.info("[REST] Get Activity");

        if(activityId < 0){
            log.error("[REST] Id activity is negative" );
            throw new NotValidTypeException("Negative Id.");
        }

        val activityDao = iActivityService.getActivity(activityId);
        log.debug("[REST] End Get activity");
        return (ActivityDto) iDaoToDtoMapper.convertToDto(activityDao);
    }

    @GetMapping(path = "/activities")
    @SuppressWarnings("unchecked")
    public List<ActivityDto> getActivities() throws NotValidTypeException {
        log.info("[REST] Get Activities");
        val activityDao = iActivityService.getActivities();
        log.debug("[REST] End Get activities");
        return (List<ActivityDto>) iDaoToDtoMapper.convertDaoListToDtoList(activityDao);
    }

    @GetMapping(path = "/activitiesWeek")
    public List<ActivityDto> getActivitiesByWeek(@RequestParam Integer week) throws NotValidTypeException {
        log.info("[REST] get a list of activities for weeks");
        val activityDao = iActivityService.getActivitiesByWeek(week);
        log.debug("[REST] End Get activity week");
        return (List<ActivityDto>) iDaoToDtoMapper.convertDaoListToDtoList(activityDao);
    }

    /**
     * Find all activities related to a specific user in a specific week and day.
     *
     * @param userId a {@literal Integer} value containing a user ID
     * @param week a {@literal Integer} value containing a week
     * @param day a {@literal Integer} value containing a day of a week
     * @return a {@literal List} of {@literal ActivityDto} containing the correspondent activities
     * @throws NotValidTypeException if one or more of the parameters is null
     */
    @GetMapping(path = "/activities/week/{week}/day/{day}")
    @SuppressWarnings("unchecked")
    public List<ActivityDto> getUserActivitiesByWeekAndDay(@RequestParam Integer userId, @PathVariable("week") Integer week, @PathVariable("day") Integer day) {
        log.info("[REST] Starting get activities of user " + userId + " during week " + week + ", day " + day);
        List<ActivityDao> retrievedActivities = this.iActivityService.getUserActivitiesByWeekAndDay(userId, week, day);
        List<ActivityDto> convertedList = (List<ActivityDto>) iDaoToDtoMapper.convertDaoListToDtoList(retrievedActivities);
        log.info("[REST] Activities retrieved successfully");
        return convertedList;
    }

    /**
     * Assign the procedure corresponding to the given procedureID to the activity corresponding to the given activityID
     *
     * @param procedureID a {@literal int} ID which identifies a procedure
     * @param activityID a {@literal int} ID which identifies an activity
     */
    @PutMapping(path = "/activity/{activityId}/assign-procedure")
    public void assignProcedure(@RequestParam int procedureID, @PathVariable("activityId") int activityID){
        log.info("[REST] Start assigning procedure " + procedureID + "to " + activityID + "activity");
        ActivityDao activity = iActivityService.getActivity(activityID);
        ProcedureDao procedure = procedureService.getProcedure(procedureID);
        iActivityService.assignProcedure(procedure, activity);
        log.info("[REST] Procedure assigned successfully to the activity");
    }

    /**
     * Assign the maintainer corresponding to the given userID to the activity corresponding to the given activityID
     *
     * @param userID a {@literal int} ID which identifies a user
     * @param activityID a {@literal int} ID which identifies an activity
     */
    @PutMapping(path = "/activity/{activityId}/assign-maintainer")
    public void assignMaintainer(@RequestParam int userID,@PathVariable("activityId") int activityID){
        log.info("[REST] Start assigning maintainer: " + userID + "to " + activityID + " activity");
        ActivityDao activity = iActivityService.getActivity(activityID);
        UserDao user = userService.getUser(userID);
        iActivityService.assignMaintainer(user, activity);
        log.info("[REST] Maintainer assigned successfully to the activity");
    }

}
