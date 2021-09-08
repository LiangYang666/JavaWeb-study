package com.miles.dao.user;

import com.miles.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    /**
     * 获取登录的用户
     */
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    /**
     * 更新用户密码
     * 
     * @param connection
     * @param id
     * @param userPassword
     * @return
     * @throws SQLException
     */
    public int updateUserPassword(Connection connection, int id, String userPassword) throws SQLException;

    /**
     * 根据参数类型获取用户总数
     * 
     * @param connection
     * @param userName
     * @param userRole
     * @return
     * @throws SQLException
     */
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    /**
     * 获取用户列表、该页面有分页功能
     * 
     * @param connection
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)
        throws SQLException;

    /**
     * 根据用户编码获取总数
     * 
     * @param connection
     * @param userCode
     * @return
     * @throws SQLException
     */
    public int getUserCount(Connection connection, String userCode) throws SQLException;

    /**
     * 添加用户
     * 
     * @param connection
     * @param user
     * @return
     * @throws SQLException
     */
    public int addUser(Connection connection, User user) throws SQLException;

}
