package com.liang.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.liang.pojo.Role;
import com.liang.pojo.User;
import com.liang.service.role.RoleServiceImpl;
import com.liang.service.user.UserService;
import com.liang.service.user.UserServiceImpl;
import com.liang.util.Constants;
import com.liang.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd")){
            this.updatePwd(req, resp);
        } else if(method.equals("pwdmodify")){
            this.pwdModify(req, resp);
        } else if(method.equals("query")){
            query(req, resp);
        } else if(method.equals("deluser")){
            delUser(req, resp);
        } else if(method.equals("add")){
            addUser(req, resp);
        } else if(method.equals("ucexist")){
            ifUserCodeExist(req, resp);
        } else if(method.equals("getrolelist")){
            getRoleList(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if(o!=null && newpassword!=null){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(), newpassword);
            if(flag){
                req.setAttribute("message", "密码修改成功，请退出重新登陆");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码不符合");
        }
        try {
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //验证旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();
        if(o==null){    //失效
            resultMap.put("result", "sessionerror");
        } else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) o).getUserPassword();
            if(oldpassword.equals(userPassword)){
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void query(HttpServletRequest req, HttpServletResponse resp) {
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndexStr = req.getParameter("pageIndex");
        int queryUserRole = 0;
        if (temp!=null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }
        int currentPageNo = 1;
        if(pageIndexStr!=null) {
            System.out.println("pageIndexStr: "+pageIndexStr);
            currentPageNo = Integer.parseInt(pageIndexStr);
        }
        System.out.println("queryUserName:"+queryUserName);
        System.out.println("currentPageNo:"+currentPageNo);


        UserServiceImpl userService = new UserServiceImpl();
        RoleServiceImpl roleService = new RoleServiceImpl();
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        int pageSize = 5;


        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        pageSupport.setTotalPageCountByRs();
        int totalPageCount = pageSupport.getTotalPageCount();
        pageSupport.setCurrentPageNo(currentPageNo);

//        if(currentPageNo < 1) {
//            currentPageNo = 1;
//        }else if(currentPageNo > totalPageCount) {//如果页面大于了最后一页就显示最后一页
//            currentPageNo =totalPageCount;
//        }

        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("currentPageNo", currentPageNo);

        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("queryUserName", queryUserName);


        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void delUser(HttpServletRequest req, HttpServletResponse resp){
        String uidStr = req.getParameter("uid");
        Map<String, String> resultMap = new HashMap<String, String>();
        resp.setContentType("application/json");
        int uid = Integer.parseInt(uidStr);
        UserServiceImpl userService = new UserServiceImpl();
        if(userService.delUser(uid)==1){
            resultMap.put("delResult", "true");
        } else {
            resultMap.put("delResult", "notexist");
        }
//        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(HttpServletRequest req, HttpServletResponse resp) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        int gender = Integer.parseInt(req.getParameter("gender"));
        Date birthday = null;
        try {
            birthday = simpleDateFormat.parse(req.getParameter("birthday"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        int userRole = Integer.parseInt(req.getParameter("userRole"));
        Date creationDate = new Date();
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        int createBy = ((User)o).getUserRole();

        User savaUser = new User();
        savaUser.setUserCode(userCode);
        savaUser.setUserName(userName);
        savaUser.setUserPassword(userPassword);
        savaUser.setGender(gender);
        savaUser.setBirthday(birthday);
        savaUser.setPhone(phone);
        savaUser.setAddress(address);
        savaUser.setUserRole(userRole);
        savaUser.setCreationDate(creationDate);
        savaUser.setCreatedBy(createBy);

        UserServiceImpl userService = new UserServiceImpl();
        if(userService.addUser(savaUser)){
            try {
                req.getRequestDispatcher("/jsp/user.do?method=query").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                resp.sendRedirect("error.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testDate(){
        String date1 = "2018-10-08";
        String date2 = "2018-10-08 14:21:02";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = simpleDateFormat.parse(date1);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        System.out.println(now);

    }
    public void ifUserCodeExist(HttpServletRequest req, HttpServletResponse resp) {
        String userCode = req.getParameter("userCode");
        Map<String, String> resultMap = new HashMap<String, String>();
        resp.setContentType("application/json");
        if(userCode.equals("")) return;

        UserServiceImpl userService = new UserServiceImpl();
        if(userService.ifUserCodeExist(userCode)){
            resultMap.put("userCode", "exist");
        }else {
            resultMap.put("userCode", "NoExist");
        }

        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) {
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        List<Map> maps = new ArrayList<Map>();

        for (Role role :roleList) {
            Integer id = role.getId();
            String roleName = role.getRoleName();
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("id", ""+id);
            resultMap.put("roleName", roleName);
            maps.add(resultMap);
        }

        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(maps));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
