package com.daayCyclic.servletManager.controller;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.service.ICompetencyService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AssignCompetencyToUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICompetencyService competencyService;

    @BeforeEach
    void setUp() {
        createRolesInDB();
        createUsersInDB();
        createCompetenciesInDB();
    }

    @Test
    void assignCompetencyToUserCompetencyNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/user/6/assign-competency")
                    .param("competency", "competency1000"))
                    .andDo(print())
                    .andExpect(status().isNotFound()
                    );
        });
    }

    @Test
    void assignCompetencyToUserUserNotPresent() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/user/100/assign-competency")
                    .param("userId", "100")
                    .param("competency", "competency1"))
                    .andDo(print())
                    .andExpect(status().isNotFound()
                    );
        });
    }

    @Test
    void assignCompetencyToUserUserNotMaintainer() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/user/1/assign-competency")
                    .param("userId", "1")
                    .param("competency", "competency1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest()
                    );
        });
    }

    @Test
    void assignCompetencyToUserEverythingOk() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/user/6/assign-competency")
                    .param("userId", "6")
                    .param("competency", "competency1"))
                    .andDo(print())
                    .andExpect(status().isOk()
                    );
        });
    }

    private RoleDao createRole(int id, String role) {
        RoleDao roleDao = new RoleDao();
        roleDao.setId(id);
        roleDao.setName(role);
        return roleDao;
    }

    private UserDao createUser(int id, String name, String surname, String role) {
        UserDao user = new UserDao();
        user.setUserId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(LocalDate.now());
        user.setRole(createRole(id, role));
        return user;
    }

    private void createRolesInDB() {
        for (int i = 1; i < 6; i++) {
            this.roleService.generateRole(this.createRole(i,"role" + i));
        }
        this.roleService.generateRole(this.createRole(6, "Maintainer"));
    }

    private void createCompetenciesInDB() {
        for (int i = 1; i < 6; i++) {
            this.competencyService.generateCompetency(new CompetencyDao(i, "competency" + i));
        }
    }

    private void createUsersInDB() {
        for (int i = 1; i < 6; i++) {
            UserDao newUser = createUser(
                    i,
                    "name" + i,
                    "surname" + i,
                    "role" + i);
            this.userService.generateUser(newUser);
        }
        this.userService.generateUser(this.createUser(6, "good", "good", "Maintainer"));
    }

}
