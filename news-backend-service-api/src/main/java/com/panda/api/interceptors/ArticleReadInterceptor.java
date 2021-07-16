package com.panda.api.interceptors;

import com.panda.utils.IPUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {
    private static final String REDIS_ARTICLE_ALREADY_READ_PREFIX = "redis_article_already_read";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IPUtils.getRequestIp(request);
        String articleId = request.getParameter("articleId");
        return !redisAdaptor.keyIsExist(REDIS_ARTICLE_ALREADY_READ_PREFIX + ":" + articleId + ":" + ip);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
