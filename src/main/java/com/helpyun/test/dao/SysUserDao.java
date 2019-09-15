package com.helpyun.test.dao;

import com.helpyun.system.entity.SysUser;
import org.jeecgframework.minidao.annotation.MiniDao;
import org.jeecgframework.minidao.annotation.Param;
import org.jeecgframework.minidao.annotation.Sql;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/1 9:29
 * @Modified By：
 */
@MiniDao
public interface SysUserDao {

    @Sql("SELECT * FROM sys_user")
    List<Map<String, Object>> getUserByAll();

    List<Map<String, Object>> getUserByQuery();

    @Sql("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username='${username}'))")
    Set<String> getRoleByUserName(@Param("username") String username);

    @Sql("select * from  sys_user  where username = '${username}'")
    SysUser getUserByName(@Param("username") String username);
}
