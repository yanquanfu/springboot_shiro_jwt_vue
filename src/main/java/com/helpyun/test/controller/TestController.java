package com.helpyun.test.controller;

import com.helpyun.common.util.JwtUtil;
import com.helpyun.shiro.authc.JwtToken;
import com.helpyun.shiro.common.LoginType;
import com.helpyun.shiro.common.UsernamePasswordToken;
import com.helpyun.system.entity.SysUser;
import com.helpyun.test.dao.SysUserDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/8/29 22:36
 * @Modified By：
 */
@Controller
@RequestMapping
public class TestController {

//    @Autowired
//    private DruidDataSource druidDataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private JwtUtil jwtUtil;


    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM sys_user ");
        List<Map<String, Object>> list2 = sysUserDao.getUserByAll();
        List<Map<String, Object>> list3 = sysUserDao.getUserByQuery();
        return "index";
    }

    @GetMapping(value = "login")
    public String loginPage(){
        return "index";
    }

//    @RequestMapping(value = "login")
    @PostMapping(value = "login")
    public String login(SysUser user, HttpServletRequest request){

//        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());

        String token = jwtUtil.creatJwtToken(user.getUsername(),user.getPassword(),null);
        JwtToken jwtToken = new JwtToken(token);

        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(jwtToken);
            return "/home/success";
        }catch (Exception e){
            e.printStackTrace();
            return "index";
        }
    }

    @RequestMapping("success")
    public String success(){
        return "/home/success";
    }

    @GetMapping(value = "wechatLogin")
    public String wechatLogin(){
        return "wechatLogin";
    }

    @PostMapping(value = "wechatLogin")
    public String doWechatLogin(@RequestParam String openId,@RequestParam String password){
//        UsernamePasswordToken token = new UsernamePasswordToken(LoginType.WECHAT_LOGIN,openId,password);

        String token = jwtUtil.creatJwtToken(openId,password,LoginType.WECHAT_LOGIN.getType());
        JwtToken jwtToken = new JwtToken(token);

        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(jwtToken);
            return "/home/wechatSuccess";
        }catch (Exception e){
            e.printStackTrace();
            return "wechatLogin";
        }
    }

    @PostMapping(value = "/logout")
    public String logout(){
        String result = null;
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser)subject.getPrincipal();
        subject.logout();
        result = "退出成功";
        return result;
    }

}
