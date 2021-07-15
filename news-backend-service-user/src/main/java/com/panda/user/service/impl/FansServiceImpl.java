package com.panda.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.panda.api.service.BaseService;
import com.panda.enums.Gender;
import com.panda.pojo.AppUser;
import com.panda.pojo.Fans;
import com.panda.user.mapper.FansMapper;
import com.panda.user.service.FansService;
import com.panda.user.service.UserService;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String userId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(userId);
        fansMapper.delete(fans);
        redisAdaptor.decrement(REDIS_USER_FOLLOW_COUNTS_PREFIX + ":" + userId, 1);
        redisAdaptor.decrement(REDIS_WRITER_FANS_COUNTS_PREFIX + ":" + writerId, 1);
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
    public Integer getFansCountByGender(String writerId, Gender gender) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(gender.type);
        return fansMapper.selectCount(fans);
    }
}
