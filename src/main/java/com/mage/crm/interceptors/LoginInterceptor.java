package com.mage.crm.interceptors;

import com.mage.crm.base.CrmConstant;
import com.mage.crm.service.UserService;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断cookie中id是否存在
        String id = CookieUtil.getCookieValue(request, "id");
        System.out.println(id);
        AssertUtil.isTrue(StringUtils.isBlank(id),CrmConstant.NO_LOGIN_CODE, CrmConstant.NO_LOGIN_MSG);
        //id解码,查询user对象
        User user = userService.queryUserById(Base64Util.deCode(id));
        AssertUtil.isTrue(null==user, CrmConstant.NO_LOGIN_CODE, CrmConstant.NO_LOGIN_MSG);
        AssertUtil.isTrue("0".equals(user.getIsValid()),CrmConstant.OPS_FAILED_CODE,CrmConstant.OPS_FAILED_MSG);
        return true;
    }
}
