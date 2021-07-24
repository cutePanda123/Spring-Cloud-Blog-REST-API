package com.panda.article.generator.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.panda.api.controller.article.ArticleGeneratedPageApi;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
@RestController
public class ArticleGeneratedPageController implements ArticleGeneratedPageApi {
    @Autowired
    private GridFSBucket gridFSBucket;

    @Value("${freemarker.html.article}")
    private String freemarkerGeneratedFileDirectory;

    @Override
    public ResponseResult getArticlePage(String articleId, String gridFsId) {
        String filePath = freemarkerGeneratedFileDirectory + File.separator + articleId + ".html";
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            EncapsulatedException.display(ResponseStatusEnum.FILE_CREATE_FAILD);
        }
        gridFSBucket.downloadToStream(new ObjectId(gridFsId), outputStream);
        return ResponseResult.ok();
    }
}
