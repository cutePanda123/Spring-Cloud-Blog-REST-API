package com.panda.fs.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.panda.api.controller.fs.FileUploadControllerApi;
import com.panda.exception.EncapsulatedException;
import com.panda.fs.resources.FileResource;
import com.panda.fs.service.UploadService;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.bo.NewAdminBO;
import com.panda.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
    public ResponseResult uploadFiles(String userId, MultipartFile[] files) throws Exception {
        if (files == null || files.length == 0) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        List<String> uploadedFilePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (file == null || StringUtils.isBlank(fileName)) {
                continue;
            }
            String[] fileNameParts = fileName.split("\\.");
            String suffix = fileNameParts[fileNameParts.length - 1];
            if (!suffix.equalsIgnoreCase("png") &&
                    !suffix.equalsIgnoreCase("jpg") &&
                    !suffix.equalsIgnoreCase("jpeg")
            ) {
                continue;
            }
            // TODO: review and audit the uploaded image
            String path = uploadService.uploadFile(file, suffix);
            logger.info("path: " + path);
            if (StringUtils.isBlank(path)) {
                continue;
            }
            String finalPath = fileResource.getHost() + path;
            uploadedFilePaths.add(finalPath);
        }

        return ResponseResult.ok(uploadedFilePaths);
    }

    @Override
    public void readFromGridFS(String faceId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null")) {
            EncapsulatedException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        File faceFile = findFaceFileByFaceId(faceId);

        FileUtils.downloadFileByStream(response, faceFile);
    }

    @Override
    public ResponseResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(faceId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        File faceFile = findFaceFileByFaceId(faceId);
        String base64Str = FileUtils.fileToBase64(faceFile);
        return ResponseResult.ok(base64Str);
    }

    @Override
    public ResponseResult uploadToGridFS(NewAdminBO bo) throws Exception {
        String file64 = bo.getImg64();
        byte[] bytes = Base64.getDecoder().decode(file64);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectId fileId = gridFSBucket.uploadFromStream(bo.getUsername() +".png", inputStream);
        String fileIdStr = fileId.toHexString();
        return ResponseResult.ok(fileIdStr);
    }

    private File findFaceFileByFaceId(String faceId) throws Exception {
        GridFSFindIterable files = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFSFile = files.first();
        if (gridFSFile == null) {
            EncapsulatedException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        String fileName = gridFSFile.getFilename();

        String tempDirPath = "/tmp/temp_faces";
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File file = new File(tempDirPath + "/" + fileName);
        OutputStream os = new FileOutputStream(file);
        gridFSBucket.downloadToStream(new ObjectId(faceId), os);
        return file;
    }
}
