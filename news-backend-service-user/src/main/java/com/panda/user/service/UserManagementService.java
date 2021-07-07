package com.panda.user.service;

import com.panda.utils.PaginationResult;
import java.util.Date;

public interface UserManagementService {
    public PaginationResult listUsers(
            String nickname,
            Integer status,
            Date startDate,
            Date endDate,
            Integer page,
            Integer pageSize);
    public void setAccountStatus(String userId, Integer status);
}
