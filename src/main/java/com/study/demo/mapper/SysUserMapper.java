package com.study.demo.mapper;

import com.study.demo.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by heyanwei-thinkpad on 2019/5/30.
 */
@Mapper
public interface SysUserMapper {

    SysUser getByPrimaryKey(String primaryKey);
}
