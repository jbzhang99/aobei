package com.aobei.trainapi.server.impl;

import com.aobei.trainapi.server.ApiProductService;
import custom.bean.EvaluateBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiProductServiceImplTest {

    @Autowired
    ApiProductService apiProductService;
    @Test
    public void homePageProducts() {
    }

    @Test
    public void recommendProducts() {
    }

    @Test
    public void productEvaluateBase() {

       EvaluateBase base  =   apiProductService.productEvaluateBase(1059874002078294016l,3);

        Assert.assertEquals(java.util.Optional.ofNullable(base.getTotal()),2l);

    }

    @Test
    public void productEvaluates() {
    }
}