package com.panda.api.interceptors;

import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.utils.RedisAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseInterceptor {
    @Autowired
    private RedisAdaptor redisAdaptor;

    public static final String REDIS_USER_TOKEN_PREFIX = "redis_user_token";
    public boolean verifyUserIdToken(
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
}
