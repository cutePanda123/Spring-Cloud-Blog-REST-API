package com.panda.api.controller.admin;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.CreateCategoryBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "article category management", tags = {"article category management"})
@RequestMapping("/api/service-admin/categoryMng")
public interface CategoryManageControllerApi {
    @PostMapping("setCategory")
    @ApiOperation(value = "create or update category", notes = "create or update category", httpMethod = "POST")
    public ResponseResult setCategory(
            @RequestBody @Valid CreateCategoryBo bo,
            BindingResult result);

    @PostMapping("listCategory")
    @ApiOperation(value = "list category (admin)", notes = "list category (admin)", httpMethod = "POST")
    public ResponseResult listCategoryForAdmin();

    @GetMapping("listCategory")
    @ApiOperation(value = "list category (user)", notes = "list category (user)", httpMethod = "GET")
    public ResponseResult listCategoryForUser();
}
