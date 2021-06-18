package com.panda.api.controller;

import com.panda.utils.RedisAdaptor;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @Autowired
    protected RedisAdaptor redisAdaptor;

    protected static final String MOBILE_SMSCODE_PREFIX = "mobile:smscode";
    protected static final String REDIS_USER_TOKEN_PREFIX = "redis_user_token";
    protected static final Integer COOKIE_DURATION = 30 * 24 * 60 * 60;

    protected Map<String, String> getErrors(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }

    protected void setCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName,
                             String cookieValue,
                             Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void setCookieValue(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName,
                             String cookieValue,
                             Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain("news.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
