package com.aobei.train.service.impl;

import com.aobei.train.mapper.OrdersDataStatisticsMapper;
import com.aobei.train.mapper.TrainCityMapper;
import com.aobei.train.model.TrainCity;
import com.aobei.train.model.TrainCityExample;
import com.aobei.train.service.OrdersDataStatisticsService;
import com.aobei.train.service.bean.OrdersStatisticsData;
import custom.bean.AreaData;
import custom.bean.DataResultSet;
import custom.bean.EffectiveOrder;
import custom.bean.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mr_bl on 2018/8/1.
 */
@Service
public class OrdersDataStatisticsServiceImpl implements OrdersDataStatisticsService {

    @Autowired
    private OrdersDataStatisticsMapper ordersDataStatisticsMapper;

    @Autowired
    private TrainCityMapper trainCityMapper;

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
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM月"));
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

    // 全部地区数据
    private Map<String, TrainCity> cityMap;

    @Override
    public List<AreaData<Long>> getOrdersNumMap(Date startDateTime, Date endDateTime) {
        if (cityMap == null) {
            cityMap = trainCityMapper.selectByExample(new TrainCityExample())
                    .stream()
                    .collect(Collectors.toMap(TrainCity::getId, Function.identity()));
        }
        List<DataResultSet> areaDataList = ordersDataStatisticsMapper.getOrdersNumMap(startDateTime, endDateTime);
        // 三级数据转为二级数据
        areaDataListChange(areaDataList);
        Map<String, AreaData<Long>> map = new LinkedHashMap<>();
        for (DataResultSet areaData : areaDataList) {
            String[] areas = areaData.getDateStr().split(" ");
            AreaData<Long> item;
            boolean hasData = false;
            if (map.containsKey(areas[0])) {
                hasData = true;
                item = map.get(areas[0]);
            } else {
                item = new AreaData<>();
                item.setName(cityMap.get(areas[0]).getName());
                item.setId(areas[0]);
                item.setItems(new ArrayList<>());
                map.put(areas[0], item);
            }
            AreaData<Long> subItem = new AreaData<>();
            subItem.setName(cityMap.get(areas[1]).getName());
            subItem.setId(areas[1]);
            subItem.setValue(areaData.getNum());
            if (hasData) {
                Map<String, AreaData<Long>> tempMap = item.getItems().stream().collect(Collectors.toMap(AreaData::getId, Function.identity()));
                AreaData<Long> tempItem = tempMap.get(areas[1]);
                // 二级数据累加
                if (tempItem == null) {
                    item.getItems().add(subItem);
                } else {
                    tempItem.setValue(tempItem.getValue() + subItem.getValue());
                }
            } else {
                item.getItems().add(subItem);
            }
        }
        return map.values().stream().peek(item -> {
            long sum = 0;
            for (AreaData<Long> subItem : item.getItems()) {
                sum += subItem.getValue();
            }
            // 设置一级地区总值
            item.setValue(sum);
        }).collect(Collectors.toList());
    }

    @Override
    public Long getOrdersGmvByClient(Date startDateTime, Date endDateTime, String client) {
        return ordersDataStatisticsMapper.getOrdersGmvByClient(startDateTime,endDateTime,client);
    }

    /**
     * 查询当前时间点之前的有效订单 / 根据服务名称模糊查询
     * @param nowDateTime
     * @param serverName
     * @return
     */
    @Override
    public List<EffectiveOrder> getEffectiveOrdersNumMonth(Date nowDateTime,String serverName) {
        List<DataResultSet> results = ordersDataStatisticsMapper.getEffectiveOrdersNumMonth(nowDateTime,serverName,0);//顾客下单
        List<DataResultSet> resultsStu = ordersDataStatisticsMapper.getEffectiveOrdersNumMonth(nowDateTime,serverName,1);//学员下单
        DataResultSet first = null;
        if (results.size() > 0){
            first = results.get(0);
        }
        Map<String, DataResultSet> map = results.stream().collect(Collectors.toMap(DataResultSet::getDateStr, Function.identity()));
        List<DataResultSet> temp = new ArrayList<>();
        List<EffectiveOrder> datas = new ArrayList<>();
        LocalDateTime startLocalDateTime = null;
        if (first != null){
            startLocalDateTime = LocalDateTime.parse(first.getDateStr()+"/01 00:00:00",DateTimeFormatter.ofPattern("yyyy/MM月/dd HH:mm:ss"));
        }else{
            startLocalDateTime = LocalDateTime.now().minusMonths(2L).with(TemporalAdjusters.firstDayOfMonth());
        }
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(nowDateTime.toInstant(), ZoneId.systemDefault());
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM月"));
            if (!map.containsKey(key)){
                DataResultSet data = new DataResultSet();
                data.setDateStr(key);
                data.setNum(0L);
                temp.add(data);
            }else {
                temp.add(map.get(key));
            }
            startLocalDateTime = startLocalDateTime.plusMonths(1);
        }
        for (int i = 0 ; i< temp.size();i++){
            DataResultSet data = temp.get(i);
            EffectiveOrder effectiveOrder = new EffectiveOrder();
            BeanUtils.copyProperties(data,effectiveOrder);
            if (i == 0){
                effectiveOrder.setIncrRate(0);
            }else{
                effectiveOrder.setIncrRate(getIncrRate(temp.get(i-1),temp.get(i)));
            }
            datas.add(effectiveOrder);
        }
        datas.stream().forEach(n ->{
            n.setStuNum(0L);
            resultsStu.stream().forEach(m ->{
                if (n.getDateStr().equals(m.getDateStr())){
                    n.setStuNum(m.getNum());
                }
            });
        });
        return datas;
    }

    /**
     * 顾客/学员下单各次数顾客/学员数量统计
     * @param startDateTime
     * @param endDateTime
     * @param proxyed
     * @return
     */
    @Override
    public List<DataResultSet> purchaseNumSum(Date startDateTime, Date endDateTime, int proxyed) {
        if (startDateTime == null || endDateTime == null){
            endDateTime = new Date();
            startDateTime = Date.from(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return ordersDataStatisticsMapper.PurchaseNumSum(startDateTime,endDateTime,proxyed);
    }

    @Override
    public List<EffectiveOrder> getEffectiveOrdersNumByIsNew(String serverName, int isNew) {
        List<EffectiveOrder> effectiveOrdersByIsNew = getEffectiveOrdersNumMonth(new Date(), serverName);
        List<DataResultSet> list = ordersDataStatisticsMapper.getEffectiveOrdersNumByIsNew(serverName, isNew);
        Map<String, DataResultSet> map = list.stream().collect(Collectors.toMap(DataResultSet::getDateStr, Function.identity()));
        effectiveOrdersByIsNew.stream().forEach(n ->{
            DataResultSet result = map.get(n.getDateStr());
            if (result != null){
                n.setIncrRate(new BigDecimal((double)(result.getNum())/(n.getNum()+ n.getStuNum()))
                        .setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
                n.setNum(result.getNum());
            }else{
                n.setNum(0);
                n.setIncrRate(0d);
            }
        });
        return effectiveOrdersByIsNew;
    }

    /**
     * 计算增长率
     * @param pre
     * @param now
     * @return
     */
    public double getIncrRate(DataResultSet pre,DataResultSet now){
        if(pre.getNum() == 0){
            return 0;
        }else{
            return new BigDecimal((double)(now.getNum()-pre.getNum())/pre.getNum())
                    .setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
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

    /**
     * 将三级数据 转为二级数据
     *
     * @param areaDataList
     * @return
     */
    private void areaDataListChange(List<DataResultSet> areaDataList) {
        for (DataResultSet areaData : areaDataList) {
            String[] areas = areaData.getDateStr().split(" ");
            int count = 0;
            boolean hasXian = false;
            for (TrainCity item : cityMap.values()) {
                if (item.getP_id().equals(areas[0])) {
                    count++;
                    if ("县".equals(item.getName())) {
                        hasXian = true;
                    }
                    if (count > 2) {
                        break;
                    }
                }
            }

            if (hasXian && count <= 2 || count == 1) {
                // 省数据下仅有一个市级
                areaData.setDateStr(areas[0] + " " + areas[2]);
            } else {
                areaData.setDateStr(areas[0] + " " + areas[1]);
            }
        }
    }
}
