package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.service.IActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements IActivityService {
    @Override
    public void generateActivity(ActivityDao activityDao) {

    }

    @Override
    public ActivityDao getActivity(Integer activityId) {
        return null;
    }

    //TODO
    @Override
    public void updateActivity(ActivityDao activityDao) {

    }

    @Override
    public List<ActivityDao> getActivities() {
        return null;
    }

    //TODO
    @Override
    public void assignMaintainer(UserDao userDao, ActivityDao activityDao) {

    }

    //TODO
    @Override
    public void assignProcedures(ProcedureDao procedureDao, ActivityDao activityDao) {

    }
}
