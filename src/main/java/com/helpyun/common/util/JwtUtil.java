package com.helpyun.common.util;

import com.helpyun.system.entity.SysUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//jwt含有三部分：头部（header）、载荷（payload）、签证（signature）
/*
*(1)头部一般有两部分信息：声明类型、声明加密的算法（通常使用HMAC SHA256）
*(2)载荷该部分一般存放一些有效的信息。jwt的标准定义包含五个字段：
*    -iss：该JWT的签发者
    - sub: 该JWT所面向的用户
    - aud: 接收该JWT的一方
    - exp(expires): 什么时候过期，这里是一个Unix时间戳
    - iat(issued at): 在什么时候签发的
* (3)签证（signature） JWT最后一个部分。该部分是使用了HS256加密后的数据；包含三个部分：
* */

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/2 21:11
 * @Modified By：
 */
@Service("jwtUtil")
@PropertySource("classpath:prop/sys.properties")
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.issuer}")
    private String JWT_ISSUER;


    public String creatJwtToken(String username,String password,String loginType) {
        final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        final long nowMillis = System.currentTimeMillis();
        //设置过期时间
        final long ttlMillis = 5 * 60 * 100000;
        final long expMillis = nowMillis + ttlMillis;

        final Date now = new Date(nowMillis);
        final Date exp = new Date(expMillis);

        //Create the Signature SecretKey
        final byte[] apiKeySecretBytes = getKeySercret();
        final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        final Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");

        //add JWT Parameters
        final JwtBuilder builder = Jwts.builder()
                .setHeaderParams(headerMap)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setIssuer(JWT_ISSUER)
                .claim("username",username)
                .claim("password",password)
                .claim("loginType",loginType)
                .signWith(signatureAlgorithm, signingKey);

        log.info("JWT[" + builder.compact() + "]");
        return builder.compact();

    }

    public Claims parseJWTToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(getKeySercret()).parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                | IllegalArgumentException e) {
            log.info("Parse JWT errror " + e.getMessage());
            return null;
        }
        return claims;
    }

    public byte[] getKeySercret(){
        return DatatypeConverter.parseBase64Binary(Base64.getEncoder().encodeToString(SECRET.getBytes()));
    }

    public SysUser getSysUser(String token){
        SysUser sysUser = new SysUser();
        try{
            Claims claims = parseJWTToken(token);

            if (claims.containsKey("username")){
                sysUser.setUsername((String)claims.get("username"));
            }
            if (claims.containsKey("password")){
                sysUser.setPassword((String)claims.get("password"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sysUser;
    }

    public String getLoginType(String token){
        Claims claims = parseJWTToken(token);
        if (claims.containsKey("loginType")){
            return (String)claims.get("loginType");
        }else{
            return null;
        }
    }

    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     *
     * @param token
     * @return
     */
    public Boolean isVerify(String token, String username,String password) {
        try {
            //得到DefaultJwtParser
            Claims claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(getKeySercret())
                    //设置需要解析的jwt
                    .parseClaimsJws(token).getBody();

            if (claims.get("password").equals(password)) {
                return true;
            }
        } catch (Exception exception) {
            return false;
        }
        return null;
    }


}
