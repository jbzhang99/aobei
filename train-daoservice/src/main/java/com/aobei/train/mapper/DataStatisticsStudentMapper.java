package com.aobei.train.mapper;

import custom.bean.DataStatisticsCustomData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 数据统计  顾客端
 */
@Mapper
public interface DataStatisticsStudentMapper {


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
     * 按天 查找服务人员的服务单数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> purchaseStudentStatisticsWithDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找产生消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> purchaseStudentStatisticsWithWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 按月 查找产生消费的用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    List<DataStatisticsCustomData> purchaseStudentStatisticsWithMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
