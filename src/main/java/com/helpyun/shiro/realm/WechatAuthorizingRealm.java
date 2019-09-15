package com.helpyun.shiro.realm;

import com.helpyun.common.util.JwtUtil;
import com.helpyun.shiro.authc.JwtToken;
import com.helpyun.shiro.common.UsernamePasswordToken;
import com.helpyun.system.entity.SysUser;
import com.helpyun.test.dao.SysUserDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/14 8:32
 * @Modified By：
 */
public class WechatAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        String openId = ((UsernamePasswordToken)authenticationToken).getOpenId();

        String token = (String) authenticationToken.getCredentials();
        if (token == null) {
            throw new AuthenticationException("token为空!");
        }
        SysUser sysUser = jwtUtil.getSysUser(token);

        if (!sysUser.getUsername().equals("openId")){
            throw new AuthenticationException("用户不存在!");
        }

        sysUser = sysUserDao.getUserByName("jeecg");

        return new SimpleAuthenticationInfo(sysUser,sysUser.getPassword(), getName());
    }
}
