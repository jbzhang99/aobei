package com.aobei.train.service.impl;

import com.aobei.train.mapper.CustomerMapper;
import com.aobei.train.mapper.DataStatisticsCustomMapper;
import com.aobei.train.mapper.OrderMapper;
import com.aobei.train.service.DataStatisticsCustomService;
import custom.bean.DataStatisticsCustomData;
import custom.bean.PurchaseCustomStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataStatisticsCustomServiceImpl implements DataStatisticsCustomService {

    @Autowired
    private DataStatisticsCustomMapper dataStatisticsCustomMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    private static final String[] CUSTOM_CLIENTS = {"wx_m_custom", "a_custom", "i_custom", "h5_custom"};

    @Override
    public List<DataStatisticsCustomData> regStatisticsWithDay(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> list = dataStatisticsCustomMapper.regStatisticsWithDay(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusDays(1);
            i++;
        }

        return list;
    }

    @Override
    public List<DataStatisticsCustomData> regStatisticsWithWeek(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> list = dataStatisticsCustomMapper.regStatisticsWithWeek(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/ww周"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusWeeks(1);
            i++;
        }
        return list;
    }

    @Override
    public List<DataStatisticsCustomData> regStatisticsWithMonth(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> list = dataStatisticsCustomMapper.regStatisticsWithMonth(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusMonths(1);
            i++;
        }
        return list;
    }

    @Override
    public List<PurchaseCustomStatisticsData> purchaseCustomStatisticsDataWithDay(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> listData0 = regStatisticsWithDay(startDate, endDate);

        List<DataStatisticsCustomData> listData1 = dataStatisticsCustomMapper.purchaseCustomStatisticsWithDay(startDate, endDate);
        List<DataStatisticsCustomData> listData2 = dataStatisticsCustomMapper.rePurchaseCustomStatisticsWithDay(startDate, endDate);
        List<DataStatisticsCustomData> listData3 = dataStatisticsCustomMapper.regClientStatisticsWithDay(startDate, endDate);

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map3 = listData3.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchaseCustomStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchaseCustomStatisticsData pcsdObj = new PurchaseCustomStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setPurchaseTotalCustomNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setRePurchaseTotalCustomNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setPurchasePercent("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate2 * 1.00) / (stepDate1 * 1.00) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setPurchasePercent(purchasePercent);
            }
            Map<String, Long> clientNumMap = new LinkedHashMap<>();
            for (String client : CUSTOM_CLIENTS) {
                String key = pcsdObj.getDateStr() + " " + client;
                clientNumMap.put(client, map3.containsKey(key) ? map3.get(key) : 0L);
            }
            pcsdObj.setClientNumMap(clientNumMap);
            list.add(pcsdObj);
        }
        return list;
    }

    @Override
    public List<PurchaseCustomStatisticsData> purchaseCustomStatisticsDataWithWeek(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> listData0 = regStatisticsWithWeek(startDate, endDate);

        List<DataStatisticsCustomData> listData1 = dataStatisticsCustomMapper.purchaseCustomStatisticsWithWeek(startDate, endDate);
        List<DataStatisticsCustomData> listData2 = dataStatisticsCustomMapper.rePurchaseCustomStatisticsWithWeek(startDate, endDate);
        List<DataStatisticsCustomData> listData3 = dataStatisticsCustomMapper.regClientStatisticsWithWeek(startDate, endDate);

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map3 = listData3.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchaseCustomStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchaseCustomStatisticsData pcsdObj = new PurchaseCustomStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setPurchaseTotalCustomNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setRePurchaseTotalCustomNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setPurchasePercent("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate2 * 1.00) / (stepDate1 * 1.00) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setPurchasePercent(purchasePercent);
            }
            Map<String, Long> clientNumMap = new LinkedHashMap<>();
            for (String client : CUSTOM_CLIENTS) {
                String key = pcsdObj.getDateStr() + " " + client;
                clientNumMap.put(client, map3.containsKey(key) ? map3.get(key) : 0L);
            }
            pcsdObj.setClientNumMap(clientNumMap);
            list.add(pcsdObj);
        }
        return list;
    }

    @Override
    public List<PurchaseCustomStatisticsData> purchaseCustomStatisticsDataWithMonth(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> listData0 = regStatisticsWithMonth(startDate, endDate);

        List<DataStatisticsCustomData> listData1 = dataStatisticsCustomMapper.purchaseCustomStatisticsWithMonth(startDate, endDate);
        List<DataStatisticsCustomData> listData2 = dataStatisticsCustomMapper.rePurchaseCustomStatisticsWithMonth(startDate, endDate);
        List<DataStatisticsCustomData> listData3 = dataStatisticsCustomMapper.regClientStatisticsWithMonth(startDate, endDate);

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map3 = listData3.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchaseCustomStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchaseCustomStatisticsData pcsdObj = new PurchaseCustomStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setPurchaseTotalCustomNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setRePurchaseTotalCustomNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setPurchasePercent("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate2 * 1.00) / (stepDate1 * 1.00) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setPurchasePercent(purchasePercent);
            }
            Map<String, Long> clientNumMap = new LinkedHashMap<>();
            for (String client : CUSTOM_CLIENTS) {
                String key = pcsdObj.getDateStr() + " " + client;
                clientNumMap.put(client, map3.containsKey(key) ? map3.get(key) : 0L);
            }
            pcsdObj.setClientNumMap(clientNumMap);
            list.add(pcsdObj);
        }
        return list;
    }

}
