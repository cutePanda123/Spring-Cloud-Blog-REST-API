package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "fans controller", tags = {"fans controller"})
@RequestMapping("/api/service-user/fans")
public interface FansControllerApi {
    @ApiOperation(
            value = "check if a user is following a writer",
            notes = "check if a user is following a writer",
            httpMethod = "POST"
    )
    @PostMapping("/checkfollowing")
    public ResponseResult isFollowing(
            @RequestParam String writerId,
            @RequestParam String userId
    );

    @ApiOperation(
            value = "a user follows a writer",
            notes = "a user follows a writer",
            httpMethod = "POST"
    )
    @PostMapping("/follow")
    public ResponseResult follow(
            @RequestParam String writerId,
            @RequestParam String userId
    );

    @ApiOperation(
            value = "a user unfollows a writer",
            notes = "a user unfollows a writer",
            httpMethod = "POST"
    )
    @PostMapping("/unfollow")
    public ResponseResult unfollow(
            @RequestParam String writerId,
            @RequestParam String userId
    );
}
