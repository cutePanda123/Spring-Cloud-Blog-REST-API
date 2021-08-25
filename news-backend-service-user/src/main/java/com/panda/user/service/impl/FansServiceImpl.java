package com.panda.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.panda.api.service.BaseService;
import com.panda.enums.Gender;
import com.panda.pojo.AppUser;
import com.panda.pojo.Fans;
import com.panda.pojo.eo.FansEo;
import com.panda.pojo.vo.FansRegionsCountsVo;
import com.panda.user.mapper.FansMapper;
import com.panda.user.service.FansService;
import com.panda.user.service.UserService;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class FansServiceImpl extends BaseService implements FansService {
    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private Sid sid;

    @Autowired
    private RedisAdaptor redisAdaptor;

    @Autowired
    RestHighLevelClient elasticsearchClient;

    private static final String ELASTICSEARCH_FANS_INDEX_NAME = "fans";
    private static final String ELASTICSEARCH_FANS_INDEX_TYPE = "_doc";

    public static final String[] regions = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Override
    public boolean isFollowing(String writerId, String userId) {
        Fans fans = new Fans();
        fans.setFanId(userId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);
        return count > 0;
    }

    @Transactional
    @Override
    public void follow(String writerId, String userId) {
        AppUser user = userService.getUser(userId);
        String fanPkId = sid.nextShort();
        Fans fans = new Fans();
        fans.setId(fanPkId);
        fans.setWriterId(writerId);
        fans.setFanId(userId);
        fans.setFace(user.getFace());
        fans.setFanNickname(user.getNickname());
        fans.setSex(user.getSex());
        fans.setProvince(user.getProvince());

        fansMapper.insert(fans);
        redisAdaptor.increment(REDIS_USER_FOLLOW_COUNTS_PREFIX + ":" + userId, 1);
        redisAdaptor.increment(REDIS_WRITER_FANS_COUNTS_PREFIX + ":" + writerId, 1);

        // save fans follow information to elasticsearch
        FansEo fansEo = new FansEo();
        BeanUtils.copyProperties(fans, fansEo);
        IndexRequest request = new IndexRequest(ELASTICSEARCH_FANS_INDEX_NAME);
        String docId = writerId + "," + userId;
        request.id(docId);

        try {
            request.source(new ObjectMapper().writeValueAsString(fans), XContentType.JSON);
            elasticsearchClient.index(request, RequestOptions.DEFAULT);
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String userId) throws IOException {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(userId);
        fansMapper.delete(fans);
        redisAdaptor.decrement(REDIS_USER_FOLLOW_COUNTS_PREFIX + ":" + userId, 1);
        redisAdaptor.decrement(REDIS_WRITER_FANS_COUNTS_PREFIX + ":" + writerId, 1);

        // delete fans follow information from elasticsearch
        String docId = writerId + "," + userId;
        DeleteRequest deleteRequest = new DeleteRequest("fans", ELASTICSEARCH_FANS_INDEX_TYPE, docId);
        elasticsearchClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @Override
    public int getFollowingCount(String userId) {
        String count = redisAdaptor.get(REDIS_USER_FOLLOW_COUNTS_PREFIX + ":" + userId);
        return count == null ? 0 : Integer.valueOf(count);
    }

    @Override
    public int getFansCount(String userId) {
        String count = redisAdaptor.get(REDIS_WRITER_FANS_COUNTS_PREFIX + ":" + userId);
        return count == null ? 0 : Integer.valueOf(count);
    }

    @Override
    public PaginationResult listFans(String writerId, Integer page, Integer pageSize) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            page = DEFAULT_PAGE_SIZE;
        }
        PageHelper.startPage(page, pageSize);
        List<Fans> fansList = fansMapper.select(fans);
        return paginationResultBuilder(fansList, page);
    }

    @Override
    public PaginationResult listFansV2(String writerId, Integer page, Integer pageSize) {
        // elasticsearch starts from 0
        if (page < 1) {
            return null;
        }
        --page;
        SearchRequest searchRequest = new SearchRequest(ELASTICSEARCH_FANS_INDEX_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(page * pageSize);
        sourceBuilder.size(pageSize);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.query(QueryBuilders.termQuery("writerId", writerId));

        SearchResponse searchResponse = null;
        try {
            searchRequest.source(sourceBuilder);
            searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RestStatus status = searchResponse.status();

        List<FansEo> fansList = new LinkedList<>();
        if (status != RestStatus.OK) {
            return paginationResultBuilder(fansList, page + 1);
        }

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            FansEo fansEo = new ObjectMapper().convertValue(sourceAsMap, FansEo.class);
            fansList.add(fansEo);
        }

        return paginationResultBuilder(fansList, page + 1);
    }

    @Override
    public Integer getFansCountByGender(String writerId, Gender gender) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(gender.type);
        return fansMapper.selectCount(fans);
    }

    @Override
    public Integer getFansCountByGenderV2(String userId, Gender gender) {
        CountRequest countRequest = new CountRequest(ELASTICSEARCH_FANS_INDEX_NAME);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        MatchPhraseQueryBuilder writerIdQuery = QueryBuilders
                .matchPhraseQuery("writerId", userId);
        MatchPhraseQueryBuilder genderQuery = QueryBuilders
                .matchPhraseQuery("sex", String.valueOf(gender.type));
        boolQueryBuilder.must(writerIdQuery).must(genderQuery);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = null;
        try {
            countResponse = elasticsearchClient.count(countRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (countResponse.status() != RestStatus.OK) {
            return 0;
        }
        return (int)countResponse.getCount();
    }

    @Override
    public List<FansRegionsCountsVo> getFansCountByRegion(String writerId) {
        List<FansRegionsCountsVo> fansRegionsCountsVos = new LinkedList<>();
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        for (String region : regions) {
            fans.setProvince(region);
            Integer count = fansMapper.selectCount(fans);
            FansRegionsCountsVo vo = new FansRegionsCountsVo();
            vo.setName(region);
            vo.setValue(count);
            fansRegionsCountsVos.add(vo);
        }
        return fansRegionsCountsVos;
    }

    @Override
    public List<FansRegionsCountsVo> getFansCountByRegionV2(String writerId) {
        return null;
    }

    @Override
    public void syncFansInfo(String relationshipId, String fansId) {
        AppUser user = userService.getUser(fansId);
        Fans fans = new Fans();
        fans.setId(relationshipId);
        fans.setFanNickname((user.getNickname()));
        fans.setSex(user.getSex());
        fans.setProvince(user.getProvince());
        fans.setFace(user.getFace());

        fansMapper.updateByPrimaryKeySelective(fans);
        fans = fansMapper.selectByPrimaryKey(relationshipId);
        String docId = fans.getWriterId() + "," + fansId;
        UpdateRequest request = new UpdateRequest("fans", docId)
                .doc("face", user.getFace(), "fanNickname",user.getNickname(),
                        "sex", user.getSex(), "province", user.getProvince());
        try {
            UpdateResponse updateResponse = elasticsearchClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
