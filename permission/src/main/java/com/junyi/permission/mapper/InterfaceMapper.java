package com.junyi.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyi.permission.entity.Interface;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface InterfaceMapper extends BaseMapper<Interface> {

    @Select(
            "select e.* "
                    + "from t_user_role b "
                    + "inner join t_role_button c on b.role_id=c.role_id "
                    + "inner join t_button_interface d on c.button_id=d.button_id "
                    + "inner join t_interface e on d.interface_id=e.guid "
                    + "where e.is_authorized = true and b.user_id = #{userId}")
    List<Interface> selectByUserId(String userId);

    @Select(
            value =
                    "select distinct b.user_id,CONCAT(e.url, e.method_type) AS url from"
                        + " t_user_role b inner join t_role_button c on b.role_id=c.role_id inner"
                        + " join t_button_interface d on c.button_id=d.button_id inner join"
                        + " t_interface e on d.interface_id=e.guid and  e.is_authorized = true")
    List<Map<String, String>> findAllUserInterface();

    @Select(
            value =
                    "select distinct b.user_id,CONCAT(e.url, e.method_type) AS url from"
                        + " t_user_role b inner join t_role_button c on b.role_id=c.role_id inner"
                        + " join t_button_interface d on c.button_id=d.button_id inner join"
                        + " t_interface e on d.interface_id=e.guid and e.is_authorized = true"
                        + " where b.user_id = #{userId}")
    List<Map<String, String>> findAllUserInterfaceByUser(String userId);
}
