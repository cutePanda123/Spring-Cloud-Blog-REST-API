package com.panda.api.service;

import com.github.pagehelper.PageInfo;
import com.panda.utils.PaginationResult;

import java.util.List;

public class BaseService {
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
