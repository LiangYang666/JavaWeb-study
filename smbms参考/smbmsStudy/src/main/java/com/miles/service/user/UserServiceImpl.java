package com.miles.service.user;

import com.miles.dao.BaseDao;
import com.miles.dao.user.UserDao;
import com.miles.dao.user.UserDaoImpl;
import com.miles.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    // 业务层都会调用Dao层，引入dao层
    private UserDao userDao;

    public UserServiceImpl() {

        userDao = new UserDaoImpl();
    }

    /**
     * 用户登录，返回用户，将用户数据保存到session中
     * 
     * @param userCode
     * @param userPassword
     * @return
     */
    @Override
    public User login(String userCode, String userPassword) {

        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return user;
    }

    /**
     * 用户密码更新
     * 
     * @param id
     * @param userPassword
     * @return
     */
    @Override
    public boolean UpdatePwd(int id, String userPassword) {

        Connection connection = null;
        boolean flag = true;

        try {
            connection = BaseDao.getConnection();
            int updateRow = userDao.updateUserPassword(connection, id, userPassword);
            if (updateRow <= 0) {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }

    /**
     * 根据参数类型获取用户总数
     * 
     * @param userName
     * @param userRole
     * @return
     */
    @Override
    public int getUserCount(String userName, int userRole) {

        Connection connection = null;
        int count = 0;

        System.out.println("UsreService-->getUserCount-->userName: " + userName);
        System.out.println("UsreService-->getUserCount-->userRole: " + userRole);
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return count;
    }

    /**
     * 根据条件获取用户列表
     * 
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {

        Connection connection = null;
        List<User> userList = null;

        System.out.println("UserService-->getUserList-->userName: " + userName);
        System.out.println("UserService-->getUserList-->userRole: " + userRole);
        System.out.println("UserService-->getUserList-->currentPageNo: " + currentPageNo);
        System.out.println("UserService-->getUserList-->pageSize: " + pageSize);

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }

        return userList;
    }

    /**
     * 根据用户编码获取用户总数
     * 
     * @param userCode
     * @return
     */
    @Override
    public int getUserCount(String userCode) {

        Connection connection = null;
        int count = 0;

        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return count;
    }

    /**
     * 返回用户是否添加成功
     * 
     * @param user
     * @return
     */
    @Override
    public boolean addUser(User user) {

        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            int resultRows = userDao.addUser(connection, user);
            if (resultRows > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    /**
     * 测试模板
     */
    @Test
    public void test() {

        UserServiceImpl userService = new UserServiceImpl();
        int admin = userService.getUserCount("admin");
        System.out.println("admin :" + admin);
    }

}
