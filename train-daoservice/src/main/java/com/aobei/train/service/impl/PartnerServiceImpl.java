package com.aobei.train.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.mapper.PartnerMapper;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class PartnerServiceImpl extends MbgServiceSupport<PartnerMapper, Long, Partner, Partner, PartnerExample> implements PartnerService{

	@Autowired
	private PartnerMapper partnerMapper;
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
	private PartnerFallintoService partnerFallintoService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private FallintoService fallintoService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(partnerMapper);
	}


	/**
	 * 合伙人下线
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDelPartner(Long partner_id) {
		//根据id 找对对应的类
		Partner partner = this.partnerService.selectByPrimaryKey(partner_id);
		StudentExample studentExample = new StudentExample();
		studentExample.or().andPartner_idEqualTo(partner.getPartner_id());
		List<Student> studentList = this.studentService.selectByExample(studentExample);
		studentList.stream().forEach(student ->{
			student.setState(0);//合伙人已下线，对应学员变成离职状态
			this.studentService.updateByPrimaryKeySelective(student);
		});
		partner.setState(0);
		//partner.setDeleted(Status.DeleteStatus.yes.value);
		StationExample stationExample = new StationExample();
		stationExample.or().andPartner_idEqualTo(partner_id);
		List<Station> list_station = stationService.selectByExample(stationExample);
		list_station.stream().forEach(station ->{
			station.setOnlined(0);//站点下线
			stationService.updateByPrimaryKey(station);
		});
		//进行修改
		int num = this.partnerService.updateByPrimaryKey(partner);
		return num;
	}

	/**
	 * 新增合伙人跳转下一步页面代码
	 */
	@Override
	@Transactional
	public void xPackNext(Partner partner, Map<String, String> justp, Map<String, String> againstp,
			Map<String, String> licensep, String ope_start, String ope_end, String coo_start, String coo_end,Map<String, String> partnerImg) {

		//保存图片信息
		//身份证正面 
		OssImg JustossImg=new OssImg();
		JustossImg.setOss_img_id(IdGenerator.generateId());
		JustossImg.setFormat(justp.get("file_format"));
		JustossImg.setName(justp.get("file_name"));
		JustossImg.setUrl(justp.get("file_url"));
		JustossImg.setBucket(justp.get("bucketName"));
		JustossImg.setEffect("合伙人身份证正面");
		JustossImg.setAccess_permissions("private");
		this.ossImgService.insertSelective(JustossImg);
		//身份证反面
		OssImg AgainstossImg=new OssImg();
		AgainstossImg.setOss_img_id(IdGenerator.generateId());
		AgainstossImg.setFormat(againstp.get("file_format"));
		AgainstossImg.setName(againstp.get("file_name"));
		AgainstossImg.setUrl(againstp.get("file_url"));
		AgainstossImg.setBucket(againstp.get("bucketName"));
		AgainstossImg.setEffect("合伙人身份证反面");
		AgainstossImg.setAccess_permissions("private");
		this.ossImgService.insertSelective(AgainstossImg);
		
		//判断营业执照是否为空
		//!(licensep.get("file_format").equals(""))
		if(partnerImg.get("file_format")!= null){
			OssImg partnerLogImg=new OssImg();
			partnerLogImg.setOss_img_id(IdGenerator.generateId());
			partnerLogImg.setFormat(partnerImg.get("file_format"));
			partnerLogImg.setName(partnerImg.get("file_name"));
			partnerLogImg.setUrl(partnerImg.get("file_url"));
			partnerLogImg.setBucket(partnerImg.get("bucketName"));
			partnerLogImg.setEffect("合伙人头像");
			partnerLogImg.setAccess_permissions("private");
			this.ossImgService.insertSelective(partnerLogImg);
			partner.setLogo_img(partnerLogImg.getOss_img_id().toString());
		}
		if(licensep.get("file_format")!= null){
			OssImg LicensepossImg=new OssImg();
			LicensepossImg.setOss_img_id(IdGenerator.generateId());
			LicensepossImg.setFormat(licensep.get("file_format"));
			LicensepossImg.setName(licensep.get("file_name"));
			LicensepossImg.setUrl(licensep.get("file_url"));
			LicensepossImg.setBucket(licensep.get("bucketName"));
			LicensepossImg.setEffect("营业执照、组织机构代码证和税务登记证");
			LicensepossImg.setAccess_permissions("private");
			this.ossImgService.insertSelective(LicensepossImg);
			partner.setBusiness_license(LicensepossImg.getOss_img_id().toString());
		}
		
		partner.setIdentity_card_just(JustossImg.getOss_img_id().toString());
		partner.setIdentity_card_against(AgainstossImg.getOss_img_id().toString());
		try {
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			//营业开始日期
			if(!("".equals(ope_start))){
				partner.setOperation_start(sf.parse(ope_start));
			}
			//营业结束日期
			if(!("".equals(ope_end))){
				partner.setOperation_end(sf.parse(ope_end));
			}
			//合作开始日期
			if(!("".equals(coo_start))){
				partner.setCooperation_start(sf.parse(coo_start));
			}
			//合作结束日期
			if(!("".equals(coo_end))){
				partner.setCooperation_end(sf.parse(coo_end));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * 添加合伙人
	 */
	@Override
	@Transactional(timeout = 5)
	public Partner xAddPartner(Map<String, Object> map, Users users) {
		// TODO Auto-generated method stub
		//获取到填写的合伙人信息
		JSONObject json = JSONObject.parseObject(map.get("parList").toString());
		Partner partner = JSONObject.toJavaObject(json,Partner.class);
		partner.setPartner_id(IdGenerator.generateId());
		partner.setState(1);
		partner.setDeleted(Status.DeleteStatus.no.value);
		partner.setAudit_state(1);
		partner.setCreat_name(users.getUsername());
		String regex = "\\s+";
		partner.setLinkman(partner.getLinkman().replaceAll(regex,""));
		//合伙人表进行添加
		this.partnerService.insertSelective(partner);

		//获取到所有的id
		List<Long> longs = JSONObject.parseArray(map.get("ids").toString(),Long.class);
		//服务项和合伙人中间表进行添加
		for (Long l : longs) {
			PartnerServiceitem partnerServiceitem=new PartnerServiceitem();
			partnerServiceitem.setPartner_id(partner.getPartner_id());
			partnerServiceitem.setServiceitem_id(l);
			this.partnerServiceitemService.insertSelective(partnerServiceitem);
		}
		return partner;
	}


	/**
	 * 编辑合伙人基本信息
	 */
	@Override
	@Transactional(timeout = 5)
	public Partner xUpdatePartner(Map<String, Object> map) {
		//获取到填写的合伙人信息
		JSONObject json=JSONObject.parseObject(map.get("parList").toString());
		Partner partner = JSONObject.toJavaObject(json,Partner.class);
        /*partner.setState(1);
		String regex = "\\s+";
		partner.setLinkman(partner.getLinkman().replaceAll(regex,""));
        //找出修改之前的合伙人信息
        Partner oldPartner = this.partnerService.selectByPrimaryKey(partner.getPartner_id());
        partner.setUser_id(oldPartner.getUser_id());
		//合伙人表进行修改
		this.partnerService.updateByPrimaryKeySelective(partner);*/
		
		//根据合伙人编号，删除其对应的服务项目
		PartnerServiceitemExample serviceitemExample = new PartnerServiceitemExample();
		serviceitemExample.or().andPartner_idEqualTo(partner.getPartner_id());
		this.partnerServiceitemService.deleteByExample(serviceitemExample);
		//获取到所有的id
		List<Long> longs = JSONObject.parseArray(map.get("ids").toString(),Long.class);
		
		//服务项和合伙人中间表进行添加
		for (Long l : longs) {
			PartnerServiceitem ps=new PartnerServiceitem();
			ps.setPartner_id(partner.getPartner_id());
			ps.setServiceitem_id(l);
			this.partnerServiceitemService.insert(ps);
		}
		return partner;
	}


	/**
	 * 添加分成策略
	 * @param str
	 * @param partnerid
	 * @return
	 */
	@Override
	@Transactional(timeout = 5)
	public int xAddPartnerToFallino(String str,Long partnerid) {
		//先根据合伙人id找到之前的分成策略
		PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
		partnerFallintoExample.or().andPartner_idEqualTo(partnerid);
		this.partnerFallintoService.deleteByExample(partnerFallintoExample);


		List<PartnerFallinto> partnerFallintos = JSONObject.parseArray(str, PartnerFallinto.class);
		int num=0;
		if(partnerFallintos.size()!=0){
			partnerFallintos.stream().forEach(partnerFallinto ->{
                Fallinto fallinto = fallintoService.selectByPrimaryKey(partnerFallinto.getFallinto_id());
                partnerFallinto.setPartner_fallinto_id(IdGenerator.generateId());
				partnerFallinto.setCreate_datetime(new Date());
				partnerFallinto.setFallinto_type(fallinto.getFallinto_type());
				this.partnerFallintoService.insert(partnerFallinto);
			});
			num++;
		}
		return num;
	}


}