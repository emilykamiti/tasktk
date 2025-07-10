package com.tasktk.action;


import com.tasktk.app.bean.UserBean;
import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class UserAction extends BaseAction {
    @EJB
    UserBeanI userBean;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            userBean.register(serializeForm(User.class, req.getParameterMap()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        resp.sendRedirect("./login");
    }
}