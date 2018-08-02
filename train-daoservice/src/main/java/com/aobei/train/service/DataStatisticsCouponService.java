package com.aobei.train.service;

import custom.bean.PurchaseCouponStatisticsData;
import custom.bean.PurchaseCouponTableStatisticsData;

import java.util.Date;
import java.util.List;

public interface DataStatisticsCouponService {

    /**
     * 优惠卷 发放与使用 数据  日
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCouponStatisticsData> couponStatisticsWithDay(Date startDate, Date endDate);

    /**
     * 递增 用户注册数据  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCouponStatisticsData> couponStatisticsWithWeek(Date startDate, Date endDate);

    /**
     * 递增 用户注册数据  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCouponStatisticsData> couponStatisticsWithMonth(Date startDate, Date endDate);


    /**
     * 优惠卷 表格相关数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCouponTableStatisticsData> couponTableDatas(Date startDate, Date endDate);
}

