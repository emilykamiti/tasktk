package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.User;

import java.sql.SQLException;



public interface AuthBeanI {
    User authenticate(User loginUser) throws SQLException;
}