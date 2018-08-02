package com.aobei.train.mapper;

import custom.bean.DataResultSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by mr_bl on 2018/8/1.
 */
@Mapper
public interface OrdersDataStatisticsMapper {

    List<DataResultSet> getOrdersDataDay(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersDataWeek(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersDataMonth(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumDay(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumWeek(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumMonth(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumClientDay(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumClientWeek(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumClientMonth(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    List<DataResultSet> getOrdersNumByStatusDay(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime,@Param("status") Integer status);

    List<DataResultSet> getOrdersNumByStatusWeek(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime,@Param("status") Integer status);

    List<DataResultSet> getOrdersNumByStatusMonth(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime,@Param("status") Integer status);

    List<DataResultSet> getOrdersNumMap(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);
}
