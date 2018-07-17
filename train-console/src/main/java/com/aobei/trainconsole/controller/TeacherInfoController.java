package com.aobei.trainconsole.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.train.IdGenerator;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Status;




@Controller
@RequestMapping("/teacherManager/teacherInfo")
public class TeacherInfoController {


	@Autowired
	private TrainCityService searchTrainAddressService;
	@Autowired
	private MyFileHandleUtil fileHandleUtil;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private OssImgService ossImgService;
	@Autowired
	private TeacherCourseService teacherCourseService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OperateLogService operateLogService;

	private static Logger logger = LoggerFactory.getLogger(TeacherInfoController.class);
	/**
	 *
	 * @param p 分页参数，当前页
	 * @param sex_selected 检索框，根据性别
	 * @param state_selected 检索框，根据状态
	 * @param course_selected 检索框，根据课程
	 * @param ps 分页参数，每页条数
	 * @param model
	 * @return
	 */
	@RequestMapping("/teacherInfoList")
	public String selectTeacherInfo(
			@RequestParam(defaultValue="1") Integer p,
			@RequestParam(defaultValue="2") Integer sex_selected,
			@RequestParam(defaultValue="2") Integer state_selected,
			@RequestParam(defaultValue="2") Long course_selected, 
			@RequestParam(defaultValue="10")Integer ps,
			Model model) {
		
		TeacherExample teacherExample = new TeacherExample();
		TeacherExample.Criteria citeria = teacherExample.or();
		if (course_selected != 2) {
			//根据课程检索条件模糊查询
			TeacherCourseExample teacherCourseExample = new TeacherCourseExample();
			teacherCourseExample.or()
					.andCourse_idEqualTo(course_selected);
			List<TeacherCourse> list_teacher = teacherCourseService.selectByExample(teacherCourseExample);
			List<Long> list = new ArrayList<Long>();
			for (TeacherCourse teacherCourse : list_teacher) {
				list.add(teacherCourse.getTeacher_id());
				citeria.andTeacher_idIn(list);
			}
			//判断是否筛选出教师
			if(list_teacher.size() != 0) {
				citeria.andTeacher_idIn(list);
			} else {
				citeria.andTeacher_idIsNull();
			}
		}
		if (sex_selected != 2) {
			citeria.andSexEqualTo(sex_selected);
		}
		if (state_selected != 2) {
			citeria.andStateEqualTo(state_selected);
		}
		citeria
			.andDeletedEqualTo(Status.DeleteStatus.no.value);
		teacherExample.setOrderByClause(TeacherExample.C.create_time+" desc");
		Page<Teacher> page = teacherService.selectByExample(teacherExample,p,ps);
		
		
		//用一个集合将教师对应的课程封装
		List<List<TeacherCourse>> courseList = new ArrayList<List<TeacherCourse>>();
		//查询一个老师对应的课程集合
		for (Teacher teacher : page.getList()) {
			
			TeacherCourseExample teacherCourseExample = new TeacherCourseExample();
			teacherCourseExample.or()
					.andTeacher_idEqualTo(teacher.getTeacher_id());
			List<TeacherCourse> list_Tcourse = teacherCourseService.selectByExample(teacherCourseExample);
			courseList.add(list_Tcourse);
		}
		//课程下拉框数据集合
		CourseExample courseExample = new CourseExample();
		courseExample.or()
			.andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Course> list_course = courseService.selectByExample(courseExample);
		
		OssImgExample ossImgExample = new OssImgExample();
		ossImgExample.or().andEffectEqualTo("教师头像").andAccess_permissionsEqualTo("private");
		List<OssImg> list = ossImgService.selectByExample(ossImgExample);
		for (OssImg ossImg : list) {
			String url = fileHandleUtil.get_signature_url(PathType.avatar_teacher_logo, ossImg, 3600l);
			ossImg.setUrl(url);
		}
		model.addAttribute("courseList", courseList);
		model.addAttribute("page", page);
		model.addAttribute("ps", ps);
		model.addAttribute("list_img",list);
		model.addAttribute("list_course", list_course);
		model.addAttribute("list_teacherInfo", page.getList());
		model.addAttribute("sex_selected", sex_selected);
		model.addAttribute("state_selected", state_selected);
		model.addAttribute("course_selected", course_selected);
		return "teacherInfo/teacherInfo_list";
	}

	/**
	 * 添加教师信息
	 * @param teacherInfo 教师对象
	 * @param img_icon 头像
	 * @param img_certification 资格证
	 * @param request
	 * @param authentication 获取当前登陆用户
	 * @param courses 课程数组
	 * @return
	 */
	@RequestMapping("/addTeacherInfo")
	public String addTeacherInfo(Teacher teacherInfo,
			MultipartFile img_icon,MultipartFile img_certification, 
			HttpServletRequest request,Authentication authentication,
			@RequestParam(value="train_course")Long[] courses) {
		String certification = request.getParameter("img_certification");
		Map<String, String> params = fileHandleUtil.file_upload(img_icon,PathType.avatar_teacher_logo);
		//保存教师头像图片信息
		OssImg icon_img = ossImgService.xInsertOssImg(params,"教师头像", "private");
		OssImg cer_img = null;
		
		if(certification!=null){			
			Map<String, String> params1 = fileHandleUtil.file_upload(img_certification,PathType.image_teacher_certification);
			//保存教师资格证片信息
			cer_img = ossImgService.xInsertOssImg(params1, "教师资格证", "private");
		}
		// 要把省市县三级查询结果拼接插入到数据库中（获取js中的name值）
		String userProvinceId = request.getParameter("userProvinceId");
		String userCityId = request.getParameter("userCityId");
		String userDistrictId = request.getParameter("userDistrictId");
		//拼接时用空字符串连接，回显使用。
		//拼接为一个字符串
		StringBuffer sb = new StringBuffer();
		sb.append(searchTrainAddressService.xSelectCityById(userProvinceId).getName()+" ")
		.append(searchTrainAddressService.xSelectCityById(userCityId).getName()+" ")
		.append(searchTrainAddressService.xSelectCityById(userDistrictId).getName());

		int i = teacherService.xInsert(sb, icon_img.getOss_img_id(), cer_img == null ? IdGenerator.generateId() : cer_img.getOss_img_id(), teacherInfo, courses);//添加教师
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[teacher] F[add_teacher] U[{}] param[地址拼接sb :{},img_id:{},cer_img(可以为null):{},teacherInfo:{},courses:{}];  result:{}",
				users.getUser_id(),sb,icon_img.getOss_img_id(),cer_img,teacherInfo,courses,String.format("添加教师信息%s" , i > 0 ? "成功":"失败"));
		return "redirect:/teacherManager/teacherInfo/teacherInfoList?p=1";
	} 

	/**
	 * 跳转到添加教师信息的页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/gotoAdd")
	public String gotoAdd(Model model,@RequestParam(value="p")Integer current) {
		//培训课程集合，页面需要遍历展示
		CourseExample courseExample = new CourseExample();
		courseExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<Course> list_course = courseService.selectByExample(courseExample);
		model.addAttribute("list_course", list_course);
		model.addAttribute("current", current);
		return "teacherInfo/teacherInfo_add";
	}

	/**
	 * 删除教师信息
	 * @param teacher_id 教师id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTeacherInfo/{teacher_id}")
	public Object deleteTeacherInfo1(@PathVariable Long teacher_id,Authentication authentication){
		int i = teacherService.xDelete(teacher_id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[teacher] F[delete_teacher] U[{}] param[teacher_id :{}];  result:{}",
				users.getUser_id(),teacher_id,String.format("删除教师信息%s" , i > 0 ? "成功":"失败"));
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", String.format("删除教师信息%s", i > 0 ? "成功":"失败"));
		return map;
	}

	/**
	 * 保存修改信息
	 * @param teacherInfo 教师对象
	 * @param model
	 * @param request
	 * @param courses 课程数组
	 * @param teacher_id 教师id
	 * @param img_eicon 头像图片对象
	 * @param img_ecertification 资格证图片对象
	 * @param authentication
	 * @return
	 */
	@RequestMapping("/updateTeacherInfo")
	public String updateTeacherInfo(Teacher teacherInfo,Model model,HttpServletRequest request
			,@RequestParam(value="train_course")Long[] courses,
			@RequestParam(value="teacher_id")Long teacher_id,
			MultipartFile img_eicon,MultipartFile img_ecertification,Authentication authentication) {
		//获取图片名字，判断是否修改
		String icon_fileName = img_eicon.getOriginalFilename();
		String certification_fileName = img_ecertification.getOriginalFilename();
		
		//因为接受模板对象没有图片值，重新通过id获取对象
		Teacher teacher = teacherService.selectByPrimaryKey(teacher_id);
		
		String userProvinceId = request.getParameter("userProvinceId");
		String userCityId = request.getParameter("userCityId");
		String userDistrictId = request.getParameter("userDistrictId");
		//拼接为一个字符串
		 StringBuffer sb = new StringBuffer();
			sb.append(searchTrainAddressService.xSelectCityById(userProvinceId).getName()+" ")
			.append(searchTrainAddressService.xSelectCityById(userCityId).getName()+" ")
			.append(searchTrainAddressService.xSelectCityById(userDistrictId).getName());		
			
		teacherInfo.setTrain_address(sb.toString());
		//teacherInfo.setCreate_time(new Date());
		teacherInfo.setDeleted(0);
		if(icon_fileName.equals("") || icon_fileName==null){ 
			//没有上传图片，保存原来的信息
			teacherInfo.setIcon(teacher.getIcon());
		}else{
			Map<String, String> params = fileHandleUtil.file_upload(img_eicon,PathType.avatar_teacher_logo);
			ossImgService.deleteByPrimaryKey(Long.parseLong(teacher.getIcon()));
			//保存教师头像图片信息			
			OssImg icon_img = ossImgService.xInsertOssImg(params, "教师头像", "private");
			teacherInfo.setIcon(String.valueOf((icon_img.getOss_img_id())));
		}
		if(certification_fileName.equals("") || certification_fileName==null){
			teacherInfo.setCertification(teacher.getCertification());
		}else{
			Map<String, String> params1 = fileHandleUtil.file_upload(img_ecertification,PathType.image_teacher_certification);
			//保存教师资格证片信息
			OssImg cer_img = ossImgService.xInsertOssImg(params1, "教师资格证", "private");
			teacherInfo.setCertification(String.valueOf(cer_img.getOss_img_id()));
		}
		//删除教师课程中间表数据
		TeacherCourseExample teacherCourseExample = new TeacherCourseExample();
		teacherCourseExample.or()
				.andTeacher_idEqualTo(teacher_id);
		teacherCourseService.deleteByExample(teacherCourseExample);
		//插入教师课程中间表数据
		for (Long course1 : courses) {
			TeacherCourse tCourse = new TeacherCourse();
			tCourse.setCourse_id(course1);
			tCourse.setTeacher_id(teacherInfo.getTeacher_id());
			teacherCourseService.insert(tCourse);
		}
		int i = teacherService.updateByPrimaryKeySelective(teacherInfo);
		OperateLog operateLog = new OperateLog();
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[teacher] F[update_teacher] U[{}] param[teacehr:{}];  result:{}",
				users.getUser_id(),teacher,String.format("修改教师信息%s" , i > 0 ? "成功":"失败"));
		return "redirect:/teacherManager/teacherInfo/teacherInfoList";
	}


	/**
	 * 跳转页面，到编辑教师信息页面
	 * @param model
	 * @param teacher_id 教师id
	 * @param current 当前页参数
	 * @param request
	 * @return
	 */
	@RequestMapping("/gotoEdit")
	public String gotoEdit(Model model, @RequestParam(value="teacher_id",required=false)Long teacher_id,@RequestParam(value="p")Integer current,HttpServletRequest request) {
		Teacher edit_teacher = teacherService.selectByPrimaryKey(teacher_id);
		CourseExample Example = new CourseExample();
		Example.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<Course> list_course = courseService.selectByExample(Example);
		
		//回显省市县数据
		String train_address = edit_teacher.getTrain_address();
		//如果所在地地址爲空，所在地地址默认为北京朝阳。前台有jquery验证
		if(train_address != null){
			String[] str = train_address.split(" ");
			for (int i=0;i<str.length;i++){
				String pname = str[0];
				String cname = str[1];
				String aname = str[2];
				/*TrainCityExample trainCityExample1 = new TrainCityExample();
				trainCityExample1.or().andNameEqualTo(pname);
				trainCityExample1.includeColumns(TrainCityExample.C.id);
				String pid = DataAccessUtils.singleResult(searchTrainAddressService.selectByExample(trainCityExample1)).getId();*/
				model.addAttribute("pid", searchTrainAddressService.xSelectCityByName(pname).getId());
				String cid = searchTrainAddressService.xSelectCityByName(cname).getId();
				model.addAttribute("cid", cid );
				TrainCityExample trainCityExample = new TrainCityExample();
				trainCityExample.or().andP_idEqualTo(cid);
				//根据pid判断区数据
				List<TrainCity> list_area = searchTrainAddressService.selectByExample(trainCityExample);
				for (TrainCity trainCity : list_area) {
					if(aname.equals(trainCity.getName())){						
						model.addAttribute("aid", trainCity.getId());
					}
				}
				model.addAttribute("pname", pname);
				model.addAttribute("cname", cname);
				model.addAttribute("aname", aname);
			}
		}
		//回显头像和资格证图片信息
		List<OssImg> list_img = ossImgService.selectByExample(new OssImgExample());
		for (OssImg trainImg : list_img) {
			if(String.valueOf(trainImg.getOss_img_id()).equals(edit_teacher.getIcon())){
				String url = fileHandleUtil.get_signature_url(PathType.avatar_teacher_logo, trainImg, 3600l);
				String img_url = url;
				String img_name = trainImg.getName();
				String img_use = trainImg.getEffect();
				model.addAttribute("img_url_icon", img_url);
				model.addAttribute("img_name_icon", img_name);
				model.addAttribute("img_use_icon", img_use);
				model.addAttribute("backTeacher_icon",trainImg.getUrl());
//				model.addAttribute("trainImg", trainImg);
				
			}
			if(String.valueOf(trainImg.getOss_img_id()).equals(edit_teacher.getCertification())){	
				String url = fileHandleUtil.get_signature_url(PathType.image_teacher_certification, trainImg, 3600l);
				String img_url = url;
				String img_name = trainImg.getName();
				String img_use = trainImg.getEffect();
				model.addAttribute("img_url_certification", img_url);
				model.addAttribute("img_name_certification", img_name);
				model.addAttribute("img_use_certification", img_use);
				model.addAttribute("backTeacher_certification", trainImg.getUrl());
				
			}
		}
		TeacherCourseExample teacherCourseExample = new TeacherCourseExample();
		teacherCourseExample.or().andTeacher_idEqualTo(teacher_id);
		List<TeacherCourse> list_Tcourse = teacherCourseService.selectByExample(teacherCourseExample);
		for (TeacherCourse teacherCourse : list_Tcourse) {
			Long course_id = teacherCourse.getCourse_id();
			CourseExample courseExample = new CourseExample();
			courseExample.or().andCourse_idEqualTo(course_id).andDeletedEqualTo(0);
			Course course = DataAccessUtils.singleResult(courseService.selectByExample(courseExample));
			if(course==null){
				list_Tcourse.remove(course);
			}
		}
		// 回显表单数据
		model.addAttribute("edit_teacher", edit_teacher);
		model.addAttribute("list_course", list_course);
		model.addAttribute("current", current);
		model.addAttribute("list_Tcourse", list_Tcourse);
		return "teacherInfo/teacherInfo_edit";
	}	
		

}
