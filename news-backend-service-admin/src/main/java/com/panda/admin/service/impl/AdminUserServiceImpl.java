package com.panda.admin.service.impl;

import com.panda.admin.mapper.AdminUserMapper;
import com.panda.admin.service.AdminUserService;
import com.panda.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser queryAdminUserByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username", username);

        AdminUser adminUser = adminUserMapper.selectOneByExample(adminExample);

        return adminUser;
    }
}
