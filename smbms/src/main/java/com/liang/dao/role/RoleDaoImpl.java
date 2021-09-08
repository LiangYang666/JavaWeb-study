package com.liang.dao.role;

import com.liang.dao.BaseDao;
import com.liang.pojo.Role;
import com.liang.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Role> roleList = new ArrayList<Role>();
        if(connection!=null){
            String sql = "select * from smbms_role";
            System.out.println("sql---->  "+sql);
            Object[] params = {};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while (rs.next()) {
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleName(rs.getString("roleName"));
                _role.setRoleCode(rs.getString("roleCode"));
                roleList.add(_role);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return roleList;
    }
}
