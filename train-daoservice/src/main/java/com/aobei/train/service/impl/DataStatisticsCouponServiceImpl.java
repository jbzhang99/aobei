package com.aobei.train.service.impl;

import com.aobei.train.mapper.DataStatisticsCouponMapper;
import com.aobei.train.service.DataStatisticsCouponService;
import custom.bean.DataStatisticsCustomData;
import custom.bean.PurchaseCouponStatisticsData;
import custom.bean.PurchaseCouponTableStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataStatisticsCouponServiceImpl implements DataStatisticsCouponService {

    @Autowired
    private DataStatisticsCouponMapper dataStatistisCouponMapper;

    @Override
    public List<PurchaseCouponStatisticsData> couponStatisticsWithDay(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> planMoneyList = dataStatistisCouponMapper.planMoneyStatisticsWithDay(startDate, endDate);
        List<DataStatisticsCustomData> usedMoneyList = dataStatistisCouponMapper.usedMoneyStatisticsWithDay(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> planMoneyMap = planMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedMoneyMap = usedMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<PurchaseCouponStatisticsData> list = new ArrayList<>();
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            PurchaseCouponStatisticsData item = new PurchaseCouponStatisticsData();
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            item.setDateStr(key);
            item.setTotalPlanMoney(planMoneyMap.containsKey(key) ? planMoneyMap.get(key) : 0L);
            item.setTotalUsedMoney(usedMoneyMap.containsKey(key) ? usedMoneyMap.get(key) : 0L);
            startLocalDateTime = startLocalDateTime.plusDays(1);
            list.add(item);
        }
        return list;
    }

    @Override
    public List<PurchaseCouponStatisticsData> couponStatisticsWithWeek(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> planMoneyList = dataStatistisCouponMapper.planMoneyStatisticsWithWeek(startDate, endDate);
        List<DataStatisticsCustomData> usedMoneyList = dataStatistisCouponMapper.usedMoneyStatisticsWithWeek(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> planMoneyMap = planMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedMoneyMap = usedMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<PurchaseCouponStatisticsData> list = new ArrayList<>();
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            PurchaseCouponStatisticsData item = new PurchaseCouponStatisticsData();
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/ww周"));
            item.setDateStr(key);
            item.setTotalPlanMoney(planMoneyMap.containsKey(key) ? planMoneyMap.get(key) : 0L);
            item.setTotalUsedMoney(usedMoneyMap.containsKey(key) ? usedMoneyMap.get(key) : 0L);
            startLocalDateTime = startLocalDateTime.plusWeeks(1);
            list.add(item);
        }
        return list;
    }

    @Override
    public List<PurchaseCouponStatisticsData> couponStatisticsWithMonth(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> planMoneyList = dataStatistisCouponMapper.planMoneyStatisticsWithMonth(startDate, endDate);
        List<DataStatisticsCustomData> usedMoneyList = dataStatistisCouponMapper.usedMoneyStatisticsWithMonth(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> planMoneyMap = planMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedMoneyMap = usedMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<PurchaseCouponStatisticsData> list = new ArrayList<>();
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            PurchaseCouponStatisticsData item = new PurchaseCouponStatisticsData();
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            item.setDateStr(key);
            item.setTotalPlanMoney(planMoneyMap.containsKey(key) ? planMoneyMap.get(key) : 0L);
            item.setTotalUsedMoney(usedMoneyMap.containsKey(key) ? usedMoneyMap.get(key) : 0L);
            startLocalDateTime = startLocalDateTime.plusMonths(1);
            list.add(item);
        }
        return list;
    }

    @Override
    public List<PurchaseCouponTableStatisticsData> couponTableDatas(Date startDate, Date endDate) {
        return null;
    }
}
