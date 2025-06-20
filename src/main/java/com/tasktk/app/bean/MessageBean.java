package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.MessageBeanI;
import com.tasktk.app.entity.Message;
import com.tasktk.app.entity.Message;
import com.tasktk.app.entity.Task;
import com.tasktk.app.entity.Team;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class MessageBean extends GenericBean<Message> implements MessageBeanI {
    private static final Logger LOGGER = Logger.getLogger(TaskBean.class.getName());
    @Override
    public Message createMessage(Message message) throws SQLException {
        LOGGER.info("create message: " + message.getContent());
        getDao().addOrUpdate(message);

        return message;
    }

    @Override
    public Message findById(Long messageId) {
        return em.find(Message.class, messageId);
    }

    public List<Message> listAll() {
        LOGGER.info("Retrieving all messages");
        TypedQuery<Message> query = em.createQuery("SELECT m FROM Message m", Message.class);
        return query.getResultList();
    }

    @Override
    public boolean updateMessage(Message message) throws SQLException {
        Message existingMessage = em.find(Message.class, message.getId());
        if(existingMessage == null){
            LOGGER.info("Message with ID" + message.getId() + "not found for update");
            return false;
        }
        if(message.getContent() !=null && message.getContent().equals(existingMessage.getContent())){
            throw new RuntimeException(("Message with name" + message.getContent()
                    + "already exists"));
        }
        existingMessage.setContent(message.getContent());

        LOGGER.info("Updating message:" + existingMessage.getContent());
        getDao().addOrUpdate(existingMessage);

        return true;
    }

    @Override
    public boolean deleteMessage(Message message) {
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
