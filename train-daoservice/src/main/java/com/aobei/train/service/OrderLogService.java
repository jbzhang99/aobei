package com.aobei.train.service;

import com.aobei.train.model.Customer;
import com.aobei.train.model.Order;
import com.aobei.train.model.OrderLogExample;
import com.aobei.train.model.OrderLog;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface OrderLogService extends MbgReadService<Long, OrderLog, OrderLog, OrderLogExample>,MbgWriteService<Long, OrderLog, OrderLog, OrderLogExample>,MbgUpsertService<Long, OrderLog, OrderLog, OrderLogExample>{
    OrderLog initOrderLog(Customer customer, Order order, String operator, String text);

    /**
     * 插入订单炒作日志
     * @param operator
     * @param user_id
     * @param orderid
     * @param logtext
     * @return
     */
    int xInsert(String operator,Long user_id,String orderid,String logtext);
}