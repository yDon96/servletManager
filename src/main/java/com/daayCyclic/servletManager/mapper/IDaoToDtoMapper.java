package com.daayCyclic.servletManager.mapper;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;

import java.util.List;

public interface IDaoToDtoMapper {

    ObjectDto convertToDto(ObjectDao objectDao) throws NotValidTypeException;

    List<? extends ObjectDto> convertDaoListToDtoList(List<? extends ObjectDao> daoObjects) throws NotValidTypeException;

}
