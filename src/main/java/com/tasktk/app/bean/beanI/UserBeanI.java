package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.User;
import java.sql.SQLException;
import java.util.List;

public interface UserBeanI extends GenericBeanI<User> {

    // Registration and authentication methods
    User register(User user) throws SQLException;
    boolean changePwd(User user);
    boolean unregister(User user);

    // CRUD methods from GenericBeanI
    User addOrUpdate(User user);
    User findById(Long userId);
    List<User> list();
    boolean update(Long id, User userUpdate);
    boolean delete(User user);

    // Additional business methods
    List<User> getUsersByTeam(Long teamId);

    // Method to match GenericBeanI signature
    User findById(Class<User> entity, Long id);
}