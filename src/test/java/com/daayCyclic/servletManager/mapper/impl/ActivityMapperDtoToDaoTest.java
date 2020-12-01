package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class ActivityMapperDtoToDaoTest {

    @Autowired
    @Qualifier("ActivityToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    private ActivityDto activityDto;

    @Autowired
    private IProcedureRepository iProcedureRepository;

    @Autowired
    private IUserRepository iUserRepository;

    private ProcedureDao procedureDao;

    private UserDao userDao;

    @BeforeEach
    private void init() {
        activityDto = new ActivityDto();
        ProcedureDao procedure = new ProcedureDao();
        procedure.setTitle("title");
        UserDao maintainer = new UserDao();
        maintainer.setName("aaa");
        maintainer.setSurname("sss");
        maintainer.setDateOfBirth(LocalDate.of(2000,1,1));
        procedureDao = iProcedureRepository.save(procedure);
        userDao = iUserRepository.save(maintainer);

    }

    @Test
    void shouldConvertToDao() {
        setActivityDto(1, userDao.getUser_id(),procedureDao.getId(), 5, true, 50, "ddd");
        ActivityDao activityDao = new ActivityDao(1, "ddd", 50, true, 5, procedureDao, userDao);
        assertEquals(activityDao,iDtoToDaoMapper.convertToDao(activityDto));
    }


    private void setActivityDto(Integer id, Integer maintainerId, Integer procedureId, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDto.setId(id);
        activityDto.setMaintainerId(maintainerId);
        activityDto.setProcedureId(procedureId);
        activityDto.setWeek(week);
        activityDto.setInterruptable(isInterruptable);
        activityDto.setEstimatedTime(estimatedTime);
        activityDto.setDescription(description);
    }
}
