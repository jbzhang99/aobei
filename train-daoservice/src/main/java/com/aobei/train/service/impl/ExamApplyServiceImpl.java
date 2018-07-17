package com.aobei.train.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.train.model.Course;
import com.aobei.train.model.CourseExamSubject;
import com.aobei.train.model.CourseExamSubjectExample;
import com.aobei.train.model.CourseExample;
import com.aobei.train.model.CourseServiceitem;
import com.aobei.train.model.CourseServiceitemExample;
import com.aobei.train.model.ExamApply;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.ExamApplyMapper;
import com.aobei.train.mapper.PartnerMapper;
import com.aobei.train.model.ExamApplyExample;
import com.aobei.train.model.Partner;
import com.aobei.train.model.PartnerExample;
import com.aobei.train.model.PlanStudent;
import com.aobei.train.model.PlanStudentExample;
import com.aobei.train.model.Student;
import com.aobei.train.model.StudentExample;
import com.aobei.train.model.StudentServiceitem;
import com.aobei.train.model.StudentServiceitemExample;
import com.aobei.train.model.ExamApplyExample.Criteria;
import com.aobei.train.service.CourseExamSubjectService;
import com.aobei.train.service.CourseService;
import com.aobei.train.service.CourseServiceitemService;
import com.aobei.train.service.ExamApplyService;
import com.aobei.train.service.PlanStudentService;
import com.aobei.train.service.StudentService;
import com.aobei.train.service.StudentServiceitemService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Score;
import custom.bean.Status;

@Service
@Transactional
public class ExamApplyServiceImpl extends MbgServiceSupport<ExamApplyMapper, Long, ExamApply, ExamApply, ExamApplyExample> implements ExamApplyService{

	@Autowired
	private PartnerMapper partnerMapper;
	@Autowired
	private ExamApplyMapper examApplyMapper;
	@Autowired
	private CourseService courseService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private PlanStudentService planStudentService;
	@Autowired
	CourseExamSubjectService courseExamSubjectService;
	@Autowired
	CourseServiceitemService courseServiceitemService;
	@Autowired
	StudentServiceitemService studentServiceitemService;
	@Autowired
	private ExamApplyService examApplyService;
	
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(examApplyMapper);
	}
	
	public List<Score> getExcelInfo(MultipartFile mfile) throws IOException{
		//获取文件完整名称
		String fileName = mfile.getOriginalFilename();
		List<Score> list = new ArrayList<Score>();
		
			/*if(!ValidateExcel(fileName)){
				return null;
			}*/
			boolean isExcel2003 = true;
			if(isExcel2007(fileName)){
				isExcel2003 = false;
			}
			list = createExcel(mfile.getInputStream(),isExcel2003);
		
		return list;
	}
	
	/**
	 * 根据excel表里的内容读取数据域
	 */
	public List<Score> createExcel(InputStream is,boolean isExcel2003) throws IOException{
		List<Score> list = null;
		
			Workbook wb = null;
			//读取excel表
			if(isExcel2003){
				wb = new HSSFWorkbook(is);
			}else{
				//当excel是2007时，创建excel2007
				wb = new XSSFWorkbook(is);
			}
		list = readExcelValue(wb);	
		
		return list;
	}
	/** 
	 * 读取excel表内容，把数据对应添加到集合中，拿到数据集合
	 * @param wb
	 * @return
	 */
	public List<Score> readExcelValue(Workbook wb){
		//得到第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		//得到excel的行数
		int totalRows = sheet.getPhysicalNumberOfRows();
		//得到excel的列数,先要判断行数不为空
		int totalCells = 0;
		if(totalRows > 1 && sheet.getRow(0)!= null){
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		//创建表对应的对象，循环excel的数据设置到对象里面插入数据库即可。
		List<Score> list = new ArrayList<Score>();
		//创建考试申请表对象
//		ExamApply examApply = new ExamApply();
//		ExamApplyExample examApplyExample = new ExamApplyExample();
		//只设置身份证，分数，passed
//		examApply.setApply_datetime(new Date());
			
//		examApply.setExam_plan_id((long)1);
		//插入到考试申请表信息。
		
		//开始循环行数
		for(int r = 2;r<totalRows;r++){
			Row row = sheet.getRow(r);
			if(row == null){
				continue;
			}
			Score scoreInfo = new Score();
			scoreInfo.setScore_manager_id(IdGenerator.generateId());
			//循环excel列
			for(int c=0; c<totalCells;c++){
				Cell cell = row.getCell(c);
				if(cell != null && ""!=cell.toString()){
					//开始设置对象属性
					if(c == 0){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
//							String stringCellValue = cell.getStringCellValue();
							scoreInfo.setName(cell.getStringCellValue());
						}
					}else if(c == 1){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){							
							if(cell.getStringCellValue().equals("女")){
								scoreInfo.setSex(0);
							}else{
								scoreInfo.setSex(1);
							}
						}
					}else if(c == 2){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
							System.out.println(cell.getStringCellValue());
							scoreInfo.setId_card(cell.getStringCellValue());
						}
					}else if(c == 3){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
//							String phone = cell.getStringCellValue();
							Long phone = (long) cell.getNumericCellValue();
							
							scoreInfo.setPhone(String.valueOf(phone));
						}
					}else if(c == 4){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							String age = String.valueOf(cell.getNumericCellValue());
							String ages = age.substring(0, age.indexOf("."));
							scoreInfo.setAge(Integer.parseInt(ages));
						}
					}else if(c == 5){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){							
//							scoreInfo.setCourse_name(cell.getStringCellValue());
//							CourseExample courseExample = new CourseExample();
//							courseExample.or().andTeam_nameLike(cell.getStringCellValue());
//							List<Course> list_course = courseMapper.selectByExample(courseTeamExample);
//							for (Course course : list_course) {
//								Long team_id = course.getCourse_id();
////								examApply.setCourse_team_id(team_id);
//							}
							CourseExample courseExample = new CourseExample();
							courseExample.or().andNameEqualTo(cell.getStringCellValue()).andDeletedEqualTo(Status.DeleteStatus.no.value);
							Course course = DataAccessUtils.singleResult(courseService.selectByExample(courseExample));
							if(course!=null){								
								scoreInfo.setCourse_name(course.getCourse_id());
							}

						}
					}else if(c == 6){
						//从string转化为date类型（只需要年月日）
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							Date date = cell.getDateCellValue();
							scoreInfo.setTrain_time(date);
						}
					}else if(c == 7){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
								//把导入的数据合伙人类型从字符串转化为long类型存储。要和相对应的合伙人信息表匹配
								List<Partner> partner_list = partnerMapper.selectByExample(new PartnerExample());
								
								for (Partner partner : partner_list) {
									if(cell.getStringCellValue().equals(partner.getName())){
										Long partner_id = partner.getPartner_id();
										scoreInfo.setFrom_partner(partner_id);
									}
								}
						}
					}else if(c ==8){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
//							List<ExamApply> list_exam = examApplyService.selectByExample(new ExamApplyExample());
//							for (ExamApply examApply2 : list_exam) {
								//如果考试申请表里没有分数信息，才能导入数据  不不不不
//								if(examApply2.getScore()==null){									
									double score = cell.getNumericCellValue();
									String str = String.valueOf(score);
									String substring = str.substring(0,str.indexOf("."));
									scoreInfo.setTest_score(Integer.parseInt(substring));
//									examApply.setScore(Integer.parseInt(substring));
//								}
//							}
						}
					}else if(c ==9){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){							
							if(cell.getStringCellValue().equals("是")){
								scoreInfo.setIf_qualified(1);
//								examApply.setPassed((byte)1);
							}else{
								scoreInfo.setIf_qualified(0);
//								examApply.setPassed((byte)0);
							}
						}
					}
				}
			}
			list.add(scoreInfo);
//			examApplyService.insertSelective(examApply);
		}
		return list;
	}
	/**
	 * 验证excel格式
	 * @param fileName
	 * @return
	 */
	public boolean ValidateExcel(String fileName){
		if(fileName == null || isExcel2003(fileName) || isExcel2007(fileName)){
//			String errorMsg = "文件不是正确的excel名称";
			return false;
		}
		return true;
	}
	//
	//判断是否为2003的excel，返回true为是
	private static boolean isExcel2003(String filePath){
		return filePath.matches("^.+\\.(?i)(xls)$");
	}
	//判断是否为2007的excel，返回true为是
	private static boolean isExcel2007(String filePath){
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
	@Transactional(timeout = 5)
	@Override
	public int xDelete(Long scoreId) {
		ExamApply examApply = new ExamApply();
		examApply.setExam_apply_id(scoreId);
		examApply.setDeleted(Status.DeleteStatus.yes.value);
		return this.updateByPrimaryKey(examApply);
	}
	@Transactional(timeout = 5)
	@Override
	public int xPasswordTrue(ExamApply examApply) {
		StudentExample studentExample1 = new StudentExample();
		com.aobei.train.model.StudentExample.Criteria criteria1 = studentExample1.or();
		criteria1.andStudent_idEqualTo(examApply.getStudent_id())
				 .andStateEqualTo(1);
		Student student1 = DataAccessUtils.singleResult(studentService.selectByExample(studentExample1));
		if(student1!=null){
			
			// 根据培训计划id和学生id和培训时间确定一条培训计划
			PlanStudentExample planStudentExampleUpload = new PlanStudentExample();
			com.aobei.train.model.PlanStudentExample.Criteria criteria3 = planStudentExampleUpload.or();
			criteria3
			.andStudent_idEqualTo(student1.getStudent_id())
			.andTrain_endEqualTo(examApply.getApply_datetime())
			.andDeletedEqualTo(Status.DeleteStatus.no.value);
			PlanStudent planStudent = DataAccessUtils
					.singleResult(planStudentService.selectByExample(planStudentExampleUpload));
			if (planStudent != null) {
				// 根据培训计划的课程id和申请表的科目id找到一条数据
				CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
				com.aobei.train.model.CourseExamSubjectExample.Criteria criteria22 = courseExamSubjectExample.or();
				criteria22
						.andExam_subject_idEqualTo(examApply.getExam_subject_id());
						//.andCourse_idEqualTo(planStudent.getCourse_id());
				CourseExamSubject courseExamSubject = DataAccessUtils
						.singleResult(courseExamSubjectService.selectByExample(courseExamSubjectExample));
				// 用正确的课程id找到关联的多个服务项目
				CourseServiceitemExample courseServiceitemExample = new CourseServiceitemExample();
				com.aobei.train.model.CourseServiceitemExample.Criteria criteria4 = courseServiceitemExample.or();
				criteria4.andCourse_idEqualTo(courseExamSubject.getCourse_id());
				// 遍历服务项目插入到 学生关联服务项中间表
				List<CourseServiceitem> list_course_serviceItem = courseServiceitemService
						.selectByExample(courseServiceitemExample);
				StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
				com.aobei.train.model.StudentServiceitemExample.Criteria criteria2 = studentServiceitemExample.or();
				criteria2.andStudent_idEqualTo(student1.getStudent_id());
				List<StudentServiceitem> list_studentServiceItem = studentServiceitemService
						.selectByExample(studentServiceitemExample);
				for (CourseServiceitem courseServiceitem : list_course_serviceItem) {
					StudentServiceitem serviceitem = new StudentServiceitem();
					serviceitem.setStudent_id(student1.getStudent_id());
					serviceitem.setTrain_plan_id(examApply.getTrain_plan_id());
					serviceitem.setStatus(1);
					serviceitem.setActive_datetime(new Date());
					serviceitem.setCreate_datetime(new Date());
					serviceitem.setServiceitem_id(courseServiceitem.getServiceitem_id());
					StudentServiceitemExample studentServiceitemExample2 = new StudentServiceitemExample();
					studentServiceitemExample2.or().andServiceitem_idEqualTo(serviceitem.getServiceitem_id())
					.andStudent_idEqualTo(student1.getStudent_id());
					StudentServiceitem singleResult = DataAccessUtils
							.singleResult(studentServiceitemService.selectByExample(studentServiceitemExample2));
					if (singleResult == null) {
						studentServiceitemService.insert(serviceitem);
					} else {
						for (StudentServiceitem studentServiceitem2 : list_studentServiceItem) {
							if (serviceitem.getStudent_id().equals(student1.getStudent_id())
									& studentServiceitem2.getServiceitem_id().equals(serviceitem.getServiceitem_id())) {
								studentServiceitemService.updateByPrimaryKey(serviceitem);
							}
						}
					} // if结束
				}
				planStudent.setPassed(1);//培训计划通过，设置pssed为1为通过
				return planStudentService.updateByPrimaryKey(planStudent);
			}
		}
		return 0;
	}
	@Transactional(timeout = 5)
	@Override
	public int xPasswordFalse(ExamApply examApply) {
			StudentExample studentExample1 = new StudentExample();
			com.aobei.train.model.StudentExample.Criteria criteria1 = studentExample1.or();
			criteria1.andStudent_idEqualTo(examApply.getStudent_id())
					 .andStateEqualTo(1);
			Student student1 = DataAccessUtils.singleResult(studentService.selectByExample(studentExample1));
			if(student1!=null){				
				// 根据培训计划id和学生id和培训时间确定一条培训计划
				PlanStudentExample planStudentExampleUpload = new PlanStudentExample();
				com.aobei.train.model.PlanStudentExample.Criteria criteria3 = planStudentExampleUpload.or();
				criteria3.andStudent_idEqualTo(student1.getStudent_id()).andTrain_endEqualTo(examApply.getApply_datetime());
				PlanStudent planStudent = DataAccessUtils
						.singleResult(planStudentService.selectByExample(planStudentExampleUpload));
				if (planStudent != null) {
					// 根据培训计划的课程id和申请表的科目id找到一条数据
					CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
					com.aobei.train.model.CourseExamSubjectExample.Criteria criteria22 = courseExamSubjectExample.or();
					criteria22
							.andExam_subject_idEqualTo(examApply.getExam_subject_id());
							//.andCourse_idEqualTo(planStudent.getCourse_id());
					CourseExamSubject courseExamSubject = DataAccessUtils
							.singleResult(courseExamSubjectService.selectByExample(courseExamSubjectExample));

					// 用正确的课程id找到关联的多个服务项目
					CourseServiceitemExample courseServiceitemExample = new CourseServiceitemExample();
					com.aobei.train.model.CourseServiceitemExample.Criteria criteria4 = courseServiceitemExample.or();
					criteria4.andCourse_idEqualTo(courseExamSubject.getCourse_id());
					// 遍历服务项目插入到 学生关联服务项中间表
					List<CourseServiceitem> list_course_serviceItem = courseServiceitemService
							.selectByExample(courseServiceitemExample);
					StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
					com.aobei.train.model.StudentServiceitemExample.Criteria criteria2 = studentServiceitemExample.or();
					criteria2.andStudent_idEqualTo(student1.getStudent_id());
					List<StudentServiceitem> list_studentServiceItem = studentServiceitemService
							.selectByExample(studentServiceitemExample);
					// for (StudentServiceitem studentServiceitem2 :
					// list_studentServiceItem) {
					for (CourseServiceitem courseServiceitem : list_course_serviceItem) {
						StudentServiceitem serviceitem = new StudentServiceitem();
						serviceitem.setStudent_id(student1.getStudent_id());
						serviceitem.setTrain_plan_id(examApply.getTrain_plan_id());
						serviceitem.setStatus(0);
						serviceitem.setActive_datetime(new Date());
						serviceitem.setCreate_datetime(new Date());
						serviceitem.setServiceitem_id(courseServiceitem.getServiceitem_id());
						// 判断服务项
						StudentServiceitemExample studentServiceitemExample2 = new StudentServiceitemExample();
						studentServiceitemExample2.or().andServiceitem_idEqualTo(serviceitem.getServiceitem_id())
						.andStudent_idEqualTo(student1.getStudent_id());
						StudentServiceitem singleResult = DataAccessUtils
								.singleResult(studentServiceitemService.selectByExample(studentServiceitemExample2));
						if (singleResult == null) {
							studentServiceitemService.insert(serviceitem);
						} else {
							for (StudentServiceitem studentServiceitem2 : list_studentServiceItem) {
								if (serviceitem.getStudent_id().equals(student1.getStudent_id())
										& studentServiceitem2.getServiceitem_id().equals(serviceitem.getServiceitem_id())) {
									studentServiceitemService.updateByPrimaryKey(serviceitem);
								}
							}
						} // if结束
						
					}
					// 如果培训计划通过，设置pssed为0为通过
					planStudent.setPassed(0);
					return planStudentService.updateByPrimaryKey(planStudent);
				}
			}

		return 0;
	}

	@Override
	public HashMap<String, Object> xUpdate(Long scoreId, String score) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ExamApply examApply = examApplyService.selectByPrimaryKey(scoreId);
		examApply.setId_check(Byte.parseByte(String.valueOf(1)));
		examApply.setScore(Integer.parseInt(score));
		if (Integer.parseInt(score) < 60) {
			examApply.setPassed(Byte.parseByte(String.valueOf(0)));
		} else {
			examApply.setPassed(Byte.parseByte(String.valueOf(1)));
		}
		int i = examApplyService.updateByPrimaryKey(examApply);
		//改变关联服务项数据
		if (examApply.getPassed() == 1 & examApply.getScore() != null) {
			int j = examApplyService.xPasswordTrue(examApply);//设置为通过
			if(j==0){
				map.put("editMessage", String.format("修改成绩信息失败**"));
				return map;
			}
		} else {
			int j = examApplyService.xPasswordFalse(examApply);
			if(j==0){
				map.put("editMessage", String.format("修改成绩信息失败**"));
				return map;
			}
		}
		map.put("editMessage", String.format("修改成绩信息%s", i > 0 ? "成功" : "失败"));
		return map;
	}

	@Override
	public HashMap<String, Object> scoreImport(MultipartFile file) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		List<Score> excelInfo = null;
		if(file != null && !file.isEmpty()){
			try {
				excelInfo = examApplyService.getExcelInfo(file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExamApplyExample examApplyExample = new ExamApplyExample();
			examApplyExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
			List<ExamApply> list_examApply = examApplyService.selectByExample(examApplyExample);
			if(excelInfo.size()!=0){
				
			
			for (Score score : excelInfo) {
				for (ExamApply examApply : list_examApply) {
					// if(examApply.getScore()==null){
					// 根据身份证和手机号确定一个学生id
					StudentExample studentExample = new StudentExample();
					com.aobei.train.model.StudentExample.Criteria criteria = studentExample.or();
					criteria.andIdentity_cardEqualTo(score.getId_card())
							 .andPhoneEqualTo(score.getPhone())
							 .andStateEqualTo(1);
					Student student = DataAccessUtils.singleResult(studentService.selectByExample(studentExample));
					if (student != null) {
						// 根据excel表学生id，培训时间，课程id在planStudent匹配数据
						PlanStudentExample planStudentExample2 = new PlanStudentExample();
						com.aobei.train.model.PlanStudentExample.Criteria criteria5 = planStudentExample2.or();
						criteria5.andStudent_idEqualTo(student.getStudent_id())
								.andTrain_beginEqualTo(score.getTrain_time())
								.andCourse_idEqualTo(score.getCourse_name()).andDeletedEqualTo(Status.DeleteStatus.no.value);
						PlanStudent planStudent2 = DataAccessUtils
								.singleResult(planStudentService.selectByExample(planStudentExample2));
						// 如果三个条件匹配错误，planStudent为null
						if (planStudent2 != null) {
							ExamApplyExample examApplyExample2 = new ExamApplyExample();
							Criteria criteria6 = examApplyExample2.or();
							criteria6.andTrain_plan_idEqualTo(planStudent2.getTrain_plan_id())
									.andStudent_idEqualTo(student.getStudent_id()).andDeletedEqualTo(Status.DeleteStatus.no.value);
							ExamApply apply = DataAccessUtils
									.singleResult(examApplyService.selectByExample(examApplyExample2));
							if (apply != null) {
								apply.setId_check((byte) 1);
								apply.setScore(score.getTest_score());
								if (score.getTest_score() >= 60) {
									apply.setPassed((byte) 1);
								} else {
									apply.setPassed((byte) 0);
								}
								int i = examApplyService.updateByPrimaryKey(apply);
								if (apply.getPassed() != null & apply.getScore() != null) {
									// 根据是否通过更改服务项，培训计划数据一致
									if (apply.getPassed() == 1 & apply.getScore() >= 60) {
										examApplyService.xPasswordTrue(apply);
									} else {
										examApplyService.xPasswordFalse(apply);
									}
									// }
								}
								if (i > 0) {
									map.put("result", "excel导入成功");
								}
							} else {
								map.put("result", "导入失败，请核对【" + score.getName() + "】培训计划信息");
								return map;
							}
						} else {
							map.put("result", "导入失败，请核对【" + score.getName() + "】培训时间信息及学员信息");
							return map;
						}
					} else {
						map.put("result", "导入失败，请核对学员【" + score.getName() + "】身份证和手机号信息或是否在职");
						return map;
					}
					// s

				}
			}
		}else{
			map.put("result", "文件数据为空，请填入学员信息后再上传");
			return map;
		}
		} else {
			map.put("result", "文件为空，请选择文件");
			return map;
		}
		return map;
	}
	
	

}