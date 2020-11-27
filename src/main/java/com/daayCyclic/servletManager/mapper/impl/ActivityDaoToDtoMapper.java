package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDaoToDtoMapper;

import java.util.List;

public class ActivityDaoToDtoMapper implements IDaoToDtoMapper {

    @Override
    public ObjectDto convertToDto(ObjectDao objectDao) throws NotValidTypeException {
        return null;
    }

    @Override
    public List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoObjects) throws NotValidTypeException {
        return null;
    }


}
