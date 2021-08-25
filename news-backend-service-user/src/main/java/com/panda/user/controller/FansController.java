package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.FansControllerApi;
import com.panda.enums.Gender;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.vo.FansGendersCountsVo;
import com.panda.user.service.FansService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    public ResponseResult unfollow(String writerId, String userId) throws IOException {
        fansService.unfollow(writerId, userId);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listFans(String writerId, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(writerId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FAILED);
        }
        return ResponseResult.ok(fansService.listFansV2(writerId, page, pageSize));
    }

    @Override
    public ResponseResult countFansGenders(String writerId) {
        Integer maleCount = fansService.getFansCountByGenderV2(writerId, Gender.male);
        Integer femaleCount = fansService.getFansCountByGenderV2(writerId, Gender.female);
        FansGendersCountsVo vo = new FansGendersCountsVo();
        vo.setFemaleCount(femaleCount);
        vo.setMaleCount(maleCount);
        return ResponseResult.ok(vo);
    }

    @Override
    public ResponseResult countFansRegions(String writerId) {
        return ResponseResult.ok(fansService.getFansCountByRegionV2(writerId));
    }

    @Override
    public ResponseResult syncFansInfo(String relationshipId, String fansId) {
        fansService.syncFansInfo(relationshipId, fansId);
        return ResponseResult.ok();
    }
}
