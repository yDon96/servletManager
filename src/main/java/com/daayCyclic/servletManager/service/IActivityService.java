package com.daayCyclic.servletManager.service;
import com.daayCyclic.servletManager.dao.*;
import java.util.*;

public interface IActivityService {

    void generateActivity(ActivityDao activityDao);

    ActivityDao ingetActivity(Integer activityId);

    void updateActivity(ActivityDao activityDao);

    List<? extends ObjectDao> getActivities();

    void assignMaintainer(UserDao userDao, ActivityDao activityDao);

    void assignProcedures(ProcedureDao procedureDao, ActivityDao activityDao);
}
