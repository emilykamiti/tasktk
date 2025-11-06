package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.AuthBeanI;
import com.tasktk.app.entity.User;
import com.tasktk.app.utility.EncryptText;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.inject.Inject;
import java.io.Serializable;


@Stateless
public class AuthBean extends GenericBean<User> implements AuthBeanI, Serializable {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private EncryptText hashText;

    public User authenticate(User loginUser) {
        if (loginUser == null || loginUser.getName() == null || loginUser.getPassword() == null) {
            throw new IllegalArgumentException("username and password are required");
        }

        try {
            // find user by name only
            User user = em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                    .setParameter("name", loginUser.getName())
                    .getSingleResult();

            // verify pswd
            String hashedInput = hashText.encrypt(loginUser.getPassword());
            if (!user.getPassword().equals(hashedInput)) {
                throw new SecurityException("Invalid credentials");
            }
            return user;

        } catch (Exception ex) {
            throw new RuntimeException("Authentication failed", ex);
        }
    }
}