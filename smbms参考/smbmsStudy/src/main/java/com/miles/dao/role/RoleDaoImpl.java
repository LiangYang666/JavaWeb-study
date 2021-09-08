package com.miles.dao.role;

import com.miles.dao.BaseDao;
import com.miles.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    /**
     * 获取角色名称
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Role> roleList = new ArrayList<>();
        if (connection != null) {
            String sql = "select * from smbms_role";
            Object[] params = {};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setRoleCode(resultSet.getString("roleCode"));
                role.setRoleName(resultSet.getString("roleName"));
                role.setCreatedBy(resultSet.getInt("createdBy"));
                role.setCreationDate(resultSet.getDate("creationDate"));
                role.setModifyBy(resultSet.getInt("modifyBy"));
                role.setModifyDate(resultSet.getDate("modifyDate"));
                roleList.add(role);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        System.out.println("RoleDao-->getRoleList-->roleList :" + roleList.size());
        return roleList;
    }
}
