package com.hm.search.web;

import com.hm.search.pojo.Goods;
import com.hm.search.pojo.SearchRequest;
import com.hm.search.pojo.SearchResult;
import com.hm.search.service.SearchService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author bystander
 * @date 2018/9/22
 */
@RestController
//@RequestMapping("search")
public class GoodsSearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<SearchResult<Goods>> search(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(searchService.search(searchRequest));
    }
}
