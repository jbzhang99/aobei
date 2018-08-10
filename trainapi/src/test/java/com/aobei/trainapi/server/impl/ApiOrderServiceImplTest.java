package com.aobei.trainapi.server.impl;

import com.aobei.train.model.Order;
import com.aobei.train.model.ServiceUnit;
import com.aobei.train.model.ServiceUnitExample;
import com.aobei.train.service.OrderService;
import com.aobei.train.service.ServiceUnitService;
import com.aobei.trainapi.server.ApiOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * Created by renpiming on 2018/5/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiOrderServiceImplTest {
    @Autowired
    OrderService orderService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    ApiOrderService apiOrderService;
    @Test
    public void paySuccess() throws Exception {
    }

    @Test
    public void dispatch() throws Exception {
        Order order  =  orderService.selectByPrimaryKey("1526901775_1");
        ServiceUnitExample example  = new ServiceUnitExample();
        example.or().andPay_order_idEqualTo("1526901775_1")
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit  = singleResult(serviceUnitService.selectByExample(example));
        orderService.dispatchOrder(order,serviceUnit);
    }

    @Test
    public void testOrderList(){



    }


}