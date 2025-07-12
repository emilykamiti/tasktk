package com.tasktk.app.api.rest;
import com.tasktk.app.bean.beanI.GenericBeanI;
import com.tasktk.app.bean.beanI.MessageBeanI;
import com.tasktk.app.entity.Message;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;

@Path("/message")
public class MessageRestApi extends BaseEntityRestApi<Message> {
    @EJB
    private MessageBeanI messageBean;

    @Override
    protected GenericBeanI<Message> getBean() {
        return messageBean;
    }

    @Override
    protected Class<Message> getEntityClass() {
        return Message.class;
    }
}