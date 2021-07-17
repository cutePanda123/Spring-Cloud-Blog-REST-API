package com.panda.article.service.impl;

import com.panda.api.service.BaseService;
import com.panda.article.mapper.CommentsMapper;
import com.panda.article.service.ArticlePortalService;
import com.panda.article.service.CommentService;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Comments;
import com.panda.pojo.vo.AppUserVo;
import com.panda.pojo.vo.ArticleDetailVo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl extends BaseService implements CommentService {
    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private Sid sid;

    @Autowired
    private CommentsMapper commentsMapper;

    @Transactional
    @Override
    public void createComment(String articleId,
                              String parentCommentId,
                              String content,
                              String userId) {
        Set<String> userIds = new HashSet<>();
        userIds.add(userId);
        List<AppUserVo> users = listPublishers(userIds);
        if (users == null || users.isEmpty()) {
            EncapsulatedException.display(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        AppUserVo userVo = users.get(0);
        ArticleDetailVo articleDetailVo = articlePortalService.getArticle(articleId);
        if (articleDetailVo == null) {
            EncapsulatedException.display(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        Comments comments = new Comments();
        comments.setArticleId(articleDetailVo.getId());
        comments.setId(sid.nextShort());
        comments.setCommentUserFace(userVo.getFace());
        comments.setWriterId(articleDetailVo.getPublishUserId());
        comments.setArticleCover(articleDetailVo.getCover());
        comments.setArticleTitle(articleDetailVo.getTitle());
        comments.setFatherId(parentCommentId);
        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(userVo.getNickname());
        comments.setContent(content);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);

        redisAdaptor.increment(REDIS_COMMENT_COUNTS_PREFIX + ":" + articleId, 1);
    }

    @Override
    public int getCommentCount(String articleId) {
        String count = redisAdaptor.get(REDIS_COMMENT_COUNTS_PREFIX + ":" + articleId);
        return count == null ? 0 : Integer.valueOf(count);
    }
}
















