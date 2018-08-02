package com.aobei.train.service;

import com.aobei.train.model.DataStatisticsCustomData;
import com.aobei.train.service.bean.PurchaseCustomStatisticsData;
import com.aobei.train.service.bean.PurchasePartnerStatisticsData;

import java.util.Date;
import java.util.List;

public interface DataStatisticsPartnerService {

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
    List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithDay(Date startDate, Date endDate);

    /**
     * 用户购买消费数统计  周
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithWeek(Date startDate, Date endDate);

    /**
     * 用户购买消费数统计  月
     * @param startDate
     * @param endDate
     * @return
     */
    List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithMonth(Date startDate, Date endDate);

}

