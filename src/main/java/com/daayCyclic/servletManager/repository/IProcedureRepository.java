package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.ProcedureDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProcedureRepository extends JpaRepository<ProcedureDao,Integer> {
}
