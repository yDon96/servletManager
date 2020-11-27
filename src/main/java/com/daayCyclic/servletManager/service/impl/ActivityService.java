package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import lombok.*;
import com.daayCyclic.servletManager.repository.*;
import com.daayCyclic.servletManager.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements IActivityService {

    @Autowired
    private IActivityRepository iActivityRepository;

    @Override
    public void generateActivity(ActivityDao activityDao) {
        iActivityRepository.save(activityDao);
    }

    @Override
    public ActivityDao getActivity(Integer activityId) {
        val activityDao = iActivityRepository.findById(activityId);
        return activityDao.orElseThrow(()  -> new RuntimeException("activity not found"));
    }

    @Override
    public void updateActivity(ActivityDao activityDao) {

    }

    @Override
    public List<? extends ObjectDao> getActivities() {

        return null;
    }

    @Override
    public void assignMaintainer(UserDao userDao, ActivityDao activityDao) {

    }

    @Override
    public void assignProcedures(ProcedureDao procedureDao, ActivityDao activityDao) {

    }
}
