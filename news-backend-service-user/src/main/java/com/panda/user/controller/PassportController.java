package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.PassportControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.bo.RegisterLoginBO;
import com.panda.user.service.UserService;
import com.panda.utils.IPUtils;
import com.panda.utils.SMSUtils;
import com.panda.utils.extend.AzureResource;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseResult getSMSCode(String mobile, HttpServletRequest request) {
        String userIp = IPUtils.getRequestIp(request);
        redisAdaptor.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        String random = String.valueOf((Math.random() * 9 + 1) * 100000);
        smsUtils.sendSmsMock("+12345678", random);
        redisAdaptor.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);

        return ResponseResult.ok();
    }

    @Override
    public ResponseResult doLogin(@Valid RegisterLoginBO registerLoginBO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseResult.errorMap(getErrors(result));
        }

        String mobile = registerLoginBO.getMobile();
        String smsCode = registerLoginBO.getSmsCode();
        String redisSmsCode = redisAdaptor.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisSmsCode) || !redisSmsCode.equals(smsCode)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        return ResponseResult.ok();
    }
}
