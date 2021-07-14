package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.FansControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.user.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FansController extends BaseController implements FansControllerApi {
    @Autowired
    private FansService fansService;

    @Override
    public ResponseResult isFollowing(String writerId, String userId) {
        return ResponseResult.ok(fansService.isFollowing(writerId, userId));
    }
}
