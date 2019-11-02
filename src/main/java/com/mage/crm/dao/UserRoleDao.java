package com.mage.crm.dao;

import com.mage.crm.vo.Role;
import com.mage.crm.vo.UserRole;

import java.util.List;

public interface UserRoleDao {
    int insertBacth(List<UserRole> roleList);

    List<UserRole> queryUserRoleByUserId(Integer userId);

    int deleteByUserId(Integer userId);

    int deleteByRoleId(Role role);

    List<UserRole> queryUserRoleByRoleId(Integer roleId);
}
