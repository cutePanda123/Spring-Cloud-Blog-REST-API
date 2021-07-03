package com.panda.admin.service;

import com.panda.pojo.AdminUser;
import com.panda.pojo.bo.NewAdminBO;
import com.panda.utils.PaginationResult;

public interface AdminUserService {
    public AdminUser queryAdminUserByUsername(String username);

    public void createAdminUser(NewAdminBO newAdminBO);

    public PaginationResult listAdminUsers(Integer page, Integer pageSize);
}
