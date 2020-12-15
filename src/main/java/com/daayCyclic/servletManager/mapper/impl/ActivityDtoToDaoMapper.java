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

        if (activityDto.getId() == null || activityDto.getId() < 0){
            log.error("[ActivityToDtoMapper] Id is not valid value.");
            throw new NotValidTypeException("Id is not valid value.");
        }

        if (activityDto.getEstimatedTime() == null || activityDto.getEstimatedTime() < 0){
            log.error("[ActivityToDtoMapper] EstimatedTime is not valid value.");
            throw new NotValidTypeException("EstimatedTime is not valid value.");
        }

        if (activityDto.getWeek() == null || activityDto.getWeek() < 0){
            log.error("[ActivityToDtoMapper] week is not valid value.");
            throw new NotValidTypeException("week is not valid value.");
        }

        if (activityDto.getMaintainerId() != null && activityDto.getMaintainerId() < 0){
            log.error("[ActivityToDtoMapper] MaintainerId negative.");
            throw new NotValidTypeException("MaintainerId negative.");
        }

        if (activityDto.getProcedureId() != null && activityDto.getProcedureId() < 0){
            log.error("[ActivityToDtoMapper] ProcedureId negative.");
            throw new NotValidTypeException("ProcedureId negative.");
        }

        if (activityDto.getMaintainerId() != null){
            UserDao user = iUserService.getUser(activityDto.getMaintainerId());
            if (user.getRole() != null && !user.isMaintainer()){
                log.error("[Activity MapperToDao] the role is not correct");
                throw new NotValidTypeException("the role is not correct.");
            }
        }
    }
}
