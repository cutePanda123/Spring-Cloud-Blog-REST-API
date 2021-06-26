package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.PassportControllerApi;
import com.panda.enums.UserAccountStatus;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AppUser;
import com.panda.pojo.bo.RegisterLoginBO;
import com.panda.user.service.UserService;
import com.panda.utils.IPUtils;
import com.panda.utils.JsonUtils;
import com.panda.utils.SMSUtils;
import com.panda.utils.extend.AzureResource;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {
    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    SMSUtils smsUtils;

    @Autowired
    AzureResource azureResource;

    @Autowired
    UserService userService;

    @Override
    public ResponseResult getSMSCode(@RequestParam String mobile, HttpServletRequest request) {
        String userIp = IPUtils.getRequestIp(request);
        redisAdaptor.setnx60s(MOBILE_SMSCODE_PREFIX + ":" + userIp, userIp);

        int code = new Random().nextInt(100000) + 100000;
        smsUtils.sendSmsMock(mobile, String.valueOf(code));
        redisAdaptor.set(MOBILE_SMSCODE_PREFIX + ":" + mobile, String.valueOf(code), 30 * 60);

        return ResponseResult.ok();
    }

    @Override
    public ResponseResult doLogin(
            @Valid RegisterLoginBO registerLoginBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (result.hasErrors()) {
            return ResponseResult.errorMap(getErrors(result));
        }

        String mobile = registerLoginBO.getMobile();
        String smsCode = registerLoginBO.getSmsCode();
        String redisSmsCode = redisAdaptor.get(MOBILE_SMSCODE_PREFIX + ":" + mobile);
        if (StringUtils.isBlank(redisSmsCode) || !redisSmsCode.equals(smsCode)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        AppUser user = userService.queryMobileIsExist(mobile);
        if (user != null && user.getActiveStatus() == UserAccountStatus.FROZEN.type) {
            return ResponseResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        } else if (user == null) {
            user = userService.createUser(mobile);
        }
        int userActiveStatus = user.getActiveStatus();
        if (userActiveStatus != UserAccountStatus.FROZEN.type) {
            String token = UUID.randomUUID().toString();
            redisAdaptor.set(REDIS_USER_TOKEN_PREFIX + ":" + user.getId(), token);
            redisAdaptor.set(REDIS_USER_INFO_PREFIX + ":" + user.getId(), JsonUtils.objectToJson(user));

            setCookie(request, response, "utoken", token, COOKIE_DURATION);
            setCookie(request, response, "uid", user.getId(), COOKIE_DURATION);
        }

        redisAdaptor.del(MOBILE_SMSCODE_PREFIX + ":" + mobile);

        return ResponseResult.ok(userActiveStatus);
    }

    @Override
    public ResponseResult logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        redisAdaptor.del(REDIS_USER_TOKEN_PREFIX + ":" + userId);
        setCookie(request, response, "utoken", "", EXPIRED_COOKIE_DURATION);
        setCookie(request, response, "uid", "", EXPIRED_COOKIE_DURATION);
        return ResponseResult.ok();
    }
}
