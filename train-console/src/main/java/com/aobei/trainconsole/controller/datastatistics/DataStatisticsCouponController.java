package com.aobei.trainconsole.controller.datastatistics;

import com.aobei.train.service.DataStatisticsCouponService;
import custom.bean.PurchaseCouponStatisticsData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 数据统计 <br>
 * 顾客数据统计
 */
@Controller
@RequestMapping("/data_statistics/coupon")
public class DataStatisticsCouponController {

    @Autowired
    private DataStatisticsCouponService dataStatisticsCouponService;
    ;

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


    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/index")
    public String index() {
        return "data_statistics/coupon";
    }


    /**
     * 加载 用优惠卷数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadCouponData")
    @ResponseBody
    public List<PurchaseCouponStatisticsData> loadCouponData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return dataStatisticsCouponService.couponStatisticsWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsCouponService.couponStatisticsWithWeek(startDate, endDate);
            default:
                return dataStatisticsCouponService.couponStatisticsWithDay(startDate, endDate);
        }
    }

    /**
     * 下载 优惠卷数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downCouponData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downCouponData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        List<PurchaseCouponStatisticsData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                list = dataStatisticsCouponService.couponStatisticsWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsCouponService.couponStatisticsWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsCouponService.couponStatisticsWithDay(startDate, endDate);
                break;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(subTitle);

        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        HSSFCellUtil.createCell(row0, 0, "日期");

        HSSFRow row1 = HSSFCellUtil.getRow(1, sheet);
        HSSFCellUtil.createCell(row1, 0, "优惠卷总额");

        HSSFRow row2 = HSSFCellUtil.getRow(2, sheet);
        HSSFCellUtil.createCell(row2, 0, "优惠卷使用总额");

        int i = 1;
        for (PurchaseCouponStatisticsData dscd : list) {
            HSSFCellUtil.createCell(row0, i, dscd.getDateStr());
            HSSFCellUtil.getCell(row1, i).setCellValue(dscd.getTotalPlanMoney() > 0 ? dscd.getTotalPlanMoney() * 0.01 : 0);
            HSSFCellUtil.getCell(row2, i).setCellValue(dscd.getTotalUsedMoney() > 0 ? dscd.getTotalUsedMoney() * 0.01 : 0);
            sheet.autoSizeColumn(i);
            i++;
        }
        String fileName = "优惠券相关数据图表导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());

    }

}
