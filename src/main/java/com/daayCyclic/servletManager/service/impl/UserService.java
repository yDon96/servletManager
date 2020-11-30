package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        repository.save(user);
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
        Optional<UserDao> foundUser = repository.findById(id);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("The given user ID is not present into the database.");
        }
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
        ArrayList<UserDao> list = new ArrayList<>();
        if (rolesList == null || rolesList.isEmpty()) {
            list.addAll(repository.findAll());
        } else {
            for (RoleDao role : rolesList) {
                list.addAll(repository.findByRole(role));
            }
        }
        return list;
    }

    @Override
    public void assignRoleToUser(UserDao user, RoleDao role) {
        user.setRole(role);
        repository.save(user);
    }

}
