package com.panda.user.controller;

import com.panda.api.controller.user.PassportControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.utils.SMSUtils;
import com.panda.utils.extend.AzureResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassportController implements PassportControllerApi {
    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    SMSUtils smsUtils;

    @Autowired
    AzureResource azureResource;

    @Override
    public ResponseResult getSMSCode() {
        String random = "123456";
        smsUtils.sendSmsMock("+12345678", random);

        return ResponseResult.ok();
    }
}
