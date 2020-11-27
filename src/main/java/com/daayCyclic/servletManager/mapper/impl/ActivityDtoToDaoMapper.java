package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import lombok.*;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import org.springframework.stereotype.Component;

@Component("ActivityToDaoMapper")
public class ActivityDtoToDaoMapper implements IDtoToDaoMapper {

    @Override
    public ActivityDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {

        if (!(objectDto instanceof ActivityDao)){
            throw new NotValidTypeException("Not valid type. (ActivityToDaoMapper)");
        }

        val activityDto = (ActivityDto) objectDto;

        return new ActivityDao();
    }
}
