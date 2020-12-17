package com.daayCyclic.servletManager.converter;

import com.daayCyclic.servletManager.dao.RoleDao;
import org.springframework.stereotype.Component;

@Component("RoleConverter")
public class RoleConverter extends Converter<String, RoleDao> {

    public RoleConverter() {
        super(RoleConverter::convertToEntity, RoleConverter::convertToDto);
    }

    private static String convertToDto(RoleDao role) {
        if (role != null && !role.getName().equals("")) {
            return role.getName();
        }
        return null;
    }

    private static RoleDao convertToEntity(String role) {
        if (role != null && !role.equals("")) {
            RoleDao newDao = new RoleDao();
            newDao.setName(role);
            return newDao;
        }
        return null;
    }

}
