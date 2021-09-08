package com.liang.dao.user;

import com.liang.dao.BaseDao;
import com.liang.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;


        if(connection!=null){
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if(rs.next()){
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
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        int execute = 0;
        PreparedStatement pstm = null;
        if(connection!=null){
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {password, id};
            execute = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return execute;

    }

    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        int count=0;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(connection!=null){
            List<Object> p = new ArrayList<Object>();
            String sql = "select COUNT(*) as count from smbms_user u,smbms_role r where u.userRole=r.id";
            if(!StringUtils.isNullOrEmpty(userName)){
                sql += " and u.userName like ?";
                p.add("%"+userName+"%");
            }
            if(userRole>0){
                sql += " and u.userRole=?";
                p.add(userRole);
            }
            Object[] params = p.toArray();
            System.out.println("sql ----> " + sql);
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if(rs.next()){
                count = rs.getInt("count");
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return count;
    }

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList  = new ArrayList<User>();
        if(connection!=null){
            List<Object> p = new ArrayList<Object>();
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole=r.id";
            if(!StringUtils.isNullOrEmpty(userName)){
                sql += " and u.userName like ?";
                p.add("%"+userName+"%");
            }
            if(userRole>0){
                sql += " and u.userRole=?";
                p.add(userRole);
            }
            sql += " order by creationDate DESC limit ?,?";
            currentPageNo = (currentPageNo - 1) * pageSize;
            p.add(currentPageNo);
            p.add(pageSize);

            Object[] params = p.toArray();
            System.out.println("sql ----> " + sql);
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return userList;
    }

    public int delUser(Connection connection, int userId) throws SQLException {
        PreparedStatement pstm = null;
        String sql = "delete from smbms_user where id=?";
        Object[] params = {userId};
        int execute = 0;
        if(connection!=null){
            execute = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return execute;
    }

    public boolean ifUserCodeExist(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        String sql = "select COUNT(*) as count from smbms_user where userCode=?";
        ResultSet rs = null;
        boolean flag=false;
        if(connection!=null){
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if(rs.next()){
                if(rs.getInt("count")>0){
                    flag=true;
                } else {
                    flag = false;
                }
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return flag;
    }

    public boolean addUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int execute=0;

        if(connection!=null){
            String sql = "insert into smbms_user " +
                    "(userCode, userName, userPassword, gender, " +
                    "birthday, phone, address, userRole, " +
                    "createdBy, creationDate) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Object[] params = { user.getUserCode(), user.getUserName(), user.getUserPassword(), user.getGender(),
                                user.getBirthday(), user.getPhone(), user.getAddress(), user.getUserRole(),
                                user.getCreatedBy(), user.getCreationDate()};

            execute = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return execute > 0;
    }


}
