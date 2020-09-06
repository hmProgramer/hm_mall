package com.hm.page.client;

import com.hm.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author bystander
 * @date 2018/9/22
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
