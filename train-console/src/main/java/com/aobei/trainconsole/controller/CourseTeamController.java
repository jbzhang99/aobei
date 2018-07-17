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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.OssImgExample.Criteria;
import com.aobei.trainconsole.configuration.CustomProperties;
import com.aobei.trainconsole.util.JacksonUtil;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.CourseToCategory;



@Controller
@RequestMapping("/courseTeam")
public class CourseTeamController {
	
	private static final Logger logger= LoggerFactory.getLogger(CourseTeamController.class);
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ServiceitemService serviceitemService;
	
	@Autowired
	private CourseServiceitemService courseServiceitemService;
	
	@Autowired
	private OssImgService ossImgService;
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private CustomProperties customProperties;
	
	@Autowired
	private ExamSubjectService examSubjectService;
	
	@Autowired
	private CourseExamSubjectService courseExamSubjectService;
	
	@Autowired
	private PlanService planService;

	@Autowired
	private UsersService usersService;
	/**
	 * 跳转到课程组显示页面
	 * @param model
	 * 
	 * @return
	 */
	@RequestMapping("/showCourseTeam")
	//@PathVariable(value="current")
	public String  showCourseTeam(@RequestParam(value="p",defaultValue="1") Integer p, Model model,@RequestParam(value="ps",defaultValue="10") Integer ps) {
		CourseExample ct=new CourseExample();
		ct.or().andDeletedEqualTo(0);
		ct.setOrderByClause(CourseExample.C.cdate+" desc");
		Page<Course> cPage = this.courseService.selectByExample(ct, p, ps);
		//得到课程集合
		List<Course> cList = cPage.getList();
		
		logger.info("课程列表查询结果共计：{}条,当前页条数：{},当前页数据显示：{}",cPage.getTotal(),cList.size(),cList);
		
		//删除最后一页、最后一条时，向前跳转一页
		if(cList.size() == 0 & p > 1) {
			cPage = this.courseService.selectByExample(ct,p-1,ps);
			cList = cPage.getList();
		}
		//封装（根据课程找到其关联的分类，服务项）
		List categoryList=this.courseService.xShowCourseTeam(cList);
		
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("cList", cList);
		model.addAttribute("page",cPage);
		return "course/courseTeam_list";
	}
	
	/**
	 * 跳转到课程组详情页
	 * @return
	 */
	@RequestMapping("/courseTeamDetails/{courseid}")
	public String courseTeamDetails(@PathVariable(value="courseid") Long courseid,Model model){
		Course course = this.courseService.selectByPrimaryKey(courseid);
		
		//根据课程查出所有的章节
		ChapterExample chapterExample = new ChapterExample();
		chapterExample.or().andCourse_idEqualTo(course.getCourse_id()).andDeletedEqualTo(0);
		List<Chapter> chapList = this.chapterService.selectByExample(chapterExample);
		//根据课程找到所对应的服务项目
		List<CourseToCategory> vptList =this.courseService.xFeng(courseid);
		model.addAttribute("categoryList", vptList);
		model.addAttribute("chapList", chapList);
		model.addAttribute("course", course);
		return "course/courseTeam_detail";
	}
	
	
	/**
	 * 删除课程组信息
	 */
	@RequestMapping("/delCourseByTeamId/{course_id}")
	@ResponseBody
	@Transactional
	public Object delCourseByTeamId(@PathVariable(value="course_id") Long course_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[courseTeam] F[delCourseByTeamId] U[{}] ,params course_id:{}",
				users.getUser_id(),course_id);
		//封装（根据课程id将其关联的章节，考试科目一并删除）
		Boolean flag=this.courseService.xDelCourseByTeamId(course_id);
		logger.info("M[courseTeam] F[delCourseByTeamId] U[{}] ,execute result:{}",
				users.getUser_id(),flag==true?"成功":"失败");
		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("message", String.format("课程删除%s!",flag==true ? "成功" : "失败"));
		return resultMap;
	}
	
	/**
	 * 跳转到课程组新增页面
	 * @return
	 */
	@RequestMapping("/createTeamShow")
	public String createTeamShow(Model model,@RequestParam(value="pageNo") Integer pageNo){
		
		//封装  课程添加页面（找到所有有服务项并且未删除的分类）
		List<Category> cList=this.courseService.xCreateTeamShow();
		
		//所有未删除 的服务项目
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		//andStateEqualTo(1)
		serviceitemExample.or().andDeletedEqualTo(0);
		List<Serviceitem> sList = this.serviceitemService.selectByExample(serviceitemExample);
		model.addAttribute("cList", cList);
		model.addAttribute("sList", sList);
		try {
			model.addAttribute("serviceItemList",JacksonUtil.object_to_json(sList));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("cgory", cList.get(0));
		return "course/courseTeam_add";
	}
	
	/**
	 * 添加课程组及课程信息
	 * @return
	 */
	@RequestMapping("/addCourseTeam")
	@Transactional
	//@ResponseBody
	public String addCourseTeam(String courseT,MultipartFile course_img,Authentication authentication){

		//获取前台传到的json数据
		//将字符串json数据转换成json对象
		JSONObject resultJson=JSONObject.parseObject(courseT);
		//将json对象按照map进行封装
		Map<String,Object> map=resultJson;
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[courseTeam] F[addCourseTeam] U[{}] ,params map:{}",
				users.getUser_id(),map);
		//保存图片
		Map<String, String> params =this.myFileHandleUtil.file_upload(course_img, PathType.image_course_logo);
		
		//封装  添加课程（章节，服务项，考试科目）
		int num = this.courseService.xAddCourseTeam(map, params);
		logger.info("M[courseTeam] F[addCourseTeam] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		return "redirect:/courseTeam/showCourseTeam";
	}
	
	/**
	 * 跳转到修改页面，并进行数据回显
	 * @return
	 */
	@RequestMapping("/updCourseTeam/{course_id}/{pageNo}")
	public String updCourseTeam(@PathVariable(value="course_id") Long course_id,Model model,@PathVariable(value="pageNo") Integer pageNo){
		
		//根据课程组编号，查出来所对应的课程组对象
		Course course = this.courseService.selectByPrimaryKey(course_id);
		OssImgExample ossImgExample = new OssImgExample();
		ossImgExample.or().andOss_img_idEqualTo(Long.parseLong(course.getImg()));
		//根据课程找到图片
		List<OssImg> ossList = this.ossImgService.selectByExample(ossImgExample);
		OssImg ossImg = ossList.get(0);
		if(ossImg.getUrl()!=null){
			String url = myFileHandleUtil.get_signature_url(PathType.image_course_logo, ossImg, 3600l);
			ossImg.setUrl(url);
			model.addAttribute("ossImg", ossImg);
		}else{
			OssImg ossImgs =new OssImg();
			model.addAttribute("ossImg", ossImgs);
		}
		//根据课程找到章节
		ChapterExample chapterExample = new ChapterExample();
		chapterExample.or().andCourse_idEqualTo(course_id).andDeletedEqualTo(0);
		chapterExample.setOrderByClause(ChapterExample.C.section_name+" asc");
		List<Chapter> chpList = this.chapterService.selectByExample(chapterExample);
		
		//所有分类
		List<Category> cList=new ArrayList<>();
		//所有未删除的分类(有服务项目)
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andDeletedEqualTo(0).andActivedEqualTo(1);
		List<Category> cteList = this.categoryService.selectByExample(categoryExample);
		for (Category category : cteList) {
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(category.getCategory_id()).andDeletedEqualTo(0);
			List<Serviceitem> example = this.serviceitemService.selectByExample(serviceitemExample);
			if(example.size() !=0){
				cList.add(category);
			}
		}
		//所有服务项目
		List<Serviceitem> sList = this.serviceitemService.selectByExample(new ServiceitemExample());
		
		//根据课程找对应的服务项目
		List<CourseToCategory> vptList = this.courseService.xFeng(course_id);
		model.addAttribute("course", course);// 课程名称
		try {
			model.addAttribute("serviceItemList",JacksonUtil.object_to_json(sList));
			model.addAttribute("list", JacksonUtil.object_to_json(chpList));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// 课程所对应的所有课程章节
		model.addAttribute("chpList",chpList);
		//分类
		model.addAttribute("cList",cList);
		//服务项目
		model.addAttribute("sList", sList);
		//课程包含的服务项目
		model.addAttribute("vptList", vptList);
		
		model.addAttribute("pageNo",pageNo);
		model.addAttribute("cgory", cList.get(0));
		return "course/courseTeam_edit";
	}
	
	
	/**
	 * 修改课程
	 */
	@RequestMapping("/editCourseTeam/{course_id}")
	@Transactional
	//@ResponseBody
	public Object editCourseTeam(@PathVariable(value="course_id") Long course_id,String courseT,MultipartFile course_img,Integer pageNo,Authentication authentication){
		
		
		//获取前台传到的json数据
		//将字符串json数据转换成json对象
		JSONObject resultJson=JSONObject.parseObject(courseT);
		//将json对象按照map进行封装
		Map<String,Object> map=resultJson;
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[courseTeam] F[editCourseTeam] U[{}] ,params course_id:{}, courseT:{},course_img:{},pageNo:{}",
				users.getUser_id(),course_id,map,course_img,pageNo);

		//获取到图片
		String originalFilename = course_img.getOriginalFilename();
		//封装   编辑课程
		Course course=this.courseService.xEditCourseTeam(map,course_id);

		int num =0;
		if(originalFilename.equals("") || course_img == null){
			 num = this.courseService.updateByPrimaryKeySelective(course);
		}else{
			Course cs = this.courseService.selectByPrimaryKey(course_id);
			long imgId = Long.parseLong(cs.getImg());
			this.ossImgService.deleteByPrimaryKey(imgId);
			Map<String, String> params =this.myFileHandleUtil.file_upload(course_img, PathType.image_course_logo);
			//保存图片信息
			String effect="课程图片";
			String privileges="private";
			//封装   (新增图片)
			OssImg ossImg = this.ossImgService.xInsertOssImg(params, effect, privileges);
			course.setImg(ossImg.getOss_img_id().toString());
			num=this.courseService.updateByPrimaryKeySelective(course);
		}
		logger.info("M[courseTeam] F[editCourseTeam] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		return "redirect:/courseTeam/showCourseTeam?p="+pageNo;
	}
	
	
	/**
	 * 将每个课程组对应的服务项目，进行封装
	 * @param teamid
	 * @return
	 */
	/*public List<CourseToCategory> feng(Long courseid){
		
	}*/
	
	/**
	 * 根据课程找到培训计划
	 * @return
	 */
	@RequestMapping("courseFindPlan/{team_id}")
	@ResponseBody
	public Object courseFindPlan(@PathVariable(value="team_id")Long team_id){
		PlanExample planExample = new PlanExample();
		planExample.or().andCourse_idEqualTo(team_id).andDeletedEqualTo(0);
		List<Plan> list = this.planService.selectByExample(planExample);
		return list;
	}
}
