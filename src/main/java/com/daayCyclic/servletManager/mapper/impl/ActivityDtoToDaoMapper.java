package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import lombok.*;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ActivityToDaoMapper")
public class ActivityDtoToDaoMapper implements IDtoToDaoMapper {

    @Override
    public ActivityDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {

        if (!(objectDto instanceof ActivityDto)){
            throw new NotValidTypeException("Not valid type. (ActivityToDaoMapper)");
        }

        val activityDto = (ActivityDto) objectDto;

        if (activityDto.isAllNull()){
            log.error("[ActivityToDtoMapper] there are 'null' fields.");
            throw new NotValidTypeException("there are 'null' fields.");
        }

        if (activityDto.isAllNotPositive()){
            log.error("[ActivityToDtoMapper] there are non-positive fields.");
            throw new NotValidTypeException("there are non-positive fields.");
        }

        return new ActivityDao();
    }
}
