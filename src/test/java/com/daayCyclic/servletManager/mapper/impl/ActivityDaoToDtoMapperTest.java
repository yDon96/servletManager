package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ActivityDaoToDtoMapperTest {

    @Autowired
    @Qualifier("ActivityToDtoMapper")
    private IDaoToDtoMapper iDaoToDtoMapper;

}
