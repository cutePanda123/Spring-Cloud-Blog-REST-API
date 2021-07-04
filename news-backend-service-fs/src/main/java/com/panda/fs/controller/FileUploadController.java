package com.panda.fs.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.panda.api.controller.fs.FileUploadControllerApi;
import com.panda.fs.resources.FileResource;
import com.panda.fs.service.UploadService;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.bo.NewAdminBO;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;


import java.io.ByteArrayInputStream;

@RestController
public class FileUploadController implements FileUploadControllerApi {
    final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    UploadService uploadService;

    @Autowired
    FileResource fileResource;

    @Autowired
    GridFSBucket gridFSBucket;

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
        if (StringUtils.isBlank(path)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String finalPath = fileResource.getHost() + path;
        return ResponseResult.ok(finalPath);
    }

    @Override
    public ResponseResult uploadAvatar(NewAdminBO bo) throws Exception {
        String file64 = bo.getImg64();
        byte[] bytes = new BASE64Decoder().decodeBuffer(file64);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectId fileId = gridFSBucket.uploadFromStream(bo.getUsername() +".png", inputStream);
        String fileIdStr = fileId.toHexString();
        return ResponseResult.ok(fileIdStr);
        //return ResponseResult.ok();
    }
}
