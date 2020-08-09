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
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {

        //1 先保存brand对象
        //todo insert与insertSelectiv之间的区别
        //todo insertSelective--有选择性的保存数据
        brand.setId(null);
        int count = brandMapper.insert(brand);
        
        //2 判断保存是否成功
        if (count != 1) {
            throw new HmException(ExceptionEnums.BRANDDATA_SAVE_ERROR);
        }
        
        //3 处理brand与category之间的关联关系
        for (Long cid: cids) {
            int saveCidAndBid = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (saveCidAndBid != 1) {
                throw new HmException(ExceptionEnums.BRANDDATA_SAVE_ERROR);
            }
        }
    }

    public Brand queryBrandByBid(Long id) {
        Brand brand = new Brand();
        brand.setId(id);
        Brand b1 = brandMapper.selectByPrimaryKey(brand);
        if (b1 == null) {
            throw new HmException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        return b1;
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)) {
            throw new HmException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        return brands;
    }

}
