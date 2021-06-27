package com.panda.fs.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    public String uploadFile(MultipartFile multipartFile, String fileExtName) throws Exception;
}
