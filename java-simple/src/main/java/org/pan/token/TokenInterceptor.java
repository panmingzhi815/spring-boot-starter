package org.pan.token;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.pan.config.ResponseMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import java.lang.reflect.Method;

/**
 * Created by panmingzhi on 2017/5/8.
 */
@Aspect
@Component
public class TokenInterceptor {

    private static Logger LOGGER = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    @Qualifier("JavaMapTokenManagerImpl")
    private TokenManager tokenManager;

    @Around("execution(* org.pan.controller..*(..))")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        // 从切点上获取目标方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // 若目标方法忽略了安全性检查，则直接调用目标方法
        if (method.isAnnotationPresent(IgnoreToken.class)) {
            LOGGER.debug("方法：{} 标记为token忽略", method);
            return pjp.proceed();
        }
        String token = getCookieToken(TokenManager.TOKEN_KEY);
        // 检查 token 有效性
        if (!tokenManager.checkToken(token)) {
            String message = String.format("token [%s] 验证未通过", token);
            return ResponseMsgBody.fail(message, null);
        }
        // 调用目标方法
        return pjp.proceed();
    }

    public String getCookieToken(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Cookie[] cookies = attributes.getRequest().getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}