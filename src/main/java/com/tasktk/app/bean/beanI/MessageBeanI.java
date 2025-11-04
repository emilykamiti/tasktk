package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Message;
import java.util.List;

public interface MessageBeanI extends GenericBeanI<Message> {
    Message addOrUpdate(Message message);
    Message findById(Long messageId);
    List<Message> list();
    boolean update(Long id, Message messageUpdate);
    boolean delete(Message message);
    List<Message> findByTeam(Long teamId);
    List<Message> findBySender(Long senderId);
    List<Message> findUnreadMessages(Long userId);
    boolean markAsRead(Long messageId);
    boolean markAllAsRead(Long teamId, Long userId);
}