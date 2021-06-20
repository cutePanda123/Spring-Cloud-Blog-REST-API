package com.panda.user.service;

import com.panda.pojo.AppUser;
import com.panda.pojo.bo.UpdateUserInfoBO;

public interface UserService {
    public AppUser queryMobileIsExist(String mobile);

    public AppUser createUser(String mobile);

    public AppUser getUser(String userId);

    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
