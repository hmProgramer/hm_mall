package com.hm.item.mapper;

import com.hm.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    //todo 注意通用mapper只能处理单表，也就是brand的数据，所以，我们需要手动编写一个方法及sql实现中间表的新增

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid")Long cid, @Param("bid")Long bid);
}
