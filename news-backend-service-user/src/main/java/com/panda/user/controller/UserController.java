package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.UserControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AppUser;
import com.panda.pojo.bo.UpdateUserInfoBO;
import com.panda.pojo.vo.UserAccountInfoVo;
import com.panda.user.service.UserService;
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


    private AppUser getUser(String userId) {
        AppUser user = userService.getUser(userId);
        return user;
    }
}
