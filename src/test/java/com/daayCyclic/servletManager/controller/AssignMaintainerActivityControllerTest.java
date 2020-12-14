package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignMaintainerActivityControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private IActivityService activityService;
    @Autowired private IUserService userService;
    @Autowired private IRoleService roleService;

    @BeforeEach
    void setUp() {
        addActivities();
        addRoles();
        addUsers();
    }

    @Test
    void assignMaintainerUserNotPresent(){
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignMaintainer")
                    .param("userID", "100")
                    .param("activityID", "1"))
                        .andDo(print())
                        .andExpect(status().isNotFound()
            );
        });
    }

    @Test
    void assignMaintainerActivityNotPresent(){
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignMaintainer")
                    .param("userID", "2")
                    .param("activityID", "100"))
                    .andDo(print())
                    .andExpect(status().isNotFound()
                    );
        });
    }

    @Test
    void assignMaintainerUserNotAMaintainer(){
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignMaintainer")
                    .param("userID", "1")
                    .param("activityID", "1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest()
                    );
        });
    }

    @Test
    void assignMaintainerEverythingGood(){
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/assignMaintainer")
                    .param("userID", "2")
                    .param("activityID", "1"))
                    .andDo(print())
                    .andExpect(status().isOk()
                    );
            assertEquals(userService.getUser(2), activityService.getActivity(1).getMaintainer());
        });

    }

    /**
     * Add some Activities into the database
     */
    void addActivities() {
        for (int i = 1; i < 6; i++) {
            ActivityDao newActivity = new ActivityDao(
                    i,
                    "description" + i,
                    10 + i,
                    true,
                    i * 2,
                    null,
                    null);
            activityService.generateActivity(newActivity);
        }
    }

    /**
     * Add some Users into the database
     */
    void addUsers() {
        for (int i = 1; i < 6; i++) {
            UserDao newUser = new UserDao(
                    i,
                    "name" + i,
                    "surname" + i,
                    LocalDate.of(1900, 1, i),
                    null
            );

            // Setto Maintainer gli utenti pari e planner gli altri:
            RoleDao role = new RoleDao();
            if (i % 2 == 0) {
                role.setId(3);
                role.setName("Maintainer");
            } else {
                role.setId(2);
                role.setName("Planner");
            }
            newUser.setRole(role);

            userService.generateUser(newUser);
        }
    }

    /**
     * Add some Roles into the database
     */
    void addRoles() {
        String[] roles = {"Admin", "Planner", "Maintainer"};
        for (String role : roles) {
            RoleDao roleDao = new RoleDao();
            roleDao.setName(role);
            roleService.generateRole(roleDao);
        }
    }

}
