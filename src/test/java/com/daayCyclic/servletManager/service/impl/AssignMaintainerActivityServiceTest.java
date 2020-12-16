package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.ActivityDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.service.IActivityService;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignMaintainerActivityServiceTest {

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
    void assignMaintainerUserNotPresent() {
        assertThrows(NotFoundException.class, () -> {
            RoleDao role = new RoleDao();
            role.setId(3);
            role.setName("Maintainer");
            UserDao user = new UserDao(100, "test", "test", LocalDate.now(), role);
            activityService.assignMaintainer(user, activityService.getActivity(2));
        });
    }

    @Test
    void assignMaintainerActivityNotPresent() {
        assertThrows(NotFoundException.class, () -> {
            ActivityDao activity = new ActivityDao();
            activity.setId(100);
            activity.setEstimatedTime(12);
            activity.setWeek(25);
            activityService.assignMaintainer(userService.getUser(2), activity);
        });
    }

    @Test
    void assignMaintainerNullUser() {
        assertThrows(NotValidTypeException.class, () -> {
            activityService.assignMaintainer(null, activityService.getActivity(2));
        });
    }

    @Test
    void assignMaintainerNullActivity() {
        assertThrows(NotValidTypeException.class, () -> {
            activityService.assignMaintainer(userService.getUser(2), null);
        });
    }

    @Test
    void assignMaintainerUserNotAMaintainer() {
        assertThrows(NotValidTypeException.class, () -> {
            activityService.assignMaintainer(userService.getUser(1), null);
        });
    }

    @Test
    void assignMaintainerEverythingGood() {
        UserDao insertedUser = userService.getUser(2);
        activityService.assignMaintainer(insertedUser, activityService.getActivity(2));
        ActivityDao retrievedActivity = activityService.getActivity(2);
        assertEquals(insertedUser, retrievedActivity.getMaintainer());
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
                    i,
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
