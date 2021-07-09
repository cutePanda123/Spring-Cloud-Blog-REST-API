package com.panda.api.controller.fs;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "File upload controller", tags = {"files"})
@RequestMapping("/api/service-files/fs")
public interface FileUploadControllerApi {
    @ApiOperation(value = "upload user avatar api", notes = "upload user avatar api", httpMethod = "POST")
    @PostMapping("/uploadAvatar")
    public ResponseResult uploadAvatar(
            @RequestParam String userId,
            MultipartFile file
    ) throws Exception;

    @ApiOperation(value = "upload multiple files api", notes = "upload multiple files api", httpMethod = "POST")
    @PostMapping("/uploadFiles")
    public ResponseResult uploadFiles(
            @RequestParam String userId,
            MultipartFile[] files
    ) throws Exception;

    @PostMapping("/uploadToGridFS")
    public ResponseResult uploadToGridFS(@RequestBody NewAdminBO bo) throws Exception;

    @GetMapping("/readInGridFS")
    public void readFromGridFS(String faceId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception;

    @GetMapping("/readFace64InGridFS")
    public ResponseResult readFace64InGridFS(String faceId,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception;
}
