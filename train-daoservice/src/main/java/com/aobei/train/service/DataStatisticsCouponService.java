package com.aobei.train.service;

import custom.bean.AreaData;
import custom.bean.CouponStatisticsData;
import custom.bean.CouponTableStatisticsData;

import java.util.Date;
import java.util.List;

public interface DataStatisticsCouponService {

    /**
     * 优惠卷 发放与使用 数据  日
     * @param startDate
     * @param endDate
     * @return
     */
    List<CouponStatisticsData> couponStatisticsWithDay(Date startDate, Date endDate);

    /**
     * 优惠卷 发放与使用 数据  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<CouponStatisticsData> couponStatisticsWithWeek(Date startDate, Date endDate);

    /**
     * 优惠卷 发放与使用 数据  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<CouponStatisticsData> couponStatisticsWithMonth(Date startDate, Date endDate);


    /**
     * 优惠卷 表格相关数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<CouponTableStatisticsData> couponTableDatas(Date startDate, Date endDate);

    /**
     * 优惠卷使用 地区数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<AreaData<Double>> couponUsedOrderMoneyAreaData(Date startDate, Date endDate);
}

