package com.panda.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.utils.IPUtils;
import com.panda.utils.JsonUtils;
import com.panda.utils.RedisAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

@Component
public class IpFilter extends ZuulFilter {
    @Value("${ipBlacklist.maxRequestCount}")
    private Integer maxRequestCount;
    @Value("${ipBlacklist.interval}")
    private Integer interval;
    @Value("${ipBlacklist.frozenDuration}")
    private Integer frozenDuration;

    private final static String ipFilterIpRedisKeyPrefix = "zuul-ip-filter-ip:";
    private final static String ipFilterFrozenDurationRedisKeyPrefix = "zuul-ip-filter-frozen-duration:";

    @Autowired
    RedisAdaptor redisAdaptor;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String ip = IPUtils.getRequestIp(request);
        String frozenDurationRedisKey = IpFilter.ipFilterFrozenDurationRedisKeyPrefix + ip;
        String ipRedisKey = IpFilter.ipFilterIpRedisKeyPrefix + ip;
        long existingFrozenDuration = redisAdaptor.ttl(frozenDurationRedisKey);
        if (existingFrozenDuration > 0) {
            blockRequestIp(context);
            return ResponseResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_ZUUL);
        }
        long requestCount = redisAdaptor.increment(ipRedisKey, 1);
        if (requestCount == 1) {
            redisAdaptor.expire(ipRedisKey, interval);
        }
        if (requestCount > maxRequestCount) {
            redisAdaptor.set(frozenDurationRedisKey, frozenDurationRedisKey, frozenDuration);
            blockRequestIp(context);
            return ResponseResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_ZUUL);
        }
        return null;
    }

    private void blockRequestIp(RequestContext context) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(500);
        String result = JsonUtils.objectToJson(
                ResponseResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_ZUUL)
        );
        context.setResponseBody(result);
        context.getResponse().setCharacterEncoding("utf-8");
        context.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
