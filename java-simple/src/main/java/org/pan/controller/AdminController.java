package org.pan.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pan.config.PageResult;
import org.pan.config.ResponseMsgBody;
import org.pan.domain.SystemAccount;
import org.pan.domain.SystemRole;
import org.pan.mapper.SystemAccountMapper;
import org.pan.token.IgnoreToken;
import org.pan.token.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@Api(value = "系统管理", tags = {"系统管理核心API"})
@RequestMapping(value = "admin")
public class AdminController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private SystemAccountMapper systemAccountMapper;

    @Autowired
    @Qualifier("JavaMapTokenManagerImpl")
    private TokenManager tokenManager;

    @IgnoreToken
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "用户登录管理平台")
    public ResponseMsgBody login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse httpServletResponse) {
        SystemAccount account = systemAccountMapper.findSystemAccount(username, password);
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
        Page<SystemAccount> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> systemAccountMapper.findSystemAccountList(username));
        return ResponseMsgBody.success("获取成功", new PageResult<>(page.getTotal(), page.getResult()));
    }

    @RequestMapping(value = "/systemAccounts", method = RequestMethod.POST)
    @ApiOperation(value = "添加登录用户", notes = "添加管理平台登录用户")
    public ResponseMsgBody addSystemAccount(@RequestBody SystemAccount systemAccount) {
        systemAccountMapper.saveSystemAccount(systemAccount);
        return ResponseMsgBody.success("添加成功", null);
    }

    @RequestMapping(value = "/systemAccounts/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改登录用户", notes = "修改管理平台登录用户")
    public ResponseMsgBody updateSystemAccount(@PathVariable("id") Long id, @RequestBody SystemAccount systemAccount) {
        systemAccount.setId(id);
        int count = systemAccountMapper.updateSystemAccount(systemAccount);
        if (count > 0) {
            return ResponseMsgBody.success("修改成功", null);
        } else {
            return ResponseMsgBody.fail("修改失败", null);
        }
    }

    @RequestMapping(value = "/systemAccounts/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除登录用户", notes = "删除管理平台登录用户")
    public ResponseMsgBody deleteSystemAccount(@PathVariable("id") Long id) {
        int count = systemAccountMapper.deleteSystemAccount(id);
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
        systemAccountMapper.saveSystemRole(systemRole);
        return ResponseMsgBody.success("添加成功", null);
    }

    @RequestMapping(value = "systemRoles/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改角色", notes = "修改管理平台角色")
    public ResponseMsgBody updateSystemRole(@PathVariable("id") Long id, @RequestBody SystemRole systemRole) {
        systemRole.setId(id);
        int count = systemAccountMapper.updateSystemRole(systemRole);
        if (count > 0) {
            return ResponseMsgBody.success("修改成功", null);
        } else {
            return ResponseMsgBody.fail("修改失败", null);
        }
    }

    @RequestMapping(value = "/systemRoles/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除登录角色", notes = "删除管理平台角色")
    public ResponseMsgBody deleteSystemRole(@PathVariable("id") Long id) {
        int count = systemAccountMapper.deleteSystemRole(id);
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
        Page<SystemRole> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> systemAccountMapper.findSystemRoleList(roleName));
        return ResponseMsgBody.success("获取成功", new PageResult<>(page.getTotal(), page.getResult()));
    }

    @IgnoreToken
    @RequestMapping(value = "/dongyun", method = RequestMethod.POST)
    @ApiOperation(value = "东云测试", notes = "东云测试")
    public String dongyun(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        return "";
    }

    @IgnoreToken
    @RequestMapping(value = "/dongyun2", method = RequestMethod.POST)
    @ApiOperation(value = "东云测试", notes = "东云测试")
    public String dongyun2(String qrcodeData,String devSn) {
        LOGGER.info("qrcodeData:{} devSn:{}",qrcodeData,devSn);
        return "{'ret':0,'count':1}";
    }
}
