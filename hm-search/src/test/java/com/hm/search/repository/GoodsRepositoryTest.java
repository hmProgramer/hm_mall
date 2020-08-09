package com.hm.search.repository;

import com.hm.common.vo.PageResult;
import com.hm.item.pojo.Spu;
import com.hm.search.client.GoodsClient;
import com.hm.search.pojo.Goods;
import com.hm.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    //创建索引库
    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void testLoadData() throws IOException {
        try {
            int page = 1;
            int rows = 100;
            int size = 0;
            do {
               //查询spu信息
                PageResult<Spu> spuPageResult = goodsClient.querySpuByPage(rows, page, null, true);
                List<Spu> itemList = spuPageResult.getItems();
                List<Goods> goodList = new ArrayList<>();
                //构建goods集合
//                List<Goods> goodList = itemList.stream().map(searchService::buildGoods).collect(Collectors.toList());
                for (Spu spu: itemList) {
                    Goods goods = searchService.buildGoods(spu);
                    goodList.add(goods);
                }
                //存入索引库
                goodsRepository.saveAll(goodList);
                page++;
                size = itemList.size();
            }while (size == 100);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}