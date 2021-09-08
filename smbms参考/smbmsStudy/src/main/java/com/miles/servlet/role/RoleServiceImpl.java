package com.miles.servlet.role;

import com.miles.dao.BaseDao;
import com.miles.dao.role.RoleDao;
import com.miles.dao.role.RoleDaoImpl;
import com.miles.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    public RoleServiceImpl() {

        this.roleDao = new RoleDaoImpl();
    }

    /**
     * 获取角色列表
     * 
     * @return
     */
    @Override
    public List<Role> getRoleList() {

        Connection connection = null;
        List<Role> roleList = null;

        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return roleList;
    }

    @Test
    public void test() {

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        System.out.println(roleList.size());
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }
    }
}
