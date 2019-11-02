package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.service.RoleService;
import com.mage.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
    @Resource
    private RoleService roleService;

    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Role> queryAllRoles(){
        return roleService.queryAllRoles();
    }

    @RequestMapping("index")
    public String index(){
        return "role";
    }
    @RequestMapping("queryRolesByParams")
    @ResponseBody
    public Map<String,Object> queryRolesByParams(RoleQuery roleQuery){
        return roleService.queryRolesByParams(roleQuery);
    }

    @ResponseBody
    @RequestMapping("insert")
    public MessageModel insert(Role role){
        roleService.insert(role);
        return new MessageModel("添加角色成功");
    }
    @ResponseBody
    @RequestMapping("update")
    public MessageModel update(Role role){
        roleService.update(role);
        return new MessageModel("修改角色成功");
    }

    @ResponseBody
    @RequestMapping("delete")
    public MessageModel delete(Role role){
        roleService.delete(role);
        return new MessageModel("删除角色成功");
    }

    @RequestMapping("addPermission")
    @ResponseBody
    public MessageModel addPermission(Integer rid,Integer[] moduleIds){
        roleService.addPermission(rid,moduleIds);
        return new MessageModel("授权成功");
    }

}
