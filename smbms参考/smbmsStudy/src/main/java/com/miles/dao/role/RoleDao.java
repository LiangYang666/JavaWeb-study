package com.miles.dao.role;

import com.miles.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {

    /**
     * 获取角色名称
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
