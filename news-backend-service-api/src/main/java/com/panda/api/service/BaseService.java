package com.panda.api.service;

import com.github.pagehelper.PageInfo;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.vo.AppUserVo;
import com.panda.utils.JsonUtils;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BaseService {
    protected static final String REDIS_ALL_CATEGORY_KEY = "redis_all_category";
    protected static final String REDIS_WRITER_FANS_COUNTS_PREFIX = "redis_fans_counts";
    protected static final String REDIS_USER_FOLLOW_COUNTS_PREFIX = "redis_user_follow_counts";
    protected static final String REDIS_ARTICLE_READ_COUNTS_PREFIX = "redis_article_read_counts";
    protected static final String REDIS_COMMENT_COUNTS_PREFIX = "redis_comment_counts";
    protected static final Integer DEFAULT_START_PAGE = 1;
    protected static final Integer DEFAULT_PAGE_SIZE = 10;
    private final static String USER_SERVICE_NAME = "SERVICE-USER";

    @Autowired
    protected RedisAdaptor redisAdaptor;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected DiscoveryClient discoveryClient;

    protected PaginationResult paginationResultBuilder(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PaginationResult result = new PaginationResult();
        result.setPage(page);
        result.setRows(list);
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        return  result;
    }

    protected List<AppUserVo> listPublishers(Set<String> publisherIds) {
        RequestEntity<Void> requestEntity = null;
        try {
            requestEntity =
                    RequestEntity.
                            get(new URI(getUserServiceListUserApiUrlWithLoadBalancer())).
                            accept(MediaType.APPLICATION_JSON).
                            header("userIds", JsonUtils.objectToJson(publisherIds)).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ResponseEntity<ResponseResult> responseEntity = restTemplate.exchange(requestEntity, ResponseResult.class);
        List<AppUserVo> userVoList = new LinkedList<>();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String users = JsonUtils.objectToJson(responseEntity.getBody().getData());
            userVoList = JsonUtils.jsonToList(users, AppUserVo.class);
        }
        return userVoList;
    }

    protected String queryUserServiceUrl() {
        List<ServiceInstance> instances = discoveryClient.getInstances(USER_SERVICE_NAME);
        if (instances.isEmpty()) {
            EncapsulatedException.display(ResponseStatusEnum.SERVICE_NOT_AVAILABLE);
        }
        ServiceInstance instance = instances.get(0);
        return "http://" + instance.getHost() + ":" + instance.getPort();
    }

    protected String queryUserServiceUrlWithLoadBalancer() {
        return "http://" + USER_SERVICE_NAME;
    }

    protected String getUserServiceListUserApiUrl() {
        return queryUserServiceUrl() + "/api/service-user/user/list";
    }
    protected String getUserServiceListUserApiUrlWithLoadBalancer() {
        return queryUserServiceUrlWithLoadBalancer() + "/api/service-user/user/list";
    }
}
