package com.helpyun.test.controller;

import com.helpyun.system.entity.SysUser;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/1 9:12
 * @Modified By：
 */
@Controller
@RequestMapping
public class TestControllerTwo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/index/testTwo",method = RequestMethod.GET)
    @ResponseBody
    public String index(){
//        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM sys_user ");
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser)subject.getPrincipal();
        return JSONObject.fromObject(sysUser).toString();
    }

}
