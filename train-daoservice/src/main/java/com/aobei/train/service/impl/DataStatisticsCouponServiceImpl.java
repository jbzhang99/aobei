package com.aobei.train.service.impl;

import com.aobei.train.mapper.CouponMapper;
import com.aobei.train.mapper.DataStatisticsCouponMapper;
import com.aobei.train.mapper.TrainCityMapper;
import com.aobei.train.model.Coupon;
import com.aobei.train.model.CouponExample;
import com.aobei.train.model.TrainCity;
import com.aobei.train.model.TrainCityExample;
import com.aobei.train.service.DataStatisticsCouponService;
import custom.bean.AreaData;
import custom.bean.DataStatisticsCustomData;
import custom.bean.CouponStatisticsData;
import custom.bean.CouponTableStatisticsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataStatisticsCouponServiceImpl implements DataStatisticsCouponService {

    @Autowired
    private DataStatisticsCouponMapper dataStatistisCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private TrainCityMapper trainCityMapper;

    private static Map<Integer, String> typeNameMap;

    static {
        // 1,派发券2,赔偿券3,领取券4,兑换券
        typeNameMap = new HashMap<>();
        typeNameMap.put(1, "派发券");
        typeNameMap.put(2, "赔偿券");
        typeNameMap.put(3, "领取券");
        typeNameMap.put(4, "兑换券");
    }

    @Override
    public List<CouponStatisticsData> couponStatisticsWithDay(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> planMoneyList = dataStatistisCouponMapper.planMoneyStatisticsWithDay(startDate, endDate);
        List<DataStatisticsCustomData> usedMoneyList = dataStatistisCouponMapper.usedMoneyStatisticsWithDay(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> planMoneyMap = planMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedMoneyMap = usedMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<CouponStatisticsData> list = new ArrayList<>();
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            CouponStatisticsData item = new CouponStatisticsData();
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
    public List<CouponStatisticsData> couponStatisticsWithWeek(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> planMoneyList = dataStatistisCouponMapper.planMoneyStatisticsWithWeek(startDate, endDate);
        List<DataStatisticsCustomData> usedMoneyList = dataStatistisCouponMapper.usedMoneyStatisticsWithWeek(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> planMoneyMap = planMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedMoneyMap = usedMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<CouponStatisticsData> list = new ArrayList<>();
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            CouponStatisticsData item = new CouponStatisticsData();
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
    public List<CouponStatisticsData> couponStatisticsWithMonth(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> planMoneyList = dataStatistisCouponMapper.planMoneyStatisticsWithMonth(startDate, endDate);
        List<DataStatisticsCustomData> usedMoneyList = dataStatistisCouponMapper.usedMoneyStatisticsWithMonth(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> planMoneyMap = planMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedMoneyMap = usedMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<CouponStatisticsData> list = new ArrayList<>();
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            CouponStatisticsData item = new CouponStatisticsData();
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM月"));
            item.setDateStr(key);
            item.setTotalPlanMoney(planMoneyMap.containsKey(key) ? planMoneyMap.get(key) : 0L);
            item.setTotalUsedMoney(usedMoneyMap.containsKey(key) ? usedMoneyMap.get(key) : 0L);
            startLocalDateTime = startLocalDateTime.plusMonths(1);
            list.add(item);
        }
        return list;
    }

    @Override
    public List<CouponTableStatisticsData> couponTableDatas(Date startDate, Date endDate) {
        CouponExample couponExample = new CouponExample();
        couponExample.or()
                .andNum_limitEqualTo(1)             // 只查有数量限制的卷
                .andCreate_timeBetween(startDate, endDate);
        couponExample.setOrderByClause(CouponExample.C.create_time.name());

        List<Coupon> couponList = couponMapper.selectByExample(couponExample);
        // 消费总额
        List<DataStatisticsCustomData> payedOrderMoneyList = dataStatistisCouponMapper.couponPayedOrderMoney(startDate, endDate);
        // 拉新用户
        List<DataStatisticsCustomData> regUserCountList = dataStatistisCouponMapper.couponRegUserCount(startDate, endDate);
        // 已产生的优惠额
        List<DataStatisticsCustomData> usedOrderMoneyList = dataStatistisCouponMapper.couponUsedOrderMoney(startDate, endDate);

        Map<String, Long> payedOrderMoneyMap = payedOrderMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> regUserCountMap = regUserCountList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> usedOrderMoneyMap = usedOrderMoneyList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<CouponTableStatisticsData> list = new ArrayList<>();
        for (Coupon coupon : couponList) {
            String key = coupon.getCoupon_id().toString();
            CouponTableStatisticsData item = new CouponTableStatisticsData();
            item.setDateStr(LocalDateTime.ofInstant(coupon.getCreate_time().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            item.setNumTotal(coupon.getNum_total().longValue());
            item.setPlanMoney(coupon.getPlan_money() == null ? 0L : coupon.getPlan_money().longValue());
            item.setNumUsed(coupon.getNum_total().longValue() - coupon.getNum_able());
            item.setType(typeNameMap.get(coupon.getType()));
            item.setUseStartDatetime(LocalDateTime.ofInstant(coupon.getUse_start_datetime().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            item.setUseEndDatetime(LocalDateTime.ofInstant(coupon.getUse_end_datetime().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            item.setGmv(payedOrderMoneyMap.containsKey(key) ? payedOrderMoneyMap.get(key) : 0L);
            item.setTotalUsedMoney(usedOrderMoneyMap.containsKey(key) ? payedOrderMoneyMap.get(key) : 0L);
            item.setRegUserCount(regUserCountMap.containsKey(key) ? payedOrderMoneyMap.get(key) : 0L);
            list.add(item);
        }
        return list;
    }

    // 全部地区数据
    private Map<String, TrainCity> cityMap;

    @Override
    public List<AreaData<Double>> couponUsedOrderMoneyAreaData(Date startDate, Date endDate) {
        if (cityMap == null) {
            cityMap = trainCityMapper.selectByExample(new TrainCityExample())
                    .stream()
                    .collect(Collectors.toMap(TrainCity::getId, Function.identity()));
        }
        List<DataStatisticsCustomData> areaDataList = dataStatistisCouponMapper.couponUsedOrderMoneyGroupArea(startDate, endDate);
        // 三级数据转为二级数据
        areaDataListChange(areaDataList);
        Map<String, AreaData<Double>> map = new LinkedHashMap<>();
        for (DataStatisticsCustomData areaData : areaDataList) {
            String[] areas = areaData.getDateStr().split(" ");
            AreaData<Double> item;
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
            AreaData<Double> subItem = new AreaData<>();
            subItem.setName(cityMap.get(areas[1]).getName());
            subItem.setId(areas[1]);
            subItem.setValue(Double.valueOf(areaData.getNum()) / 100);
            if (hasData) {
                Map<String, AreaData<Double>> tempMap = item.getItems().stream().collect(Collectors.toMap(AreaData::getId, Function.identity()));
                AreaData<Double> tempItem = tempMap.get(areas[1]);
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
            double sum = 0;
            for (AreaData<Double> subItem : item.getItems()) {
                sum += subItem.getValue();
            }
            // 设置一级地区总值
            item.setValue(sum);
        }).collect(Collectors.toList());
    }

    /**
     * 将三级数据 转为二级数据
     *
     * @param areaDataList
     * @return
     */
    private void areaDataListChange(List<DataStatisticsCustomData> areaDataList) {
        for (DataStatisticsCustomData areaData : areaDataList) {
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
