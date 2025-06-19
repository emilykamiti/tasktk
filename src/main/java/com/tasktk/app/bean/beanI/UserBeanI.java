package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.User;

import java.sql.SQLException;

public interface UserBeanI extends GenericBeanI<User>{
    User register (User user) throws SQLException;

    boolean changePwd(User user) throws SQLException;

    boolean unregister(User user);

    User findById(Long userId);
}
