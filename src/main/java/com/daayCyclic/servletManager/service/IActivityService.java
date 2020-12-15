package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;

import java.util.List;

public interface IActivityService {
    Integer generateActivity(ActivityDao activityDao) throws DuplicateGenerationException;
    void updateActivity(ActivityDao activityDao);
    List<ActivityDao> getActivities();
    ActivityDao getActivity(Integer activityId);
    List<ActivityDao> getUserActivitiesByWeekAndDay(Integer userId, Integer week, Integer day);
    void assignMaintainer(UserDao userDao, ActivityDao activityDao);
    void assignProcedure(ProcedureDao procedureDao, ActivityDao activityDao);
    List<ActivityDao> getActivitiesByWeek(Integer week);
}
