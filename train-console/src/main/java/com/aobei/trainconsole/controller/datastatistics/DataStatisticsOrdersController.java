package com.aobei.trainconsole.controller.datastatistics;

import com.aobei.train.service.OrdersDataStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 数据统计 <br>
 * 订单数据统计
 *
 */
@Controller
@RequestMapping("/data_statistics/orders")
public class DataStatisticsOrdersController {

    @Autowired
    private OrdersDataStatisticsService ordersDataStatisticsService;


    /**
     * 跳转到订单数据统计页面
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "data_statistics/orders";
    }

    @ResponseBody
    @RequestMapping("/getDataSet")
    public Object getDataSet(int type,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return ordersDataStatisticsService.getOrdersDataMonth(startDate,endDate);
            case 2:
                return ordersDataStatisticsService.getOrdersDataWeek(startDate,endDate);
            default:
                return ordersDataStatisticsService.getOrdersDataDay(startDate,endDate);
        }
    }

    @ResponseBody
    @RequestMapping("/getDataSetTable")
    public Object getDataSetTable(int type,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return ordersDataStatisticsService.getOrdersTableDataMonth(startDate,endDate);
            case 2:
                return ordersDataStatisticsService.getOrdersTableDataWeek(startDate,endDate);
            default:
                return ordersDataStatisticsService.getOrdersTableDataDay(startDate,endDate);
        }
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
