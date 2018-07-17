package com.aobei.trainconsole.util;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WriteExcel {
    //导出表的列名
    private String[] rowName;
    //每行作为一个Object对象
    private List<Object[]> dataList = new ArrayList<Object[]>();

    //构造方法，传入要导出的数据
    public WriteExcel(String[] rowName, List<Object[]> dataList) {
        this.dataList = dataList;
        this.rowName = rowName;
    }

    /*
     * 导出数据
     * */
    public InputStream export() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();                        // 创建工作簿对象
        XSSFSheet sheet = workbook.createSheet("sheet1");        // 创建工作表

        //sheet样式定义[getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展] 
        XSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
        XSSFCellStyle style = this.getStyle(workbook);                    //单元格样式对象

        // 定义所需列数
        int columnNum = rowName.length;
        XSSFRow rowRowName = sheet.createRow(0);                // 在索引2的位置创建行(最顶端的行开始的第二行)

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            XSSFCell cellRowName = rowRowName.createCell(n);                //创建列头对应个数的单元格
            cellRowName.setCellType(XSSFCell.CELL_TYPE_STRING);                //设置列头单元格的数据类型
            XSSFRichTextString text = new XSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text);                                    //设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
        }

        //将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {

            Object[] obj = dataList.get(i);//遍历每个对象
            XSSFRow row = sheet.createRow(i + 1);//创建所需的行数
            for (int j = 0; j < obj.length; j++) {
                XSSFCell cell = null;   //设置单元格的数据类型
                cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
                if (!"".equals(obj[j]) && obj[j] != null) {
                    cell.setCellValue(obj[j].toString());                        //设置单元格的值
                }
                cell.setCellStyle(style);                                    //设置单元格样式
            }
        }
        //让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            sheet.autoSizeColumn(colNum);
            double width = (sheet.getColumnWidth(colNum) * 1.5);
            if (width > 255 * 256){
                width = 255 * 256;
            }
            sheet.setColumnWidth(colNum,(int) width);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        return is;
    }

    /*
     * 列头单元格样式
     */
    public XSSFCellStyle getColumnTopStyle(XSSFWorkbook workbook) {

        // 设置字体
        XSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        XSSFColor color = new XSSFColor();
		color.setIndexed(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(color);
        //设置左边框;
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(color);
        //设置右边框;
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(color);
        //设置顶边框;
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(color);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*
   * 列数据信息单元格样式
   */
    public XSSFCellStyle getStyle(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        XSSFColor color = new XSSFColor();
		color.setIndexed(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(color);
        //设置左边框;
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(color);
        //设置右边框;
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(color);
        //设置顶边框;
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(color);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

}
