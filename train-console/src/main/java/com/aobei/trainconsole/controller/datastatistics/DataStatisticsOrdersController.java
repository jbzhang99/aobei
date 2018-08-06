package com.aobei.trainconsole.controller.datastatistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.OrdersDataStatisticsService;
import com.aobei.train.service.bean.OrdersStatisticsData;
import custom.bean.AreaData;
import custom.bean.DataResultSet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.Row;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 柱状图加载
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
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

    /**
     * 订单相关数据表格加载
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
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
     * 订单数据地图加载
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/getDataSetMap")
    public Object getDataSetMap(int type,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        endDate = endDateBoundary(endDate);
        List<AreaData<Long>> list = ordersDataStatisticsService.getOrdersNumMapUp(startDate, endDate);
        return list;
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

    /**
     * 下载 订单柱状图数据
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downOrdersGmvData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downOrdersGmvData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        Map<String, Object> map = downloadDataPack(type, startDate, endDate);
        String subTitle = (String)map.get("subTitle");
        List<OrdersStatisticsData> list = (List<OrdersStatisticsData>)map.get("list");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(subTitle);

        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        HSSFCellUtil.createCell(row0, 0, "日期");

        HSSFRow row1 = HSSFCellUtil.getRow(1, sheet);
        HSSFCellUtil.createCell(row1, 0, "订单数");

        int i = 1;
        for (OrdersStatisticsData data : list) {
            HSSFCellUtil.createCell(row0, i, data.getDateStr());
            HSSFCellUtil.getCell(row1, i).setCellValue(data.getGmv());
            sheet.autoSizeColumn(i);
            i++;
        }
        String fileName = "订单GMV数据导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    /**
     * 下载 相关数据表格
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downOrdersTableData", produces = "application/vnd.ms-excel; charset=utf-8")
    public void downOrdersTableData(
            HttpServletResponse response,
            int type,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);

        Map<String, Object> map = downloadDataPack(type, startDate, endDate);
        String subTitle = (String)map.get("subTitle");
        List<OrdersStatisticsData> list = (List<OrdersStatisticsData>)map.get("list");

        HSSFWorkbook workbook = new HSSFWorkbook();
        String[] columnTitles = {"日期", "GMV", "各端订单数(小程序)", "各端订单数(安卓)", "各端订单数(IOS)",
                "各端订单数(H5)","各端订单数(电商)","订单合计(合计)","订单合计(已完成)","订单合计(待服务)"};

        HSSFSheet sheet = workbook.createSheet(subTitle);
        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            HSSFCellUtil.createCell(row0, i, columnTitles[i]);
        }

        int n = 1;
        for (OrdersStatisticsData data : list) {
            HSSFRow row = HSSFCellUtil.getRow(n++, sheet);
            HSSFCellUtil.createCell(row, 0, data.getDateStr());
            HSSFCellUtil.getCell(row, 1).setCellValue(data.getGmv());
            HSSFCellUtil.getCell(row, 2).setCellValue(data.getClientNumMap().get("wx_m_custom"));
            HSSFCellUtil.getCell(row, 3).setCellValue(data.getClientNumMap().get("a_custom"));
            HSSFCellUtil.getCell(row, 4).setCellValue(data.getClientNumMap().get("i_custom"));
            HSSFCellUtil.getCell(row, 5).setCellValue(data.getClientNumMap().get("h5_custom"));
            HSSFCellUtil.getCell(row, 6).setCellValue(data.getClientNumMap().get("eb_custom"));
            HSSFCellUtil.getCell(row, 7).setCellValue(data.getNum());
            HSSFCellUtil.getCell(row, 8).setCellValue(data.getCompleteNum());
            HSSFCellUtil.getCell(row, 9).setCellValue(data.getWaitServiceNum());
        }

        // 最后一行数据
        HSSFRow rowLast = HSSFCellUtil.getRow(n, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            if (n > 1) {
                HSSFCell cell = HSSFCellUtil.getCell(rowLast, i);
                if (i == 0) {
                    cell.setCellValue("合计");
                }  else {
                    String colString = CellReference.convertNumToColString(i);
                    cell.setCellFormula(String.format("SUM(%s%d:%s%d)", colString, 2, colString, n));
                }
            }
            sheet.autoSizeColumn(i);
        }

        String fileName = "订单相关数据表格导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    /**
     * 下载 订单地图数据
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/downOrdersMapData", produces = "application/vnd.ms-excel; charset=utf-8")
    @ResponseBody
    public void downOrdersMapData(
            HttpServletResponse response,
            int type,
            String data,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        endDate = endDateBoundary(endDate);
        String subTitle = String.format(
                "%s至%s",
                LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        JSONArray jsonArray = JSON.parseArray(data);

        HSSFWorkbook workbook = new HSSFWorkbook();
        String[] columnTitles = {"区域", "订单数"};

        HSSFSheet sheet = workbook.createSheet(subTitle);
        HSSFRow row0 = HSSFCellUtil.getRow(0, sheet);
        for (int i = 0; i < columnTitles.length; i++) {
            HSSFCellUtil.createCell(row0, i, columnTitles[i]);
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            HSSFRow row = HSSFCellUtil.getRow(i+1, sheet);
            HSSFCellUtil.createCell(row, 0, obj.getString("name"));
            HSSFCellUtil.getCell(row, 1).setCellValue(obj.getLong("value"));
        }

        for (int i = 0; i < columnTitles.length; i++) {
            sheet.autoSizeColumn(i);
        }
        String fileName = "地域订单数据导出" + subTitle + ".xls";
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    /**
     * 组装下载数据集
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<String,Object> downloadDataPack(int type,Date startDate,Date endDate){
        Map<String,Object> map = new HashMap<>();
        List<OrdersStatisticsData> list;
        String subTitle;
        switch (type) {
            case 3:
                subTitle = String.format(
                        "按月%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                list = ordersDataStatisticsService.getOrdersTableDataMonth(startDate, endDate);
                break;
            case 2:
                subTitle = String.format(
                        "按周%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-ww周")));
                list = ordersDataStatisticsService.getOrdersTableDataWeek(startDate,endDate);
                break;
            default:
                subTitle = String.format(
                        "按日%s至%s",
                        LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                list = ordersDataStatisticsService.getOrdersTableDataDay(startDate,endDate);
                break;
        }
        map.put("subTitle",subTitle);
        map.put("list",list);
        return map;
    }
}
