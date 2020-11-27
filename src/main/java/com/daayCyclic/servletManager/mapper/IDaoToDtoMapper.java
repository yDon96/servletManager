package com.daayCyclic.servletManager.mapper;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;

import java.util.List;

public interface IDaoToDtoMapper {

    ObjectDto convertToDto(ObjectDao objectDao) throws NotValidTypeException;

    List<ActivityDto> convertDaoListToDtoList(List<ActivityDao> daoObjects) throws NotValidTypeException;
}
