package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.entity.User;
import com.tasktk.app.entity.UserTeam;
import com.tasktk.app.utility.EncryptText;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class UserBean extends GenericBean<User> implements UserBeanI {
    private static final Logger LOGGER = Logger.getLogger(UserBean.class.getName());

    @Inject
    private EncryptText encryptText;

    @PersistenceContext
    private EntityManager em;

    @Override
    public User register(User user) throws SQLException {
        validateUser(user);

        if (getDao().doesUserExistByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        String hashedPassword = encryptText.encrypt(user.getPassword());
        user.setPassword(hashedPassword);

        LOGGER.info("Registering user: " + user.getName());
        return getDao().addOrUpdate(user);
    }

    @Override
    public User addOrUpdate(User user) {
        LOGGER.info("Creating/Updating user: " + user.getName());
        return getDao().addOrUpdate(user);
    }

    @Override
    public User findById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User user = getDao().findById(User.class, userId);
        if (user == null) {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }

    @Override
    public User findById(Class<User> entity, Long id) {
        return findById(id);
    }

    @Override
    public List<User> list() {
        return em.createQuery(
                "SELECT DISTINCT u FROM User u " +
                        "LEFT JOIN FETCH u.userTeams " +
                        "LEFT JOIN FETCH u.assignedTasks",
                User.class
        ).getResultList();
    }

    @Override
    public boolean update(Long id, User userUpdate) {
        User existingUser = findById(id);

        if (userUpdate.getEmail() != null && !userUpdate.getEmail().equals(existingUser.getEmail())) {
            validateEmailUniqueness(userUpdate.getEmail(), id);
        }

        updateUserFields(existingUser, userUpdate);
        getDao().addOrUpdate(existingUser);
        return true;
    }

    @Override
    public boolean changePwd(User user) {
        validatePasswordChange(user);

        User existingUser = findUserByEmail(user.getEmail());
        if (existingUser == null) {
            throw new RuntimeException("User does not exist");
        }

        existingUser.setPassword(encryptText.encrypt(user.getPassword()));
        getDao().addOrUpdate(existingUser);
        return true;
    }

    @Override
    public boolean delete(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID are required for deletion");
        }

        try {
            LOGGER.info("Deleting user with id: " + user.getId());
            getDao().delete(User.class, user.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.warning("User with ID " + user.getId() + " not found for deletion");
            return false;
        }
    }

    @Override
    public boolean unregister(User user) {
        return delete(user);
    }

    @Override
    public List<User> getUsersByTeam(Long teamId) {
        TypedQuery<UserTeam> query = em.createQuery(
                "SELECT ut FROM UserTeam ut WHERE ut.team.id = :teamId",
                UserTeam.class
        );
        query.setParameter("teamId", teamId);

        return query.getResultList().stream()
                .map(UserTeam::getUser)
                .collect(Collectors.toList());
    }

    // Business logic methods
    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("User password is required");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role is required");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePasswordChange(User user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new RuntimeException("Password & confirm password do not match");
        }
        if (user.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
    }

    private void validateEmailUniqueness(String email, Long excludeUserId) {
        if (getDao().doesUserExistByEmail(email)) {
            User existingUser = findUserByEmail(email);
            if (!existingUser.getId().equals(excludeUserId)) {
                throw new RuntimeException("User with email " + email + " already exists");
            }
        }
    }

    private void updateUserFields(User existingUser, User userUpdate) {
        if (userUpdate.getName() != null) existingUser.setName(userUpdate.getName());
        if (userUpdate.getEmail() != null) existingUser.setEmail(userUpdate.getEmail());
        if (userUpdate.getRole() != null) existingUser.setRole(userUpdate.getRole());
    }

    private User findUserByEmail(String email) {
        User exampleUser = new User();
        exampleUser.setEmail(email);
        List<User> users = getDao().list(exampleUser);
        return users.isEmpty() ? null : users.get(0);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}