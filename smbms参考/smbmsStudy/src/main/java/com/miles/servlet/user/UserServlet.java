package com.miles.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miles.dao.user.UserDao;
import com.miles.dao.user.UserDaoImpl;
import com.miles.pojo.Role;
import com.miles.pojo.User;
import com.miles.service.user.UserService;
import com.miles.service.user.UserServiceImpl;
import com.miles.servlet.role.RoleServiceImpl;
import com.miles.util.Constants;
import com.miles.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import sun.security.util.Resources_es;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String method = req.getParameter("method");
        System.out.println("UserServlet--->method is " + method);

        // 实现复用~~~~~~
        // 想添加新的增删改查，直接用if(method != "savepwd" && method != null);

        // 更新密码
        if (method != null && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        }

        // 匹配旧密码
        if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        }

        if (method != null && method.equals("query")) {
            this.query(req, resp);
        }

        if (method != null && method.equals("getrolelist")) {
            this.getRoleList(req, resp);
        }

        if (method != null && method.equals("ucexist")) {
            this.queryUserCodeExist(req, resp);
        }

        if (method != null && method.equals("add")) {
            this.addUser(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

    /**
     * 更新用户密码
     * 
     * @param req
     * @param resp
     */
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入了UserServlet-->updatePwd");
        // 通过session获取用户id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;

        System.out.println("o: " + o.toString());
        System.out.println("newpassword: " + newpassword);
        System.out.println("UserServlet-->updatePwd-->" + (o != null) + "-->" + (newpassword != null));

        if (o != null && newpassword != null) {
            UserService userService = new UserServiceImpl();

            flag = userService.UpdatePwd(((User)o).getId(), newpassword);
            if (flag) {
                // 密码修改成功
                req.setAttribute("message", "密码修改成功，请退出，使用新密码登录");
                // 密码修改成功,移除session(移除后不能再次修改密码,建议不移除)
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                // req.setAttribute("message", "密码修改失败");
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            // 密码修改有问题
            req.setAttribute("message", "新密码有问题");
        }

        try {
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 忘记密码，从session中获取用户密码，返回json
     * 
     * @param req
     * @param resp
     */
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入了UserServlet-->pwdModify");
        // 从session中获取用户密码
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldPassword = req.getParameter("oldpassword");

        Map<String, String> resultMap = new HashMap<>();

        if (o == null) {// session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldPassword)) {// 旧密码为空
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User)o).getUserPassword();
            if (!oldPassword.equals(userPassword)) {
                resultMap.put("result", "false");
            } else {
                resultMap.put("result", "true");
            }
        }

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            resp.setContentType("application/json");
            Object json = JSONArray.toJSON(resultMap);

            // 测试json格式
            System.out.println("json" + json.toString());
            out.write(json.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 用户管理页 查询功能
     * 
     * @param req
     * @param resp
     */
    public void query(HttpServletRequest req, HttpServletResponse resp) {

        String queryName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        System.out.println("UserServlet-->query-->queryName: " + queryName);
        System.out.println("UserServlet-->query-->queryUserRole: " + temp);
        System.out.println("UserServlet-->query-->pageIndex: " + pageIndex);

        // 获取用户列表
        List<User> userList = null;
        UserServiceImpl userService = new UserServiceImpl();

        // 第一此请求肯定是走第一页，页面大小固定的
        // 设置页面容量
        int PageSize = 5;// 把它设置在配置文件里,后面方便修改

        // 当前页码
        int CurrentPageNo = 1;

        if (queryName == null) {
            queryName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
            System.out.println("queryUserRole : " + queryUserRole);
        }
        if (pageIndex != null) {
            CurrentPageNo = Integer.parseInt(pageIndex);
        }

        // 获取用户总数
        int totalCount = userService.getUserCount(queryName, queryUserRole);

        // 总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(CurrentPageNo);
        pageSupport.setPageSize(PageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();// 总共有几页

        if (CurrentPageNo < 1) {
            CurrentPageNo = 1;
        } else if (CurrentPageNo > totalPageCount) {
            CurrentPageNo = totalPageCount;
        }

        userList = userService.getUserList(queryName, queryUserRole, CurrentPageNo, PageSize);
        req.setAttribute("userList", userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryName);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", CurrentPageNo);
        req.setAttribute("queryUserRole", queryUserRole);

        // 返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户列表
     * 
     * @param req
     * @param resp
     */
    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) {

        List<Role> roleList = null;
        RoleServiceImpl roleService = new RoleServiceImpl();

        roleList = roleService.getRoleList();

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            resp.setContentType("application/json");
            Object json = JSONArray.toJSON(roleList);

            System.out.println("UserService-->getRoleList-->json: " + json.toString());
            out.write(json.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 查询usercode是否存在 角色编码
     * 
     * @param req
     * @param resp
     */
    public void queryUserCodeExist(HttpServletRequest req, HttpServletResponse resp) {

        String userCode = req.getParameter("userCode");
        Map<String, String> resultMap = new HashMap<>();
        System.out.println("UserServlet-->queryUserCodeExist-->userCode : " + userCode);

        if (userCode != null && !userCode.equals("")) {
            UserServiceImpl userService = new UserServiceImpl();
            int userCount = userService.getUserCount(userCode);
            if (userCount > 0) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "NoExist");
            }
        } else {
            resultMap.put("userCode", "isNull");
        }

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            resp.setContentType("application/json");
            Object json = JSONArray.toJSON(resultMap);

            System.out.println("UserServlet-->queryUserCodeExist-->json: " + json.toString());

            out.write(json.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 添加用户页面
     * 
     * @param req
     * @param resp
     */
    public void addUser(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("----------------------addUser--------start------------------");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        System.out.println("user : " + user.toString());
        UserService userService = new UserServiceImpl();
        boolean flag = userService.addUser(user);
        if (flag) {
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                req.getRequestDispatcher("useradd.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------------------addUser-------end-------------------");
    }

}
