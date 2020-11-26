package com.daayCyclic.servletManager.mapper;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;

public interface IDtoToDaoMapper {

    ObjectDao convertToDao(ObjectDto objectDto) throws NotValidTypeException;

}
