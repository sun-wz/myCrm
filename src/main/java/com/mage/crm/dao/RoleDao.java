package com.mage.crm.dao;

import com.mage.crm.query.RoleQuery;
import com.mage.crm.vo.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleDao {
    @Select("select id,role_name as'roleName' from t_role where is_valid =1")
    List<Role> queryAllRoles();

    List<Role> queryRolesByParams(RoleQuery roleQuery);


    int insert(Role role);

    int update(Role role);

    int delete(Role role);

    Role queryRoleById(Integer rid);
}
