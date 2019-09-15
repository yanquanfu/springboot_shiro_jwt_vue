package com.helpyun.shiro.authc;

import com.helpyun.common.util.JwtUtil;
import com.helpyun.config.ShiroConfig;
import com.helpyun.shiro.common.UsernamePasswordToken;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/14 8:17
 * @Modified By：
 */
public class ModularRealmAuthenticator extends org.apache.shiro.authc.pam.ModularRealmAuthenticator {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken token) {
        assertRealmsConfigured();
        // 根据token选择realm
        String loginType = null;
        if (null == token ){
            loginType =  ShiroConfig.DefaultRealm;
        }else{
            Claims claims = jwtUtil.parseJWTToken((String)token.getPrincipal());
            if (null != claims && claims.containsKey("loginType")){
                loginType = (String)claims.get("loginType");
            }else{
                loginType =  ShiroConfig.DefaultRealm;
            }
        }

//        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

//        UsernamePasswordToken usernamePasswordToken = null;
//        if (token instanceof UsernamePasswordToken) {
//            usernamePasswordToken = (UsernamePasswordToken) token;
//        }
//
//        String loginType = null == usernamePasswordToken || usernamePasswordToken.getLoginType() == null ? ShiroConfig.DefaultRealm : usernamePasswordToken.getLoginType().getType();
        Collection<Realm> realms = getRealms();
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(loginType)) {
                typeRealms.add(realm);
            }
        }
        if (typeRealms.size() > 0) {
            return doSingleRealmAuthentication(((ArrayList<Realm>) typeRealms).get(0), token);
        } else {
            return doMultiRealmAuthentication(typeRealms, token);
        }
    }
}
