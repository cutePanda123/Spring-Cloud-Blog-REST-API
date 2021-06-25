package com.panda.user.service.impl;

import com.panda.enums.Gender;
import com.panda.enums.UserAccountStatus;
import com.panda.exception.CustomException;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AppUser;
import com.panda.pojo.bo.UpdateUserInfoBO;
import com.panda.user.mapper.AppUserMapper;
import com.panda.user.service.UserService;
import com.panda.utils.DateUtils;
import com.panda.utils.DesensitizationUtils;
import com.panda.utils.JsonUtils;
import com.panda.utils.RedisAdaptor;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private RedisAdaptor redisAdaptor;

    private static final String[] USER_AVATARS = new String[]
    {
        "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png",
            "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png",
            "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png"
    };

    private static final String REDIS_USER_INFO_PREFIX = "redis_user_info";

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

    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String userId = updateUserInfoBO.getId();
        String redisKey = REDIS_USER_INFO_PREFIX + ":" + userId;
        // delete cache to make database&redis consistent even if redis write fails
        redisAdaptor.del(redisKey);
        AppUser userInfo = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO, userInfo);
        userInfo.setUpdatedTime(new Date());
        userInfo.setActiveStatus(UserAccountStatus.ACTIVE.type);
        int res = appUserMapper.updateByPrimaryKeySelective(userInfo);
        if (res != 1) {
            EncapsulatedException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }

        // save new user info to redis cache
        AppUser user = getUser(userId);
        redisAdaptor.set(redisKey, JsonUtils.objectToJson(user));

        // delete cache to make database&redis consistent even if there are user read
        // requests between previous cache delete and database write
        // a future optimization could be listening to database update and trigger event
        // to sync database data to redis, canal can be a option
        try {
            Thread.sleep(100);
            redisAdaptor.del(redisKey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
