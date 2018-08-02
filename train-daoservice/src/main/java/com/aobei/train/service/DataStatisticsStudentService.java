package com.aobei.train.service;

import com.aobei.train.service.bean.PurchaseStudentStatisticsData;
import custom.bean.DataStatisticsCustomData;

import java.util.Date;
import java.util.List;

public interface DataStatisticsStudentService {

    /**
     * 递增 用户注册数据  日
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> incrementingRegStatisticsWithDay(Date startDate, Date endDate);

    /**
     * 递增 用户注册数据  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> incrementingRegStatisticsWithWeek(Date startDate, Date endDate);

    /**
     * 递增 用户注册数据  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> incrementingRegStatisticsWithMonth(Date startDate, Date endDate);


    /**
     * 用户购买消费数统计  日
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseStudentStatisticsData> purchaseCustomStatisticsDataWithDay(Date startDate, Date endDate);

    /**
     * 用户购买消费数统计  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseStudentStatisticsData> purchaseCustomStatisticsDataWithWeek(Date startDate, Date endDate);

    /**
     * 用户购买消费数统计  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchaseStudentStatisticsData> purchaseCustomStatisticsDataWithMonth(Date startDate, Date endDate);

}

