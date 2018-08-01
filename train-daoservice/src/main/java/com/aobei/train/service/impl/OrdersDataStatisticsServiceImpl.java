package com.aobei.train.service.impl;

import com.aobei.train.mapper.OrdersDataStatisticsMapper;
import com.aobei.train.service.OrdersDataStatisticsService;
import custom.bean.DataResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mr_bl on 2018/8/1.
 */
@Service
public class OrdersDataStatisticsServiceImpl implements OrdersDataStatisticsService {

    @Autowired
    private OrdersDataStatisticsMapper ordersDataStatisticsMapper;

    @Override
    public Map<String,Long> getOrdersDataDay(Date startDate, Date endDate) {
        Map<String,Long> dataSet = new TreeMap<>();
        List<DataResultSet> dataResultSet = ordersDataStatisticsMapper.getOrdersDataDay(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        dataResultSet.forEach(n ->{
            dataSet.put(n.getDateStr(),n.getNum());
        });
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!dataSet.containsKey(key)) {
               dataSet.put(key,0l);
            }
            startLocalDateTime = startLocalDateTime.plusDays(1);
        }
        return dataSet;
    }

    @Override
    public Map<String,Long> getOrdersDataWeek(Date startDate, Date endDate) {
        Map<String,Long> dataSet = new TreeMap<>();
        List<DataResultSet> dataResultSet = ordersDataStatisticsMapper.getOrdersDataWeek(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        dataResultSet.forEach(n ->{
            dataSet.put(n.getDateStr(),n.getNum());
        });
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/ww周"));
            if (!dataSet.containsKey(key)) {
                dataSet.put(key,0l);
            }
            startLocalDateTime = startLocalDateTime.plusWeeks(1);
        }
        return dataSet;
    }

    @Override
    public Map<String,Long> getOrdersDataMonth(Date startDate, Date endDate) {
        Map<String,Long> dataSet = new TreeMap<>();
        List<DataResultSet> dataResultSet = ordersDataStatisticsMapper.getOrdersDataMonth(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        dataResultSet.forEach(n ->{
            dataSet.put(n.getDateStr(),n.getNum());
        });
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            if (!dataSet.containsKey(key)) {
                dataSet.put(key,0l);
            }
            startLocalDateTime = startLocalDateTime.plusMonths(1);
        }
        return dataSet;
    }
}
