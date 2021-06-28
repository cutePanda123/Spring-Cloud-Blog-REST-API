package com.panda.fs.controller;

import com.panda.api.controller.fs.FileUploadControllerApi;
import com.panda.fs.service.UploadService;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController implements FileUploadControllerApi {
    final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    UploadService uploadService;
    
    @Override
    public ResponseResult uploadAvatar(String userId, MultipartFile file) throws Exception {
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String suffix = fileNames[fileNames.length - 1];
        if (!suffix.equalsIgnoreCase("png") &&
                !suffix.equalsIgnoreCase("jpg") &&
                !suffix.equalsIgnoreCase("jpeg")
        ) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }
        String path = uploadService.uploadFile(file, suffix);
        logger.info("path: " + path);
        return ResponseResult.ok(path);
    }
}
