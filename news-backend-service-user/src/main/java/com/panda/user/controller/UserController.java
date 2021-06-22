package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.UserControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AppUser;
import com.panda.pojo.bo.UpdateUserInfoBO;
import com.panda.pojo.vo.AppUserVo;
import com.panda.pojo.vo.UserAccountInfoVo;
import com.panda.user.service.UserService;
import com.panda.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController extends BaseController implements UserControllerApi {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

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

    @Override
    public ResponseResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseResult.errorMap(getErrors(result));
        }

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
        return ResponseResult.ok(appUserVo);
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
