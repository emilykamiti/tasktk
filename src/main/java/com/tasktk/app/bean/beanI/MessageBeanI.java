package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Message;

import java.sql.SQLException;

public interface MessageBeanI extends  GenericBeanI<Message>{

    boolean updateMessage(Message message) throws SQLException;



}
