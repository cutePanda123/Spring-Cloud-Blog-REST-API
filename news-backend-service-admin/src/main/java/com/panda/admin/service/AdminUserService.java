package com.panda.admin.service;

import com.panda.pojo.AdminUser;
import com.panda.pojo.bo.NewAdminBO;

public interface AdminUserService {
    public AdminUser queryAdminUserByUsername(String username);

    public void createAdminUser(NewAdminBO newAdminBO);
}
