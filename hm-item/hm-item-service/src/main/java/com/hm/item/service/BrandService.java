package com.hm.item.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.common.vo.PageResult;
import com.hm.item.mapper.BrandMapper;


import com.hm.item.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, boolean desc, String key) {

        //1 分页
        PageHelper.startPage(page,rows);

        Example example = new Example(Brand.class);
        //2 过滤
        if (!StringUtils.isEmpty(key)){
            //过滤条件
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key);
        }

        //3 排序
        //order by id DESC
        if (!StringUtils.isEmpty(sortBy)){
            String orderBySql = sortBy+ (desc ? " DESC":" ASC");
            example.setOrderByClause(orderBySql);
        }

        //4 查询
        List<Brand> brandList = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brandList)){
            throw new HmException(ExceptionEnums.BRANDDATA_IS_NULL);
        }

        //5 解析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);

        return new  PageResult<>(pageInfo.getTotal(),brandList);
    }
}
