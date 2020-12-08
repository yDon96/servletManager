package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompetencyRepository extends JpaRepository<CompetencyDao, Integer> {
}
