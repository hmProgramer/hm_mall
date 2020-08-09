package com.hm.search.client;

import com.hm.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void queryCategoryByIds(){
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(1L,2L,3L));
        Assert.assertEquals(3,categoryList.size());
        for (Category ca: categoryList) {
            System.out.println(ca);
        }

    }

}