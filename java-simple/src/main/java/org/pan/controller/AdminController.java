package org.pan.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pan.config.PageResult;
import org.pan.config.ResponseMsgBody;
import org.pan.domain.SystemAccount;
import org.pan.domain.SystemResource;
import org.pan.domain.SystemRole;
import org.pan.mapper.AdminMapper;
import org.pan.token.IgnoreToken;
import org.pan.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@Api(value = "系统管理")
@RequestMapping(value = "admin")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    @Qualifier("JavaMapTokenManagerImpl")
    private TokenManager tokenManager;

    @IgnoreToken
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "用户登录管理平台")
    public ResponseMsgBody login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse httpServletResponse) {
        SystemAccount account = adminMapper.findSystemAccount(username, password);
        if (account != null) {
            httpServletResponse.addCookie(new Cookie(TokenManager.TOKEN_KEY, tokenManager.createToken(username)));
            return ResponseMsgBody.success("登录成功", account);
        } else {
            httpServletResponse.addCookie(new Cookie(TokenManager.TOKEN_KEY, null));
            return ResponseMsgBody.fail("用户名或密码不正确", null);
        }
    }

    @IgnoreToken
    @RequestMapping(value = "/session", method = RequestMethod.DELETE)
    @ApiOperation(value = "用户注销", notes = "用户注销管理平台")
    public ResponseMsgBody logout(HttpServletResponse httpServletResponse, @CookieValue(name = TokenManager.TOKEN_KEY, required = false, defaultValue = "") String tokenKey) {
        if (StringUtils.isEmpty(tokenKey)) {
            return ResponseMsgBody.success("用户己注销", null);
        }
        tokenManager.removeToken(tokenKey);
        httpServletResponse.addCookie(new Cookie(TokenManager.TOKEN_KEY, null));
        return ResponseMsgBody.success("注销成功", null);
    }

    @RequestMapping(value = "/systemAccounts", method = RequestMethod.GET)
    @ApiOperation(value = "用户列表", notes = "管理平台登录用户列表")
    public ResponseMsgBody accountList(@RequestParam(value = "username", required = false) final String username,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize) {
        Page<SystemAccount> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> adminMapper.findSystemAccountList(username));
        return ResponseMsgBody.success("获取成功", new PageResult<>(page.getTotal(), page.getResult()));
    }

    @RequestMapping(value = "/systemAccounts", method = RequestMethod.POST)
    @ApiOperation(value = "添加登录用户", notes = "添加管理平台登录用户")
    public ResponseMsgBody addSystemAccount(@RequestBody SystemAccount systemAccount) {
        adminMapper.saveSystemAccount(systemAccount);
        return ResponseMsgBody.success("添加成功", null);
    }

    @RequestMapping(value = "/systemAccounts/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改登录用户", notes = "修改管理平台登录用户")
    public ResponseMsgBody updateSystemAccount(@PathVariable("id") Long id, @RequestBody SystemAccount systemAccount) {
        systemAccount.setId(id);
        int count = adminMapper.updateSystemAccount(systemAccount);
        if (count > 0) {
            return ResponseMsgBody.success("修改成功", null);
        } else {
            return ResponseMsgBody.fail("修改失败", null);
        }
    }

    @RequestMapping(value = "/systemAccounts/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除登录用户", notes = "删除管理平台登录用户")
    public ResponseMsgBody deleteSystemAccount(@PathVariable("id") Long id) {
        int count = adminMapper.deleteSystemAccount(id);
        if (count > 0) {
            return ResponseMsgBody.success("删除成功", null);
        } else {
            return ResponseMsgBody.fail("删除失败", null);
        }
    }

    @RequestMapping(value = "/systemAccounts/{id}/roles", method = RequestMethod.POST)
    @ApiOperation(value = "分配登录用户所属角色", notes = "分配管理平台登录用户所属角色")
    public ResponseMsgBody addSystemAccountRole(@PathVariable("id") Long id,@RequestParam("role_id_arr[]") List<Long> role_id_arr){
        adminMapper.deleteSystemAccountRole(id, role_id_arr);
        int count = adminMapper.addSystemAccountRole(id, role_id_arr);
        if (count > 0) {
            return ResponseMsgBody.success("分配成功", null);
        } else {
            return ResponseMsgBody.fail("分配失败", null);
        }
    }

    @RequestMapping(value = "/systemAccounts/{id}/roles", method = RequestMethod.DELETE)
    @ApiOperation(value = "取消登录用户所属角色", notes = "取消管理平台登录用户所属角色")
    public ResponseMsgBody deleteSystemAccountRole(@PathVariable("id") Long id,@RequestParam("role_id_arr[]") List<Long> role_id_arr){
        int count = adminMapper.deleteSystemAccountRole(id, role_id_arr);
        if (count > 0) {
            return ResponseMsgBody.success("删除成功", null);
        } else {
            return ResponseMsgBody.fail("删除失败", null);
        }
    }

    @RequestMapping(value = "/systemRoles", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色", notes = "添加管理平台角色")
    public ResponseMsgBody addSystemRole(@RequestBody SystemRole systemRole) {
        systemRole.setUpdateTime(new Date());
        adminMapper.saveSystemRole(systemRole);
        return ResponseMsgBody.success("添加成功", null);
    }

    @RequestMapping(value = "systemRoles/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改角色", notes = "修改管理平台角色")
    public ResponseMsgBody updateSystemRole(@PathVariable("id") Long id, @RequestBody SystemRole systemRole) {
        systemRole.setId(id);
        int count = adminMapper.updateSystemRole(systemRole);
        if (count > 0) {
            return ResponseMsgBody.success("修改成功", null);
        } else {
            return ResponseMsgBody.fail("修改失败", null);
        }
    }

    @RequestMapping(value = "/systemRoles/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除登录角色", notes = "删除管理平台角色")
    public ResponseMsgBody deleteSystemRole(@PathVariable("id") Long id) {
        int count = adminMapper.deleteSystemRole(id);
        if (count > 0) {
            return ResponseMsgBody.success("删除成功", null);
        } else {
            return ResponseMsgBody.fail("删除失败", null);
        }
    }

    @RequestMapping(value = "/systemRoles", method = RequestMethod.GET)
    @ApiOperation(value = "角色列表", notes = "管理平台登录角色列表")
    public ResponseMsgBody roleList(@RequestParam(value = "roleName", required = false) final String roleName,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize) {
        Page<SystemRole> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> adminMapper.findSystemRoleList(roleName));
        return ResponseMsgBody.success("获取成功", new PageResult<>(page.getTotal(), page.getResult()));
    }


    @RequestMapping(value = "/systemResources", method = RequestMethod.POST)
    @ApiOperation(value = "添加资源", notes = "添加管理平台可访问的资源")
    public ResponseMsgBody addSystemResource(@RequestBody SystemResource systemResource) {
        adminMapper.saveSystemResource(systemResource);
        return ResponseMsgBody.success("添加成功", null);
    }

    @RequestMapping(value = "systemResources/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改资源", notes = "修改管理平台可访问的资源")
    public ResponseMsgBody updateSystemResource(@PathVariable("id") Long id, @RequestBody SystemResource systemResource) {
        systemResource.setId(id);
        int count = adminMapper.updateSystemResource(systemResource);
        if (count > 0) {
            return ResponseMsgBody.success("修改成功", null);
        } else {
            return ResponseMsgBody.fail("修改失败", null);
        }
    }

    @RequestMapping(value = "/systemResources/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除资源", notes = "删除管理平台可访问的资源")
    public ResponseMsgBody deleteSystemResource(@PathVariable("id") Long id) {
        int count = adminMapper.deleteSystemResource(id);
        if (count > 0) {
            return ResponseMsgBody.success("删除成功", null);
        } else {
            return ResponseMsgBody.fail("删除失败", null);
        }
    }

    @RequestMapping(value = "/systemResources", method = RequestMethod.GET)
    @ApiOperation(value = "资源列表", notes = "管理平台资源列表")
    public ResponseMsgBody resourceList(@RequestParam(value = "resourceName", required = false) final String resourceName,
                                    @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize) {
        Page<SystemResource> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> adminMapper.findSystemResourceList(resourceName));
        return ResponseMsgBody.success("获取成功", new PageResult<>(page.getTotal(), page.getResult()));
    }


    @RequestMapping(value = "/systemRoles/{id}/resources", method = RequestMethod.POST)
    @ApiOperation(value = "分配角色所属资源", notes = "分配管理平台登录角色所属资源")
    public ResponseMsgBody addSystemRoleResource(@PathVariable("id") Long id,@RequestParam("resource_id_arr[]") List<Long> resource_id_arr){
        adminMapper.deleteSystemRoleResource(id, resource_id_arr);
        int count = adminMapper.addSystemRoleResource(id, resource_id_arr);
        if (count > 0) {
            return ResponseMsgBody.success("分配成功", null);
        } else {
            return ResponseMsgBody.fail("分配失败", null);
        }
    }

    @RequestMapping(value = "/systemRoles/{id}/resources", method = RequestMethod.DELETE)
    @ApiOperation(value = "取消登录角色所属资源", notes = "取消管理平台登录角色所属资源")
    public ResponseMsgBody deleteSystemRoleResource(@PathVariable("id") Long id,@RequestParam("resource_id_arr[]") List<Long> resource_id_arr){
        int count = adminMapper.deleteSystemRoleResource(id, resource_id_arr);
        if (count > 0) {
            return ResponseMsgBody.success("删除成功", null);
        } else {
            return ResponseMsgBody.fail("删除失败", null);
        }
    }

}
