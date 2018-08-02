package com.aobei.train.service.impl;

import com.aobei.train.mapper.*;
import com.aobei.train.model.*;
import com.aobei.train.service.DataStatisticsCustomService;
import com.aobei.train.service.DataStatisticsPartnerService;
import com.aobei.train.service.bean.PurchaseCustomStatisticsData;
import com.aobei.train.service.bean.PurchasePartnerStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataStatisticsPartnerServiceImpl implements DataStatisticsPartnerService {

    @Autowired
    private DataStatisticsCustomMapper dataStatisticsCustomMapper;

    @Autowired
    private DataStatisticsPartnerMapper dataStatisticsPartnerMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PartnerMapper partnerMapper;

    private static final String[] CUSTOM_CLIENTS = {"wx_m_custom", "a_custom", "i_custom", "h5_custom"};

    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithDay(Date startDate, Date endDate) {
        //CustomerExample customerExample = new CustomerExample();
        //customerExample.or().andCreate_datetimeLessThan(startDate);
        PartnerExample partnerExample=new PartnerExample();
        partnerExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list=dataStatisticsPartnerMapper.regStatisticsWithDay(startDate, endDate);
        //List<DataStatisticsCustomData> list = dataStatisticsCustomMapper.regStatisticsWithDay(startDate, endDate);
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
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithWeek(Date startDate, Date endDate) {
        PartnerExample partnerExample=new PartnerExample();
        partnerExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list=dataStatisticsPartnerMapper.regStatisticsWithWeek(startDate, endDate);
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

    /**
     * 地图数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithMonth(Date startDate, Date endDate) {
        PartnerExample partnerExample=new PartnerExample();
        partnerExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list=dataStatisticsPartnerMapper.regStatisticsWithMonth(startDate, endDate);
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
    public List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithDay(Date startDate, Date endDate) {
        //合伙人总数
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithDay(startDate, endDate);
        //合伙人派单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsPartnerMapper.sendOrdersPartnerStatisticsWithDay(startDate, endDate);
        //合伙人拒单数
        List<DataStatisticsCustomData> listData2 = dataStatisticsPartnerMapper.singleOrdersPartnerStatisticsWithDay(startDate, endDate);
        //找到所有合伙人
        List<Partner> partners = this.partnerMapper.selectByExample(new PartnerExample());
       /* if(!partners.isEmpty()){
            partners.stream().forEach(partner -> {
                //每个合伙人的派单数
                List<DataStatisticsCustomData> sendList = dataStatisticsPartnerMapper.oneSendOrdersPartnerStatisticsWithDay(startDate, endDate,partner.getPartner_id());
                //每个合伙人的拒单数
                List<DataStatisticsCustomData> singleList = dataStatisticsPartnerMapper.oneSingleOrdersPartnerStatisticsWithDay(startDate, endDate,partner.getPartner_id());

            });
        }*/


        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        //Map<String, Long> map3 = listData3.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchasePartnerStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchasePartnerStatisticsData pcsdObj=new PurchasePartnerStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());

            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setSendOrdersTotalNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setSingleOrdersTotalNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setOrderRate("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate1 * 1.00) / (stepDate1 +stepDate2) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setOrderRate(purchasePercent);
            }
            /*Map<String, Long> clientNumMap = new LinkedHashMap<>();
            for (String client : CUSTOM_CLIENTS) {
                String key = pcsdObj.getDateStr() + " " + client;
                clientNumMap.put(client, map3.containsKey(key) ? map3.get(key) : 0L);
            }
            pcsdObj.setClientNumMap(clientNumMap);*/
            list.add(pcsdObj);
        }
        return list;
    }

    @Override
    public List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithWeek(Date startDate, Date endDate) {
       /* List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithWeek(startDate, endDate);
        //合伙人派单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsPartnerMapper.sendOrdersPartnerStatisticsWithWeek(startDate, endDate);
        //合伙人拒单数
        List<DataStatisticsCustomData> listData2 = dataStatisticsPartnerMapper.singleOrdersPartnerStatisticsWithWeek(startDate, endDate);

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        //<String, Long> map3 = listData3.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

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
           *//* Map<String, Long> clientNumMap = new LinkedHashMap<>();
            for (String client : CUSTOM_CLIENTS) {
                String key = pcsdObj.getDateStr() + " " + client;
                clientNumMap.put(client, map3.containsKey(key) ? map3.get(key) : 0L);
            }
            pcsdObj.setClientNumMap(clientNumMap);*//*
            list.add(pcsdObj);
        }*/
        return null;
    }

    @Override
    public List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithMonth(Date startDate, Date endDate) {
        /*List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithMonth(startDate, endDate);

        //合伙人派单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsPartnerMapper.sendOrdersPartnerStatisticsWithMonth(startDate, endDate);
        //合伙人拒单数
        List<DataStatisticsCustomData> listData2 = dataStatisticsPartnerMapper.singleOrdersPartnerStatisticsWithMonth(startDate, endDate);
        *//*List<DataStatisticsCustomData> listData1 = dataStatisticsCustomMapper.purchaseCustomStatisticsWithMonth(startDate, endDate);
        List<DataStatisticsCustomData> listData2 = dataStatisticsCustomMapper.rePurchaseCustomStatisticsWithMonth(startDate, endDate);
        List<DataStatisticsCustomData> listData3 = dataStatisticsCustomMapper.regClientStatisticsWithMonth(startDate, endDate);*//*

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        //Map<String, Long> map3 = listData3.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

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
//            Map<String, Long> clientNumMap = new LinkedHashMap<>();
//            for (String client : CUSTOM_CLIENTS) {
//                String key = pcsdObj.getDateStr() + " " + client;
//                clientNumMap.put(client, map3.containsKey(key) ? map3.get(key) : 0L);
//            }
//            pcsdObj.setClientNumMap(clientNumMap);
            list.add(pcsdObj);
        }*/
        return null;
    }

}
