package org.pan.token;

/**
 * Created by panmingzhi on 2017/5/8.
 */
public interface TokenManager {

    String TOKEN_KEY = "token_key";

    String createToken(String username);

    boolean checkToken(String username);

    void removeToken(String tokenKey);
}
