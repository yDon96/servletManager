package com.daayCyclic.servletManager.service;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;

import java.util.List;

public interface IActivityService {
    void generateActivity(ActivityDao activityDao);
    ActivityDao getActivity(Integer activityId);
    void updateActivity(ActivityDao activityDao);
    List<ActivityDao> getActivities();
    void assignMaintainer(UserDao userDao, ActivityDao activityDao);
    void assignProcedures(ProcedureDao procedureDao, ActivityDao activityDao);
}
