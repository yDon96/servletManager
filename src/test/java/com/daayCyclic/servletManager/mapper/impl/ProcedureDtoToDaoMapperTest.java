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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    private void setProcedureDto(Integer id,String title,String description) {
        procedureDto.setId(id);
        procedureDto.setTitle(title);
        procedureDto.setDescription(description);
    }
}