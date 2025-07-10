package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.entity.User;
import com.tasktk.app.utility.EncryptText;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserBean extends GenericBean<User> implements UserBeanI {

    private static final Logger LOGGER = Logger.getLogger(UserBean.class.getName());

    @Inject
    private EncryptText encryptText;

    @Override
    public User register(User user) throws SQLException {

        if(getDao().doesUserExistByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        String hashedPassword = encryptText.encrypt(user.getPassword());
        user.setPassword(hashedPassword);

        LOGGER.info("Register user: " + user.getName());
        return getDao().addOrUpdate(user);
    }

    @Override
    public User findById(Long userId) {
        return getDao().findById(User.class, userId);
    }

    @Override
    public boolean changePwd(User user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new RuntimeException("Password & confirm password do not match");
        }

        // Create example user for query
        User exampleUser = new User();
        exampleUser.setName(user.getName());
        exampleUser.setEmail(user.getEmail());

        List<User> checkUser = getDao().list(exampleUser);
        if (checkUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        try {
            User existingUser = checkUser.get(0);
            existingUser.setPassword(encryptText.encrypt(user.getPassword()));
            getDao().addOrUpdate(existingUser);
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean unregister(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID are required for deletion");
        }

        try {
            LOGGER.info("Deleting user with id: " + user.getId());
            getDao().delete(User.class, user.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.info("User with ID " + user.getId() + " not found for deletion");
            return false;
        }
    }
}