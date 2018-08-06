package com.aobei.trainconsole.controller.datastatistics;

import com.aobei.train.service.OrdersDataStatisticsService;
import custom.bean.DataResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据统计 <br>
 * dashboard概况
 *
 */
@Controller
@RequestMapping("/data_statistics/dashboard")
public class DataStatisticsDashboardController {


    @Autowired
    private OrdersDataStatisticsService ordersDataStatisticsService;

    @GetMapping("/index")
    public String index(){
        return "data_statistics/dashboard";
    }

    /**
     * 订单总GMV统计
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/gmvYearTotalSum")
    public Object gmvYearTotalSum(int type,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        Map<String, Long> map = ordersDataStatisticsService.getOrdersDataMonth(startDate, endDate);
        Iterator<Map.Entry<String, Long>> iterator = map.entrySet().iterator();
        long gmv = 0;
        while (iterator.hasNext()){
            Map.Entry<String, Long> entry = iterator.next();
            gmv = gmv + entry.getValue();
        }
        DataResultSet dataResultSet = new DataResultSet();
        dataResultSet.setDateStr("总销售额");
        dataResultSet.setNum(gmv);
        return dataResultSet;
    }

    /**
     * 订单总GMV柱状图统计
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/gmvYearSum")
    public Object gmvYearSum(int type,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        return ordersDataStatisticsService.getOrdersDataMonth(startDate,endDate);
    }

    /**
     * APP GMV统计
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/gmvYearAppSum")
    public Object gmvYearAppSum(int type,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        Long a_custom_gmv = ordersDataStatisticsService.getOrdersGmvByClient(startDate, endDate, "a_custom");
        Long i_custom_gmv = ordersDataStatisticsService.getOrdersGmvByClient(startDate, endDate, "i_custom");
        DataResultSet dataResultSet = new DataResultSet();
        dataResultSet.setDateStr("APP");
        dataResultSet.setNum(a_custom_gmv + i_custom_gmv);
        return dataResultSet;
    }

    /**
     * 小程序 GMV统计
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/gmvYearWxmSum")
    public Object gmvYearWxmSum(int type,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        Long gmv = ordersDataStatisticsService.getOrdersGmvByClient(startDate, endDate, "wx_m_custom");
        DataResultSet dataResultSet = new DataResultSet();
        dataResultSet.setDateStr("小程序");
        dataResultSet.setNum(gmv);
        return dataResultSet;
    }

    /**
     * H5 GMV统计
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/gmvYearH5Sum")
    public Object gmvYearH5Sum(int type,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        Long gmv = ordersDataStatisticsService.getOrdersGmvByClient(startDate, endDate, "h5_custom");
        DataResultSet dataResultSet = new DataResultSet();
        dataResultSet.setDateStr("H5");
        dataResultSet.setNum(gmv);
        return dataResultSet;
    }

    /**
     * 电商 GMV统计
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/gmvYearEbSum")
    public Object gmvYearEbSum(int type,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        Long gmv = ordersDataStatisticsService.getOrdersGmvByClient(startDate, endDate, "eb_custom");
        DataResultSet dataResultSet = new DataResultSet();
        dataResultSet.setDateStr("电商");
        dataResultSet.setNum(gmv);
        return dataResultSet;
    }


    /**
     * 处理日期范围向后的边界值
     *
     * @param endDate
     * @return endDate 23:59:59
     */
    private Date endDateBoundary(Date endDate) {
        LocalDateTime endLocalDate = LocalDateTime
                .ofInstant(endDate.toInstant(), ZoneId.systemDefault())
                .plusDays(1L).minusSeconds(1L);
        return Date.from(endLocalDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
