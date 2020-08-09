package com.hm.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id; // spuId

    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌

    @Field(type = FieldType.keyword, index = false) //index=false表示不作索引进行分词
    private String subTitle;// 卖点
    private Long brandId;// 品牌id
    private Long cid1;// 1级分类id
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间
    private Set<Double> price;// 价格


    @Field(type = FieldType.keyword, index = false)  //多个sku的json结构，既不需要搜素也不需要过滤，存到es只是用来展示用
    private String skus;// sku信息的json结构

    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}