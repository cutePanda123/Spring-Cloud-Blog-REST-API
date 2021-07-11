package com.panda.api.controller.admin;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.RelatedLinkBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "related website links management", tags = {"related website links management"})
@RequestMapping("api/service-admin/relatedLinkMng")
public interface RelatedWebsiteLinkControllerApi {
    @ApiOperation(value = "create or update related website links", notes = "create or update related website links", httpMethod = "POST")
    @PostMapping("/saveOrUpdateRelatedLink")
    public ResponseResult saveOrUpdateRelatedLink(
            @RequestBody @Valid RelatedLinkBo bo,
            BindingResult result);

    @ApiOperation(value = "list related website links", notes = "list related website links", httpMethod = "POST")
    @PostMapping("/listRelatedLinks")
    public ResponseResult listRelatedLinks();

    @ApiOperation(value = "delete link", notes = "delete link", httpMethod = "POST")
    @PostMapping("/delete")
    public ResponseResult deleteLink(@RequestParam String linkId);

    @ApiOperation(value = "list related website links for user", notes = "list related website links for user", httpMethod = "GET")
    @GetMapping("user/list")
    public ResponseResult listAliveLinks();
}
