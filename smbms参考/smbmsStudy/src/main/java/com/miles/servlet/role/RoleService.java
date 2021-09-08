package com.miles.servlet.role;

import com.miles.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * 获取角色列表
     * 
     * @return
     */
    public List<Role> getRoleList();
}
