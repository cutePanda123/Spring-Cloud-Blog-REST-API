package com.panda.article.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.panda.api.controller.BaseController;
import com.panda.api.controller.article.ArticleControllerApi;
import com.panda.article.service.ArticleService;
import com.panda.enums.ArticleCoverType;
import com.panda.enums.ArticleReviewStatus;
import com.panda.enums.YesNoType;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateArticleBo;
import com.panda.pojo.vo.ArticleDetailVo;
import com.panda.utils.JsonUtils;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Slf4j
@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {
    @Autowired
    private RedisAdaptor redisAdaptor;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    private final static String articleServiceGetArticleDetailApiUrl =
            "http://www.news.com:8007/api/service-article/article/portal/get?articleId=";

    private final static String freemarkerArticleObjectName = "articleDetail";

    @Transactional
    @Override
    public ResponseResult createArticle(@Valid CreateArticleBo bo, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseResult.errorMap(getErrors(result));
        }
        if (bo.getArticleType() == ArticleCoverType.image.type && StringUtils.isBlank(bo.getArticleCover())) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
        }
        if (bo.getArticleType() == ArticleCoverType.text.type) {
            bo.setArticleCover("");
        }
        String categories = redisAdaptor.get(REDIS_ALL_CATEGORY_KEY);
        if (StringUtils.isBlank(categories)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        List<Category> categoryList = JsonUtils.jsonToList(categories, Category.class);
        Category category = categoryList
                .stream()
                .filter(item -> item.getId() == bo.getCategoryId())
                .findAny().orElse(null);
        if (category == null) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }
        articleService.createArticle(bo, category);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listForUser(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = articleService.listArticles(userId, keyword, status, startDate, endDate, page, pageSize);
        return ResponseResult.ok(result);
    }

    @Override
    public ResponseResult listForAdmin(Integer status, Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = articleService.listArticlesWithStatus(status, page, pageSize);
        return ResponseResult.ok(result);
    }

    @Transactional
    @Override
    public ResponseResult reviewArticle(String articleId, Integer isPassed) {
        Integer reviewStatus = null;
        if (isPassed == YesNoType.yes.type) {
            reviewStatus = ArticleReviewStatus.success.type;
        } else if (isPassed == YesNoType.no.type) {
            reviewStatus = ArticleReviewStatus.failed.type;
        } else {
            return ResponseResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        articleService.updateArticleReviewStatus(articleId, reviewStatus);
        if (reviewStatus == ArticleReviewStatus.success.type) {
            try {
                String content = generateHtml(articleId);
                String fileId = uploadToGridFs(articleId + ".html", content);
                articleService.saveArticleGridFsFileId(articleId, fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseResult.ok();
    }

    @Transactional
    @Override
    public ResponseResult deleteArticle(String userId, String articleId) {
        articleService.deleteArticle(userId, articleId);
        return ResponseResult.ok();
    }

    @Transactional
    @Override
    public ResponseResult withdrawArticle(String userId, String articleId) {
        articleService.withdrawArticle(userId, articleId);
        return ResponseResult.ok();
    }

    private String generateHtml(String articleId) throws Exception {
        Configuration configuration = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource(File.separator).getPath();
        configuration.setDirectoryForTemplateLoading(new File(classpath + "templates"));
        Template template = configuration.getTemplate("detail.ftl", "utf-8");
        ArticleDetailVo articleDetailVo = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put(freemarkerArticleObjectName, articleDetailVo);

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
    }

    private String uploadToGridFs(String fileName, String content) {
        InputStream inputStream = IOUtils.toInputStream(content);
        return gridFSBucket.uploadFromStream(fileName, inputStream).toString();
    }

    private ArticleDetailVo getArticleDetail(String articleId) {
        ResponseEntity<ResponseResult> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(
                    articleServiceGetArticleDetailApiUrl + articleId,
                    ResponseResult.class
            );
        }catch (Exception e) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }

        ArticleDetailVo vo = null;
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String users = JsonUtils.objectToJson(responseEntity.getBody().getData());
            vo = JsonUtils.jsonToPojo(users, ArticleDetailVo.class);
        }
        return vo;
    }
}
