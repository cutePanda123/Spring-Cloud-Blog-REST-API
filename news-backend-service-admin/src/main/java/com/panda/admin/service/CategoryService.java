package com.panda.admin.service;

import com.panda.pojo.Category;

import java.util.List;

public interface CategoryService {
    public void createCategory(Category category);

    public void modifyCategory(Category category);

    public boolean isExist(String categoryName);

    public List<Category> listCategories();

    public List<Category> listCachedCategories();
}
