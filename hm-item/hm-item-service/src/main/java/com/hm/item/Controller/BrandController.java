package com.hm.item.Controller;

import com.hm.common.vo.PageResult;
import com.hm.item.pojo.Brand;
import com.hm.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;


    /**
     * 通过分页查询获得品牌信息
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @RequestMapping("page")
    private ResponseEntity<PageResult<Brand>> queryBrandByPageAndSort(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false")  boolean desc,
            @RequestParam(value = "key",required = false) String key){
        PageResult<Brand> brandPageResult = this.brandService.queryBrandByPage(page, rows, sortBy, desc, key);
        System.out.println(brandPageResult);
        return ResponseEntity.ok(brandPageResult);
    }


    @PostMapping
    public  ResponseEntity<Void> saveBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
