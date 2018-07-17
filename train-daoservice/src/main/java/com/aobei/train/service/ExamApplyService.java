package com.aobei.train.service;

import com.aobei.train.model.ExamApplyExample;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;


import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.train.model.ExamApply;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

import custom.bean.Score;

import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ExamApplyService extends MbgReadService<Long, ExamApply, ExamApply, ExamApplyExample>,MbgWriteService<Long, ExamApply, ExamApply, ExamApplyExample>,MbgUpsertService<Long, ExamApply, ExamApply, ExamApplyExample>{

	 /**获取文件名称.传递集合
	  * @param mfile
	  * @return
	  * @throws IOException
	  */
	 List<Score> getExcelInfo(MultipartFile mfile) throws IOException;
	 /**
	  * 根据excel表里的内容读取数据域,传递集合
	  * @param is
	  * @param isExcel2003
	  * @return
	  * @throws IOException
	  */
	  List<Score> createExcel(InputStream is,boolean isExcel2003) throws IOException;
	 /**
	  * 获取excel表的数据，并返回数据为一个对象集合
	  * @param wb
	  * @return
	  */
	  List<Score> readExcelValue(Workbook wb);
	  /**
	   *  验证excel格式
	   * @param fileName
	   * @return
	   */
	   boolean ValidateExcel(String fileName);
	   
	   /**
	    * 成绩导入方法
	    * @param file
	    * @return
	    */
	   HashMap<String, Object> scoreImport(MultipartFile file);
	   /**
	    * 编辑方法 修改成绩，修改服务项，修改培训计划
	    * @param scoreId examApply表主键id
	    * @param score 成绩分数
	    * @return
	    */
	   HashMap<String, Object> xUpdate(Long scoreId,String score);
	   /**
	    * 删除方法
	    * @param scoreId examApply表主键id
	    * @return
	    */
	   int xDelete(Long scoreId);
	   /**
	    * 修改服务项Status状态为1，培训计划passed为1  通过
	    * @param examApply
	    * @return
	    */
	   int xPasswordTrue(ExamApply examApply);
	   /**
	    * 修改服务项Status状态为0，培训计划passed为0  不通过
	    * @param examApply
	    * @return
	    */
	   int xPasswordFalse(ExamApply examApply);
}