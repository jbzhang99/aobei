package com.aobei.train.service;

import com.aobei.train.model.*;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

import custom.bean.OrderInfo;

import com.github.liyiorg.mbg.support.service.MbgUpsertService;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService extends MbgReadService<Long, Student, Student, StudentExample>,MbgWriteService<Long, Student, Student, StudentExample>,MbgUpsertService<Long, Student, Student, StudentExample>{

    /**
     * 从服务站点获得所有服务站点下，支持该产品的的学员信息。
     * @param station
     * @return
     */
    List<Student> getStudentByStationAndProduct(Station station,Product product);

    /**
     * 获取一个服务人员一天的任务排期情况, 只展示时间是否被占用。不计算具体是那个服务占用了。
     * @param student
     * @param dateTime
     * @return
     */
    Integer[] getStudentTaskSchedule(Student student,String dateTime);

    /**
     * 计算一个服务人员在某一天针对某一个商品是否还有库存,
     * 粗略算法，仅计算当天是否有库存，并没有针对sku中的时间端。
     * @param student
     * @param sku
     * @return Map<String,Integer>
     */
    List<Map<String,Integer>> countSku(Student student,ProSku sku,String dateTime);
    /**获取文件名称.传递集合
	  * @param mfile
	  * @return
	  * @throws IOException
	  */
	 List<Student> getExcelInfo(MultipartFile mfile) throws IOException;
	 /**
	  * 根据excel表里的内容读取数据域,传递集合
	  * @param is
	  * @param isExcel2003
	  * @return
	  * @throws IOException
	  */
	  List<Student> createExcel(InputStream is,boolean isExcel2003) throws IOException;
	 /**
	  * 获取excel表的数据，并返回数据为一个对象集合
	  * @param wb
	  * @return
	  */
	  List<Student> readExcelValue(Workbook wb);
	  /**
	   *  验证excel格式
	   * @param fileName
	   * @return
	   */
	   boolean ValidateExcel(String fileName);

	Page<Student> xPackPage(Integer p, Integer ps, Integer sex_selected, Integer state_selected, Integer grade_selected,
			Long partner_name_selected, Long serItem_selected,Long insurance_selected, String conditions);

	HashMap<String, Object> xAddStudent(MultipartFile file);

	void xExportStudent(Sheet sheet, Row row, Cell cell);

	List<Serviceitem> xScreenServiceItem(String str);

	void xEditStudent(Long student_id, Map<String, Object> map);


	void xAddStudent(Student student, Map<String, Object> map);

    List<Insurance> xScreenInsurance(String str, Long student_id);

    int xAddInsurance(String str, Long student_id);
}