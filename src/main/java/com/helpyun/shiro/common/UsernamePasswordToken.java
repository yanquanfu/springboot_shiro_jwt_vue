package com.helpyun.shiro.common;

import com.helpyun.shiro.common.LoginType;
import lombok.Data;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/13 21:47
 * @Modified By：
 */
@Data
@Deprecated
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken  {

    //登录方式
    private LoginType loginType;
    //微信code
    private String openId;

    public UsernamePasswordToken(String username,String password){
        super(username,password);
    }

    public UsernamePasswordToken(LoginType loginType, final String openId,String password) {
        super(openId,password);
        this.loginType = loginType;
        this.openId = openId;
    }

}
