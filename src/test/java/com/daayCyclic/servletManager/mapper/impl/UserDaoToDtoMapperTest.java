package com.daayCyclic.servletManager.mapper.impl;

import com.daayCyclic.servletManager.dao.ObjectDao;
import com.daayCyclic.servletManager.dao.ProcedureDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.dto.ObjectDto;
import com.daayCyclic.servletManager.dto.UserDto;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoToDtoMapperTest extends UserMapperTest{

    private UserDaoToDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserDaoToDtoMapper();
    }

    @Test
    void convertToDtoGoodObject() {
        UserDao userDao = createDaoUser(1, "Giacomo", "Nanni");
        assertDoesNotThrow(() -> {
            UserDto userDto = (UserDto) mapper.convertToDto(userDao);
            checkDtoDaoEquality(userDto, userDao);
        });
    }

    @Test
    void convertToDtoBadObject() {
        ProcedureDao user = new ProcedureDao();
        assertThrows(NotValidTypeException.class, () -> mapper.convertToDto(user));
    }

    @Test
    void convertToDtoNullObject() {
        assertThrows(NotValidTypeException.class, () -> mapper.convertToDto(null));
    }

    @Test
    void convertToDtoNullAttributes() {
        UserDao userDao = createDaoUser(1, null, null);
        assertDoesNotThrow(() -> {
            UserDto userDto = (UserDto) mapper.convertToDto(userDao);
            checkDtoDaoEquality(userDto, userDao);
        });
    }

    @Test
    void convertToDtoListEmpty() {
        ArrayList<UserDao> list = new ArrayList<>();
        assertDoesNotThrow(() -> {
            List<? extends ObjectDto> listDto = mapper.convertDaoListToDtoList(list);
            assertEquals(0, listDto.size());
        });
    }

    @Test
    void convertToDtoListNull() {
        assertDoesNotThrow(() -> assertNull(mapper.convertDaoListToDtoList(null)));
    }

    @Test
    void convertToDtoGoodList() {
        ArrayList<UserDao> listDao = createDaoList();
        assertDoesNotThrow(() -> {
            List<? extends ObjectDto> listDto = mapper.convertDaoListToDtoList(listDao);
            assertEquals(listDao.size(), listDto.size());
            for (int i = 0; i < listDto.size(); i++) {
                assertTrue(listDto.get(i) instanceof UserDto);
                checkDtoDaoEquality((UserDto) listDto.get(i), listDao.get(i));
            }
        });
    }

    @Test
    void convertToDtoWrongElements() {
        ArrayList<ObjectDao> listDao = new ArrayList<>(createDaoList());
        listDao.add(new ProcedureDao());
        assertThrows(NotValidTypeException.class,
                () -> mapper.convertDaoListToDtoList(listDao));
    }

    // BEGIN Utilities methods
    RoleDao createDaoRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setRole(role);
        return roleDao;
    }

    UserDao createDaoUser(int id, String name, String surname) {
        UserDao user = new UserDao();
        user.setUser_id(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.of(1968, 1, 1));
        user.setRole(createDaoRole(1, "System Administrator"));
        return user;
    }

    ArrayList<UserDao> createDaoList() {
        ArrayList<UserDao> list = new ArrayList<>();
        list.add(createDaoUser(1, "Mario", "Rossi"));
        list.add(createDaoUser(1, "Mario", "Ratto"));
        list.add(createDaoUser(1, "Giacomo", "Vanvitelli"));
        return list;
    }
    // END Utilities Methods
}