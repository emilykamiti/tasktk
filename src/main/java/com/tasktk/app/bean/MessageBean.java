package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.MessageBeanI;
import com.tasktk.app.entity.Message;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class MessageBean extends GenericBean<Message> implements MessageBeanI {
    private static final Logger LOGGER = Logger.getLogger(MessageBean.class.getName());

    @Override
    public Message addOrUpdate(Message message) {
        validateMessage(message);
        LOGGER.info("Creating/Updating message: " + message.getContent());
        return getDao().addOrUpdate(message);
    }

    @Override
    public Message findById(Long messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        Message message = getDao().findById(Message.class, messageId);
        if (message == null) {
            throw new EntityNotFoundException("Message with ID " + messageId + " not found");
        }
        return message;
    }

    @Override
    public List<Message> list() {
        LOGGER.info("Retrieving all messages");
        return getDao().list(new Message());
    }

    @Override
    public boolean update(Long id, Message messageUpdate) {
        Message existingMessage = findById(id);
        updateMessageFields(existingMessage, messageUpdate);
        getDao().addOrUpdate(existingMessage);
        return true;
    }

    @Override
    public boolean delete(Message message) {
        if (message == null || message.getId() == null) {
            throw new IllegalArgumentException("Message and message ID are required for deletion");
        }
        try {
            LOGGER.info("Deleting message with ID: " + message.getId());
            getDao().delete(Message.class, message.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.warning("Message with ID " + message.getId() + " not found for deletion");
            return false;
        }
    }

    // Business logic methods
    private void validateMessage(Message message) {
        if (message.getTeam() == null) {
            throw new IllegalArgumentException("Message must be associated with a team");
        }
        if (message.getSender() == null) {
            throw new IllegalArgumentException("Message sender is required");
        }
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content is required");
        }
        if (message.getContent().length() > 1000) {
            throw new IllegalArgumentException("Message content cannot exceed 1000 characters");
        }
    }

    private void updateMessageFields(Message existingMessage, Message messageUpdate) {
        if (messageUpdate.getTeam() != null) existingMessage.setTeam(messageUpdate.getTeam());
        if (messageUpdate.getSender() != null) existingMessage.setSender(messageUpdate.getSender());
        if (messageUpdate.getContent() != null) existingMessage.setContent(messageUpdate.getContent());
        if (messageUpdate.getIsRead() != null) existingMessage.setIsRead(messageUpdate.getIsRead());
        if (messageUpdate.getTimeStamp() != null) existingMessage.setTimeStamp(messageUpdate.getTimeStamp());
    }

    // Business methods
    public List<Message> findByTeam(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        return getDao().findByProperty(Message.class, "team.id", teamId);
    }

    public List<Message> findBySender(Long senderId) {
        if (senderId == null) {
            throw new IllegalArgumentException("Sender ID cannot be null");
        }
        return getDao().findByProperty(Message.class, "sender.id", senderId);
    }

    public List<Message> findUnreadMessages(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return java.util.Collections.emptyList();
    }

    public boolean markAsRead(Long messageId) {
        Message message = findById(messageId);
        message.setIsRead(true);
        getDao().addOrUpdate(message);
        return true;
    }

    public boolean markAllAsRead(Long teamId, Long userId) {
        List<Message> unreadMessages = findUnreadMessages(userId);
        for (Message message : unreadMessages) {
            message.setIsRead(true);
            getDao().addOrUpdate(message);
        }
        return true;
    }

    @Override
    public Message findById(Class<Message> entity, Long id) {
        return findById(id); // Delegate to the other method
    }
}