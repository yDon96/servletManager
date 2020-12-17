package com.daayCyclic.servletManager.service.impl;

import com.daayCyclic.servletManager.dao.RoleDao;
import com.daayCyclic.servletManager.exception.DuplicateGenerationException;
import com.daayCyclic.servletManager.exception.NotFoundException;
import com.daayCyclic.servletManager.exception.NotValidTypeException;
import com.daayCyclic.servletManager.repository.IRoleRepository;
import com.daayCyclic.servletManager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(value = "RoleService")
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository repository;

    /**
     * Create a new role into the database.
     *
     * @param role the {@literal RoleDao} to save into the database.
     * @throws DuplicateGenerationException if a role already exists is generated.
     */
    @Override
    public void generateRole(RoleDao role) {
        log.debug("[RoleService] Generating role.");
        RoleDao newRole = this.validate(role);
        val roleDao = this.repository.findByName(newRole.getName());
        if(roleDao.isPresent()){
            log.error("[RoleService] Trying to create a role that already exist with name: " + role.getName());
            throw new DuplicateGenerationException();
        }
        repository.save(newRole);
    }

    /**
     * Retrieve a {@literal RoleDao} from the database, correspondent to the given {@literal String}.
     *
     * @param roleName a {@literal String} representing the name of the role.
     * @return a {@literal RoleDao} representing the found role.
     * @throws NotFoundException if the role is not present.
     */
    @Override
    public RoleDao getRole(String roleName) {
        log.debug("[RoleService] Get role with name: " + roleName);
        val roleDao = repository.findByName(roleName);
        return roleDao.orElseThrow(() -> new NotFoundException("The given role is not present into the database."));
    }

    /**
     * Retrieve from the database all the {@literal RoleDao}.
     *
     * @return a {@literal List} of {@literal RoleDao} containing all the roles in the database.
     */
    @Override
    public List<RoleDao> getRoles() {
        log.info("[RoleService] Get all roles.");
        return repository.findAll();
    }

    private void checkIntegrity(RoleDao role) {
        if (role == null) { throw new NotValidTypeException("The given role is null."); }
        if (role.getName() == null || role.getName().equals("")) { throw new NotValidTypeException("The given role is empty."); }
    }

    private RoleDao validate(RoleDao role) {
        this.checkIntegrity(role);
        // For future developments
        return role;
    }

}
