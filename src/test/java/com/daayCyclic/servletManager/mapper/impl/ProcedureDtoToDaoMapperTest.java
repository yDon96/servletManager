package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ProcedureDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProcedureDtoToDaoMapperTest {

    @Autowired
    @Qualifier("ProcedureDtoToDaoMapper")
    private IDtoToDaoMapper iDtoToDaoMapper;

    private ProcedureDto procedureDto;

    @BeforeEach
    private void init() {
        procedureDto = new ProcedureDto();
    }

    @Test
    void shouldConvertToDaoIfIdIsNull() {
        setProcedureDto(null,"myTitle","myDescription");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(procedureDto));
    }

    @Test
    void shouldConvertToDaoIfIdIsNotNull() {
        setProcedureDto(1,"myTitle","myDescription");
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(procedureDto));
    }


    @Test
    void shouldConvertToDaoIfDescriptionIsNull() {
        setProcedureDto(2,"myTitle",null);
        assertDoesNotThrow(() -> iDtoToDaoMapper.convertToDao(procedureDto));
    }

    @Test()
    void shouldThrowExceptionConvertToDaoIfDifferentType() {
        ActivityDto activityDto = new ActivityDto();
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(activityDto);
        });
    }

    @Test
    void shouldThrowExceptionConvertToDaoIfIdTitleNull() {
        setProcedureDto(2,null,"myDescription");
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(procedureDto);
        });
    }

    @Test()
    void shouldThrowExceptionConvertToDaoADtoWithEmptyField() {
        setProcedureDto(null,null,null);
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(procedureDto);
        });
    }

    @Test()
    void shouldThrowExceptionConvertToDaoADtoIfIdIsNegative() {
        setProcedureDto(-222,null,null);
        assertThrows(NotValidTypeException.class, () -> {
            iDtoToDaoMapper.convertToDao(procedureDto);
        });
    }

    private void setProcedureDto(Integer id,String title,String description) {
        procedureDto.setId(id);
        procedureDto.setTitle(title);
        procedureDto.setDescription(description);
    }
}