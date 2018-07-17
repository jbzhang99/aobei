package com.aobei.trainapi;


import com.aobei.train.model.Order;
import com.aobei.train.model.OrderExample;
import com.aobei.train.model.Product;
import com.aobei.train.service.CouponService;
import com.aobei.train.service.OrderService;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.CouponResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.text.SimpleDateFormat;
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DaoTest {

    @Autowired
    CouponService couponService;
    @Autowired
    OrderService orderService;



    @Test
    public void testcouponUserDefine() {

        Map<String, Object> map = new HashMap<>();
        map.put("uid", 1054072042874716160l);
        map.put("now",new Date());
        List<CouponResponse> list = couponService.selectByUid(map);

        for (CouponResponse coupon : list) {

        }


    }


    @Test
    public void testTimeStamp(){
//        Order order  = orderService.selectByPrimaryKey("1080108650942472192009664");
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//        format.format(order.getCreate_datetime());






    }



    @Test
    public void testOrderList(){




    }

    @Test
    public void testExampleParam(){


        String orderid  = null;
        Order product = orderService.selectByPrimaryKey(orderid);
    }

}
