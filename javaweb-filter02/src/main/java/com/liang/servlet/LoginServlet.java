package com.liang.servlet;

import com.liang.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        if(username.equals("admin")){
            req.getSession().setAttribute(Constant.USER_SESSION, req.getSession().getId());
            System.out.println("存储了USER_SESSION");
            resp.sendRedirect("/sys/success.jsp");
        }else {
            resp.sendRedirect("/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Login进入Post");
        doGet(req, resp);
    }
}
