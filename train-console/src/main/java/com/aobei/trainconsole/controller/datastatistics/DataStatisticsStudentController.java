package com.aobei.trainconsole.controller.datastatistics;

import com.aobei.train.service.DataStatisticsCustomService;
import com.aobei.train.service.DataStatisticsStudentService;
import com.aobei.train.service.bean.PurchaseStudentStatisticsData;
import custom.bean.DataStatisticsCustomData;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计 <br>
 * 顾客数据统计
 */
@Controller
@RequestMapping("/data_statistics/student")
public class DataStatisticsStudentController {

    @Autowired
    private DataStatisticsCustomService dataStatisticsCustomService;

    @Autowired
    private DataStatisticsStudentService dataStatisticsStudentService;

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

        return "data_statistics/student";
    }


    /**
     * 加载 用户数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadStudentRegData")
    @ResponseBody
    public List<DataStatisticsCustomData> loadStudentRegData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return dataStatisticsStudentService.incrementingRegStatisticsWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsStudentService.incrementingRegStatisticsWithWeek(startDate, endDate);
            default:
                return dataStatisticsStudentService.incrementingRegStatisticsWithDay(startDate, endDate);
        }
    }

    /**
     * 下载 用户数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downStudentRegData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downStudentRegData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        List<DataStatisticsCustomData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                list = dataStatisticsStudentService.incrementingRegStatisticsWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsStudentService.incrementingRegStatisticsWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsStudentService.incrementingRegStatisticsWithDay(startDate, endDate);
                break;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(subTitle);

        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        HSSFCellUtil.createCell(row0, 0, "日期");

        HSSFRow row1 = HSSFCellUtil.getRow(1, sheet);
        HSSFCellUtil.createCell(row1, 0, "服务人员数数");

        int i = 1;
        for (DataStatisticsCustomData dscd : list) {
            HSSFCellUtil.createCell(row0, i, dscd.getDateStr());
            HSSFCellUtil.getCell(row1, i).setCellValue(dscd.getNum());
            sheet.autoSizeColumn(i);
            i++;
        }
        String fileName = "服务人员相关数据导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());

    }

    /**
     * 加载 用户表格中数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadStudentTableData")
    @ResponseBody
    public List<PurchaseStudentStatisticsData> loadStudentTableData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return dataStatisticsStudentService.purchaseStudentStatisticsDataWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsStudentService.purchaseStudentStatisticsDataWithWeek(startDate, endDate);
            default:
                return dataStatisticsStudentService.purchaseStudentStatisticsDataWithDay(startDate, endDate);
        }
    }

    /**
     * 下载 表格用户相关数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downStudentTableData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downStudentTableData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        List<PurchaseStudentStatisticsData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                list = dataStatisticsStudentService.purchaseStudentStatisticsDataWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsStudentService.purchaseStudentStatisticsDataWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsStudentService.purchaseStudentStatisticsDataWithDay(startDate, endDate);
                break;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        String[] columnTitles = {"日期", "服务人员总数","在职人数","离职人数","流失率","服务单数"};

        HSSFSheet sheet = workbook.createSheet(subTitle);
        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            HSSFCellUtil.createCell(row0, i, columnTitles[i]);
        }

        int n = 1;
        long studentNum = 0;
        long noJobNum = 0;
        for (PurchaseStudentStatisticsData dscd : list) {
            HSSFRow row = HSSFCellUtil.getRow(n++, sheet);
            HSSFCellUtil.createCell(row, 0, dscd.getDateStr());
            HSSFCellUtil.getCell(row, 1).setCellValue(dscd.getTotalCustomNum());
            HSSFCellUtil.getCell(row, 2).setCellValue(dscd.getOnJobNum());
            HSSFCellUtil.getCell(row, 3).setCellValue(dscd.getNoJobNum());
            HSSFCellUtil.getCell(row, 4).setCellValue(dscd.getRunoffNum()+"%");
            HSSFCellUtil.getCell(row, 5).setCellValue(dscd.getServiceunitTotalNum());
            studentNum += dscd.getTotalCustomNum();
            noJobNum += dscd.getNoJobNum();
        }

        // 最后一行数据
        HSSFRow rowLast = HSSFCellUtil.getRow(n, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            if (n > 1) {
                HSSFCell cell = HSSFCellUtil.getCell(rowLast, i);
                if (i == 0) {
                    cell.setCellValue("合计");
                } else if (i == 4) { // 复购率
                    if (noJobNum > 0) {
                        cell.setCellValue(Math.round((noJobNum * 1.00 / studentNum * 1.00) * 100) + "%");
                    } else {
                        cell.setCellValue("0%");
                    }
                }  else {
                    String colString = CellReference.convertNumToColString(i);
                    cell.setCellFormula(String.format("SUM(%s%d:%s%d)", colString, 2, colString, n));
                }
            }
            sheet.autoSizeColumn(i);
        }

        String fileName = "服务人员相关数据表格导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());

    }


    /**
     * 加载 用户地图数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadStudentMapData")
    @ResponseBody
    public List<DataStatisticsCustomData> loadStudentMapData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        // 暂定用户注册数据都为北京
        List<DataStatisticsCustomData> list = dataStatisticsStudentService.incrementingRegStatisticsWithMonth(startDate, endDate);
        long sum = list.stream().mapToLong(n -> n.getNum()).sum();
        DataStatisticsCustomData beijin = new DataStatisticsCustomData();
        beijin.setDateStr("北京");
        beijin.setNum(sum);
        list.clear();
        list.add(beijin);
        return list;
    }

    /**
     * 下载 用户地图数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downStudentMapData", produces = "application/vnd.ms-excel; charset=utf-8")
    @ResponseBody
    public void downStudentMapData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        // 暂定用户注册数据都为北京
        List<DataStatisticsCustomData> list = dataStatisticsStudentService.incrementingRegStatisticsWithMonth(startDate, endDate);
        long sum = list.stream().mapToLong(n -> n.getNum()).sum();
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("北京", sum);

        HSSFWorkbook workbook = new HSSFWorkbook();
        DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
        String[] columnTitles = {"区域", "服务人员数"};

        HSSFSheet sheet = workbook.createSheet("区域服务人员数数");
        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            HSSFCellUtil.createCell(row0, i, columnTitles[i]);
        }

        int n = 1;
        for (Map.Entry<String,Long> entry : map.entrySet()) {
            HSSFRow row = HSSFCellUtil.getRow(n++, sheet);
            HSSFCellUtil.createCell(row, 0, entry.getKey());
            HSSFCellUtil.getCell(row, 1).setCellValue(entry.getValue());
        }

        for (int i = 0; i < columnTitles.length; i++) {
            sheet.autoSizeColumn(i);
        }
        String subTitle = String.format(
                "%s至%s",
                LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String fileName = "地域服务人员数导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }


}
