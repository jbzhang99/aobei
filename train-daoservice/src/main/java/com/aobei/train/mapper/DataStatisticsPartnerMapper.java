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
    List<DataStatisticsCustomData> oneSendOrdersPartnerStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按日   单个合伙人拒单数
    List<DataStatisticsCustomData> oneSingleOrdersPartnerStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按周   单个合伙人派单数
    List<DataStatisticsCustomData> oneSendOrdersPartnerStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按周   单个合伙人拒单数
    List<DataStatisticsCustomData> oneSingleOrdersPartnerStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按月   单个合伙人派单数
    List<DataStatisticsCustomData> oneSendOrdersPartnerStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //按月   单个合伙人拒单数
    List<DataStatisticsCustomData> oneSingleOrdersPartnerStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
