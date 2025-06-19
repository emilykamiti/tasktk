package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.AuthBeanI;
import com.tasktk.app.entity.AuditLog;
import com.tasktk.app.entity.User;
import com.tasktk.app.utility.EncryptText;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.inject.Inject;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Stateless
public class AuthBean extends GenericBean<User> implements AuthBeanI, Serializable {
    private static final String AUTH_QUERY = "FROM User u WHERE u.username = :username AND u.password = :password";
    private static final DateTimeFormatter LOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PersistenceContext
    private EntityManager em;

    @Inject
    private EncryptText hashText;

    @Inject
    private Event<AuditLog> logger;

    public User authenticate(User loginUser) {
        if (loginUser == null || loginUser.getUsername() == null || loginUser.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        String hashedPassword;
        try {
            hashedPassword = hashText.encrypt(loginUser.getPassword());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to encrypt password", ex);
        }

        TypedQuery<User> query = em.createQuery(AUTH_QUERY, User.class)
                .setParameter("username", loginUser.getUsername())
                .setParameter("password", hashedPassword);

        User user;
        try {
            user = query.getSingleResult();
        } catch (NoResultException ex) {
            throw new RuntimeException("Invalid username or password");
        }

        AuditLog log = new AuditLog();
        log.setLogDetails(String.format("%s logged in at %s.",
                user.getUsername(), LocalDateTime.now().format(LOG_FORMATTER)));

        em.persist(log);
        logger.fire(log);

        return user;
    }
}