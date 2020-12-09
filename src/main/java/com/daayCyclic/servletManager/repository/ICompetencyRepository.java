package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICompetencyRepository extends JpaRepository<CompetencyDao, Integer> {

    Optional<CompetencyDao> findByName(String name);

}
