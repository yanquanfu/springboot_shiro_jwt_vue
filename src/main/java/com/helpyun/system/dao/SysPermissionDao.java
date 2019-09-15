package com.helpyun.system.dao;

import com.helpyun.system.entity.SysPermission;
import org.jeecgframework.minidao.annotation.MiniDao;

import java.util.List;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/13 17:36
 * @Modified By：
 */
@MiniDao
public interface SysPermissionDao {


    List<SysPermission> queryByUser(String username);
}
