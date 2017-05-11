package org.pan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.pan.domain.SystemAccount;
import org.pan.domain.SystemRole;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by panmingzhi on 2017/5/3.
 */
@Mapper
@Component
public interface SystemAccountMapper {

    List<SystemAccount> findSystemAccountList(@Param("username") String username);

    SystemAccount findSystemAccount(@Param("username") String username, @Param("password") String password);

    void saveSystemAccount(SystemAccount systemAccount);

    int updateSystemAccount(SystemAccount systemAccount);

    int deleteSystemAccount(@Param("id") Long id);

    void saveSystemRole(SystemRole systemRole);

    int updateSystemRole(SystemRole systemRole);

    int deleteSystemRole(Long id);

    void findSystemRoleList(@Param("roleName")String roleName);
}
