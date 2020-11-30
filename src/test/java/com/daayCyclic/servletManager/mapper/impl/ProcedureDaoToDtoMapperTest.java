package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProcedureDaoToDtoMapperTest {

    @Autowired
    @Qualifier("ProcedureMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

    private ProcedureDao procedureDao;

    @BeforeEach
    private void init(){
        procedureDao = new ProcedureDao();
    }

    @Test
    void shouldConvertToDto() throws NotValidTypeException {
        setProcedureDao(1,"generic","description");
        assertEquals(new ProcedureDto(1,"generic","description"),iDaoToDtoMapper.convertToDto(procedureDao));
    }

    @Test
    void shouldConvertToDtoMissingTitle() throws NotValidTypeException {
        setProcedureDao(1,null,"description");
        assertEquals(new ProcedureDto(1,null,"description"),iDaoToDtoMapper.convertToDto(procedureDao));
    }

    @Test
    void shouldConvertToDtoMissingDescription() throws NotValidTypeException {
        setProcedureDao(1,"generic",null);
        assertEquals(new ProcedureDto(1,"generic",null),iDaoToDtoMapper.convertToDto(procedureDao));
    }

    @Test
    void shouldConvertToDtoWithOnlyId() throws NotValidTypeException {
        setProcedureDao(1,null,null);
        assertEquals(new ProcedureDto(1,null,null),iDaoToDtoMapper.convertToDto(procedureDao));
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdIsNotSet() {
        setProcedureDao(null,"generic","description");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(procedureDao);
        });

    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndDescriptionIsNotSet() {
        setProcedureDao(null,"generic",null);
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(procedureDao);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDtoIfIdAndTitleIsNotSet() {
        setProcedureDao(null,null,"description");
        assertThrows(NullPointerException.class, () -> {
            iDaoToDtoMapper.convertToDto(procedureDao);
        });
    }


    @Test()
    void shouldThrowExceptionConvertToDtoIfDifferentType() {
        ActivityDao activityDao = new ActivityDao();
        activityDao.setId(1);
        assertThrows(NotValidTypeException.class, () -> {
            iDaoToDtoMapper.convertToDto(activityDao);
        });
    }

    private void setProcedureDao(Integer id,String title,String description) {
        procedureDao.setId(id);
        procedureDao.setTitle(title);
        procedureDao.setDescription(description);
    }
}