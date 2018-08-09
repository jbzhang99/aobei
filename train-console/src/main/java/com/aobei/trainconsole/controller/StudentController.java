package com.aobei.trainconsole.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aobei.train.IdGenerator;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.SkuTime;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;
import com.github.liyiorg.mbg.bean.Page;

@Controller
@RequestMapping("/student")
public class StudentController {

	private static final Logger logger= LoggerFactory.getLogger(StudentController.class);
	
	@Autowired
	private StudentService studentService;
	
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private ServiceitemService serviceitemService;
	
	@Autowired
	private StudentServiceitemService studentServiceitemService;
	
	@Autowired
	private OssImgService ossImgService;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private StationService stationService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private InsuranceRegisterService insuranceRegisterService;

	@Autowired
	private InsuranceService insuranceService;
	/*@Autowired
	private CustomProperties customProperties;*/
	/**
	 * 列表显示
	 * @param model
	 * @param sex_selected
	 * @param state_selected
	 * @param grade_selected
	 * @param partner_name_selected
	 * @param conditions
	 * @return
	 */
	@RequestMapping("/showStudent")
	public String showStudent(Model model,@RequestParam(value="p",defaultValue="1") Integer p,@RequestParam(value="ps",defaultValue="10") Integer ps,
			@RequestParam(defaultValue="2") Integer sex_selected,
			@RequestParam(defaultValue="2") Integer state_selected,
			@RequestParam(defaultValue="0") Integer grade_selected,
			@RequestParam(defaultValue="2") Long partner_name_selected,
			@RequestParam(defaultValue="0") Long serItem_selected, @RequestParam(defaultValue="0") Long insurance_selected,
			 String conditions){
		
		//封装（根据条件查询page）
		Page<Student> page=this.studentService.xPackPage(p,ps,sex_selected,state_selected,grade_selected,partner_name_selected,serItem_selected,insurance_selected,conditions);
		//学员集合
		List<Student> stuList = page.getList();
		//合伙人集合
		List<Partner> pList = this.partnerService.selectByExample(new PartnerExample());
		
		//服务项目集合
		List<Serviceitem> serList = this.serviceitemService.selectByExample(new ServiceitemExample());
		
		//查找出学员所对应的服务项目
		StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
		studentServiceitemExample.or().andStatusEqualTo(1);
		List<StudentServiceitem> stuSerList = this.studentServiceitemService.selectByExample(studentServiceitemExample);

		//查找出全部保险
        List<Insurance> insuranceList = this.insuranceService.selectByExample(new InsuranceExample());

        //查找出学员对应的保险
        List<InsuranceRegister> insuranceRegisters = this.insuranceRegisterService.selectByExample(new InsuranceRegisterExample());

        model.addAttribute("serList", serList);
		model.addAttribute("stuSerList", stuSerList);
		model.addAttribute("pList", pList);
		model.addAttribute("stuList", stuList);
		model.addAttribute("page", page);
		model.addAttribute("sex_selected", sex_selected);
		model.addAttribute("state_selected", state_selected);
		model.addAttribute("grade_selected", grade_selected);
		model.addAttribute("serItem_selected", serItem_selected);
        model.addAttribute("insurance_selected", insurance_selected);
		model.addAttribute("partner_name_selected", partner_name_selected);
		model.addAttribute("conditions", conditions);
        model.addAttribute("insuranceList", insuranceList);
        model.addAttribute("insuranceRegisterList", insuranceRegisters);

		return "student/student_list";
	}
	
	/**
	 * 跳转到编辑页面
	 * @param student_id
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping("/updStudent/{student_id}/{pageNo}")
	public String updStudent(@PathVariable(value="student_id") Long student_id,@PathVariable(value="pageNo") Integer pageNo,Model model){
		//根据学员编号找出学员对象
		Student student = this.studentService.selectByPrimaryKey(student_id);
		if(student.getLogo_img()!=null){
			//头像(私有)
			long logo_id = Long.parseLong(student.getLogo_img());
			OssImg ossImg=this.ossImgService.selectByPrimaryKey(logo_id);
			ossImg.setUrl(myFileHandleUtil.get_signature_url(PathType.avatar_service_logo,ossImg,3600L));
			model.addAttribute("logo_img", ossImg);
		}else{
			OssImg ossImg=new OssImg();
			model.addAttribute("logo_img",ossImg);
		}
		//找到所对应的图片
		if(student.getHealth()!=null){
			//健康证
			long sh_id = Long.parseLong(student.getHealth());
			OssImg ossImg=this.ossImgService.selectByPrimaryKey(sh_id);
			ossImg.setUrl(myFileHandleUtil.get_signature_url(PathType.image_student_health,ossImg,3600L));
			model.addAttribute("img", ossImg);
		}else{
			OssImg ossImg=new OssImg();
			model.addAttribute("img",ossImg);
		}
		
		if(student.getCard_just()!=null){
			//身份证正面
			long just_id = Long.parseLong(student.getCard_just());
			OssImg justimg=this.ossImgService.selectByPrimaryKey(just_id);
			justimg.setUrl(myFileHandleUtil.get_signature_url(PathType.image_user_idcard,justimg,3600L));
			model.addAttribute("img_just", justimg);
		}else{
			OssImg ossImg=new OssImg();
			model.addAttribute("img_just",ossImg);
		}
		
		if(student.getCard_against()!=null){
			//身份证反面
			long against_id = Long.parseLong(student.getCard_against());
			OssImg againstImg = this.ossImgService.selectByPrimaryKey(against_id);
			againstImg.setUrl(myFileHandleUtil.get_signature_url(PathType.image_user_idcard,againstImg,3600L));
			model.addAttribute("img_against", againstImg);
		}else{
			OssImg ossImg=new OssImg();
			model.addAttribute("img_against",ossImg);
		}
		if(student.getInnocence_proof()!=null){
			//无犯罪证明
            Map maps=JSON.parseObject(student.getInnocence_proof());
            long innocence_proof_id = Long.parseLong(maps.get("id").toString());

			OssImg innocenceProofImg = this.ossImgService.selectByPrimaryKey(innocence_proof_id);
            innocenceProofImg.setUrl(myFileHandleUtil.get_signature_url(PathType.img_student_Innocence,innocenceProofImg,3600L));
			model.addAttribute("img_innocence", innocenceProofImg);
		}else{
			OssImg ossImg=new OssImg();
			model.addAttribute("img_innocence",ossImg);
		}
		//根据学员编号找出所对应的服务项目
		StudentServiceitemExample serviceitemExample=new StudentServiceitemExample();
		serviceitemExample.or().andStudent_idEqualTo(student_id).andTrain_plan_idIsNull();
		List<StudentServiceitem> pfsList = this.studentServiceitemService.selectByExample(serviceitemExample);
		
		//已具备的服务项目
		StudentServiceitemExample example=new StudentServiceitemExample();
		example.or().andStudent_idEqualTo(student_id).andTrain_plan_idIsNotNull().andStatusEqualTo(1);
		List<StudentServiceitem> ssList = this.studentServiceitemService.selectByExample(example);
		//查出所有的 服务项目
		ServiceitemExample serviceitemExample2 = new ServiceitemExample();
		serviceitemExample2.or().andDeletedEqualTo(0);
		List<Serviceitem> perList = this.serviceitemService.selectByExample(serviceitemExample2);
		//查出隶属合伙人
		PartnerExample partnerExample = new PartnerExample();
		//partnerExample.or().andDeletedEqualTo(0);
		List<Partner> pList = this.partnerService.selectByExample(partnerExample);
		if(ssList.size() !=0){
			model.addAttribute("ssList", ssList);
		}else{
			model.addAttribute("ssList", new ArrayList<StudentServiceitem>());
		}
		model.addAttribute("pList", pList);
		if(pfsList.size() !=0){
			model.addAttribute("pfsList", pfsList);
		}else{
			model.addAttribute("pfsList", new ArrayList<StudentServiceitem>());
		}
		
		
		String job_cret = student.getJob_cert();
		List<Map<String,String>> parseArray = JSON.parseObject(job_cret, new TypeReference<List<Map<String, String>>>() {

		});
		/*for (Map<String, String> map : parseArray) {
			OssImg img = ossImgService.selectByPrimaryKey(Long.parseLong(map.get("id")));
			map.put("filename", img.getName());
		}*/
		//查出隶属合伙人的所有站点
		StationExample stationExample = new StationExample();
		stationExample.or().andPartner_idEqualTo(student.getPartner_id());
		List<Station> stationList = this.stationService.selectByExample(stationExample);

        SkuTime Times =new SkuTime();
		if(student.getService_times()!=null){
            Times=JSONObject.parseObject(student.getService_times(), SkuTime.class);
        }

        List<Integer> weeks =new ArrayList<>();
        if(student.getService_cycle()!=null){
            weeks=JSONArray.parseArray(student.getService_cycle(), Integer.class);
        }
        model.addAttribute("list_cert", parseArray);
		model.addAttribute("stationList", stationList);
		model.addAttribute("perList", perList);
		model.addAttribute("student", student);
        model.addAttribute("Times", Times);
        model.addAttribute("weeks", weeks);
		model.addAttribute("pageNo", pageNo);
		return "student/student_edit";
	}
	
	/**
	 * 修改学员
	 * @param student_id
	 * @return
	 */
	@RequestMapping("/editStudent/{student_id}")
	//@ResponseBody
	@Transactional
	public String editStudent(@PathVariable(value="student_id") Long student_id,String studentL,
							  MultipartFile[] job_cret,
							  MultipartFile s_health,
							  MultipartFile s_just,
							  MultipartFile s_against,
							  MultipartFile logo,
                              MultipartFile s_Innocence_proof,
							  Integer pageNo,Authentication authentication){


		Student stu = this.studentService.selectByPrimaryKey(student_id);

		//获取到图片
		String logo_name=logo.getOriginalFilename();
		String originalFilename = s_health.getOriginalFilename();
		String against_name = s_against.getOriginalFilename();
		String just_name = s_just.getOriginalFilename();
		String innocence_proof_name = s_Innocence_proof.getOriginalFilename();

		//获取前台传到的json数据
		//String str = request.getParameter("result");
		//将字符串json数据转换成json对象
		JSONObject resultJson=JSONObject.parseObject(studentL);
		//将json对象按照map进行封装
		Map<String,Object> map=resultJson;

		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[student] F[editStudent] U[{}] params student_id:{}, 学员基本信息以及技能证书图片文件对象集合map:{}, " +
						"健康证 s_health:{},身份证(正) s_just:{},身份证(反) s_against:{},头像 logo:{},当前页 pageNo:{}",
				users.getUser_id(),student_id,map,s_health,s_just,s_against,logo,pageNo);

		//创建学员集合
		JSONArray jsonArry = JSONArray.parseArray(map.get("studentList").toString());
		List<Student> sList = jsonArry.toJavaList(Student.class);
		Student student = sList.get(0);
		student.setUser_id(stu.getUser_id());
		
		JSONArray img_ids = JSONArray.parseArray(map.get("img_list").toString());
		List<String> imgList = img_ids.toJavaList(String.class);
		
		if(job_cret!=null){
			List<Map<String,Object>> job_crets=new ArrayList<>();
			for (int i = 0; i < job_cret.length; i++) {
				Map<String,Object> json=new HashMap<>();
				for (int j = i; j < imgList.size(); j++) {
					long id = Long.parseLong(imgList.get(j));
					if(!StringUtils.isEmpty(job_cret[i].getOriginalFilename())){
						if(id==0 & job_cret[i].getOriginalFilename()!=""){
							Map<String, String> params = this.myFileHandleUtil.file_upload(job_cret[i],PathType.img_student_cert);
							String effect="学员技能证书";
							String privileges="public";
							OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
							json.put("id", img.getOss_img_id());
							json.put("url", img.getUrl());
							job_crets.add(json);

						}/*else{//没有更改的图片
							long sid = Long.parseLong(imgList.get(j));
							OssImg img = this.ossImgService.selectByPrimaryKey(sid);
							json.put("id", img.getOss_img_id());
							json.put("url", img.getUrl());
							job_crets.add(json);
						}*/
						break;
					}else{
						if(id!=0){
							long sid = Long.parseLong(imgList.get(j));
							OssImg img = this.ossImgService.selectByPrimaryKey(sid);
							json.put("id", img.getOss_img_id());
							json.put("url", img.getUrl());
							job_crets.add(json);
							break;
						}
					}
				}
			}
			student.setJob_cert(JSONArray.toJSONString(job_crets));
			
		}
		
		
		
		//学员头像
		if(logo_name.equals("") || logo==null){
			student.setDeleted(stu.getDeleted());
			student.setCdate(stu.getCdate());
			student.setLogo_img(stu.getLogo_img());
			//保存编辑后的内容推荐
			this.studentService.updateByPrimaryKey(student);
		}else{
			String logos = student.getLogo_img();
			if(!("".equals(logos)) || logos!=null){
				//显示切换了图片，先将原来的图片删除
				if(stu.getLogo_img()!=null){
					OssImg ossImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(stu.getLogo_img()));
					this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
				}
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(logo,PathType.avatar_service_logo);
				//保存图片信息
				String effect="学员头像图";
				String privileges="public";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setLogo_img(img.getOss_img_id().toString());
				student.setCdate(stu.getCdate());
				student.setDeleted(stu.getDeleted());
				this.studentService.updateByPrimaryKeySelective(student);
			}
		}
		
		
		//身份证正面
		if(just_name.equals("") || s_just==null){
			student.setDeleted(stu.getDeleted());
			student.setCdate(stu.getCdate());
			student.setCard_just(stu.getCard_just());
			//保存编辑后的内容推荐
			this.studentService.updateByPrimaryKey(student);
		}else{
			String just = student.getCard_just();
			if(!("".equals(just)) || just!=null){
				//显示切换了图片，先将原来的图片删除
				if(stu.getCard_just()!=null){
					OssImg ossImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(stu.getCard_just()));
					this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
				}
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(s_just,PathType.image_user_idcard);
				//保存图片信息
				String effect="学员身份证正面图";
				String privileges="private";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setCard_just(img.getOss_img_id().toString());
				student.setCdate(stu.getCdate());
				student.setDeleted(stu.getDeleted());
				this.studentService.updateByPrimaryKeySelective(student);
			}
		}
				
				
		//身份证反面
		if(against_name.equals("") || s_against==null){
			student.setDeleted(stu.getDeleted());
			student.setCdate(stu.getCdate());
			student.setCard_against(stu.getCard_against());
			//保存编辑后的内容推荐
			this.studentService.updateByPrimaryKey(student);
		}else{
			String against = student.getCard_against();
			if(!("".equals(against)) || against!=null){
				//显示切换了图片，先将原来的图片删除
				if(stu.getCard_against()!=null){
					OssImg ossImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(stu.getCard_against()));
					this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
				}
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(s_against,PathType.image_user_idcard);
				//保存图片信息
				String effect="学员身份证反面图";
				String privileges="private";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setCard_against(img.getOss_img_id().toString());
				student.setCdate(stu.getCdate());
				student.setDeleted(stu.getDeleted());
				this.studentService.updateByPrimaryKeySelective(student);
			}
		}

		
		//健康证
		//判断图片是否修改
		if(originalFilename.equals("") || s_health == null){
			student.setDeleted(stu.getDeleted());
			student.setCdate(stu.getCdate());
			student.setHealth(stu.getHealth());
			//保存编辑后的内容推荐
			this.studentService.updateByPrimaryKey(student);
		}else{
			String health = student.getHealth();
			if(!("".equals(health)) || health!=null){
				//显示切换了图片，先将原来的图片删除
				if(stu.getHealth()!=null){
					OssImg ossImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(stu.getHealth()));
					this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
				}
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(s_health,PathType.image_student_health);
				//保存图片信息
				String effect="学员健康证图";
				String privileges="private";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setHealth(img.getOss_img_id().toString());
				student.setCdate(stu.getCdate());
				student.setDeleted(stu.getDeleted());
				this.studentService.updateByPrimaryKeySelective(student);
			}
		}

        if(innocence_proof_name.equals("") || s_Innocence_proof == null){
            student.setDeleted(stu.getDeleted());
            student.setCdate(stu.getCdate());
            student.setInnocence_proof(stu.getInnocence_proof());
            //保存编辑后的内容推荐
            this.studentService.updateByPrimaryKey(student);
        }else{
            String innocence_proof = student.getInnocence_proof();
            if(!("".equals(innocence_proof)) || innocence_proof!=null){
                //显示切换了图片，先将原来的图片删除
                if(stu.getInnocence_proof()!=null){
                    Map maps=JSON.parseObject(stu.getInnocence_proof());
                    OssImg ossImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(maps.get("id").toString()));
                    this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
                }
                //设置要上传的bucket
                Map<String, String> params = this.myFileHandleUtil.file_upload(s_Innocence_proof,PathType.img_student_Innocence);
                //保存图片信息
                String effect="学员无犯罪证明图";
                String privileges="private";
                OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
                Map<String,Object> json=new HashMap<>();
                json.put("id", img.getOss_img_id());
                json.put("url", img.getUrl());
                student.setInnocence_proof(JSONArray.toJSONString(json));
                student.setCdate(stu.getCdate());
                student.setDeleted(stu.getDeleted());
                this.studentService.updateByPrimaryKeySelective(student);
            }
        }

		//封装（修改学员）
		this.studentService.xEditStudent(student_id,map);
		logger.info("M[student] F[editStudent] U[{}] execute 最后编辑完学员信息 result:{}",users.getUser_id(),student);
		//清除掉之前的缓存
		this.cacheReloadHandler.my_employeeManagementReload(stu.getPartner_id());
		this.cacheReloadHandler.my_employeeManagementReload(student.getPartner_id());
		this.cacheReloadHandler.studentInfoByUserIdReload(stu.getUser_id());
		this.cacheReloadHandler.selectStuUndoneOrderReload(student_id);
		this.cacheReloadHandler.studentInfoByUserIdReload(stu.getUser_id());
		return "redirect:/student/showStudent?p="+pageNo;
	}
	
	/**
	 * 导入
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/leadStudent")
	@Transactional
	public Object leadStudent(MultipartFile file,Authentication authentication){
		//读取excle内容增加学员

		HashMap<String,Object> map=this.studentService.xAddStudent(file);
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[student] F[addStudent] U[{}] ,execute result:{}",users.getUser_id(),map.get("result"));

		return map;
	}
	
	
	
	/**
	 * 导出
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportStudent")
	//@ResponseBody
	public void exportStudent(HttpServletResponse response,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[student] F[exportStudent] U[{}] ",users.getUser_id());
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        Sheet sheet = wb.createSheet("学员信息");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        Row row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        Cell cell = row.createCell(0);  
        cell.setCellValue("姓名");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("隶属合伙人");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("身份证号");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("性别");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("年龄");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("创建时间");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("状态");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("服务项目");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);  
        cell.setCellValue("等级");  
        cell.setCellStyle(style);
        
        //封装（根据excle格式定义导出数据）
        this.studentService.xExportStudent(sheet,row,cell); 
        // 第六步，弹框将文件存到指定位置  
        try {
        	response.setContentType("application/vnd.ms-excel;charset=utf-8");
        	response.setCharacterEncoding("utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=student.xls");
        	ServletOutputStream out = response.getOutputStream();
        	wb.write(out);
        	out.close();
        } catch (Exception e1) {
			e1.printStackTrace();
			logger.error("异常", e1);
		}
    }  
	
	/**
	 * 下载excle模板
	 */
	@RequestMapping("/downloadExcle")
	public String downloadExcle(HttpServletResponse response){
		try {
			File file =ResourceUtils.getFile("classpath:templates/student/学员信息.xlsx");
			InputStream is = new FileInputStream(file);
			XSSFWorkbook workbook;
			try {
				workbook = new XSSFWorkbook(is);
				XSSFSheet sheet = workbook.getSheetAt(0);
				int rowIndex = 0;
				XSSFRow titleRow = sheet.getRow(rowIndex++);
				rowIndex++;
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        	response.setCharacterEncoding("utf-8");
	        	response.setHeader("Content-Disposition","attachment;filename=student.xlsx");
	        	ServletOutputStream out = response.getOutputStream();
	        	workbook.write(out);
	        	out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询没添加过的服务项目
	 * @param request
	 * @return
	 */
	@RequestMapping("/screenServiceItem")
	@ResponseBody
	public List<Serviceitem> screenServiceItem(HttpServletRequest request,Authentication authentication){

		String str = request.getParameter("result");
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		List<Serviceitem> list=this.studentService.xScreenServiceItem(str);
		return list;
	}
	
	/**
	 * 根据服务项目id找到该对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/ServiceItemSelect/{id}")
	@ResponseBody
	public Object ServiceItemSelect(@PathVariable(value="id") Long id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		//根据id找到服务项目对象
		Serviceitem serviceitem = this.serviceitemService.selectByPrimaryKey(id);
		return serviceitem;
	}
	
	/**
	 * 根据合伙人id找到对应的全部站点信息
	 * @return
	 */
	@RequestMapping("/partnerSelectStation/{partner_id}")
	@ResponseBody
	public Object partnerSelectStation(@PathVariable(value="partner_id") Long partner_id){
		Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
		//查出所有的站点
		StationExample stationExample = new StationExample();
		stationExample.or().andPartner_idEqualTo(partner_id);
		List<Station> stationList = this.stationService.selectByExample(stationExample);
		return stationList;
	}

	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("/addStudentShow")
	public Object addStudentShow(Model model){
		//查出隶属合伙人
		PartnerExample partnerExample = new PartnerExample();
		//partnerExample.or().andDeletedEqualTo(0);
		List<Partner> pList = this.partnerService.selectByExample(partnerExample);
		//查出隶属合伙人的所有站点
        StationExample stationExample = new StationExample();
        stationExample.or().andPartner_idEqualTo(pList.get(0).getPartner_id());
        List<Station> stationList = this.stationService.selectByExample(stationExample);
		model.addAttribute("stationList",stationList);
		model.addAttribute("pList",pList);
		return "student/student_add";
	}

	/**
	 * 添加学生
	 * @param studentL
	 * @param job_cret
	 * @param s_health
	 * @param s_just
	 * @param s_against
	 * @param logo
	 * @param s_Innocence_proof
	 * @param authentication
	 * @return
	 */
	@RequestMapping("/addStudent")
	//@ResponseBody
	@Transactional
	public String addStudent(String studentL,
							  MultipartFile[] job_cret,
							  MultipartFile s_health,
							  MultipartFile s_just,
							  MultipartFile s_against,
							  MultipartFile logo,
							  MultipartFile s_Innocence_proof,
							  Authentication authentication){

		//获取到图片
		String logo_name=logo.getOriginalFilename();
		String originalFilename = s_health.getOriginalFilename();
		String against_name = s_against.getOriginalFilename();
		String just_name = s_just.getOriginalFilename();
		String Innocence_proof_name = s_Innocence_proof.getOriginalFilename();
		//获取前台传到的json数据
		//String str = request.getParameter("result");
		//将字符串json数据转换成json对象
		JSONObject resultJson=JSONObject.parseObject(studentL);
		//将json对象按照map进行封装
		Map<String,Object> map=resultJson;

		//创建学员集合
		JSONArray jsonArry = JSONArray.parseArray(map.get("studentList").toString());
		List<Student> sList = jsonArry.toJavaList(Student.class);
		Student student = sList.get(0);
		student.setStudent_id(IdGenerator.generateId());
		student.setCdate(new Date());
		student.setDeleted(0);

		if(job_cret!=null){
			List<Map<String,Object>> job_crets=new ArrayList<>();
			for (int i = 0; i < job_cret.length; i++) {
				Map<String,Object> json=new HashMap<>();
				Map<String, String> params = this.myFileHandleUtil.file_upload(job_cret[i],PathType.img_student_cert);
				String effect="学员技能证书";
				String privileges="public";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				json.put("id", img.getOss_img_id());
				json.put("url", img.getUrl());
				job_crets.add(json);
			}
			student.setJob_cert(JSONArray.toJSONString(job_crets));
		}else{
			student.setJob_cert(null);
		}



		//学员头像
		if(!(logo_name.equals(""))){
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(logo,PathType.avatar_service_logo);
				//保存图片信息
				String effect="学员头像图";
				String privileges="public";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setLogo_img(img.getOss_img_id().toString());
		}else{
			student.setLogo_img(null);
		}


		//身份证正面
		if(!(just_name.equals(""))){
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(s_just,PathType.image_user_idcard);
				//保存图片信息
				String effect="学员身份证正面图";
				String privileges="private";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setCard_just(img.getOss_img_id().toString());
		}else{
			student.setCard_just(null);
		}


		//身份证反面
		if(!(against_name.equals(""))){
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(s_against,PathType.image_user_idcard);
				//保存图片信息
				String effect="学员身份证反面图";
				String privileges="private";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setCard_against(img.getOss_img_id().toString());
		}else{
			student.setCard_against(null);
		}


		//健康证
		//判断图片是否修改
		if(!(originalFilename.equals(""))){
				//设置要上传的bucket
				Map<String, String> params = this.myFileHandleUtil.file_upload(s_health,PathType.image_student_health);
				//保存图片信息
				String effect="学员健康证图";
				String privileges="private";
				OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
				student.setHealth(img.getOss_img_id().toString());
		}else{
			student.setHealth(null);
		}

		if(!(Innocence_proof_name.equals(""))){
			//设置要上传的bucket
			Map<String, String> params = this.myFileHandleUtil.file_upload(s_Innocence_proof,PathType.img_student_Innocence);
			//保存图片信息
			String effect="学员无犯罪证明";
			String privileges="private";
			OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
			Map<String,Object> json=new HashMap<>();
			json.put("id", img.getOss_img_id());
			json.put("url", img.getUrl());
			student.setInnocence_proof(JSONArray.toJSONString(json));
		}else{
			student.setInnocence_proof(null);
		}

		//封装（修改学员）
		this.studentService.xAddStudent(student,map);
		return "redirect:/student/showStudent";
	}

    /**
     * 点击保险设置  查询出添加过得保险
     * @param student_id
     * @param pageNo
     * @return
     */
    @RequestMapping("/sendInsurance/{student_id}/{pageNo}")
    @ResponseBody
    public List<Insurance> sendInsurance(@PathVariable(value="student_id") Long student_id,@PathVariable(value="pageNo") Integer pageNo){
        List<Insurance> insuranceList=new ArrayList<>();
        InsuranceRegisterExample insuranceRegisterExample = new InsuranceRegisterExample();
        insuranceRegisterExample.or().andUidEqualTo(student_id);
        List<InsuranceRegister> insuranceRegisters = this.insuranceRegisterService.selectByExample(insuranceRegisterExample);
        if(insuranceRegisters.size()!=0){
            insuranceRegisters.stream().forEach(insuranceRegister ->{
                Insurance insurance = this.insuranceService.selectByPrimaryKey(insuranceRegister.getInsurance_id());
                insuranceList.add(insurance);
            });
        }
        return insuranceList;
    }


	/**
	 * 查询学生不具备的保险
	 * @param request
	 * @param student_id
	 * @return
	 */
	@RequestMapping("/screenInsurance/{student_id}")
    @ResponseBody
    public List<Insurance> screenInsurance(HttpServletRequest request,@PathVariable(value="student_id") Long student_id){
        String str = request.getParameter("result");
        List<Insurance> insuranceList=this.studentService.xScreenInsurance(str,student_id);
        return insuranceList;
    }


	/**
	 * 添加保险
	 * @param request
	 * @param student_id
	 * @return
	 */
	@RequestMapping("/addStudentInsurance/{student_id}")
    @ResponseBody
    @Transactional
    public Object addStudentInsurance(HttpServletRequest request,@PathVariable(value="student_id") Long student_id,Authentication authentication){
        String str = request.getParameter("result");
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[student] F[addInsurance] U[{}],params 保险集合request:{}, student_id:{}",users.getUser_id(),str,student_id);
        int num=this.studentService.xAddInsurance(str,student_id);
        Map<String, String> map = new HashMap<>();
        map.put("msg",String.format("设置%s", num> 0 ? "成功":"失败"));
		logger.info("M[student] F[addInsurance] U[{}],execute result:{}",users.getUser_id(),String.format("设置%s", num> 0 ? "成功":"失败"));
        return map;
    }

	/**
	 * 检查输入的学员电话是否已存在
	 * @param student_phone
	 * @return
	 */
	@RequestMapping("/student_phone_check_exist")
	@ResponseBody
	public Object student_phone_check_exist(String student_phone){
		StudentExample studentExample = new StudentExample();
		studentExample.or().andPhoneEqualTo(student_phone);
		List<Student> students = studentService.selectByExample(studentExample);
		if (students.size() > 0){
			return false;
		}else{
			return true;
		}
	}

}
