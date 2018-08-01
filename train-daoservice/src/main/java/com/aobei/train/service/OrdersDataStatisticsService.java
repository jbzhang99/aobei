package com.aobei.train.service;

import custom.bean.DataResultSet;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mr_bl on 2018/8/1.
 */
public interface OrdersDataStatisticsService {

    Map<String,Long> getOrdersDataDay(Date startDateTime, Date endDateTime);

    Map<String,Long> getOrdersDataWeek(Date startDateTime, Date endDateTime);

    Map<String,Long> getOrdersDataMonth(Date startDateTime, Date endDateTime);
}
