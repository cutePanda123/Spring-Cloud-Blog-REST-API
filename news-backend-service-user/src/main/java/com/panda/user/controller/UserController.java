package com.panda.user.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.UserControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AppUser;
import com.panda.pojo.bo.UpdateUserInfoBO;
import com.panda.pojo.vo.AppUserVo;
import com.panda.pojo.vo.UserAccountInfoVo;
import com.panda.user.service.FansService;
import com.panda.user.service.UserService;
import com.panda.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController extends BaseController implements UserControllerApi {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FansService fansService;

    @Override
    public ResponseResult getAccountInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser user = getUser(userId);
        UserAccountInfoVo userAccountInfoVo = new UserAccountInfoVo();
        BeanUtils.copyProperties(user, userAccountInfoVo);
        return ResponseResult.ok(userAccountInfoVo);
    }

//    @Override
//    public ResponseResult updateUserInfoV2(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
//        if (result.hasErrors()) {
//            return ResponseResult.errorMap(getErrors(result));
//        }
//
//        userService.updateUserInfo(updateUserInfoBO);
//        return ResponseResult.ok();
//    }

    @Override
    public ResponseResult updateUserInfoV2(@Valid UpdateUserInfoBO updateUserInfoBO) {
        userService.updateUserInfo(updateUserInfoBO);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult getUserInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser user = getUser(userId);
        AppUserVo appUserVo = new AppUserVo();
        BeanUtils.copyProperties(user, appUserVo);
        appUserVo.setFollowingCount(fansService.getFollowingCount(userId));
        appUserVo.setFansCount(fansService.getFansCount(userId));
        return ResponseResult.ok(appUserVo);
    }

    @HystrixCommand(fallbackMethod = "listUsersFallback")
    @Override
    public ResponseResult listUsers(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        int a = 1 / 0;
        List<AppUserVo> userVos = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        userIdList.forEach(userId -> {
            AppUser user = getUser(userId);
            AppUserVo userVo = new AppUserVo();
            BeanUtils.copyProperties(user, userVo);
            userVos.add(userVo);
        });

        return ResponseResult.ok(userVos);
    }

    public ResponseResult listUsersFallback(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        List<AppUserVo> userVos = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        userIdList.forEach(userId -> {
            AppUserVo userVo = new AppUserVo();
            userVos.add(userVo);
        });

        return ResponseResult.ok(userVos);
    }

    private AppUser getUser(String userId) {
        String redisKey = REDIS_USER_INFO_PREFIX + ":" + userId;
        String userJsonStr = redisAdaptor.get(redisKey);
        AppUser user = null;
        if (StringUtils.isNotBlank(userJsonStr)) {
            user = JsonUtils.jsonToPojo(userJsonStr, AppUser.class);
        } else {
            user = userService.getUser(userId);
            redisAdaptor.set(redisKey, JsonUtils.objectToJson(user));
        }
        return user;
    }
}
