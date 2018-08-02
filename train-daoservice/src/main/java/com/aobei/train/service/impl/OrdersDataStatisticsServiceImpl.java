package com.aobei.train.service.impl;

import com.aobei.train.mapper.OrdersDataStatisticsMapper;
import com.aobei.train.service.OrdersDataStatisticsService;
import com.aobei.train.service.bean.OrdersStatisticsData;
import custom.bean.DataResultSet;
import custom.bean.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mr_bl on 2018/8/1.
 */
@Service
public class OrdersDataStatisticsServiceImpl implements OrdersDataStatisticsService {

    @Autowired
    private OrdersDataStatisticsMapper ordersDataStatisticsMapper;

    private static final String[] CUSTOM_CLIENTS = {"wx_m_custom", "a_custom", "i_custom", "h5_custom","eb_custom"};

    /**
     * 按日查询GMV
     * @param startDate
     * @param endDate
     * @return
     */
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

    /**
     * 按周查询GMV
     * @param startDate
     * @param endDate
     * @return
     */
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

    /**
     * 按月查询GMV
     * @param startDate
     * @param endDate
     * @return
     */
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

    /**
     * 按日查询表格中数据
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Override
    public List<OrdersStatisticsData> getOrdersTableDataDay(Date startDateTime, Date endDateTime) {
        //gmv查询---已补无数据
        Map<String, Long> gmvMap = getOrdersDataDay(startDateTime, endDateTime);
        //订单数量查询
        List<DataResultSet> ordersNumDay = ordersDataStatisticsMapper.getOrdersNumDay(startDateTime, endDateTime);
        Map<String, Long> ordersNumMap = ordersNumDay.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //各端订单数量统计查询
        List<DataResultSet> ordersNumClientDay = ordersDataStatisticsMapper.getOrdersNumClientDay(startDateTime, endDateTime);
        Map<String, Long> ordersNumClientMap = ordersNumClientDay.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //已完成订单数量查询
        List<DataResultSet> ordersNumCompleteDay = ordersDataStatisticsMapper.getOrdersNumByStatusDay(startDateTime, endDateTime, Status.OrderStatus.done.value);
        Map<String, Long> ordersNumCompleteMap = ordersNumCompleteDay.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //待服务订单数量查询
        List<DataResultSet> ordersNumWaitServiceDay = ordersDataStatisticsMapper.getOrdersNumByStatusDay(startDateTime, endDateTime, Status.OrderStatus.wait_service.value);
        Map<String, Long> ordersNumWaitServiceMap = ordersNumWaitServiceDay.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));

        //数据封装
        return dataPackaging(gmvMap,ordersNumMap,ordersNumClientMap,ordersNumCompleteMap,ordersNumWaitServiceMap);
    }

    /**
     * 按周查询表格中数据
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Override
    public List<OrdersStatisticsData> getOrdersTableDataWeek(Date startDateTime, Date endDateTime) {
        //gmv查询---已补无数据
        Map<String, Long> gmvMap = getOrdersDataWeek(startDateTime, endDateTime);
        //订单数量查询
        List<DataResultSet> ordersNumWeek = ordersDataStatisticsMapper.getOrdersNumWeek(startDateTime, endDateTime);
        Map<String, Long> ordersNumMap = ordersNumWeek.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //各端订单数量统计查询
        List<DataResultSet> ordersNumClientWeek = ordersDataStatisticsMapper.getOrdersNumClientWeek(startDateTime, endDateTime);
        Map<String, Long> ordersNumClientMap = ordersNumClientWeek.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //已完成订单数量查询
        List<DataResultSet> ordersNumCompleteWeek = ordersDataStatisticsMapper.getOrdersNumByStatusWeek(startDateTime, endDateTime, Status.OrderStatus.done.value);
        Map<String, Long> ordersNumCompleteMap = ordersNumCompleteWeek.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //待服务订单数量查询
        List<DataResultSet> ordersNumWaitServiceWeek = ordersDataStatisticsMapper.getOrdersNumByStatusWeek(startDateTime, endDateTime, Status.OrderStatus.wait_service.value);
        Map<String, Long> ordersNumWaitServiceMap = ordersNumWaitServiceWeek.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));

        //数据封装
        return dataPackaging(gmvMap,ordersNumMap,ordersNumClientMap,ordersNumCompleteMap,ordersNumWaitServiceMap);
    }

    /**
     * 按月查询表格中数据
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Override
    public List<OrdersStatisticsData> getOrdersTableDataMonth(Date startDateTime, Date endDateTime) {
        //gmv查询---已补无数据
        Map<String, Long> gmvMap = getOrdersDataMonth(startDateTime, endDateTime);
        //订单数量查询
        List<DataResultSet> ordersNumMonth = ordersDataStatisticsMapper.getOrdersNumMonth(startDateTime, endDateTime);
        Map<String, Long> ordersNumMap = ordersNumMonth.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //各端订单数量统计查询
        List<DataResultSet> ordersNumClientMonth = ordersDataStatisticsMapper.getOrdersNumClientMonth(startDateTime, endDateTime);
        Map<String, Long> ordersNumClientMap = ordersNumClientMonth.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //已完成订单数量查询
        List<DataResultSet> ordersNumCompleteMonth = ordersDataStatisticsMapper.getOrdersNumByStatusMonth(startDateTime, endDateTime, Status.OrderStatus.done.value);
        Map<String, Long> ordersNumCompleteMap = ordersNumCompleteMonth.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));
        //待服务订单数量查询
        List<DataResultSet> ordersNumWaitServiceMonth = ordersDataStatisticsMapper.getOrdersNumByStatusMonth(startDateTime, endDateTime, Status.OrderStatus.wait_service.value);
        Map<String, Long> ordersNumWaitServiceMap = ordersNumWaitServiceMonth.stream()
                .collect(Collectors.toMap(DataResultSet::getDateStr, DataResultSet::getNum));

        //数据封装
        return dataPackaging(gmvMap,ordersNumMap,ordersNumClientMap,ordersNumCompleteMap,ordersNumWaitServiceMap);
    }

    @Override
    public List<DataResultSet> getOrdersNumMap(Date startDateTime, Date endDateTime) {
        return ordersDataStatisticsMapper.getOrdersNumMap(startDateTime,endDateTime);
    }

    public List<OrdersStatisticsData> dataPackaging(Map<String, Long> gmvMap,
                                                    Map<String, Long> ordersNumMap,
                                                    Map<String, Long> ordersNumClientMap,
                                                    Map<String, Long> ordersNumCompleteMap,
                                                    Map<String, Long> ordersNumWaitServiceMap){
        List<OrdersStatisticsData> list = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> iterator = gmvMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Long> entry = iterator.next();
            String key = entry.getKey();
            Long gmv = entry.getValue();

            OrdersStatisticsData data = new OrdersStatisticsData();
            data.setDateStr(key);
            data.setGmv(gmv);
            data.setNum(ordersNumMap.get(key) == null ? 0l : ordersNumMap.get(key));
            data.setCompleteNum(ordersNumCompleteMap.get(key) == null ? 0l : ordersNumCompleteMap.get(key));
            data.setWaitServiceNum(ordersNumWaitServiceMap.get(key) == null ? 0l : ordersNumWaitServiceMap.get(key));

            Map<String, Long> clientNumMap = new LinkedHashMap<>();
            for (String client : CUSTOM_CLIENTS) {
                String keyConcat = key + " " + client;
                clientNumMap.put(client, ordersNumClientMap.containsKey(keyConcat) ? ordersNumClientMap.get(keyConcat) : 0L);
            }
            data.setClientNumMap(clientNumMap);
            list.add(data);
        }
        return list;
    }
}
