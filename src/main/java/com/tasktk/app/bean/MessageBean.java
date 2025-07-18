package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.MessageBeanI;
import com.tasktk.app.entity.Activity;
import com.tasktk.app.entity.Message;
import com.tasktk.app.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class MessageBean extends GenericBean<Message> implements MessageBeanI {
    private static final Logger LOGGER = Logger.getLogger(TaskBean.class.getName());


    @Override
    public Message addOrUpdate(Message message) {
        LOGGER.info("create message: " + message.getContent());
        getDao().addOrUpdate(message);

        return message;
    }

    public Message findById(Long messageId) {
        return getDao().findById(Message.class, messageId);
    }

    public List<Message> list() {
        LOGGER.info("Retrieving all messages");
        return getDao().list(new Message());
    }

    @Override
    public boolean update(Long id, Message messageUpdate) {
        if (messageUpdate.getContent() != null) {
            Message existingMessage = getDao().findById(Message.class, id);
            if (existingMessage!= null  && !id.equals(existingMessage.getId())) {
                throw new RuntimeException("Team with name " + messageUpdate.getContent() + " already exists");
            }
        }
        return getDao().update(id, messageUpdate);
    }

    //delete message
    public boolean delete(Message message) {
        if (message == null || message.getId()== null){
            throw  new IllegalArgumentException(("Message and message ID are required for deletion"));
        }
        try{
            LOGGER.info("Deleting message with ID:" + message.getId());
            getDao().delete(Task.class, message.getId());
            return true;
        }catch (EntityNotFoundException e){
            LOGGER.info("Message with ID" + message.getId() + "not found deletion");
            return false;
        }
    }
}
