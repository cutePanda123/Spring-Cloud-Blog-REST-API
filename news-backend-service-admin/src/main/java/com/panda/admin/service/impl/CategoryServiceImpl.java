package com.panda.admin.service.impl;

import com.panda.admin.mapper.CategoryMapper;
import com.panda.admin.service.CategoryService;
import com.panda.api.service.BaseService;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Category;
import com.panda.utils.JsonUtils;
import com.panda.utils.RedisAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    RedisAdaptor redisAdaptor;

    @Transactional
    @Override
    public void createCategory(Category category) {
        int rowNum = categoryMapper.insert(category);
        if (rowNum != 1) {
            EncapsulatedException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        redisAdaptor.del(REDIS_ALL_CATEGORY_KEY);
    }

    @Transactional
    @Override
    public void modifyCategory(Category category) {
        int rowNum = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowNum != 1) {
            EncapsulatedException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        redisAdaptor.del(REDIS_ALL_CATEGORY_KEY);
    }

    @Override
    public boolean isExist(String categoryName) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", categoryName);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories != null && !categories.isEmpty();
    }

    @Override
    public List<Category> listCategories() {
        List<Category> list = categoryMapper.selectAll();
        return list;
    }

    @Override
    public List<Category> listCachedCategories() {
        String categories = redisAdaptor.get(REDIS_ALL_CATEGORY_KEY);
        List<Category> categoryList = null;
        if (StringUtils.isBlank(categories)) {
            categoryList = listCategories();
            redisAdaptor.set(REDIS_ALL_CATEGORY_KEY, JsonUtils.objectToJson(categoryList));
        } else {
            categoryList = JsonUtils.jsonToList(categories, Category.class);
        }
        return categoryList;
    }
}
