package com.helpyun.shiro.realm;


import com.helpyun.common.util.JwtUtil;
import com.helpyun.common.util.PasswordUtil;
import com.helpyun.shiro.authc.JwtToken;
import com.helpyun.shiro.common.UsernamePasswordToken;
import com.helpyun.system.dao.SysPermissionDao;
import com.helpyun.system.entity.SysPermission;
import com.helpyun.system.entity.SysUser;
import com.helpyun.test.dao.SysUserDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/13 16:39
 * @Modified By：
 */
public class FormAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysPermissionDao sysPermissionDao;
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
        // 给该用户设置角色，角色信息存在 t_role 表中取
        authorizationInfo.setRoles(sysUserDao.getRoleByUserName(username));
        // 给该用户设置权限，权限信息存在 t_permission 表中取

        // 从数据库获取所有的权限
        Set<String> permissionSet = new HashSet<>();
        List<SysPermission> permissionList = sysPermissionDao.queryByUser(username);
        for (SysPermission po : permissionList) {
            if (null != po) {
                permissionSet.add(po.getUrl());
            }
        }
        authorizationInfo.setStringPermissions(permissionSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
//        String token = ((UsernamePasswordToken) auth).getUsername();
//        String password = new String(((UsernamePasswordToken) auth).getPassword());
        if (token == null) {
            throw new AuthenticationException("token为空!");
        }
        SysUser sysUser = jwtUtil.getSysUser(token);
        if (sysUser == null){
            throw new AuthenticationException("token非法无效!");
        }
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();

        sysUser = sysUserDao.getUserByName(username);
        if (sysUser == null) {
            throw new AuthenticationException("用户不存在!");
        }

        // 解密获得username，用于和数据库进行对比
//        String username = JwtUtil.getUsername(token);
//        if (username == null) {
//            throw new AuthenticationException("token非法无效!");
//        }
//
//        String username = (String) auth.getPrincipal();
//        // 查询用户信息
//        SysUser sysUser = sysUserDao.getUserByName(username);
//        if (sysUser == null) {
//            throw new AuthenticationException("用户不存在!");
//        }
        String passwordEncode = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        if (!sysUser.getPassword().equals(passwordEncode)) {
            throw new AuthenticationException("密码错误!");
        }

//        //校验token是否超时失效 & 或者账号密码是否错误
//        if (!jwtTokenRefresh(token, username, sysUser.getPassword())) {
//            throw new AuthenticationException("用户名或密码错误!");
//        }

        return new SimpleAuthenticationInfo(sysUser, token, getName());
    }


//    /**
//     * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
//     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
//     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
//     * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
//     * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
//     * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
//     * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
//     * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
//     *    参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
//     *
//     * @param userName
//     * @param passWord
//     * @return
//     */
//    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
////        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
//        if (null != token && !token.equals("")) {
//            //校验token有效性
//            if (!JwtUtil.verify(token, userName, passWord)) {
//                String newAuthorization = JwtUtil.sign(userName, passWord);
//                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
//                //设置超时时间
//                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
//            } else {
//                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
//                //设置超时时间
//                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
//            }
//            return true;
//        }
//        return false;
//    }





}
