package com.daayCyclic.servletManager.repository;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICompetencyRepository extends JpaRepository<CompetencyDao, Integer> {

    Optional<CompetencyDao> findByName(String name);

    @Query(value = "SELECT COUNT(*) AS owned_competencies FROM " +
            "(SELECT m_comp.user_id from activity_procedure as proc, users_competencies as m_comp, procedures_competencies as p_comp " +
            "where proc.id = p_comp.procedure_id and m_comp.competency_id = p_comp.competency_id and m_comp.user_id = :user_id and proc.id = :procedure_id) " +
            "AS SKILLS",
            nativeQuery = true)
    Integer countUserOwnedCompetenciesRequiredFromProcedure(@Param("user_id") Integer userId,
                                                            @Param("procedure_id") Integer procedureId);

}
