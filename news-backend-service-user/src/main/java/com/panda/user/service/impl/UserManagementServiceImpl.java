package com.panda.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.panda.api.service.BaseService;
import com.panda.enums.UserAccountStatus;
import com.panda.pojo.AppUser;
import com.panda.user.mapper.AppUserMapper;
import com.panda.user.service.UserManagementService;
import com.panda.utils.PaginationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserManagementServiceImpl extends BaseService implements UserManagementService {
    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public PaginationResult listUsers(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        if (UserAccountStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("activeStatus", status);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (startDate != null) {
            criteria.andLessThanOrEqualTo("createdTime", endDate);
        }
        PageHelper.startPage(page, pageSize);
        List<AppUser> list = appUserMapper.selectByExample(example);
        return paginationResultBuilder(list, page);
    }

    @Override
    public void setAccountStatus(String userId, Integer status) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(status);
        appUserMapper.updateByPrimaryKeySelective(user);
    }
}
