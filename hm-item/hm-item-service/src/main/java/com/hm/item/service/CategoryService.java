package com.hm.item.service;

import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.item.mapper.CategoryMapper;
import com.hm.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category>  queryCategoryListByParentId(Long pid){
        //1 组装查询条件
        Category category = new Category();
        category.setParentId(pid);

        //todo 问题  return this.categoryMapper.select(category)和 return categoryMapper.select(category) 区别
        //2 查询 mapper
        List<Category> categoryList = categoryMapper.select(category);
        //3 结果为空时 走通用异常处理
        if (CollectionUtils.isEmpty(categoryList)){
            throw  new HmException(ExceptionEnums.CATEGORYDATA_IS_NULL);
        }
        return categoryList;
    }
}
