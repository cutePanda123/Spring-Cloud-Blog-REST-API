package com.panda.api.service;

import com.github.pagehelper.PageInfo;
import com.panda.utils.PaginationResult;

import java.util.List;

public class BaseService {
    protected static final String REDIS_ALL_CATEGORY_KEY = "redis_all_category";
    protected static final String REDIS_WRITER_FANS_COUNTS_PREFIX = "redis_fans_counts";
    protected static final String REDIS_USER_FOLLOW_COUNTS_PREFIX = "redis_user_follow_counts";
    protected static final Integer DEFAULT_START_PAGE = 1;
    protected static final Integer DEFAULT_PAGE_SIZE = 10;
    protected PaginationResult paginationResultBuilder(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PaginationResult result = new PaginationResult();
        result.setPage(page);
        result.setRows(list);
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        return  result;
    }
}
