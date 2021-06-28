package com.panda.admin.service;

import com.panda.pojo.AdminUser;

public interface AdminUserService {
    public AdminUser queryAdminUserByUsername(String username);
}
