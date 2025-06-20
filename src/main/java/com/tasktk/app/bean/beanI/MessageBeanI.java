package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Message;

import java.sql.SQLException;

public interface MessageBeanI extends  GenericBeanI<Message>{
    Message createMessage (Message message) throws SQLException;

    boolean updateMessage(Message message) throws SQLException;

    boolean deleteMessage(Message message);

    Message findById(Long messageId);
}
