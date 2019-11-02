package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.*;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.Role;
import com.mage.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModuleDao moduleDao;
    public List<Role> queryAllRoles() {
        return roleDao.queryAllRoles();
    }


    public Map<String, Object> queryRolesByParams(RoleQuery roleQuery) {
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getRows());
        List<Role> roleList = roleDao.queryRolesByParams(roleQuery);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",rolePageInfo.getTotal());
        map.put("rows",rolePageInfo.getList());
        return map;

    }

    public void insert(Role role) {
        AssertUtil.isTrue(StringUtils.isEmpty(role.getRoleName()),"角色名不需为空");
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleDao.insert(role)<1,"添加角色失败");

    }

    public void update(Role role) {
        AssertUtil.isTrue(StringUtils.isEmpty(role.getRoleName()),"角色名不需为空");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleDao.update(role)<1,"修改角色失败");
    }

    public void delete(Role role) {
        AssertUtil.isTrue(roleDao.delete(role)<1,"删除角色失败");
        List<UserRole> userRoleList = userRoleDao.queryUserRoleByRoleId(Integer.parseInt(role.getId()));
        if(userRoleList.size()>0){
            AssertUtil.isTrue(userRoleDao.deleteByRoleId(role)<userRoleList.size(),"删除角色失败");
        }

    }
    /**
     * 1.参数合法性校验
     *    rid 角色记录必须存在
     *    moduleIds  可空
     * 2.删除原始权限
     *     查询原始权限
     *         原始权限存在  执行删除操作
     * 3. 执行批量添加
     *     根据moduleId  查询每个模块  权限值
     *     List<Permission>
     */
    public void addPermission(Integer rid, Integer[] moduleIds) {
        AssertUtil.isTrue(null==rid||null==roleDao.queryRoleById(rid),"角色不存在");
        int count=permissionDao.queryPermissionCountByRid(rid);
        if(count>0){
            AssertUtil.isTrue(permissionDao.deletePermissionByRid(rid)<count,CrmConstant.OPS_FAILED_MSG);
        }
        List<Permission> permissionList = null;
        if(null!=moduleIds&&moduleIds.length>0) {//判断前台传过来的moduleIds是否为空
            permissionList=new ArrayList<>();
            for (int i=0;i<moduleIds.length;i++){
                Permission permission = new Permission();
                permission.setRoleId(rid);
                permission.setModuleId(moduleIds[i]);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                Integer moduleId=moduleIds[i];
                permission.setAclValue(moduleDao.queryOptValueById(moduleId));
                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionDao.insertBatch(permissionList)<moduleIds.length, CrmConstant.OPS_FAILED_MSG);
        }
    }
}
