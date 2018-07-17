package com.aobei.train.service;

import com.aobei.train.model.Order;
import com.aobei.train.model.OrderItemExample;
import com.aobei.train.model.OrderItem;
import com.aobei.train.model.ProSku;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface OrderItemService extends MbgReadService<Long, OrderItem, OrderItem, OrderItemExample>,MbgWriteService<Long, OrderItem, OrderItem, OrderItemExample>,MbgUpsertService<Long, OrderItem, OrderItem, OrderItemExample>{


    OrderItem initOrderItem(Order order, ProSku proSku, int num);

    Integer xSumNum(OrderItemExample orderItemExample);
}