package com.aobei.train.mapper;

import custom.bean.DataStatisticsCustomData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface DataStatisticsCouponMapper {

    /**
     * 按天 优惠卷 预算金额统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> planMoneyStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按周 优惠卷 预算金额统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> planMoneyStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 优惠卷 预算金额统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> planMoneyStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 按天 优惠卷 使用金额统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> usedMoneyStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按周 优惠卷 使用金额统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> usedMoneyStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 优惠卷 使用金额统计
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> usedMoneyStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 优惠卷产生订单消费总额
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> couponPayedOrderMoney(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 拉新用户数量
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> couponRegUserCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 优惠卷产生订单优惠总额
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> couponUsedOrderMoney(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
