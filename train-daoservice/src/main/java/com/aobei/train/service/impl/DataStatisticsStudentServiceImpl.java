package com.aobei.train.service.impl;

import com.aobei.train.mapper.DataStatisticsStudentMapper;
import com.aobei.train.model.StudentExample;
import com.aobei.train.service.DataStatisticsStudentService;
import com.aobei.train.service.bean.PurchaseStudentStatisticsData;
import custom.bean.DataStatisticsCustomData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataStatisticsStudentServiceImpl implements DataStatisticsStudentService {

    @Autowired
    private DataStatisticsStudentMapper dataStatisticsStudentMapper;


    private static final String[] CUSTOM_CLIENTS = {"wx_m_custom", "a_custom", "i_custom", "h5_custom"};

    /**
     * a按日查询数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithDay(Date startDate, Date endDate) {
        StudentExample studentExample=new StudentExample();
        studentExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list = dataStatisticsStudentMapper.regStatisticsWithDay(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusDays(1);
            i++;
        }
        return list;
    }
    /**
     * 按周查询数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithWeek(Date startDate, Date endDate) {
        StudentExample studentExample=new StudentExample();
        studentExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list =dataStatisticsStudentMapper.regStatisticsWithWeek(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/ww周"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusWeeks(1);
            i++;
        }
        return list;
    }
    /**
     * 按月查询数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithMonth(Date startDate, Date endDate) {
        StudentExample studentExample=new StudentExample();
        studentExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list =dataStatisticsStudentMapper.regStatisticsWithMonth(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM月"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusMonths(1);
            i++;
        }
        return list;
    }

    /**
     * 按日查询 数据表格数据  每一列数据进行封装
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PurchaseStudentStatisticsData> purchaseStudentStatisticsDataWithDay(Date startDate, Date endDate) {
        //服务人员总数
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithDay(startDate, endDate);

        //服务单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsStudentMapper.purchaseCustomStatisticsWithDay(startDate, endDate);
        //数据补全
        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchaseStudentStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchaseStudentStatisticsData pcsdObj=new PurchaseStudentStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setServiceunitTotalNum(stepDate1);

            list.add(pcsdObj);
        }
        return list;
    }

    /**
     * 按周 数据表格数据  每一列数据进行封装
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PurchaseStudentStatisticsData> purchaseStudentStatisticsDataWithWeek(Date startDate, Date endDate) {
        //服务人员总数
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithWeek(startDate, endDate);
        //服务单总数
        List<DataStatisticsCustomData> listData1 = dataStatisticsStudentMapper.purchaseCustomStatisticsWithWeek(startDate, endDate);
        //数据补全
        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        List<PurchaseStudentStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchaseStudentStatisticsData pcsdObj=new PurchaseStudentStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setServiceunitTotalNum(stepDate1);
            list.add(pcsdObj);
        }
        return list;
    }

    /**
     * 按月 数据表格数据  每一列数据进行封装
     */
    @Override
    public List<PurchaseStudentStatisticsData> purchaseStudentStatisticsDataWithMonth(Date startDate, Date endDate) {
        //服务人员总数
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithMonth(startDate, endDate);
        //服务单总数
        List<DataStatisticsCustomData> listData1 = dataStatisticsStudentMapper.purchaseCustomStatisticsWithMonth(startDate, endDate);
        //数据补全
        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchaseStudentStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchaseStudentStatisticsData pcsdObj=new PurchaseStudentStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setServiceunitTotalNum(stepDate1);
            list.add(pcsdObj);
        }
        return list;
    }

}
