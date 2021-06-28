package com.panda.api.controller.fs;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "File upload controller", tags = {"files"})
@RequestMapping("/api/service-files/fs")
public interface FileUploadControllerApi {
    @ApiOperation(value = "upload user avatar api", notes = "upload user avatar api", httpMethod = "POST")
    @PostMapping("/uploadAvatar")
    public ResponseResult uploadAvatar(
            @RequestParam String userId,
            MultipartFile file
    ) throws Exception;
}
