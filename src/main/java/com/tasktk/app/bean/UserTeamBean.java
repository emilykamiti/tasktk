package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.UserTeamBeanI;
import com.tasktk.app.entity.UserTeam;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserTeamBean extends GenericBean<UserTeam> implements UserTeamBeanI {
    private static final Logger LOGGER = Logger.getLogger(UserTeamBean.class.getName());

    @Override
    public UserTeam addOrUpdate(UserTeam userTeam) {
        LOGGER.info("Creating/Updating user-team relationship");
        return getDao().addOrUpdate(userTeam);
    }

    @Override
    public UserTeam findById(Long userTeamId) {
        if (userTeamId == null) {
            throw new IllegalArgumentException("UserTeam ID cannot be null");
        }
        UserTeam userTeam = getDao().findById(UserTeam.class, userTeamId);
        if (userTeam == null) {
            throw new EntityNotFoundException("UserTeam with ID " + userTeamId + " not found");
        }
        return userTeam;
    }

    @Override
    public UserTeam findById(Class<UserTeam> entity, Long id) {
        return findById(id);
    }

    @Override
    public List<UserTeam> list() {
        LOGGER.info("Retrieving all user-team relationships");
        return getDao().list(new UserTeam());
    }

    @Override
    public boolean update(Long id, UserTeam userTeamUpdate) {
        UserTeam existingUserTeam = findById(id);

        // Update fields if provided
        if (userTeamUpdate.getUser() != null) existingUserTeam.setUser(userTeamUpdate.getUser());
        if (userTeamUpdate.getTeam() != null) existingUserTeam.setTeam(userTeamUpdate.getTeam());
        if (userTeamUpdate.getRoleInTeam() != null) existingUserTeam.setRoleInTeam(userTeamUpdate.getRoleInTeam());

        getDao().addOrUpdate(existingUserTeam);
        return true;
    }

    @Override
    public boolean delete(UserTeam userTeam) {
        if (userTeam == null || userTeam.getId() == null) {
            throw new IllegalArgumentException("UserTeam and UserTeam ID are required for deletion");
        }
        try {
            LOGGER.info("Deleting user-team relationship with ID: " + userTeam.getId());
            getDao().delete(UserTeam.class, userTeam.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.warning("UserTeam with ID " + userTeam.getId() + " not found for deletion");
            return false;
        }
    }

    @Override
    public boolean updateUserTeam(UserTeam userTeam) throws SQLException {
        return update(userTeam.getId(), userTeam);
    }

    @Override
    public boolean deleteUserTeam(UserTeam userTeam) {
        return delete(userTeam);
    }
}