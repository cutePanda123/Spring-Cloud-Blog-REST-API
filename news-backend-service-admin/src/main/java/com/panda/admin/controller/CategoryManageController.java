package com.panda.admin.controller;

import com.panda.admin.service.CategoryService;
import com.panda.api.controller.BaseController;
import com.panda.api.controller.admin.CategoryManageControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.Category;
import com.panda.pojo.bo.CreateCategoryBo;
import com.panda.utils.JsonUtils;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CategoryManageController extends BaseController implements CategoryManageControllerApi {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisAdaptor redisAdaptor;

    @Override
    public ResponseResult setCategory(@Valid CreateCategoryBo bo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            log.error(result.toString());
            return ResponseResult.errorMap(map);
        }
        if (categoryService.isExist(bo.getName())) {
            return ResponseResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
        }
        Category category = new Category();
        BeanUtils.copyProperties(bo, category);

        if (category.getId() == null) {
            // create new category
            categoryService.createCategory(category);
        } else {
            // update existing category
            categoryService.modifyCategory(category);
        }
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listCategoryForAdmin() {
        List<Category> categories = categoryService.listCategories();
        return ResponseResult.ok(categories);
    }

    @Override
    public ResponseResult listCategoryForUser() {
        List<Category> categories = categoryService.listCachedCategories();
        return ResponseResult.ok(categories);
    }
}
