package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.ActivityDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public interface IActivityRepository extends JpaRepository<ActivityDao,Integer> {

    Integer save(ActivityDao activityDao);

    Optional<ActivityDao> findById(Integer activityId);

    List<ActivityDao> findAll();
}
