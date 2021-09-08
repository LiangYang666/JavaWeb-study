package com.miles.dao.user;

import com.miles.dao.BaseDao;
import com.miles.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    /**
     * 获取登录的用户
     *
     * @param connection
     * @param userCode
     * @return
     */
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement prst = null;
        ResultSet rs = null;
        User user = null;

        if (connection != null) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, prst, rs, sql, params);

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResources(null, prst, rs);
        }

        return user;
    }

    /**
     * 更新用户密码
     *
     * @param connection
     * @param id
     * @param userPassword
     * @return
     * @throws SQLException
     */
    @Override
    public int updateUserPassword(Connection connection, int id, String userPassword) throws SQLException {

        PreparedStatement preparedStatement = null;
        // ResultSet resultSet = null;
        int resultRow = 0;

        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {userPassword, id};
            resultRow = BaseDao.execute(connection, preparedStatement, sql, params);
        }

        BaseDao.closeResources(null, preparedStatement, null);
        return resultRow;
    }

    /**
     * 根据参数类型获取用户总数
     *
     * @param connection
     * @param userName
     * @param userRole
     * @return
     * @throws SQLException
     */
    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = 0;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select count(*) as count from smbms_user u,smbms_role r where u.userRole = r.id ");

            ArrayList<Object> list = new ArrayList<>();// 用来存放参数，需要模糊匹配
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append("and u.userRole = ?");
                list.add(userRole);
            }

            Object[] params = list.toArray();

            System.out.println("UserDao-->getUserCount-->sql: " + sql.toString());

            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);

            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }

            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return count;
    }

    /**
     * 获取用户列表、该页面有分页功能
     *
     * @param connection
     * @param userName
     * @param userRole
     * @param currentPageNo
     *            当前页
     * @param pageSize
     *            页面容量
     * @return
     * @throws SQLException
     */
    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)
        throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();

        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id ");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append("and u.userRole = ?");
                list.add(userRole);
            }

            // 数据库实现分页显示 limit startIndex，pageSize；总数, startIndex从0开始
            sql.append(" order by u.id ASC limit ?,?");
            System.out.println("UserDao-->getUserList-->sql: " + sql.toString());
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return userList;
    }

    /**
     * 根据用户编码获取用户总数
     * 
     * @param connection
     * @param userCode
     * @return
     * @throws SQLException
     */
    @Override
    public int getUserCount(Connection connection, String userCode) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int count = 0;
        if (connection != null) {
            String sql = "select count(*) as count from smbms_user u where u.userCode = ? ";
            Object[] params = {userCode};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return count;
    }

    /**
     * 添加用户
     * 
     * @param connection
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public int addUser(Connection connection, User user) throws SQLException {

        PreparedStatement preparedStatement = null;
        int resultRows = 0;
        if (connection != null) {
            String sql =
                "insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate,modifyBy,modifyDate)\n"
                    + "value (?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(), user.getGender(),
                user.getBirthday(), user.getPhone(), user.getAddress(), user.getUserRole(), user.getCreatedBy(),
                user.getCreationDate(), user.getModifyBy(), user.getModifyDate()};
            resultRows = BaseDao.execute(connection, preparedStatement, sql, params);
        }
        return resultRows;
    }
}
