package com.panda.article.mapper;

import com.panda.my.mapper.MyMapper;
import com.panda.pojo.Comments;
import com.panda.pojo.vo.CommentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentsMapperCustom {
    public List<CommentVo> listComments(
            @Param("paramMap") Map<String, Object> map
    );
}