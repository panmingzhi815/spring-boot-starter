package org.pan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.pan.domain.SystemAccount;
import org.pan.domain.SystemResource;
import org.pan.domain.SystemRole;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by panmingzhi on 2017/5/3.
 */
@Mapper
@Component
public interface AdminMapper {

    List<SystemAccount> findSystemAccountList(@Param("username") String username);

    SystemAccount findSystemAccount(@Param("username") String username, @Param("password") String password);

    void saveSystemAccount(SystemAccount systemAccount);

    int updateSystemAccount(SystemAccount systemAccount);

    int deleteSystemAccount(@Param("id") Long id);

    void saveSystemRole(SystemRole systemRole);

    int updateSystemRole(SystemRole systemRole);

    int deleteSystemRole(Long id);

    List<SystemRole> findSystemRoleList(@Param("roleName")String roleName);

    int deleteSystemAccountRole(@Param("id") Long id,@Param("role_id_arr") List<Long> role_id_arr);

    int addSystemAccountRole(@Param("id") Long id,@Param("role_id_arr") List<Long> role_id_arr);

    void saveSystemResource(SystemResource systemResource);

    int updateSystemResource(SystemResource systemResource);

    int deleteSystemResource(@Param("id")Long id);

    List<SystemResource> findSystemResourceList(@Param("resourceName")String resourceName);

    int deleteSystemRoleResource(Long id, List<Long> resource_id_arr);

    int addSystemRoleResource(Long id, List<Long> resource_id_arr);
}
