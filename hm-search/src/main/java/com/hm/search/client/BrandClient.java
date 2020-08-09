package com.hm.search.client;

import com.hm.item.api.BrandApi;
import com.hm.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hanmeng
 * @date 2018/9/22
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}
