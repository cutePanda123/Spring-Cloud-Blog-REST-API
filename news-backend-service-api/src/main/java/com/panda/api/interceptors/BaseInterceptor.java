package com.panda.api.interceptors;

import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.utils.RedisAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class BaseInterceptor {
    @Autowired
    protected RedisAdaptor redisAdaptor;

    protected static final String REDIS_USER_TOKEN_PREFIX = "redis_user_token";
    protected static final String REDIS_USER_INFO_PREFIX = "redis_user_info";
    protected static final String REDIS_ADMIN_TOKEN_PREFIX = "redis_admin_token";

    protected boolean verifyUserIdToken(
            String id,
            String token,
            String redisKeyPrefix) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(token)) {
            EncapsulatedException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        String redisToken = redisAdaptor.get(redisKeyPrefix + ":" + id);
        if (StringUtils.isBlank(redisToken)) {
            EncapsulatedException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        if (!redisToken.equalsIgnoreCase(token)) {
            EncapsulatedException.display(ResponseStatusEnum.TICKET_INVALID);
            return false;
        }
        return true;
    }

    protected String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookieName == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                String value = cookie.getValue();
                return value;
            }
        }
        return null;
    }
}
