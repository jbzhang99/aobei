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
}
