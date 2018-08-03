package com.aobei.trainconsole.controller.datastatistics;

import com.aobei.train.model.DataStatisticsSinglePartnerData;
import com.aobei.train.service.DataStatisticsCustomService;
import com.aobei.train.service.DataStatisticsPartnerService;
import com.aobei.train.service.bean.PurchasePartnerStatisticsData;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import custom.bean.DataStatisticsCustomData;
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
import java.util.*;
import java.util.Date;
import java.util.List;

/**
 * 数据统计 <br>
 * 顾客数据统计
 */
@Controller
@RequestMapping("/data_statistics/partner")
public class DataStatisticsPartnerController {

    @Autowired
    private DataStatisticsPartnerService dataStatisticsPartnerService;

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
        return "data_statistics/partner";
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
                return dataStatisticsPartnerService.incrementingRegStatisticsWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsPartnerService.incrementingRegStatisticsWithWeek(startDate, endDate);
            default:
                return dataStatisticsPartnerService.incrementingRegStatisticsWithDay(startDate, endDate);
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
    @GetMapping(value = "/downPartnerRegData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downPartnerRegData(
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
                list = dataStatisticsPartnerService.incrementingRegStatisticsWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsPartnerService.incrementingRegStatisticsWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsPartnerService.incrementingRegStatisticsWithDay(startDate, endDate);
                break;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(subTitle);

        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        HSSFCellUtil.createCell(row0, 0, "日期");

        HSSFRow row1 = HSSFCellUtil.getRow(1, sheet);
        HSSFCellUtil.createCell(row1, 0, "合伙人数");

        int i = 1;
        for (DataStatisticsCustomData dscd : list) {
            HSSFCellUtil.createCell(row0, i, dscd.getDateStr());
            HSSFCellUtil.getCell(row1, i).setCellValue(dscd.getNum());
            sheet.autoSizeColumn(i);
            i++;
        }
        String fileName = "合伙人相关数据导出" + subTitle + ".xls";
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
    public List<PurchasePartnerStatisticsData> loadCustomTableData(
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = endDateBoundary(endDate);
        switch (type) {
            case 3:
                return dataStatisticsPartnerService.purchasePartnerStatisticsDataWithMonth(startDate, endDate);
            case 2:
                return dataStatisticsPartnerService.purchasePartnerStatisticsDataWithWeek(startDate, endDate);
            default:
                return dataStatisticsPartnerService.purchasePartnerStatisticsDataWithDay(startDate, endDate);
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
    @GetMapping(value = "/downPartnerTableData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downPartnerTableData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        List<PurchasePartnerStatisticsData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                list = dataStatisticsPartnerService.purchasePartnerStatisticsDataWithMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = dataStatisticsPartnerService.purchasePartnerStatisticsDataWithWeek(startDate, endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = dataStatisticsPartnerService.purchasePartnerStatisticsDataWithDay(startDate, endDate);
                break;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        List<String> columnTitles =new ArrayList<>();
        columnTitles.add("日期");
        columnTitles.add("合伙人总数");
        columnTitles.add("派单总数");
        columnTitles.add("拒单总数");
        columnTitles.add("接单率");
        int s=0;
        List<String> longList=new ArrayList<>();

        for (PurchasePartnerStatisticsData dscd : list) {
            List<DataStatisticsSinglePartnerData> dscdList = dscd.getList();
            if(s==0){
                dscdList.stream().forEach(dd ->{
                    columnTitles.add(dd.getName()+"派单数");
                    columnTitles.add(dd.getName()+"拒单数");
                    columnTitles.add(dd.getName()+"接单率");
                });
            }
            s++;
        }
        XSSFSheet sheet = workbook.createSheet(subTitle);
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnTitles.size(); i++) {
            row.createCell(i).setCellValue(columnTitles.get(i)+"");
            if(i>4){
                longList.add("0");
            }
        }

        int n = 1;
        long sumPurchaseTotalCustomNum = 0;
        long sumRePurchaseTotalCustomNum = 0;
        for (PurchasePartnerStatisticsData dscd : list) {
            List<DataStatisticsSinglePartnerData> dscdList = dscd.getList();

            XSSFRow rows = sheet.createRow(n++);
            rows.createCell(0).setCellValue(dscd.getDateStr());
            rows.createCell(1).setCellValue(dscd.getTotalCustomNum());
            rows.createCell(2).setCellValue(dscd.getSendOrdersTotalNum());
            rows.createCell(3).setCellValue(dscd.getSingleOrdersTotalNum());
            rows.createCell(4).setCellValue(dscd.getOrderRate() + "%");

            int num=0;

            for(int i=0;i<dscdList.size()*3;i+=3){
                longList.set(i,(Integer.parseInt(longList.get(i))+(int)(long)dscdList.get(num).getSendNum())+"");
                longList.set(i+1,(Integer.parseInt(longList.get(i+1))+(int)(long)dscdList.get(num).getSendNum())+"");
                rows.createCell(4+i+1).setCellValue(dscdList.get(num).getSendNum()+"");
                rows.createCell(4+(i+1)+1).setCellValue(dscdList.get(num).getSingleNum()+"");
                rows.createCell(4+(i+2)+1).setCellValue(dscdList.get(num).getOrderRate()+"%");
                num++;
            }
            sumPurchaseTotalCustomNum += dscd.getSendOrdersTotalNum();
            sumRePurchaseTotalCustomNum += dscd.getSingleOrdersTotalNum();
        }

        // 最后一行数据
        XSSFRow rowLast = sheet.createRow(n);
        int colNum=0;
        for (int i = 0; i < columnTitles.size(); i++) {
            if(n>1){
                XSSFCell cell = rowLast.createCell(i);
                if(i==0) {
                    cell.setCellValue("合计");
                }else  if (i == 4){//接单率
                    if (sumRePurchaseTotalCustomNum > 0) {
                        cell.setCellValue(Math.round((sumRePurchaseTotalCustomNum * 1.00 / sumPurchaseTotalCustomNum * 1.00) * 100) + "%");
                    } else {
                        cell.setCellValue("0%");
                    }
                }else if(i>4){
                    //for (int j = 0; j < longList.size(); j+=3) {

                        rowLast.getCell(i).setCellValue(longList.get(colNum));
                        rowLast.createCell(i+1).setCellValue(longList.get(colNum));
                        if(Integer.parseInt(longList.get(colNum))==0){
                            rowLast.createCell(i+2).setCellValue("0%");
                        }else{
                            rowLast.createCell(i+2).setCellValue(Math.round((Integer.parseInt(longList.get(colNum)) * 1.00 / (Integer.parseInt(longList.get(colNum))+Integer.parseInt(longList.get(colNum+1))) * 1.00) * 100) + "%");
                        }
                    //}
                    i=i+2;
                    colNum+=3;
                } else{
                    String colString = CellReference.convertNumToColString(i);
                    cell.setCellFormula(String.format("SUM(%s%d:%s%d)", colString, 2, colString, n));
                }

            }
        }


        String fileName = "合伙人相关数据表格导出" + subTitle + ".xls";
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
        List<DataStatisticsCustomData> list = dataStatisticsPartnerService.incrementingRegStatisticsWithMonth(startDate, endDate);
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
    @GetMapping(value = "/downPartnerMapData", produces = "application/vnd.ms-excel; charset=utf-8")
    @ResponseBody
    public void downPartnerMapData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        // 暂定用户注册数据都为北京
        List<DataStatisticsCustomData> list = dataStatisticsPartnerService.incrementingRegStatisticsWithMonth(startDate, endDate);
        long sum = list.stream().mapToLong(n -> n.getNum()).sum();
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("北京", sum);

        HSSFWorkbook workbook = new HSSFWorkbook();
        DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
        String[] columnTitles = {"区域", "合伙人数"};

        HSSFSheet sheet = workbook.createSheet("区域合伙人数");
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
        String fileName = "地域合伙人数导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

}
