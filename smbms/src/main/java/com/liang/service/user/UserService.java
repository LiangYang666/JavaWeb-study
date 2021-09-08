package com.liang.service.user;

import com.liang.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public User login(String userCode, String userPassword);

    public boolean updatePwd(int id, String password);

    public int getUserCount(String queryUserName, int queryUserRole);

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    public int delUser(int userId);

    public boolean ifUserCodeExist(String userCode);

    public boolean addUser(User user);
}
