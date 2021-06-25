package com.panda.api.interceptors;

import com.panda.enums.UserAccountStatus;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AppUser;
import com.panda.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAccountStatusInterceptor extends BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String redisKey = REDIS_USER_INFO_PREFIX + ":" + userId;
        String userJsonStr = redisAdaptor.get(redisKey);
        AppUser user = null;
        if (StringUtils.isNotBlank(userJsonStr)) {
            user = JsonUtils.jsonToPojo(userJsonStr, AppUser.class);
        } else {
            EncapsulatedException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        if (user.getActiveStatus() == null || user.getActiveStatus() != UserAccountStatus.ACTIVE.type) {
            EncapsulatedException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
