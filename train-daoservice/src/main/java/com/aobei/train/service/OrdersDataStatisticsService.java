package com.aobei.train.service;

import com.aobei.train.service.bean.OrdersStatisticsData;
import custom.bean.AreaData;
import custom.bean.DataResultSet;
import custom.bean.EffectiveOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mr_bl on 2018/8/1.
 */
public interface OrdersDataStatisticsService {

    /**
     * 按日查询GMV
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Map<String,Long> getOrdersDataDay(Date startDateTime, Date endDateTime);

    /**
     * 按周查询GMV
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Map<String,Long> getOrdersDataWeek(Date startDateTime, Date endDateTime);

    /**
     * 按月查询GMV
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Map<String,Long> getOrdersDataMonth(Date startDateTime, Date endDateTime);

    /**
     * 按日查询表格中数据
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<OrdersStatisticsData> getOrdersTableDataDay(Date startDateTime, Date endDateTime);

    /**
     * 按周查询表格中数据
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<OrdersStatisticsData> getOrdersTableDataWeek(Date startDateTime, Date endDateTime);

    /**
     * 按月查询表格中数据
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<OrdersStatisticsData> getOrdersTableDataMonth(Date startDateTime, Date endDateTime);


    /**
     * 查询指定时间范围内数据地图订单数量
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<AreaData<Long>> getOrdersNumMap(Date startDateTime, Date endDateTime);

    /**
     * 根据客户端查询一定时间范围内的订单GMV
     * @param startDateTime
     * @param endDateTime
     * @param client
     * @return
     */
    Long getOrdersGmvByClient(Date startDateTime, Date endDateTime,String client);

    /**
     * 查询当前时间点之前的有效订单 / 根据服务名称模糊查询
     * @param nowDateTime
     * @return
     */
    List<EffectiveOrder> getEffectiveOrdersNumMonth(Date nowDateTime, String serverName);

    /**
     * 顾客/学员下单各次数顾客/学员数量统计
     * @param startDateTime
     * @param endDateTime
     * @param proxyed
     * @return
     */
    List<DataResultSet> purchaseNumSum(Date startDateTime, Date endDateTime,int proxyed);

    /**
     * 根据 isNew 统计 新老顾客有效订单
     * @param isNew  1 新顾客  0 老顾客
     * @param serverName
     * @return
     */
    List<EffectiveOrder> getEffectiveOrdersNumByIsNew(String serverName,int isNew);
}
