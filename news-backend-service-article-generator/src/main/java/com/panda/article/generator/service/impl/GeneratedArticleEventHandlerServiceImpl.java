package com.panda.article.generator.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.panda.article.generator.service.GeneratedArticleEventHandlerService;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class GeneratedArticleEventHandlerServiceImpl implements GeneratedArticleEventHandlerService {

    @Autowired
    private GridFSBucket gridFSBucket;

    @Value("${freemarker.html.article}")
    private String freemarkerGeneratedFileDirectory;

    @Override
    public void downloadGeneratedArticle(String articleId, String gridFsId) {
        String filePath = freemarkerGeneratedFileDirectory + File.separator + articleId + ".html";
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            EncapsulatedException.display(ResponseStatusEnum.FILE_CREATE_FAILD);
        }
        gridFSBucket.downloadToStream(new ObjectId(gridFsId), outputStream);
    }
}
