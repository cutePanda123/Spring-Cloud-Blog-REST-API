package com.panda.user.service;

import com.panda.pojo.AppUser;

public interface UserService {
    public AppUser queryMobileIsExist(String mobile);

    public AppUser createUser(String mobile);

    public AppUser getUser(String userId);
}
