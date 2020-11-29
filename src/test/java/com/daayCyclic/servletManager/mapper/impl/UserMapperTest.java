package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.UserDto;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    void checkDtoDaoEquality(UserDto userDto, UserDao userDao) {
        assertEquals(userDto.getUser_id(), userDao.getUser_id());
        assertEquals(userDto.getName(), userDao.getName());
        assertEquals(userDto.getSurname(), userDao.getSurname());
        assertEquals(userDto.getDateOfBirth(), userDao.getDateOfBirth());
        assertEquals(userDto.getRole(), userDao.getRole().getRole());
    }

}
