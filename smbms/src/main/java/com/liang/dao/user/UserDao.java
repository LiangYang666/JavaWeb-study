package com.liang.dao.user;

import com.liang.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public abstract User getLoginUser(Connection connection, String userCode) throws SQLException;
    public int updatePwd(Connection connection, int id, String password) throws SQLException;
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;
    public int delUser(Connection connection, int userId) throws SQLException;
    public boolean ifUserCodeExist(Connection connection, String userCode) throws SQLException;
    public boolean addUser(Connection connection, User user) throws SQLException;

}
