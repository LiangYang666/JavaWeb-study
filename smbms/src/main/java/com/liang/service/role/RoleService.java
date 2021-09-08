package com.liang.service.role;

import com.liang.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleService {

    public List<Role> getRoleList();
}
