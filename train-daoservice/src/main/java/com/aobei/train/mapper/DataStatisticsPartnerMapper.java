package com.aobei.train.mapper;

import com.aobei.train.model.DataStatisticsSinglePartnerData;
import custom.bean.DataStatisticsCustomData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 数据统计  顾客端
 */
@Mapper
public interface DataStatisticsPartnerMapper {


    /**
     * 按天 查找用户注册量
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找用户注册量
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找用户注册量
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按天 查找用户注册量  client 区分
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regClientStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找用户注册量   client 区分
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regClientStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找用户注册量   client 区分
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regClientStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查找用户注册量   client 区分
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> regClientStatistics(@Param("endDate") Date endDate);



    /**
     * 按天 查找产生消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> purchaseCustomStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找产生消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> purchaseCustomStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找产生消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> purchaseCustomStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 按天 查找产生复购消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> rePurchaseCustomStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找产生复购消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> rePurchaseCustomStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找产生复购消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> rePurchaseCustomStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查找某个日期前 有购买的用户数
     * @return
     */
    Long countPurchaseCustomStatistics(@Param("endDate") Date endDate);

    /**
     * 查找某个日期前 有复购的用户数
     * @return
     */
    Long countRePurchaseCustomStatistics(@Param("endDate") Date endDate);


    //按日  合伙人派单数
    List<DataStatisticsCustomData> sendOrdersPartnerStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按日合伙人拒单数
    List<DataStatisticsCustomData> singleOrdersPartnerStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按周  合伙人派单数
    List<DataStatisticsCustomData> sendOrdersPartnerStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按周  合伙人拒单数
    List<DataStatisticsCustomData> singleOrdersPartnerStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按月  合伙人派单数
    List<DataStatisticsCustomData> sendOrdersPartnerStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按月  合伙人拒单数
    List<DataStatisticsCustomData> singleOrdersPartnerStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按日   单个合伙人派单数
    List<DataStatisticsSinglePartnerData> oneSendOrdersPartnerStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按日   单个合伙人拒单数
    List<DataStatisticsSinglePartnerData> oneSingleOrdersPartnerStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
