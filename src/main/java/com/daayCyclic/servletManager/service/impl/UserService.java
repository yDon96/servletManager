package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.CompetencyDao;
import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.dao.UserDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IUserRepository;
import com.daayCyclic.servletManager.service.ICompetencyService;
import com.daayCyclic.servletManager.service.IRoleService;
import com.daayCyclic.servletManager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service(value = "UserService")
public class UserService implements IUserService{

    @Autowired
    private IUserRepository repository;

    @Autowired
    private ICompetencyService competencyService;

    @Autowired
    private IRoleService roleService;

    /**
     * Save the given user into the database. If a user with the same ID of the one passed is already present, updates it.
     *
     * @param user the user to save/update
     * @throws NotValidTypeException if some of parameters of the given user doesn't respect database constraints
     */
    @Override
    public void generateUser(UserDao user) {
        log.info("[SERVICE: User] Saving into the database the given UserDao: " + user);
        if (this.isPresent(user)) {
            throw new DuplicateGenerationException("The given user already exist into the database");
        }
        UserDao validatedUser = this.validate(user);
        repository.save(validatedUser);
        log.info("[SERVICE: User] Saving completed successfully");
    }

    /**
     * Update an existing user if it exist.
     *
     * @param user the {@literal UserDao} to update.
     * @throws NotFoundException if the given user is not present into the database.
     */
    @Override
    public void updateUser(UserDao user) {
        log.info("[SERVICE: User] Starting update of the given user: " + user + " into the database");
        UserDao validatedUser = this.validate(user);
        if (!this.isPresent(validatedUser)) {
            throw new NotFoundException("The given user is not present into the database");
        }
        repository.save(validatedUser);
        log.info("[SERVICE: User] Update completed successfully");
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
     * @param rolesList a list containing the roles of the user needed.
     * @return a {@literal List<UserDao>} containing all users if {@literal rolesList} is empty,
     * or users of that list otherwise.
     */
    @Override
    public List<UserDao> getUsers(List<RoleDao> rolesList) {
        log.info("[SERVICE: User] Starting a getUsers with the given roles.");
        ArrayList<UserDao> list = new ArrayList<>();
        if (rolesList == null || rolesList.isEmpty()) {
            log.info("[SERVICE: User] No role specified, retrieve all users.");
            list.addAll(repository.findAll());
        } else {
            log.info("[SERVICE: User] Roles to find: " + rolesList);
            for (RoleDao role : rolesList) {
                list.addAll(repository.findByRole(role));
            }
        }
        log.info("[SERVICE: User] getUsers completed successfully.");
        return list;
    }

    /**
     * Assign a role to a user.
     *
     * @param user the {@literal UserDao} to assign the role to.
     * @param role a {@literal RoleDao} to assign.
     */
    @Override
    public void assignRoleToUser(UserDao user, RoleDao role) {
        user.setRole(role);
        this.updateUser(this.validate(user));
    }

    /**
     * Assign a competency to a maintainer.
     *
     * @param competency a {@literal CompetencyDao} to assign.
     * @param user a {@literal UserDao} to assign.
     * @throws NotValidTypeException if competency or user are null, or the user is not a maintainer.
     */
    @Override
    public void assignCompetencyToUser(CompetencyDao competency, UserDao user) {
        log.info("[SERVICE: User] Starting to assign " + competency + " to user: " + user);
        UserDao validatedUser = this.validate(user);
        if (competency == null) {
            String message = "Competency can't be null";
            log.info("[SERVICE: User] " + message);
            throw new NotValidTypeException(message);
        }
        if (!(validatedUser.isMaintainer())) {
            String message = "The given user is not a maintainer";
            log.info("[SERVICE: User] " + message);
            throw new NotValidTypeException(message);
        }

        if (competency.getUsers() == null) {
            competency.setUsers(new HashSet<>());
        }
        competency.getUsers().add(validatedUser);

        if (validatedUser.getCompetencies() == null) {
            validatedUser.setCompetencies(new HashSet<>());
        }
        validatedUser.getCompetencies().add(competency);

        // Update user and competency into the db
        this.updateUser(this.validate(validatedUser));
        this.competencyService.updateCompetency(competency);
        log.info("[SERVICE: User] Assign of competency to user completed successfully.");
    }

    public boolean isPresent(UserDao user) {
        if (user != null && user.getUserId() != null) {
            return this.repository.existsById(user.getUserId());
        }
        return false;
    }

    private void checkIntegrity(UserDao user) {
        if (user == null) { throw new NotValidTypeException("The UserDao can't be null."); }
        if (user.getName() == null || user.getName().equals("")) { throw new NotValidTypeException("The UserDao name can't be null or empty."); }
        if (user.getSurname() == null || user.getSurname().equals("")) { throw new NotValidTypeException("The UserDao surname can't be null or empty."); }
        if (user.getDateOfBirth() == null) { throw new NotValidTypeException("The UserDao date of birth can't be null."); }
    }

    private UserDao validate(UserDao user) {
        this.checkIntegrity(user);
        if (user.getRole() != null && user.getRole().getName() != null) {
            user.setRole(this.roleService.getRole(user.getRole().getName()));
        }
        Set<CompetencyDao> savedCompetencies = user.getCompetencies();
        if (savedCompetencies != null) {
            Set<CompetencyDao> checkedCompetencies = new LinkedHashSet<>();
            for (CompetencyDao competency : savedCompetencies) {
                if (competency.getName() != null)
                checkedCompetencies.add(this.competencyService.getCompetency(competency.getName()));
            }
            user.setCompetencies(checkedCompetencies);
        }
        return user;
    }

}
