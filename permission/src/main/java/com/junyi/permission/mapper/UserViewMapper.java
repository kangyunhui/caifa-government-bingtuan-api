package com.junyi.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.junyi.permission.annotation.XzcodeFilter;
import com.junyi.permission.model.UserView;

import org.apache.ibatis.annotations.Select;

@XzcodeFilter
public interface UserViewMapper extends BaseMapper<UserView> {
    @Select(
            "select * from t_user dt where exists (select * from t_user_role b where"
                    + " dt.guid=b.user_id and b.role_id = #{roleId})")
    IPage<UserView> selectByRole(IPage<UserView> page, String roleId);

    @Select(
            "select * from t_user dt where not exists (select * from t_user_role b where"
                    + " dt.guid=b.user_id and b.role_id = #{roleId})")
    IPage<UserView> selectAbsentByRole(IPage<UserView> page, String roleId);
}
