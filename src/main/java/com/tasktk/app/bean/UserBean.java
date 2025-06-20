package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.entity.User;
import com.tasktk.app.utility.EncryptText;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserBean extends GenericBean<User> implements UserBeanI {

    private static final Logger LOGGER = Logger.getLogger(UserBean.class.getName());

    @Inject
    private EncryptText encryptText;

    @PersistenceContext
    private EntityManager em;

    //create user
    @Override
    public User register(User user) throws SQLException {

    if(genericDao.doesUserExistByEmail(user.getEmail())){
        throw new RuntimeException("User with email" + user.getEmail() + "already exists");
    }
    String hashedPassword = (encryptText.encrypt(user.getPassword()));
    user.setPassword(hashedPassword);

    LOGGER.info("Register user: " + user.getName());
    getDao().addOrUpdate(user);

    return user;
    }

    //get users?

    //get user
    @Override
    public User findById(Long userId) {
        return em.find(User.class, userId);
    }

    //update
    @Override
    public boolean changePwd(User user) {

       if (!user.getPassword().equals(user.getConfirmPassword()))
           throw new RuntimeException(("Password & confirm password do not match"));

       List <User> checkUser = list(new User(user.getName(), user.getConfirmPassword(), user.getEmail()));
       if  (checkUser.isEmpty())
           throw new RuntimeException("User does not exist");
       try{
           checkUser.get(0).setPassword(encryptText.encrypt(user.getPassword()));
       }catch (Exception ex){
           throw new RuntimeException(ex.getMessage());
       }
       getDao().addOrUpdate((checkUser.get(0)));

        return false;
    }

    //delete
    @Override
    public boolean unregister(User user) {
        if(user == null || user.getId() == null){
            throw new IllegalArgumentException(("User and user ID are required for deletion to happen"));
        }
        try {
            LOGGER.info("Deleting user with id: " +user.getId());
            getDao().delete(User.class, user.getId());
            return true;
        }catch (EntityNotFoundException e){
            LOGGER.info("User with ID" + user.getId() + "not found for deletion");
            return false;
        }
    }

}
