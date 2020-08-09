package com.hm.search.client;

import com.hm.common.vo.PageResult;
import com.hm.item.api.GoodsApi;
import com.hm.item.pojo.Sku;
import com.hm.item.pojo.Spu;
import com.hm.item.pojo.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
