package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.FansControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.user.service.FansService;
import org.apache.commons.lang3.StringUtils;
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


    @Override
    public ResponseResult follow(String writerId, String userId) {
        fansService.follow(writerId, userId);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult unfollow(String writerId, String userId) {
        fansService.unfollow(writerId, userId);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listFans(String writerId, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(writerId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FAILED);
        }
        return ResponseResult.ok(fansService.listFans(writerId, page, pageSize));
    }
}
