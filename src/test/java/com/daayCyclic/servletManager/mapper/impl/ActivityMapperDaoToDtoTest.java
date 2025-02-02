package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActivityMapperDaoToDtoTest {

    @Autowired
    @Qualifier("ActivityToDtoMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    private ActivityDao activityDao;

    private ProcedureDao procedure;

    private UserDao maintainer;

    @BeforeEach
    private void init(){
        activityDao = new ActivityDao();
        ProcedureDao procedureDao = new ProcedureDao();
        UserDao userDao = new UserDao();
        procedureDao.setTitle("t");
        userDao.setName("a");
        userDao.setSurname("s");
        userDao.setDateOfBirth(LocalDate.of(2000,1,1));
        RoleDao roleDao = new RoleDao();
        roleDao.setId(1);
        roleDao.setName("maintainer");
        userDao.setRole(roleDao);
        procedure =  procedureDao;
        maintainer = userDao;
    }

    @Test
    void shouldConvertToDto() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUserId(), procedure.getId(), 5, true, 50, "ddd"), iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldConvertToDtoWithHourAndDay() throws NotValidTypeException {
        setActivityDao(1,maintainer,procedure,5,3,20,true,25,"s");
        assertEquals(new ActivityDto(1, maintainer.getUserId(), procedure.getId(), 5, 3, 20, true, 25, "s"), iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldConvertToDtoWithHourIsNull() throws NotValidTypeException {
        setActivityDao(1,maintainer,procedure,5,3,null,true,25,"s");
        assertEquals(new ActivityDto(1, maintainer.getUserId(), procedure.getId(), 5, 3, null, true, 25, "s"), iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldConvertToDtoWithDayIsNull() throws NotValidTypeException {
        setActivityDao(1,maintainer,procedure,5,null,18,true,25,"s");
        assertEquals(new ActivityDto(1, maintainer.getUserId(), procedure.getId(), 5, null, 18, true, 25, "s"), iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdIsNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingDescription() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, null);
        assertEquals(new ActivityDto(1, maintainer.getUserId(), procedure.getId(), 5, true, 50, null),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndDescriptionNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, null);
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingMaintainer() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, null, procedure.getId(), 5, true, 50, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndMaintainerNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingProcedure() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUserId(), null, 5, true, 50, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndProcedureNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldConvertToDtoMissingEstimateTime() throws NotValidTypeException {
        setActivityDao(1, maintainer, procedure, 5, true, null, "ddd");
        assertEquals(new ActivityDto(1, maintainer.getUserId(), procedure.getId(), 5, true, null, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndEstimateTimeNotSet() {
        setActivityDao(null, maintainer, procedure, 5, true, null, "ddd");
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoWithIdNegative() {
        setActivityDao(-7, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfDifferentType() {
        ProcedureDao procedureDao = new ProcedureDao();
        procedureDao.setId(1);
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(procedureDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoTheRoleIsNotMaintainer() {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(3);
        roleDao.setName("Admin");
        maintainer.setRole(roleDao);
        setActivityDao(1, maintainer, procedure, 5, true, 50, "ddd");
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }
    @Test
    void shouldConvertToDtoMissingRoleMaintainer() throws NotValidTypeException {
        setActivityDao(1, null, procedure, 5, true, 50, "ddd");
        assertEquals(new ActivityDto(1, null, procedure.getId(), 5, true, 50, "ddd"),iDaoToDtoMapper.convertToDto(activityDao));
    }

    private void setActivityDao(Integer id, UserDao maintainer, ProcedureDao procedure, Integer week, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDao.setId(id);
        activityDao.setMaintainer(maintainer);
        activityDao.setProcedure(procedure);
        activityDao.setWeek(week);
        activityDao.setInterruptable(isInterruptable);
        activityDao.setEstimatedTime(estimatedTime);
        activityDao.setDescription(description);
    }

    private void setActivityDao(Integer id, UserDao maintainer, ProcedureDao procedure, Integer week, Integer startingDay, Integer startingHour, boolean isInterruptable, Integer estimatedTime, String description) {
        activityDao.setId(id);
        activityDao.setMaintainer(maintainer);
        activityDao.setProcedure(procedure);
        activityDao.setWeek(week);
        activityDao.setStartingDay(startingDay);
        activityDao.setStartingHour(startingHour);
        activityDao.setInterruptable(isInterruptable);
        activityDao.setEstimatedTime(estimatedTime);
        activityDao.setDescription(description);
    }
}
