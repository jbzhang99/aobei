package com.aobei.train.service;

import com.aobei.train.service.bean.OrdersStatisticsData;
import custom.bean.DataResultSet;

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
}
