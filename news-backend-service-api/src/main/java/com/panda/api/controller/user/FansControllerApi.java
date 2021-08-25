package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

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
    ) throws IOException;

    @ApiOperation(
            value = "a user unfollows a writer",
            notes = "a user unfollows a writer",
            httpMethod = "POST"
    )
    @PostMapping("/list")
    public ResponseResult listFans(
            @RequestParam String writerId,
            @ApiParam(name = "page", value = "page number", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "size per page", required = false)
            @RequestParam Integer pageSize);

    @PostMapping("/count/genders")
    public ResponseResult countFansGenders(@RequestParam String writerId);

    @PostMapping("/count/regions")
    public ResponseResult countFansRegions(@RequestParam String writerId);

    @ApiOperation(
            value = "update fans information in elasticsearch",
            notes = "update fans information in elasticsearch",
            httpMethod = "POST"
    )
    @PostMapping("/sync")
    public ResponseResult syncFansInfo(
            @RequestParam String relationshipId,
            @RequestParam String fansId
    );
}
