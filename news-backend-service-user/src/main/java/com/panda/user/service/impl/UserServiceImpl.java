package com.panda.user.service.impl;

import com.panda.enums.Gender;
import com.panda.enums.UserAccountStatus;
import com.panda.pojo.AppUser;
import com.panda.user.mapper.AppUserMapper;
import com.panda.user.service.UserService;
import com.panda.utils.DateUtils;
import com.panda.utils.DesensitizationUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private Sid sid;

    @Autowired
    private AppUserMapper appUserMapper;

    private static final String[] USER_AVATARS = new String[]
    {
        "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png",
            "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png",
            "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png"
    };

    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example example = new Example(AppUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile", mobile);
        AppUser user = appUserMapper.selectOneByExample(example);
        return user;
    }

    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        String userId = sid.nextShort();
        AppUser user = new AppUser();
        user.setId(userId);
        user.setMobile(mobile);
        user.setNickname("User: " + DesensitizationUtils.commonDisplay(mobile));
        int idx = new Random().nextInt(3);
        user.setFace(USER_AVATARS[idx]);
        user.setBirthday(DateUtils.stringToDate("2021-01-15"));
        user.setSex(Gender.unknown.type);
        user.setActiveStatus(UserAccountStatus.INACTIVE.type);

        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        appUserMapper.insert(user);
        return user;
    }
}
