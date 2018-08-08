package com.aobei.trainconsole.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.aobei.train.handler.CacheReloadHandler;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.HttpAddressUtil;
import com.aobei.trainconsole.util.JacksonUtil;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.liyiorg.mbg.bean.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/partner")
public class PartnerController {
	
	private static final Logger logger= LoggerFactory.getLogger(PartnerController.class);
	
	@Autowired
	private MyFileHandleUtil myfileHandleUtil;
	
/*	@Autowired
	private CustomProperties customProperties;
*/
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ServiceitemService serviceitemService;
	
	@Autowired
	private PartnerServiceitemService partnerServiceitemService;
	
	@Autowired
	private OssImgService ossImgService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TrainCityService searchTrainAddressService;
	
	@Autowired
	private StationService stationService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private ProductService productService;

	@Autowired
	private FallintoService fallintoService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;

    @Autowired
    private CacheReloadHandler cacheReloadHandler;

	/**
	 * 列表显示页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/showPartner")
	public String showPartner(Model model,@RequestParam(value="p",defaultValue="1") Integer p,@RequestParam(value="ps",defaultValue="10") Integer ps,Authentication authentication,
							  @RequestParam(defaultValue="2") int state_selected, @RequestParam(defaultValue="0") int auditState_selected){

		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[showPartner] U[{}] log messages",users.getUser_id());

		//分页显示
		PartnerExample partnerExample = new PartnerExample();
        PartnerExample.Criteria criteria = partnerExample.or();
        if(state_selected !=2){
            criteria.andStateEqualTo(state_selected);
		}
		if(auditState_selected !=0){
            criteria.andAudit_stateEqualTo(auditState_selected);
		}
		partnerExample.setOrderByClause(PartnerExample.C.cdate+" desc");
		Page<Partner> page = this.partnerService.selectByExample(partnerExample,p,ps);
		List<Partner> partnerList = page.getList();
		//删除最后一页只有一条数据时，跳转到上一页
		if(partnerList.size() == 0 & p>1) {
			page = this.partnerService.selectByExample(partnerExample,p-1,ps);
			partnerList = page.getList();
		}
		model.addAttribute("partnerList", partnerList);
		model.addAttribute("page", page);
		model.addAttribute("state_selected",state_selected);
		model.addAttribute("auditState_selected",auditState_selected);
		return "partner/partner_list";
	}
	
	/**
	 * 跳转添加页面
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/addpartnerShow/{pageNo}")
	public String addpartnerShow(@PathVariable(value="pageNo") Integer pageNo,Model model){
        PartnerExample partnerExample = new PartnerExample();
        partnerExample.or().andDeletedEqualTo(0);
        List<Partner> partners = this.partnerService.selectByExample(partnerExample);
        model.addAttribute("partners",partners);
        return "partner/partner_add";
	}
	
	/**
	 * 下线
	 * @param partner_id
	 * @return
	 */
	@RequestMapping("/delPartner/{partner_id}")
	@ResponseBody
	@Transactional
	public Object delPartner(@PathVariable(value="partner_id")Long partner_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[delPartner] U[{}],params partner_id:{}",users.getUser_id(),partner_id);
		int num =this.partnerService.xDelPartner(partner_id);
		logger.info("M[partner] F[delPartner] U[{}],execute result:{}",users.getUser_id(),String.format("合伙人信息下线%s!", num> 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("合伙人信息下线%s!", num> 0 ? "成功" : "失败"));
		return map;
	}

    /**
     * 上线
     * @param partner_id
     * @param authentication
     * @return
     */
    @RequestMapping("/upPartner/{partner_id}")
    @ResponseBody
    @Transactional(timeout = 5)
    public Object upPartner(@PathVariable(value="partner_id")Long partner_id,Authentication authentication){
        //获取登录的用户id
        //Users users = usersService.xSelectUserByUsername(authentication.getName());
        //logger.info("M[partner] F[delPartner] U[{}],params partner_id:{}",users.getUser_id(),partner_id);
        //logger.info("M[partner] F[delPartner] U[{}],execute result:{}",users.getUser_id(),String.format("合伙人信息下线%s!", num> 0 ? "成功" : "失败"));
        Map<String, String> map = new HashMap<>();
        //检查是否存在分成策略
        PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
        partnerFallintoExample.or().andPartner_idEqualTo(partner_id);
        List<PartnerFallinto> partnerFallintos = this.partnerFallintoService.selectByExample(partnerFallintoExample);
        //检查是否存在合作项目
        PartnerServiceitemExample partnerServiceitemExample = new PartnerServiceitemExample();
        partnerServiceitemExample.or().andPartner_idEqualTo(partner_id);
        List<PartnerServiceitem> partnerServiceitems = this.partnerServiceitemService.selectByExample(partnerServiceitemExample);
        if(/*!partnerFallintos.isEmpty() && */!partnerServiceitems.isEmpty()){
            //根据id 找对对应的类
            Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
            StudentExample studentExample = new StudentExample();
            studentExample.or().andPartner_idEqualTo(partner.getPartner_id());
            List<Student> studentList = this.studentService.selectByExample(studentExample);
            studentList.stream().forEach(student ->{
                student.setState(1);//合伙人已上线，对应学员变成在职状态
                this.studentService.updateByPrimaryKeySelective(student);
            });
            partner.setState(1);
            //partner.setDeleted(Status.DeleteStatus.yes.value);
            StationExample stationExample = new StationExample();
            stationExample.or().andPartner_idEqualTo(partner_id);
            List<Station> list_station = stationService.selectByExample(stationExample);
            list_station.stream().forEach(station ->{
                station.setOnlined(1);//站点上线
                stationService.updateByPrimaryKey(station);
            });
            //进行修改
            int num = this.partnerService.updateByPrimaryKey(partner);
            map.put("msg", String.format("合伙人信息上线%s!", num> 0 ? "成功" : "失败"));
        }else{
            map.put("msg", String.format("上线失败，请将合伙人对应的合作项目信息填写完整！"));
        }
        return map;
    }
	
	/**
	 * 跳转下一步分类增加界面
	 * @return
	 */
	@RequestMapping("/next/{pageNo}")
	@Transactional
	public String next(Partner partner,Model model,@PathVariable(value="pageNo") Integer pageNo,
			MultipartFile just,MultipartFile against,MultipartFile license,String ope_start,String ope_end,
			String coo_start,String coo_end,MultipartFile partnerImg){
		//保存要上传的图片
		//设置要上传的bucket
		Map<String, String> justp = myfileHandleUtil.file_upload(just,PathType.image_user_idcard);
		Map<String, String> againstp = myfileHandleUtil.file_upload(against,PathType.image_user_idcard);
		Map<String, String> licensep=new HashMap<>();
		if(!("".equals(license.getOriginalFilename()))  ){
			licensep= myfileHandleUtil.file_upload(license,PathType.image_partner_businesslicense);
		}
		Map<String, String> partnerp=new HashMap<>();
		if(!("".equals(partnerImg.getOriginalFilename()))  ){
			partnerp= myfileHandleUtil.file_upload(partnerImg,PathType.avatar_partner_logo);
		}
		
		//保存图片的封装方法（合伙人对象,身份证正面,身份证反面,营业执照,营业开始日期,营业结束日期,合作开始日期,合作结束日期）
		this.partnerService.xPackNext(partner,justp,againstp,licensep,ope_start,ope_end,coo_start,coo_end,partnerp);
		
		//查询出所的父级分类
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andDeletedEqualTo(0).andActivedEqualTo(1);
		List<Category> categoryList = this.categoryService.selectByExample(categoryExample);
		//定义一个集合，封装有服务项目的分类
		List<Category> pcList=new ArrayList<>();
		for (Category category : categoryList) {
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(category.getCategory_id()).andDeletedEqualTo(0);
			List<Serviceitem> sList = this.serviceitemService.selectByExample(serviceitemExample);
			if(sList.size() !=0){
				pcList.add(category);
			}
		}
		//查询出所有的服务项目
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		serviceitemExample.or().andDeletedEqualTo(0);
		List<Serviceitem> perList = this.serviceitemService.selectByExample(serviceitemExample);
		
		model.addAttribute("pcList", pcList);
		model.addAttribute("perList", perList);
		model.addAttribute("pageNo", pageNo);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.addAttribute("partner",objectMapper.writeValueAsString(partner));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "partner/partner_next";
	}
	
	/**
	 * 添加合伙人信息
	 */
	@RequestMapping("/addPartner")
	@ResponseBody
	@Transactional
	public Object addPartner(HttpServletRequest request,Authentication authentication){
		//获取登录用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());

		//获取前台传到的json数据
		String str = request.getParameter("result");
		//将字符串json数据转换成json对象
		JSONObject resultJson=JSONObject.parseObject(str);
		//将json对象按照map进行封装
		Map<String,Object> map=resultJson;
		logger.info("M[partner] F[addPartner] U[{}],params 合伙人信息以及服务项id集合： map:{}",users.getUser_id(),map);
		//保存合伙人信息封装方法
		Partner partner=this.partnerService.xAddPartner(map,users);
		
		//获取虚拟地址，维护station表
		String stationList = request.getParameter("stationList");
		List<VirtualAddress> list_virtual = JSONObject.parseArray(stationList, VirtualAddress.class);
		if(!list_virtual.isEmpty()){
			logger.info("M[partner] F[addPartner] U[{}],params 合伙人虚拟地址集合：list_virtual:{}",users.getUser_id(),list_virtual);
			for (VirtualAddress virtualAddress : list_virtual) {
				//获取虚拟地址信息
				String address = virtualAddress.getAddress();
				String addressV = virtualAddress.getAddressV();
				//String sub_address = request.getParameter("street");
				if(address!=null){				
					//获取上下线时间
					Date online = virtualAddress.getOnline();
					Date upline = virtualAddress.getUpline();
					String province = "";
					String city ="";
					String area ="";
					String street ="";
					String lng = "";
					String lat = "";
					String[] split = addressV.split(" ");
					for (int i =0 ; i<split.length ; i++){
						province = split[0];
						city = split[1];
						area = split[2];
						
					}
					String[] split1 = address.split(" ");
					for (int i =0 ; i<split1.length ; i++){
						if(split1.length==4){							
							street = split1[3];
						}
					}
					//调用接口，查询经度纬度
					try {
						String replaceAll = address.replaceAll(" ", "");
						Map<String, String> coordinate = HttpAddressUtil.coordinate_GaoDe(replaceAll,city);
						if(coordinate!=null){							
							//经度
							lng = coordinate.get("lng_b");
							//纬度
							lat = coordinate.get("lat_b");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//为对应合伙人添加虚拟地址
					Station station = new Station();
					station.setStation_id(IdGenerator.generateId());
					station.setPartner_id(partner.getPartner_id());
					station.setAddress(address);
					station.setUsername(partner.getLinkman());
					station.setPhone(partner.getPhone());
					station.setDeleted(0);
					station.setLbs_lng(lng);
					station.setLbs_lat(lat);
					station.setCreate_datetime(new Date());
					station.setSub_address(street);
					station.setProvince(Integer.parseInt(province));
					station.setCreate_datetime(new Date());
					station.setOnline_datetime(online);
					station.setOffline_datetime(upline);
					if(station.getOnline_datetime().before(new Date()) && station.getOffline_datetime().after(new Date()) | station.getOnline_datetime()==new Date()){						
						station.setOnlined(1);//1为上线
					}else{
						station.setOnlined(0);;//0为下线
					}
					
					station.setArea(Integer.parseInt(area));
					station.setCity(Integer.parseInt(city));
					stationService.insertSelective(station);
				}
			}
		}

		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("message", "添加成功");
		logger.info("M[partner] F[addPartner] U[{}],execute result:{}",users.getUser_id(),resultMap.get("message"));
		return resultMap;
	}
	
	/**
	 * 跳转合伙人基本信息页面
	 * @return
	 */
	@RequestMapping("/editPartner/{partner_id}/{pageNo}")
	public String editPartner(@PathVariable(value="partner_id") Long partner_id,@PathVariable(value="pageNo") Integer pageNo,Model model){
		//根据partner_id查询出合伙人对象
		Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
		OssImg ossImg=new OssImg();
		
		//找到对应合伙人的图片
		//营业执照
		if(partner.getBusiness_license()!=null){
			long license_id = Long.parseLong(partner.getBusiness_license());
			OssImg ossimg = this.ossImgService.selectByPrimaryKey(license_id);
			if(ossimg!=null) {
				String get_signature_url = myfileHandleUtil.get_signature_url(PathType.image_partner_businesslicense, ossimg, 3600l);
				ossimg.setUrl(get_signature_url);
			}
			model.addAttribute("licenseUrl", ossimg);
		}else{
			model.addAttribute("licenseUrl", ossImg);
		}
		//身份证正面
		if(partner.getIdentity_card_just()!=null){
			long just_id = Long.parseLong(partner.getIdentity_card_just());
			OssImg ossimg = this.ossImgService.selectByPrimaryKey(just_id);
			if(ossimg!=null) {
				String get_signature_url = myfileHandleUtil.get_signature_url(PathType.image_user_idcard, ossimg, 3600l);
				ossimg.setUrl(get_signature_url);
			}
			model.addAttribute("justUrl", ossimg);
		}else{
			model.addAttribute("justUrl", ossImg);
		}
		
		//身份证反面
		if(partner.getIdentity_card_against()!=null){
			long against_id = Long.parseLong(partner.getIdentity_card_against());
			OssImg ossimg = this.ossImgService.selectByPrimaryKey(against_id);
			if(ossimg!=null){
				String get_signature_url = myfileHandleUtil.get_signature_url(PathType.image_user_idcard, ossimg, 3600l);
				ossimg.setUrl(get_signature_url);
			}
			model.addAttribute("againstUrl", ossimg);
		}else{
			model.addAttribute("againstUrl", ossImg);
		}

		if(partner.getLogo_img()!=null){
			long logo_id = Long.parseLong(partner.getLogo_img());
			OssImg ossimg = this.ossImgService.selectByPrimaryKey(logo_id);
			if(ossimg!=null) {
				String get_signature_url = myfileHandleUtil.get_signature_url(PathType.avatar_partner_logo, ossimg, 3600l);
				ossimg.setUrl(get_signature_url);
			}
			model.addAttribute("logoUrl", ossimg);
		}else{
			model.addAttribute("logoUrl", ossImg);
		}
			
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("partner", partner);
		//清除掉之前的缓存
		this.cacheReloadHandler.store_informationReload(partner.getPartner_id());
		this.cacheReloadHandler.partnerInfoByUserIdReload(partner.getUser_id());
		this.cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
		return "partner/partner_edit";
	}
	
	/**
	 * 合伙人基本信息修改
	 */
	@RequestMapping("/nextEdit/{pageNo}")
	@Transactional
	public String nextEdit(Partner partner,Model model,@PathVariable(value="pageNo") Integer pageNo
			,String ope_start,String ope_end,String coo_start,String coo_end,MultipartFile license,
			MultipartFile just,MultipartFile against,MultipartFile partnerImg){
		//现根据id找到对象
		Partner partnerAll = this.partnerService.selectByPrimaryKey(partner.getPartner_id());
		//保存要上传的图片
		String license_name = license.getOriginalFilename();
		String against_name = against.getOriginalFilename();
		String just_name = just.getOriginalFilename();
		String logo_name = partnerImg.getOriginalFilename();

		/*//查询合伙人关联所有站点信息
		StationExample stationExample = new StationExample();
		stationExample.or().andPartner_idEqualTo(partner.getPartner_id());
		List<Station> list_station = stationService.selectByExample(stationExample);*/
		
		//身份证反面
		if(against_name.equals("") || against==null){
			partner.setIdentity_card_against(partnerAll.getIdentity_card_against());
		}else{
			//显示切换了图片，先将原来的图片删除
			if(partnerAll.getIdentity_card_against()!=null){
				long against_id = Long.parseLong(partnerAll.getIdentity_card_against());
				OssImg AgainstossImg = this.ossImgService.selectByPrimaryKey(against_id);
				if(AgainstossImg !=null){
					this.ossImgService.deleteByPrimaryKey(AgainstossImg.getOss_img_id());
				}

			}
			//设置要上传的bucket
			Map<String, String> params = this.myfileHandleUtil.file_upload(against,PathType.image_user_idcard);
			//保存图片信息
			String effect="合伙人身份证反面图";
			String privileges="private";
			OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
			partner.setIdentity_card_against(img.getOss_img_id().toString());
		}
		//身份证正面
		if(just_name.equals("") || just==null){
			partner.setIdentity_card_just(partnerAll.getIdentity_card_just());
		}else{
			//显示切换了图片，先将原来的图片删除
			if(partnerAll.getIdentity_card_just()!=null){
				long just_id = Long.parseLong(partnerAll.getIdentity_card_just());
				OssImg justossImg = this.ossImgService.selectByPrimaryKey(just_id);
				if(justossImg!=null){
					this.ossImgService.deleteByPrimaryKey(justossImg.getOss_img_id());
				}

			}
			//设置要上传的bucket
			Map<String, String> params = this.myfileHandleUtil.file_upload(just,PathType.image_user_idcard);
			String effect="合伙人身份证正面图";
			String privileges="private";
			OssImg img = this.ossImgService.xInsertOssImg(params, effect, privileges);
			partner.setIdentity_card_just(img.getOss_img_id().toString());
		}
		
		if(license_name.equals("") || license==null){
			partner.setBusiness_license(partnerAll.getBusiness_license());
		}else{
			//显示切换了图片，先将原来的图片删除
			if(partnerAll.getBusiness_license()!=null){
				long license_id = Long.parseLong(partnerAll.getBusiness_license());
				OssImg ossImg = this.ossImgService.selectByPrimaryKey(license_id);
				if(ossImg !=null){
					this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
				}

			}
			//设置要上传的bucket
			Map<String, String> params = this.myfileHandleUtil.file_upload(license,PathType.image_partner_businesslicense);
			
			String effect="合伙人营业执照图";
			String privileges="private";
			OssImg imgs = this.ossImgService.xInsertOssImg(params, effect, privileges);
			partner.setBusiness_license(imgs.getOss_img_id().toString());
		}
		if(logo_name.equals("") || partnerImg==null){
			partner.setLogo_img(partnerAll.getLogo_img());
		}else{
			//显示切换了图片，先将原来的图片删除
			if(partnerAll.getLogo_img()!=null){
				long logo_id = Long.parseLong(partnerAll.getLogo_img());
				OssImg ossImg = this.ossImgService.selectByPrimaryKey(logo_id);
				if(ossImg !=null){
					this.ossImgService.deleteByPrimaryKey(ossImg.getOss_img_id());
				}
			}
			//设置要上传的bucket
			Map<String, String> params = this.myfileHandleUtil.file_upload(partnerImg,PathType.avatar_partner_logo);

			String effect="合伙人头像图";
			String privileges="private";
			OssImg imgs = this.ossImgService.xInsertOssImg(params, effect, privileges);
			partner.setLogo_img(imgs.getOss_img_id().toString());
		}
		try {
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			if(!(ope_start.equals(""))){
				partner.setOperation_start(sf.parse(ope_start));
			}
			if(!(ope_end.equals(""))){
				partner.setOperation_end(sf.parse(ope_end));
			}
			if(!(coo_start.equals(""))){
				partner.setCooperation_start(sf.parse(coo_start));
			}
			if(!(coo_end.equals(""))){
				partner.setCooperation_end(sf.parse(coo_end));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andDeletedEqualTo(0).andActivedEqualTo(1);
		List<Category> categoryList = this.categoryService.selectByExample(categoryExample);
		//定义一个集合，封装有服务项目的分类
		List<Category> pcList=new ArrayList<>();
		for (Category category : categoryList) {
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(category.getCategory_id()).andDeletedEqualTo(0);
			List<Serviceitem> sList = this.serviceitemService.selectByExample(serviceitemExample);
			if(sList.size() !=0){
				pcList.add(category);
			}
		}
		//查询出所有的服务项目
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		serviceitemExample.or().andDeletedEqualTo(0);
		List<Serviceitem> perList = this.serviceitemService.selectByExample(serviceitemExample);
		//查找出该合伙人对应的服务项目
		PartnerServiceitemExample example = new PartnerServiceitemExample();
		example.or().andPartner_idEqualTo(partner.getPartner_id());
		List<PartnerServiceitem> pfpList = this.partnerServiceitemService.selectByExample(example);
		
		ObjectMapper objectMapper = new ObjectMapper();
		model.addAttribute("pfpList",pfpList);
		model.addAttribute("perList", perList);
		model.addAttribute("pcList", pcList);
		try {
			model.addAttribute("partner",objectMapper.writeValueAsString(partner));
			String object_to_json = JacksonUtil.object_to_json(list_station);
			model.addAttribute("list_station", object_to_json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		model.addAttribute("pageNo", pageNo);*/
		//return "partner/partner_nextEdit";
        partner.setState(1);
        String regex = "\\s+";
        partner.setLinkman(partner.getLinkman().replaceAll(regex,""));
        //找出修改之前的合伙人信息
        Partner oldPartner = this.partnerService.selectByPrimaryKey(partner.getPartner_id());
        partner.setUser_id(oldPartner.getUser_id());
		this.partnerService.updateByPrimaryKeySelective(partner);
		return "redirect:/partner/showPartner?p="+pageNo;
	}

	@RequestMapping("/editPartnerCategory/{partner_id}/{pageNo}")
	public String editPartnerCategory(@PathVariable(value="partner_id") Long partner_id,@PathVariable(value="pageNo") Integer pageNo,Model model){
        Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
        //查询合伙人关联所有站点信息
        StationExample stationExample = new StationExample();
        stationExample.or().andPartner_idEqualTo(partner.getPartner_id());
        List<Station> list_station = stationService.selectByExample(stationExample);

        CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andDeletedEqualTo(0).andActivedEqualTo(1);
		List<Category> categoryList = this.categoryService.selectByExample(categoryExample);
		//定义一个集合，封装有服务项目的分类
		List<Category> pcList=new ArrayList<>();
		for (Category category : categoryList) {
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(category.getCategory_id()).andDeletedEqualTo(0);
			List<Serviceitem> sList = this.serviceitemService.selectByExample(serviceitemExample);
			if(sList.size() !=0){
				pcList.add(category);
			}
		}
		//查询出所有的服务项目
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		serviceitemExample.or().andDeletedEqualTo(0);
		List<Serviceitem> perList = this.serviceitemService.selectByExample(serviceitemExample);
		//查找出该合伙人对应的服务项目
		PartnerServiceitemExample example = new PartnerServiceitemExample();
		example.or().andPartner_idEqualTo(partner.getPartner_id());
		List<PartnerServiceitem> pfpList = this.partnerServiceitemService.selectByExample(example);

		ObjectMapper objectMapper = new ObjectMapper();
		model.addAttribute("pfpList",pfpList);
		model.addAttribute("perList", perList);
		model.addAttribute("pcList", pcList);
		try {
			model.addAttribute("partner",objectMapper.writeValueAsString(partner));
			String object_to_json = JacksonUtil.object_to_json(list_station);
			model.addAttribute("list_station", object_to_json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		model.addAttribute("pageNo", pageNo);
		return "partner/partner_nextEdit";
	}

	/**
	 * 合伙人下一步信息修改
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatePartner")
	@ResponseBody
	//@Transactional
	public Object updatePartner(HttpServletRequest request,Authentication authentication){
		//获取前台传到的json数据
		String str = request.getParameter("result");
		//将字符串json数据转换成json对象
		JSONObject resultJson=JSONObject.parseObject(str);
		//将json对象按照map进行封装
		Map<String,Object> map=resultJson;
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[updatePartner] U[{}],params 合伙人信息以及服务项id集合： map:{}",users.getUser_id(),map);
		//封装  （编辑合伙人基本信息）
		Partner partner= this.partnerService.xUpdatePartner(map);
		/*//先删除合伙人对应的所有站点信息，再从页面插入数据库
		StationExample stationExample = new StationExample();
		stationExample.or().andPartner_idEqualTo(partner.getPartner_id());
		List<Station> list_station = stationService.selectByExample(stationExample);
		for (Station station : list_station) {
			stationService.deleteByPrimaryKey(station.getStation_id());
		}*/
		//获取虚拟地址，维护satation表
		String stationList = request.getParameter("stationList");
		List<VirtualAddress> list_virtual = JSONObject.parseArray(stationList, VirtualAddress.class);
			if(!list_virtual.isEmpty()){
			logger.info("M[partner] F[updatePartner] U[{}],params 合伙人虚拟地址集合：list_virtual:{}",users.getUser_id(),list_virtual);
			for (VirtualAddress virtualAddress : list_virtual) {
				//获取虚拟地址信息
				String address = virtualAddress.getAddress();
				String addressV = virtualAddress.getAddressV();
				//String sub_address = request.getParameter("street");
				if(address!=null){				
					//获取上下线时间
					Date online = virtualAddress.getOnline();
					Date upline = virtualAddress.getUpline();
					String province = "";
					String city ="";
					String area ="";
					String street ="";
					String lng = "";
					String lat = "";
					String[] split = addressV.split(" ");
					for (int i =0 ; i<split.length ; i++){
						province = split[0];
						city = split[1];
						area = split[2];
					}
					String[] split1 = address.split(" ");
					for (int i =0 ; i<split1.length ; i++){
						if(split1.length==4){							
							street = split1[3];
						}
					}
					//调用接口，查询经度纬度
					try {
						String replaceAll = address.replaceAll(" ", "");
						Map<String, String> coordinate = HttpAddressUtil.coordinate_GaoDe(replaceAll,city);
						if(coordinate!=null){							
							//经度
							lng = coordinate.get("lng_b");
							//纬度
							lat = coordinate.get("lat_b");
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//为对应合伙人添加虚拟地址
					Station station = new Station();
					
					station.setPartner_id(partner.getPartner_id());
					station.setAddress(address);
					station.setUsername(partner.getLinkman());
					station.setPhone(partner.getPhone());
					station.setDeleted(0);
					station.setLbs_lng(lng);
					station.setLbs_lat(lat);
					//station.setCreate_datetime(new Date());
					station.setSub_address(street);
					station.setProvince(Integer.parseInt(province));

					station.setOnline_datetime(online);
					station.setOffline_datetime(upline);
					if(station.getOnline_datetime().before(new Date()) && station.getOffline_datetime().after(new Date()) | station.getOnline_datetime()==new Date()){						
						station.setOnlined(1);//1为上线
					}else{
						station.setOnlined(0);;//0为下线
					}
					station.setArea(Integer.parseInt(area));
					station.setCity(Integer.parseInt(city));
					if(virtualAddress.getId().length()<3){
						station.setCreate_datetime(new Date());
						station.setStation_id(IdGenerator.generateId());
						stationService.insertSelective(station);
					}else{
						//station.setCreate_datetime();
						station.setStation_id(Long.parseLong(virtualAddress.getId()));
						stationService.updateByPrimaryKeySelective(station);
					}
					
				}
		}
		}
		
		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("message", "编辑成功");
		logger.info("M[partner] F[updatePartner] U[{}],execute result:{}",users.getUser_id(),resultMap.get("message"));
		//清除掉之前的缓存
        this.cacheReloadHandler.store_informationReload(partner.getPartner_id());
        this.cacheReloadHandler.partnerInfoByUserIdReload(partner.getUser_id());
		this.cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
		return resultMap;
	}
	
	/**
	 * 删除合伙人站点
	 * @param station
	 * @return
	 */
	@Transactional(timeout = 5)
	@ResponseBody
	@RequestMapping("/del_station")
	public Object del_station(@RequestParam(value="station_id")Long station,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[del_station] U[{}],params station:{}",users.getUser_id(),station);
		HashMap<String,String> map = new HashMap<String,String>();
        Station stat = this.stationService.selectByPrimaryKey(station);
        if(station!=null){
			int i = stationService.deleteByPrimaryKey(station);
            this.cacheReloadHandler.store_informationReload(stat.getPartner_id());
            this.cacheReloadHandler.partnerInfoByUserIdReload(stat.getPartner_id());
			map.put("message", String.format("删除%s", i > 0 ? "成功":"失败"));
		}
		logger.info("M[partner] F[del_station] U[{}],execute result:{}",users.getUser_id(),map);
		return map;
	}


	/**
	 * 跳转到分成策略页面
	 * @param partner_id
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping("/addFallintoShow/{partner_id}/{pageNo}")
	public Object addFallintoShow(@PathVariable(value = "partner_id") Long partner_id,Model model,@PathVariable(value = "pageNo") int pageNo){
		//现根据合伙人编号找到其对应的服务项目
        PartnerServiceitemExample partnerServiceitemExample = new PartnerServiceitemExample();
        partnerServiceitemExample.or().andPartner_idEqualTo(partner_id);
        List<PartnerServiceitem> partnerServiceitems = this.partnerServiceitemService.selectByExample(partnerServiceitemExample);
        List<Product> ProductList=new ArrayList<>();
        //再根据服务项找到对应产品
        partnerServiceitems.stream().forEach(partnerServiceitem ->{
            ProductExample productExample = new ProductExample();
            productExample.or().andServiceitem_idEqualTo(partnerServiceitem.getServiceitem_id()).andDeletedEqualTo(0);
            List<Product> products = this.productService.selectByExample(productExample);
            products.stream().forEach(pro ->{
                boolean flag = ProductList.contains(pro.getProduct_id());
                if(flag==false){
                    ProductList.add(pro);
                }
            });

        });
        //查询出所有的分成策略
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.or().andActivedEqualTo(1).andDeletedEqualTo(0);
        List<Fallinto> fallintos = this.fallintoService.selectByExample(fallintoExample);

        List<Map<String,Object>> proAndFallintoList=new ArrayList<>();

        //根据商品找到对应的分成策略
        if(ProductList.size()!=0){
            ProductList.stream().forEach(product ->{
                PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                partnerFallintoExample.or().andProduct_idEqualTo(product.getProduct_id()).andPartner_idEqualTo(partner_id);
                List<PartnerFallinto> partnerFallintos = this.partnerFallintoService.selectByExample(partnerFallintoExample);
                Map<String,Object> maps=new HashMap<>();
                maps.put("product",product);
                if(partnerFallintos.size()!=0){
                    maps.put("partnerFallinto", DataAccessUtils.singleResult(partnerFallintos));
                }else{
                    maps.put("partnerFallinto",null);
                }
                maps.put("fallintos",fallintos);
                proAndFallintoList.add(maps);
            });
        }
        model.addAttribute("proAndFallintoList",proAndFallintoList);
        model.addAttribute("partner_id",partner_id);
        model.addAttribute("pageNo",pageNo);
        return "partner/partner_fallinto";
	}


	/**
	 * 保存分成策略
	 * @param request
	 * @param partnerid
	 * @return
	 */
    @Transactional
	@ResponseBody
    @RequestMapping("/addPartnerToFallino/{partnerid}")
	public Object addPartnerToFallino(HttpServletRequest request,@PathVariable(value = "partnerid") Long partnerid,Authentication authentication){

        //获取前台传到的json数据
        String str = request.getParameter("result");
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[partner] F[addPartnerToFallino] U[{}],params 策略集合request:{}, partnerid:{}",users.getUser_id(),str,partnerid);

       int num= this.partnerService.xAddPartnerToFallino(str,partnerid);
		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("msg",String.format("保存%s", num> 0 ? "成功":"失败"));
        logger.info("M[partner] F[addPartnerToFallino] U[{}],execute result:{}",users.getUser_id(),String.format("保存%s", num> 0 ? "成功":"失败"));
        return resultMap;
    }


    /**
     * 查询所有的分成策略
     * @return
     */
	@Transactional
	@ResponseBody
	@RequestMapping("/isHaveFallinto")
    public Object isHaveFallinto(){
		//查询出所有的分成策略
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.or().andActivedEqualTo(1);
        List<Fallinto> fallintos = this.fallintoService.selectByExample(fallintoExample);
		return fallintos;
	}

	/**
	 * 检查输入的合伙人电话是否已存在
	 * @return
	 */
	@RequestMapping("/partner_phone_check_exist")
	@ResponseBody
	public Object partner_phone_check_exist(String phone){
        PartnerExample partnerExample = new PartnerExample();
        partnerExample.or().andPhoneEqualTo(phone);
        List<Partner> partners = this.partnerService.selectByExample(partnerExample);
        if (partners.size() > 0){
			return false;
		}else{
			return true;
		}
	}


	/**
	 * 提审
	 * @param partner_id
	 * @param authentication
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/auditPartner/{partner_id}")
	public Object auditPartner(@PathVariable(value = "partner_id") Long partner_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[auditPartner] U[{}],params partnerid:{}",users.getUser_id(),partner_id);
        Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
        partner.setAudit_state(2);
        int num = this.partnerService.updateByPrimaryKey(partner);
        Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("msg",String.format("提审%s", num> 0 ? "成功":"失败"));
		logger.info("M[partner] F[auditPartner] U[{}],execute result:{}",users.getUser_id(),String.format("提审%s", num> 0 ? "成功":"失败"));
		return resultMap;
	}

	/**
	 * 合伙人信息审核列表
	 * @param model
	 * @param p
	 * @param ps
	 * @param authentication
	 * @return
	 */
	@RequestMapping("/showAuditPartner")
	public String showAuditPartner(Model model,@RequestParam(value="p",defaultValue="1") Integer p,@RequestParam(value="ps",defaultValue="10") Integer ps,Authentication authentication,
                                   @RequestParam(defaultValue="2") int state_selected, @RequestParam(defaultValue="0") int auditState_selected){

		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[showAuditPartner] U[{}] log messages",users.getUser_id());

		//分页显示
		PartnerExample partnerExample = new PartnerExample();
        PartnerExample.Criteria criteria = partnerExample.or();
        if(state_selected !=2){
            criteria.andStateEqualTo(state_selected);
        }
        if(auditState_selected !=0){
            criteria.andAudit_stateEqualTo(auditState_selected);
        }
        criteria.andAudit_stateBetween(2,4);
		partnerExample.setOrderByClause(PartnerExample.C.cdate+" desc");
		Page<Partner> page = this.partnerService.selectByExample(partnerExample,p,ps);
		List<Partner> partnerList = page.getList();
		//删除最后一页只有一条数据时，跳转到上一页
		if(partnerList.size() == 0 & p>1) {
			page = this.partnerService.selectByExample(partnerExample,p-1,ps);
			partnerList = page.getList();
		}
		model.addAttribute("partnerList", partnerList);
		model.addAttribute("page", page);
		model.addAttribute("state_selected",state_selected);
		model.addAttribute("auditState_selected",auditState_selected);
		return "partner/partner_audit_list";
	}

	/**
	 * 审核通过
	 * @param partner_id
	 * @param authentication
	 * @param auditOpinionValue
	 * @param auditState
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/saveAuditOpinion/{partner_id}")
	public Object saveAuditOpinion(@PathVariable(value = "partner_id") Long partner_id,Authentication authentication,String auditOpinionValue,int auditState){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[partner] F[saveAuditOpinion] U[{}],params partnerid:{},auditOpinionValue:{}",users.getUser_id(),partner_id,auditOpinionValue);
		Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
		Map<String, Object> resultMap=new HashMap<>();
		if(auditState==3){
			partner.setAudit_state(3);
			resultMap.put("msg",String.format("已驳回"));
            logger.info("M[partner] F[auditPartner] U[{}],execute result:{}",users.getUser_id(),String.format("已驳回"));
		}else if(auditState==4){
			partner.setAudit_state(4);
			resultMap.put("msg",String.format("已通过"));
            logger.info("M[partner] F[auditPartner] U[{}],execute result:{}",users.getUser_id(),String.format("已通过"));
		}
		partner.setAudit_opinion(auditOpinionValue);
		partner.setAudit_name(users.getUsername());
		int num = this.partnerService.updateByPrimaryKey(partner);


		return resultMap;
	}

    /**
     * 合伙人详情
     * @param partner_id
     * @param model
     * @return
     */
	@RequestMapping("/partnerDetail/{partner_id}")
	public String partnerDetail(@PathVariable(value = "partner_id") Long partner_id,Model model){
        Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
        OssImg logoImg =new OssImg();
        OssImg businessImg =new OssImg();
        OssImg justImg =new OssImg();
        OssImg againstImg =new OssImg();

        if(partner.getLogo_img() !=null){
            logoImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(partner.getLogo_img()));
        }
        if(partner.getBusiness_license() !=null) {
            businessImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(partner.getBusiness_license()));
            String url = myfileHandleUtil.get_signature_url(PathType.image_partner_businesslicense, businessImg, 3600l);
            businessImg.setUrl(url);
        }
        if(partner.getIdentity_card_just() !=null) {
            justImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(partner.getIdentity_card_just()));
            String url = myfileHandleUtil.get_signature_url(PathType.image_user_idcard, justImg, 3600l);
            justImg.setUrl(url);
        }
        if(partner.getIdentity_card_against() !=null) {
            againstImg = this.ossImgService.selectByPrimaryKey(Long.parseLong(partner.getIdentity_card_against()));
            String url = myfileHandleUtil.get_signature_url(PathType.image_user_idcard, againstImg, 3600l);
            againstImg.setUrl(url);
        }
        //合伙人包含的站点
        StationExample stationExample = new StationExample();
        stationExample.or().andPartner_idEqualTo(partner_id).andDeletedEqualTo(0);
        List<Station> stations = this.stationService.selectByExample(stationExample);

        //合伙人存在的服务项目
        List<Serviceitem> serviceitemList=new ArrayList<>();
        PartnerServiceitemExample partnerServiceitemExample = new PartnerServiceitemExample();
        partnerServiceitemExample.or().andPartner_idEqualTo(partner_id);
        List<PartnerServiceitem> partnerServiceitems = partnerServiceitemService.selectByExample(partnerServiceitemExample);
        if(!partnerServiceitems.isEmpty()){
            partnerServiceitems.stream().forEach(partnerServiceitem -> {
                Serviceitem serviceitem = serviceitemService.selectByPrimaryKey(partnerServiceitem.getServiceitem_id());
                serviceitemList.add(serviceitem);
            });
        }
        model.addAttribute("partner",partner);
        model.addAttribute("logoImg",logoImg);
        model.addAttribute("businessImg",businessImg);
        model.addAttribute("justImg",justImg);
        model.addAttribute("againstImg",againstImg);
        model.addAttribute("stations",stations);
        model.addAttribute("serviceitemList",serviceitemList);
        return "partner/partner_detail";
    }
}
