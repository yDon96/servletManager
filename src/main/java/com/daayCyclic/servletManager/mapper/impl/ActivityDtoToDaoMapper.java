package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;

public class ActivityDtoToDaoMapper implements IDtoToDaoMapper {

    @Override
    public ObjectDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {
        return null;
    }
}
