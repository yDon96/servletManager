package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(value = "UserService")
public class UserService implements IUserService{

    @Autowired
    private IUserRepository repository;


    /**
     * Save the given user into the database. If a user with the same ID of the one passed is already present, updates it.
     *
     * @param user the user to save/update
     * @throws org.springframework.dao.DataIntegrityViolationException if some of parameters of the given user doesn't respect database constraints
     * @throws InvalidDataAccessApiUsageException if the given user is {@literal null}
     */
    @Override
    public void generateUser(UserDao user) {
        log.info("[SERVICE: User] Saving into the database the given UserDao: " + user);
        repository.save(user);
        log.info("[SERVICE: User] Saving completed successfully");
    }


    /**
     * Find and return the user correspondent to {@literal id} if it present.
     *
     * @param id the id of the user to find
     * @return a {@literal UserDao} correspondent to {@literal id}
     * @throws NotFoundException if no such id is present in database
     */
    @Override
    public UserDao getUser(int id) {
        log.info("[SERVICE: User] Starting a getUser for a user with ID: " + id + " into the database");
        Optional<UserDao> foundUser = repository.findById(id);
        if (foundUser.isEmpty()) {
            log.info("[SERVICE: User] There is no user with that ID");
            throw new NotFoundException("The given user ID is not present into the database.");
        }
        log.info("[SERVICE: User] The user is present, getUser completed successfully");
        return foundUser.get();
    }

    /**
     * Find a list of user of the given roles (all users if the list is empty).
     *
     * @param rolesList a list containing the roles of the user needed
     * @return a {@literal List<UserDao>} containing all users if {@literal rolesList} is empty, or users of that list otherwise
     */
    @Override
    public List<UserDao> getUsers(List<RoleDao> rolesList) {
        log.info("[SERVICE: User] Starting a getUsers with the given roles");
        ArrayList<UserDao> list = new ArrayList<>();
        if (rolesList == null || rolesList.isEmpty()) {
            log.info("[SERVICE: User] No role specified, retrieve all users");
            list.addAll(repository.findAll());
        } else {
            log.info("[SERVICE: User] Roles to find: " + rolesList);
            for (RoleDao role : rolesList) {
                list.addAll(repository.findByRole(role));
            }
        }
        log.info("[SERVICE: User] getUsers completed successfully");
        return list;
    }

    @Override
    public void assignRoleToUser(UserDao user, RoleDao role) {
        user.setRole(role);
        repository.save(user);
    }

    @Override
    public void assignCompetencyToUser(CompetencyDao competency, UserDao user) {

    }

}
