package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.CompetencyDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface IActivityRepository extends JpaRepository<ActivityDao,Integer> {

    @Query(value = "SELECT * FROM activity WHERE maintainer_user_id = :id AND week = :week AND starting_day = :day",
            nativeQuery = true)
    List<ActivityDao> findUserActivitiesByWeekAndDay(@Param("id") Integer userId,
                                                     @Param("week") Integer week,
                                                     @Param("day") Integer day);

    List<ActivityDao> findByWeek(Integer week);
  
}
