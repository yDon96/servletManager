package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.ActivityDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public interface IActivityRepository extends JpaRepository<ActivityDao,Integer> {
}
