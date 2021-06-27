package com.panda.fs.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploadServiceImpl implements com.panda.fs.service.UploadService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public String uploadFile(MultipartFile multipartFile, String fileExtName) throws Exception {
        StorePath storePath = fastFileStorageClient.uploadFile(
                multipartFile.getInputStream(),
                multipartFile.getSize(),
                fileExtName,
                null
        );
        return storePath.getFullPath();
    }
}
