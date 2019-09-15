package com.helpyun.shiro.authc;
 
import org.apache.shiro.authc.AuthenticationToken;

/***
 * @description
 * @author yanquanfu rebort
 * @date 2019/9/15
 * @param  
 * @return 
 */
public class JwtToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private String token;
 
    public JwtToken(String token) {
        this.token = token;
    }
 
    @Override
    public Object getPrincipal() {
        return token;
    }
 
    @Override
    public Object getCredentials() {
        return token;
    }
}
