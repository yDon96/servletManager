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

    /**
     * Convert a {@literal ActivityDto} to a {@literal ActivityDao}.
     *
     * @param objectDto the {@literal ObjectDto} object to convert.
     * @return a {@literal ObjectDao} that is a conversion of the given object.
     * @throws NotValidTypeException if the object is not a {@literal ActivityDto}.
     */
    @Override
    public ActivityDao convertToDao(ObjectDto objectDto) throws NotValidTypeException {
        log.info("[ActivityMapperToDao] Initialization conversion to Dao.");
        if (!(objectDto instanceof ActivityDto)){
            log.error("[ActivityMapperToDao] Not valid type.");
            throw new NotValidTypeException("Not valid type. (ActivityToDaoMapper)");
        }

        val activityDto = (ActivityDto) objectDto;
        ProcedureDao procedure = null;
        UserDao maintainer = null;

        if (activityDto.getMaintainerId() != null){
            maintainer = iUserService.getUser(activityDto.getMaintainerId());
        }
        if (activityDto.getProcedureId() != null){
            procedure = iProcedureService.getProcedure(activityDto.getProcedureId());
        }
        log.info("[ActivityMapperToDao] Conversion successfully.");
        return new ActivityDao(activityDto.getId(),
                activityDto.getDescription(),
                activityDto.getEstimatedTime(),
                activityDto.isInterruptable(),
                activityDto.getWeek(),
                activityDto.getStartingDay(),
                activityDto.getStartingHour(),
                procedure,
                maintainer);
    }

}
