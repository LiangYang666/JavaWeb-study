package com.liang.service.user;

import com.liang.dao.BaseDao;
import com.liang.dao.user.UserDao;
import com.liang.dao.user.UserDaoImpl;
import com.liang.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String userPassword) {
        Connection connection = BaseDao.getConnection();
        User user = null;
        try {
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    public boolean updatePwd(int id, String password) {
        boolean flag = false;
        Connection connection = BaseDao.getConnection();
        int rows = 0;
        try {
            rows = userDao.updatePwd(connection, id, password);
            if(rows>0)  flag=true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    public int getUserCount(String queryUserName, int queryUserRole) {
        int count=0;
        Connection connection = BaseDao.getConnection();
        try {
            count = userDao.getUserCount(connection, queryUserName, queryUserRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = BaseDao.getConnection();
        List<User> userList = null;
        try {
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

    public int delUser(int userId) {
        Connection connection = BaseDao.getConnection();
        int execute = 0;
        try {
            execute = userDao.delUser(connection, userId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return execute;
    }

    public boolean ifUserCodeExist(String userCode) {
        Connection connection = BaseDao.getConnection();
        boolean flag=false;
        try {
            flag = userDao.ifUserCodeExist(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    public boolean addUser(User user) {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;
        try {
            flag = userDao.addUser(connection, user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Test
    public void Test1(){
        UserServiceImpl userService = new UserServiceImpl();
        String name = "test";
        String userPassword = "123";
        User admin = userService.login(name, userPassword);
        System.out.println(admin.getUserPassword());
    }
    @Test
    public void Test2(){
        UserServiceImpl userService = new UserServiceImpl();
        System.out.println(userService.getUserCount("张", 0));
    }
    @Test
    public void Test3(){
        UserServiceImpl userService = new UserServiceImpl();
        System.out.println(userService.getUserList("", 0, 1, 5).toString());
    }

}
