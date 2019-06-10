package com.study.demo.dubbo.service;

import com.heyanwei.rpc.service.SysUserService;
import com.study.demo.mapper.SysUserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by heyanwei-thinkpad on 2019/5/30.
 */
@Service(version = "1.0.0")
public class DefaultSysUser implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    public Object getByPrimaryKey(String id){
        return sysUserMapper.getByPrimaryKey(id);
    }

}
