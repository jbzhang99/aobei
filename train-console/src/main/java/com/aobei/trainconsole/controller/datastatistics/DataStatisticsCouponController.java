package com.aobei.trainconsole.controller.datastatistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.DataStatisticsCouponService;
import custom.bean.AreaData;
import custom.bean.CouponStatisticsData;
import custom.bean.CouponTableStatisticsData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
     * 加载 优惠卷数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadCouponData")
    @ResponseBody
    public List<CouponStatisticsData> loadCouponData(
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
        List<CouponStatisticsData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM月")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM月")));
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

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(subTitle);

        Row row0 = CellUtil.getRow(0, sheet);
        CellUtil.createCell(row0, 0, "日期");

        Row row1 = CellUtil.getRow(1, sheet);
        CellUtil.createCell(row1, 0, "优惠卷总额");

        Row row2 = CellUtil.getRow(2, sheet);
        CellUtil.createCell(row2, 0, "优惠卷使用总额");

        int i = 1;
        for (CouponStatisticsData dscd : list) {
            CellUtil.createCell(row0, i, dscd.getDateStr());
            CellUtil.getCell(row1, i).setCellValue(dscd.getTotalPlanMoney() > 0 ? dscd.getTotalPlanMoney() * 0.01 : 0);
            CellUtil.getCell(row2, i).setCellValue(dscd.getTotalUsedMoney() > 0 ? dscd.getTotalUsedMoney() * 0.01 : 0);
            sheet.autoSizeColumn(i);
            i++;
        }
        String fileName = "优惠券相关数据图表导出" + subTitle + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }


    /**
     * 加载 用优惠卷数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadCouponTableData")
    @ResponseBody
    public List<CouponTableStatisticsData> loadCouponTableData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        return dataStatisticsCouponService.couponTableDatas(startDate, endDate);
    }

    /**
     * 下载优惠卷表格数据
     *
     * @param response
     * @param startDate
     * @param endDate
     * @throws IOException
     */
    @GetMapping(value = "/downCouponTableData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downCouponTableData(
            HttpServletResponse response,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        List<CouponTableStatisticsData> list = dataStatisticsCouponService.couponTableDatas(startDate, endDate);
        String subTitle = String.format(
                "%s至%s",
                LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        XSSFWorkbook workbook = new XSSFWorkbook();
        String[] columnTitles = {"日期", "优惠券总金额", "优惠券数量", "优惠券使用类别", "优惠券使用有效期", "优惠券产生GMV", "优惠券拉新用户数", "优惠券使用总张数", "优惠券使用总金额"};

        XSSFSheet sheet = workbook.createSheet(subTitle);
        Row row0 = CellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            CellUtil.createCell(row0, i, columnTitles[i]);
        }

        int n = 1;
        for (CouponTableStatisticsData ctsd : list) {
            Row row = CellUtil.getRow(n++, sheet);
            CellUtil.createCell(row, 0, ctsd.getDateStr());
            CellUtil.getCell(row, 1).setCellValue(ctsd.getPlanMoney() == null ? 0 : ctsd.getPlanMoney() * 0.01);
            CellUtil.getCell(row, 2).setCellValue(ctsd.getNumTotal());
            CellUtil.getCell(row, 3).setCellValue(ctsd.getType());
            CellUtil.createCell(row, 4, String.format("%s - %s", ctsd.getUseStartDatetime(), ctsd.getUseEndDatetime()));
            CellUtil.getCell(row, 5).setCellValue(ctsd.getGmv());
            CellUtil.getCell(row, 6).setCellValue(ctsd.getRegUserCount());
            CellUtil.getCell(row, 7).setCellValue(ctsd.getNumUsed());
            CellUtil.getCell(row, 8).setCellValue(ctsd.getTotalUsedMoney() * 0.01);
        }

        // 最后一行数据
        Row rowLast = CellUtil.getRow(n, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            if (n > 1) {
                Cell cell = CellUtil.getCell(rowLast, i);
                if (i == 0) {
                    cell.setCellValue("合计");
                } else if (i == 3 || i == 4) {
                    // no set
                } else {
                    String colString = CellReference.convertNumToColString(i);
                    cell.setCellFormula(String.format("SUM(%s%d:%s%d)", colString, 2, colString, n));
                }
            }
            sheet.autoSizeColumn(i);
        }
        String fileName = "优惠券相关数据图表" + subTitle + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());

    }

    /**
     * 加载 优惠卷地图数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadCouponMapData")
    @ResponseBody
    public List<AreaData<Double>> loadCouponTableData(
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        List<AreaData<Double>> list = dataStatisticsCouponService.couponUsedOrderMoneyAreaData(startDate, endDate);
        return list;
    }


    /**
     * 下载 地图数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downCouponMapData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downCouponMapData(
            HttpServletResponse response,
            int type,
            String data,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {

        String subTitle = String.format(
                "按日%s至%s",
                LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(subTitle);
        Row row0 = CellUtil.getRow(0, sheet);
        CellUtil.createCell(row0, 0, "区域");
        CellUtil.createCell(row0, 1, "优惠券使用金额");
        JSONArray jsonArray = JSON.parseArray(data);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Row row = CellUtil.getRow(i + 1, sheet);
            CellUtil.getCell(row, 0).setCellValue(obj.getString("name"));
            CellUtil.getCell(row, 1).setCellValue(obj.getDouble("value"));
        }
        String fileName = "优惠券相关数据地图导出" + subTitle + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

}
