package com.panda.user.service.impl;

import com.panda.api.service.BaseService;
import com.panda.pojo.Fans;
import com.panda.user.mapper.FansMapper;
import com.panda.user.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FansServiceImpl extends BaseService implements FansService {
    @Autowired
    private FansMapper fansMapper;

    @Override
    public boolean isFollowing(String writerId, String userId) {
        Fans fans = new Fans();
        fans.setFanId(userId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);
        return count > 0;
    }
}
