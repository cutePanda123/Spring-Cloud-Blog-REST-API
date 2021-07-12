package com.panda.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.panda.api.service.BaseService;
import com.panda.article.mapper.ArticleMapper;
import com.panda.article.service.ArticlePortalService;
import com.panda.enums.ArticleReviewStatus;
import com.panda.enums.YesNoType;
import com.panda.pojo.Article;
import com.panda.utils.PaginationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PaginationResult listArticles(String keyword, Integer category, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("publishTime").desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isAppoint", YesNoType.no.type);
        criteria.andEqualTo("isDelete", YesNoType.no.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.success.type);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }
        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);

        return paginationResultBuilder(articles, page);
    }
}
