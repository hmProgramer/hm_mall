package com.hm.page.client;

import com.hm.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author bystander
 * @date 2018/9/22
 */
@FeignClient("item-service")
public interface SpecClient extends SpecApi {
}
