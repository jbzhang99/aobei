package com.aobei.train.mapper;

import com.aobei.train.model.Order;
import com.aobei.train.model.OrderExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends MbgReadMapper<String, Order, Order, OrderExample>, MbgWriteMapper<String, Order, Order, OrderExample> {
}