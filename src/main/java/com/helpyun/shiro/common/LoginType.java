package com.helpyun.shiro.common;

/**
 * @ClassName LoginType
 * @Description //TODO
 * @Author Robert Y
 * @Date 20:48 2019/5/16
 * @Version 1.0
 **/

public enum LoginType {

    /*
     * @Author Robert Y
     * @Description TODO
     * @Date 22:45 2019/5/17
     * @Param
     * @return
     */
    SYSTEM("FormAuthorizingRealm"),
    /**
     * 第三方登录(微信登录)
     */
    WECHAT_LOGIN("WechatAuthorizingRealm"),
    /**
     * 通用
     */
    COMMON("AuthorizationRealm"),
    /**
     * 用户密码登录
     */
    USER_PASSWORD("UserPasswordRealm");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
