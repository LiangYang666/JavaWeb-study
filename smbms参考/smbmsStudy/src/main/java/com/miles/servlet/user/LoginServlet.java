package com.miles.servlet.user;

import com.miles.pojo.User;
import com.miles.service.user.UserService;
import com.miles.service.user.UserServiceImpl;
import com.miles.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    /**
     * 接受用户参数、调用业务层、转发视图
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("LoginServlet......");
        // 获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        // 调用业务层，对比账号密码
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);

        if ((null != user) && userPassword.equals(user.getUserPassword())) {
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            resp.sendRedirect("jsp/frame.jsp");
        } else if ((null != user) && !userPassword.equals(user.getUserPassword())) {
            req.setAttribute("error", "密码错误,请重新输入!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "用户" + userCode + "不存在！");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}
