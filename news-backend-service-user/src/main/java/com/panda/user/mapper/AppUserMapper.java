package com.panda.user.mapper;

import com.panda.my.mapper.MyMapper;
import com.panda.pojo.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}