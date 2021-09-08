package com.miles.service.user;

import com.miles.pojo.User;

import java.util.List;

public interface UserService {

    /**
     * 用户登录，返回用户，将用户数据保存到session中
     * 
     * @param userCode
     * @param userPassword
     * @return
     */
    public User login(String userCode, String userPassword);

    /**
     * 用户密码更新
     * 
     * @param id
     * @param userPassword
     * @return
     */
    public boolean UpdatePwd(int id, String userPassword);

    /**
     * 根据参数类型获取用户总数
     * 
     * @param userName
     * @param userRole
     * @return
     */
    public int getUserCount(String userName, int userRole);

    /**
     * 根据条件获取用户列表
     * 
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    /**
     * 根据用户编码获取用户总数
     * 
     * @param userCode
     * @return
     */
    public int getUserCount(String userCode);

    /**
     * 添加用户
     * 
     * @param user
     * @return
     */
    public boolean addUser(User user);
}
