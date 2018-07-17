package com.aobei.train.mapper;

import com.aobei.train.model.OrderItem;
import com.aobei.train.model.OrderItemExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends MbgReadMapper<Long, OrderItem, OrderItem, OrderItemExample>, MbgWriteMapper<Long, OrderItem, OrderItem, OrderItemExample> {
    Integer xSumNum(OrderItemExample orderItemExample);
}