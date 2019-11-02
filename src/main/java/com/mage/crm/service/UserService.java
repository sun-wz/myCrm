package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.dto.UserDto;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.Md5Util;
import com.mage.crm.vo.User;
import com.mage.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private HttpSession session;

    public UserModel userLogin(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isEmpty(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isEmpty(userPwd),"密码不能为空");
        User user = userDao.queryUserByName(userName);
        AssertUtil.isTrue(null==user,"用户不存在");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已被注销");
        AssertUtil.isTrue(!Md5Util.enCode(userPwd).equals(user.getUserPwd()),"用户名或密码错误");

        /**
         * 获取用户权限  根据用户拥有的角色
         */
        List<String> permissions=permissionDao.queryPermissionsByUserId(user.getId() + "");
        System.out.println(permissions);
        if(null!=permissions&&permissions.size()>0){
            session.setAttribute("userPermission", permissions);
        }

        return createUserModel(user);
    }
    public UserModel createUserModel(User user){
        UserModel userModel = new UserModel();
        userModel.setId(Base64Util.enCode(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    public void updatePwd(String id, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(StringUtils.isEmpty(oldPassword),"原密码不能为空");
        AssertUtil.isTrue(StringUtils.isEmpty(newPassword),"新密码不能为空");
        AssertUtil.isTrue(StringUtils.isEmpty(confirmPassword),"确认新密码不能为空");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码要与原密码不同");
        AssertUtil.isTrue(!confirmPassword.equals(newPassword),"新密码要与确认密码相同");
        User user = userDao.queryUserById(Base64Util.deCode(id));
        AssertUtil.isTrue(null==user,"用户不存在了，建议重新登录");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已被注销了");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.enCode(oldPassword)),"原密码错误");
        AssertUtil.isTrue(userDao.updatePwd(user.getId(),Md5Util.enCode(newPassword))<1,"用户密码更新失败");
    }

    public User queryUserById(String id) {
        return userDao.queryUserById(id);
    }

    public List<User> queryAllCustomerManager() {
        return userDao.queryAllCustomerManager();
    }

    public Map<String,Object> queryUsersByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        List<UserDto> userDtoList = userDao.queryUsersByParams(userQuery);
        //判断是否查询到数据
        if(userDtoList!=null&&userDtoList.size()>0){
            for(UserDto userDto:userDtoList){
                String roleIdsStr = userDto.getRoleIdsStr();
                if(!StringUtil.isEmpty(roleIdsStr)){
                    String[] roleIdArray = roleIdsStr.split(",");
                    for(int i=0;i<roleIdArray.length;i++){
                        List<Integer> roleIds = userDto.getRoleIds();
                        roleIds.add(Integer.parseInt(roleIdArray[i]));
                    }
                }
            }
        }


        PageInfo<UserDto> userPageInfo = new PageInfo<>(userDtoList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",userPageInfo.getTotal());
        map.put("rows",userPageInfo.getList());
        return map;
    }

    public void insert(User user) {
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone());
        //校验用户名
        User u = userDao.queryUserByName(user.getUserName());
        AssertUtil.isTrue(u!=null,"用户名已存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.enCode("123456"));
        AssertUtil.isTrue(userDao.insert(user)<1,"用户添加失败");
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }
    }

    public void checkParams(String userName,String trueName,String phone){
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(trueName),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空");
    }

    public void relateRoles(List<Integer> roleIds,Integer userId){
        List<UserRole> roleList=new ArrayList<UserRole>();
        for (Integer roleId:roleIds){
            UserRole userRole = new UserRole();
            userRole.setIsValid(1);
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            roleList.add(userRole);
        }
        AssertUtil.isTrue(userRoleDao.insertBacth(roleList)<1,"用户角色添加失败");

    }

    public void update(User user) {
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone());


        //校验用户名
        User u = userDao.queryUserByName(user.getUserName());
        AssertUtil.isTrue(u!=null&&!u.getId().equals(user.getId()),"用户名已存在");
        user.setUpdateDate(new Date());
        //删除用户的角色绑定
        List<UserRole> userRoleList = userRoleDao.queryUserRoleByUserId(Integer.parseInt(user.getId()));
        if(userRoleList.size()>0){
            AssertUtil.isTrue(userRoleDao.deleteByUserId(Integer.parseInt(user.getId())) < userRoleList.size(), "系统出错");
        }
       //添加用户与角色关系
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }

    }

    public void delete(Integer userId) {
        AssertUtil.isTrue(userDao.delete(userId)<1,"用户删除失败");
        List<UserRole> userRoleList = userRoleDao.queryUserRoleByUserId(userId);
        if(userRoleList.size()>0){
            AssertUtil.isTrue(userRoleDao.deleteByUserId(userId)<userRoleList.size(),"用户角色关系删除失败");
        }

    }
}
