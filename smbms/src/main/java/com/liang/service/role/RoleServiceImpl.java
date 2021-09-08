package com.liang.service.role;

import com.liang.dao.BaseDao;
import com.liang.dao.role.RoleDao;
import com.liang.dao.role.RoleDaoImpl;
import com.liang.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection = BaseDao.getConnection();
        List<Role> roleList = null;
        try {
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return roleList;
    }
    @Test
    public void Test1(){
        RoleService roleService = new RoleServiceImpl();
        for (Role role : roleService.getRoleList()) {
            System.out.println(role.getRoleName());
        }
    }
}
