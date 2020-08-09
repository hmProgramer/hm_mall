package com.hm.search.client;

import com.hm.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
