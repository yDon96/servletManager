package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.repository.IProcedureRepository;
import com.daayCyclic.servletManager.repository.IRoleRepository;
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

    @Autowired
    private IRoleRepository iRoleRepository;

    private ProcedureDao procedureDao;

    private UserDao userDao;

    @BeforeEach
    private void init() {
        activityDto = new ActivityDto();
        ProcedureDao procedure = new ProcedureDao();
        RoleDao roleDao = new RoleDao();
        RoleDao roleDao1 = new RoleDao();
        roleDao1.setId(5);
        roleDao.setId(4);
        roleDao.setName("maintainer");
        roleDao1.setName("Admin");
        procedure.setTitle("title");
        UserDao maintainer = new UserDao();
        maintainer.setName("aaa");
        maintainer.setSurname("sss");
        maintainer.setDateOfBirth(LocalDate.of(2000,1,1));
        maintainer.setRole(iRoleRepository.save(roleDao));
        iRoleRepository.save(roleDao1);
        procedureDao = iProcedureRepository.save(procedure);
        userDao = iUserRepository.save(maintainer);
    }

    @Test
    void shouldConvertToDao() {
        setActivityDto(1, userDao.getUserId(), procedureDao.getId(), 5, true, 50, "ddd");
        ActivityDao activityDao = new ActivityDao(1, "ddd", 50, true, 5, procedureDao, userDao);
        assertEquals(activityDao,iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test
    void shouldConvertToDaoWithHourAndDay() {
        setActivityDto(1,userDao.getUserId(), procedureDao.getId(), 5, 4, 12, true, 50,"ddd");
        ActivityDao activityDao = new ActivityDao(1, "ddd", 50, true, 5, 4, 12, procedureDao, userDao);
        assertEquals(activityDao,iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test()
    void shouldConvertToDaoWithHourNull() {
        setActivityDto(1,userDao.getUserId(), procedureDao.getId(), 5, 4, null, true, 50,"ddd");
        ActivityDao activityDao = new ActivityDao(1, "ddd", 50, true, 5, 4, null, procedureDao, userDao);
        assertEquals(activityDao,iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test()
    void shouldConvertToDaoWithDayNull() {
        setActivityDto(1,userDao.getUserId(), procedureDao.getId(), 5, null, 22, true, 50,"ddd");
        ActivityDao activityDao = new ActivityDao(1, "ddd", 50, true, 5, null, 22, procedureDao, userDao);
        assertEquals(activityDao,iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test
    void shouldConvertToDaoIfIdIsNull() {
        setActivityDto(1, userDao.getUserId(), procedureDao.getId(), 5, true, 50, "ddd");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test
    void shouldConvertToDaoIfDescriptionIsNull() {
        setActivityDto(1, userDao.getUserId(), procedureDao.getId(), 5, true, 50, null);
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test()
    void shouldThrowExceptionConvertToDaoIfDifferentType() {
        ProcedureDto procedureDto = new ProcedureDto();
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(procedureDto);
        });
    }

    @Test()
    void shouldThrowExceptionConvertToDaoIfMaintainerIdIsNull() {
        setActivityDto(1, null, procedureDao.getId(), 5, true, 50, "ddd");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(activityDto));
    }


    @Test
    void shouldConvertToDaoIfProcedureIdIsNull() {
        setActivityDto(1, userDao.getUserId(), null, 5, true, 50, "ddd");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test()
    void shouldConvertToDaoADtoIfIdIsNegative() {
        setActivityDto(-1, userDao.getUserId(), procedureDao.getId(), 5, true, 50, "ddd");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(activityDto));
    }

    @Test()
    void shouldThrowExceptionConvertToDaoADtoIfMaintainerIdIsNegative() {
        setActivityDto(1, -userDao.getUserId(), procedureDao.getId(), 5, true, 50, "ddd");
        assertThrows(NotFoundException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
    }

    @Test()
    void shouldThrowExceptionConvertToDaoADtoIfProcedureIdIsNegative() {
        setActivityDto(1, userDao.getUserId(), -procedureDao.getId(), 8, true, 50, "ddd");
        assertThrows(NotFoundException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
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

    private void setActivityDto(Integer id, Integer maintainerId, Integer procedureId, Integer week, Integer startingDay, Integer startingHour, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDto.setId(id);
        activityDto.setMaintainerId(maintainerId);
        activityDto.setProcedureId(procedureId);
        activityDto.setWeek(week);
        activityDto.setStartingDay(startingDay);
        activityDto.setStartingHour(startingHour);
        activityDto.setInterruptable(isInterruptable);
        activityDto.setEstimatedTime(estimatedTime);
        activityDto.setDescription(description);
    }
}
