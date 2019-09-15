package com.helpyun.config;

import com.helpyun.shiro.authc.JwtFilter;
import com.helpyun.shiro.authc.ModularRealmAuthenticator;
//import com.helpyun.shiro.common.CredentialsMatcher;
import com.helpyun.shiro.realm.FormAuthorizingRealm;
import com.helpyun.shiro.realm.WechatAuthorizingRealm;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.*;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/13 18:24
 * @Modified By：
 */
@Configuration
public class ShiroConfig {

    public static String DefaultRealm = "FormAuthorizingRealm";

    @Bean
    public FormAuthorizingRealm formAuthorizingRealm() {
        FormAuthorizingRealm formAuthorizingRealm = new FormAuthorizingRealm();
        return formAuthorizingRealm;
    }

    @Bean
    public WechatAuthorizingRealm wechatAuthorizingRealm(){
        WechatAuthorizingRealm wechatAuthorizingRealm = new WechatAuthorizingRealm();
        wechatAuthorizingRealm.setCredentialsMatcher(new AllowAllCredentialsMatcher());
        return wechatAuthorizingRealm;
    }

//    @Bean
//    public CredentialsMatcher credentialsMatcher(){
//        return new CredentialsMatcher();
//    }

    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<>();
        realms.add(formAuthorizingRealm());
        realms.add(wechatAuthorizingRealm());
        securityManager.setRealms(realms);
//        // 自定义session管理 使用redis
//        securityManager.setSessionManager(sessionManager())
//        //注入记住我管理器;
//        securityManager.setRememberMeManager(rememberMeManager())

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/success");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        Map<String,String> filterChainMap = new LinkedHashMap<>();

        filterChainMap.put("/css/**","anon");
        filterChainMap.put("/js/**","anon");
        filterChainMap.put("/img/**","anon");

        filterChainMap.put("/login","anon");
        filterChainMap.put("/logout","logout");
        filterChainMap.put("/","anon");
        filterChainMap.put("/wechatLogin","anon");

        filterChainMap.put("/index/**","authc");
        filterChainMap.put("/user/student*/**", "roles[admin]");


        Map<String,Filter> filterMap = new HashMap<>();
        filterMap.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        filterChainMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     * @return
     */
//    public RedisManager redisManager() {
//        logger.info("创建shiro redisManager,连接Redis..URL= " + host + ":" + port)
//        RedisManager redisManager = new RedisManager()
//        redisManager.setHost(host)
//        redisManager.setPort(port)
//        redisManager.setExpire(1800)// 配置缓存过期时间
//        redisManager.setTimeout(timeout)
//        // redisManager.setPassword(password)
//        return redisManager
//    }
    /**
     * cookie管理对象;记住我功能
     * @return
     */
//    @Bean
//    public CookieRememberMeManager rememberMeManager(){
//        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager()
//        cookieRememberMeManager.setCookie(rememberMeCookie())
//        return cookieRememberMeManager
//    }
}
