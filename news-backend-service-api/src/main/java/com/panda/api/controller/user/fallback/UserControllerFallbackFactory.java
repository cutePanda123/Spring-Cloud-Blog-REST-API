package com.panda.api.controller.user.fallback;

import com.panda.api.controller.user.UserControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.UpdateUserInfoBO;
import com.panda.pojo.vo.AppUserVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserControllerFallbackFactory implements FallbackFactory<UserControllerApi> {
    @Override
    public UserControllerApi create(Throwable throwable) {
        return new UserControllerApi() {
            @Override
            public ResponseResult getAccountInfo(String userId) {
                return null;
            }

            @Override
            public ResponseResult updateUserInfoV2(@Valid UpdateUserInfoBO updateUserInfoBO) {
                return null;
            }

            @Override
            public ResponseResult getUserInfo(String userId) {
                return null;
            }

            @Override
            public ResponseResult listUsers(String userIds) {
                List<AppUserVo> users = new ArrayList<>();
                return ResponseResult.ok(users);
            }
        };
    }
}
