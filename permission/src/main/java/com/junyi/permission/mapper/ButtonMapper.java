package com.junyi.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyi.permission.entity.Button;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ButtonMapper extends BaseMapper<Button> {
    @Select(
            "select c.* "
                    + "from t_user_role a "
                    + "inner join t_role_button b on a.role_id=b.role_id "
                    + "inner join t_button c on b.button_id=c.guid "
                    + "where a.user_id = #{userId}")
    List<Button> findAllByUser(String userId);
}
