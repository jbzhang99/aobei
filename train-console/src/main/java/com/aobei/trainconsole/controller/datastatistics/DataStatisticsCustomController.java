package com.aobei.trainconsole.controller.datastatistics;

import com.aobei.train.service.DataStatisticsCustomService;
import custom.bean.DataStatisticsCustomData;
import custom.bean.PurchaseCustomStatisticsData;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计 <br>
 * 顾客数据统计
 */
@Controller
@RequestMapping("/data_statistics/custom")
public class DataStatisticsCustomController {

    @Autowired
    private DataStatisticsCustomService dataStatisticsCustomService;
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

        return "data_statistics/custom";
    }


    /**
     * 加载 用户数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/loadCustomRegData")
    @ResponseBody
    public List<DataStatisticsCustomData> loadCustomRegData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return dataStatisticsCustomService.regStatisticsWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsCustomService.regStatisticsWithWeek(startDate, endDate);
            default:
                return dataStatisticsCustomService.regStatisticsWithDay(startDate, endDate);
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
    @GetMapping(value = "/downCustomRegData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downCustomRegData(
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
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM月")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM月")));
                list = dataStatisticsCustomService.regStatisticsWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsCustomService.regStatisticsWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsCustomService.regStatisticsWithDay(startDate, endDate);
                break;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(subTitle);

        Row row0 = CellUtil.getRow(0, sheet);
        CellUtil.createCell(row0, 0, "日期");

        Row row1 = CellUtil.getRow(1, sheet);
        CellUtil.createCell(row1, 0, "顾客数");

        int i = 1;
        for (DataStatisticsCustomData dscd : list) {
            CellUtil.createCell(row0, i, dscd.getDateStr());
            CellUtil.getCell(row1, i).setCellValue(dscd.getNum());
            sheet.autoSizeColumn(i);
            i++;
        }
        String fileName = "顾客相关数据导出" + subTitle + ".xlsx";
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
    @GetMapping("/loadCustomTableData")
    @ResponseBody
    public List<PurchaseCustomStatisticsData> loadCustomTableData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return dataStatisticsCustomService.purchaseCustomStatisticsDataWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsCustomService.purchaseCustomStatisticsDataWithWeek(startDate, endDate);
            default:
                return dataStatisticsCustomService.purchaseCustomStatisticsDataWithDay(startDate, endDate);
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
    @GetMapping(value = "/downCustomTableData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downCustomTableData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        List<PurchaseCustomStatisticsData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM月")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM月")));
                list = dataStatisticsCustomService.purchaseCustomStatisticsDataWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsCustomService.purchaseCustomStatisticsDataWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsCustomService.purchaseCustomStatisticsDataWithDay(startDate, endDate);
                break;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        String[] columnTitles = {"日期", "顾客总数", "产生消费的\n顾客总数", "产生复购的\n顾客总数", "复购率", "各端顾客数(小程序)", "各端顾客数(安卓)", "各端顾客数(IOS)", "各端顾客数(H5)"};

        XSSFSheet sheet = workbook.createSheet(subTitle);
        Row row0 = CellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            CellUtil.createCell(row0, i, columnTitles[i]);
        }

        int n = 1;
        long sumPurchaseTotalCustomNum = 0;
        long sumRePurchaseTotalCustomNum = 0;
        for (PurchaseCustomStatisticsData dscd : list) {
            Row row = CellUtil.getRow(n++, sheet);
            CellUtil.createCell(row, 0, dscd.getDateStr());
            CellUtil.getCell(row, 1).setCellValue(dscd.getTotalCustomNum());
            CellUtil.getCell(row, 2).setCellValue(dscd.getPurchaseTotalCustomNum());
            CellUtil.getCell(row, 3).setCellValue(dscd.getRePurchaseTotalCustomNum());
            CellUtil.createCell(row, 4, dscd.getPurchasePercent() + "%");
            CellUtil.getCell(row, 5).setCellValue(dscd.getClientNumMap().get("wx_m_custom"));
            CellUtil.getCell(row, 6).setCellValue(dscd.getClientNumMap().get("a_custom"));
            CellUtil.getCell(row, 7).setCellValue(dscd.getClientNumMap().get("i_custom"));
            CellUtil.getCell(row, 8).setCellValue(dscd.getClientNumMap().get("h5_custom"));
            sumPurchaseTotalCustomNum += dscd.getPurchaseTotalCustomNum();
            sumRePurchaseTotalCustomNum += dscd.getRePurchaseTotalCustomNum();
        }

        // 最后一行数据
        Row rowLast = CellUtil.getRow(n, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            if (n > 1) {
                Cell cell = CellUtil.getCell(rowLast, i);
                if (i == 0) {
                    cell.setCellValue("合计");
                } else if (i == 4) { // 复购率
                    if (sumRePurchaseTotalCustomNum > 0) {
                        cell.setCellValue(Math.round((sumRePurchaseTotalCustomNum * 1.00 / sumPurchaseTotalCustomNum * 1.00) * 100) + "%");
                    } else {
                        cell.setCellValue("0%");
                    }
                } else {
                    String colString = CellReference.convertNumToColString(i);
                    cell.setCellFormula(String.format("SUM(%s%d:%s%d)", colString, 2, colString, n));
                }
            }
            sheet.autoSizeColumn(i);
        }

        String fileName = "顾客相关数据表格导出" + subTitle + ".xlsx";
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
    @GetMapping("/loadCustomMapData")
    @ResponseBody
    public List<DataStatisticsCustomData> loadCustomMapData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        // 暂定用户注册数据都为北京
        List<DataStatisticsCustomData> list = dataStatisticsCustomService.regStatisticsWithMonth(startDate, endDate);
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
    @GetMapping(value = "/downCustomMapData", produces = "application/vnd.ms-excel; charset=utf-8")
    @ResponseBody
    public void downCustomMapData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        // 暂定用户注册数据都为北京
        List<DataStatisticsCustomData> list = dataStatisticsCustomService.regStatisticsWithMonth(startDate, endDate);
        long sum = list.stream().mapToLong(n -> n.getNum()).sum();
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("北京", sum);

        XSSFWorkbook workbook = new XSSFWorkbook();
        String[] columnTitles = {"区域", "顾客数"};

        XSSFSheet sheet = workbook.createSheet("区域顾客数");
        Row row0 = CellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            CellUtil.createCell(row0, i, columnTitles[i]);
        }

        int n = 1;
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            Row row = CellUtil.getRow(n++, sheet);
            CellUtil.createCell(row, 0, entry.getKey());
            CellUtil.getCell(row, 1).setCellValue(entry.getValue());
        }

        for (int i = 0; i < columnTitles.length; i++) {
            sheet.autoSizeColumn(i);
        }
        String subTitle = String.format(
                "%s至%s",
                LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String fileName = "地域顾客数导出" + subTitle + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }


}
