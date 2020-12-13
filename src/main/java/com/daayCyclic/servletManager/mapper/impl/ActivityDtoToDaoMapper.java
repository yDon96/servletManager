package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ActivityDto;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.mapper.IDtoToDaoMapper;
import com.daayCyclic.servletManager.service.IProcedureService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ActivityToDaoMapper")
public class ActivityDtoToDaoMapper implements IDtoToDaoMapper {

    @Autowired
    private IProcedureService iProcedureService;

    @Autowired
    private IUserService iUserService;

    @Override
    public ActivityDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {

        if (!(objectDto instanceof ActivityDto)){
            throw new NotValidTypeException("Not valid type. (ActivityToDaoMapper)");
        }

        val activityDto = (ActivityDto) objectDto;
        ProcedureDao procedure = null;
        UserDao maintainer = null;
        checkConsistentActivityDto(activityDto);

        if (activityDto.getMaintainerId() != null){
            maintainer = iUserService.getUser(activityDto.getMaintainerId());
        }
        if (activityDto.getProcedureId() != null){
            procedure = iProcedureService.getProcedure(activityDto.getProcedureId());
        }


        return new ActivityDao(activityDto.getId(),
                activityDto.getDescription(),
                activityDto.getEstimatedTime(),
                activityDto.isInterruptable(),
                activityDto.getWeek(),
                procedure,
                maintainer);
    }

    private void checkConsistentActivityDto(ActivityDto activityDto){
        if (activityDto.isAllNull()){
            log.error("[ActivityToDtoMapper] all fields are null.");
            throw new NotValidTypeException("all fields are null.");
        }
        if (activityDto.isFielsNull()){
            if (activityDto.getId() != null){
                if (activityDto.getId() < 0){
                    log.error("[ActivityToDtoMapper] Id negative.");
                    throw new NotValidTypeException("Id negative.");
                }
            }
            if (activityDto.getEstimatedTime() != null){
                if (activityDto.getEstimatedTime() < 0){
                    log.error("[ActivityToDtoMapper] EstimatedTime negative.");
                    throw new NotValidTypeException("EstimatedTime negative.");
                }
            }
            if (activityDto.getWeek() != null){
                if (activityDto.getWeek() < 0){
                    log.error("[ActivityToDtoMapper] Week negative.");
                    throw new NotValidTypeException("Week negative.");
                }
            }
            if (activityDto.getMaintainerId() != null){
                if (activityDto.getMaintainerId() < 0){
                    log.error("[ActivityToDtoMapper] MaintainerId negative.");
                    throw new NotValidTypeException("MaintainerId negative.");
                }
            }
            if (activityDto.getProcedureId() != null){
                if (activityDto.getProcedureId() < 0){
                    log.error("[ActivityToDtoMapper] ProcedureId negative.");
                    throw new NotValidTypeException("ProcedureId negative.");
                }
            }
        }
        if (!activityDto.isFielsNull()){
            if (activityDto.isAlLFieldsNegative()){
                log.error("[ActivityToDtoMapper] there are negative fields.");
                throw new NotValidTypeException("there are negative fields.");
            }
        }
    }
}
