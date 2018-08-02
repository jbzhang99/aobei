package com.aobei.train.service;

import custom.bean.DataStatisticsCustomData;
import custom.bean.PurchaseCustomStatisticsData;

import java.util.Date;
import java.util.List;

public interface DataStatisticsCustomService  {

    /**
     * 用户注册数据  日
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regStatisticsWithDay(Date startDate, Date endDate);

    /**
     * 用户注册数据  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regStatisticsWithWeek(Date startDate, Date endDate);

    /**
     * 用户注册数据  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regStatisticsWithMonth(Date startDate, Date endDate);


    /**
     * 用户购买消费数统计  日
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCustomStatisticsData> purchaseCustomStatisticsDataWithDay(Date startDate, Date endDate);

    /**
     * 用户购买消费数统计  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCustomStatisticsData> purchaseCustomStatisticsDataWithWeek(Date startDate, Date endDate);

    /**
     * 用户购买消费数统计  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseCustomStatisticsData> purchaseCustomStatisticsDataWithMonth(Date startDate, Date endDate);

}

