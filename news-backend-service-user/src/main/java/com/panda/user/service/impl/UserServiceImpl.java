package com.panda.user.service.impl;

import com.panda.pojo.AppUser;
import com.panda.user.mapper.AppUserMapper;
import com.panda.user.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    Sid sid;

    @Autowired
    public AppUserMapper appUserMapper;

    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example example = new Example(AppUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(mobile, mobile);
        AppUser user = appUserMapper.selectOneByExample(example);
        return user;
    }

    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        String userId = sid.nextShort();
        AppUser user = new AppUser();
        user.setId(userId);
        return null;
    }
}
