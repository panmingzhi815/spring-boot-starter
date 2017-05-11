package org.pan.token.impl;

import com.github.pagehelper.util.StringUtil;
import org.pan.token.TokenManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by panmingzhi on 2017/5/8.
 */
@Component("JavaMapTokenManagerImpl")
public class JavaMapTokenManagerImpl implements TokenManager {

    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    @Override
    public String createToken(String username) {
        String token = createUUID();
        tokenMap.put(token, username);
        return token;
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean checkToken(String token) {
        return !StringUtil.isEmpty(token) && tokenMap.containsKey(token);
    }

    @Override
    public void removeToken(String tokenKey) {
        if (StringUtils.isEmpty(tokenKey)) {
            return;
        }
        tokenMap.remove(tokenKey);
    }
}
