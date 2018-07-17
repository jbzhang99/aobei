package com.aobei.train.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aobei.train.service.AppPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.CmsBanner;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.CmsBannerMapper;
import com.aobei.train.model.CmsBannerExample;
import com.aobei.train.model.CmsBannerInfo;
import com.aobei.train.model.OssImg;
import com.aobei.train.model.CmsBannerExample.Criteria;
import com.aobei.train.service.CmsBannerService;
import com.aobei.train.service.OssImgService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class CmsBannerServiceImpl extends MbgServiceSupport<CmsBannerMapper, Long, CmsBanner, CmsBanner, CmsBannerExample> implements CmsBannerService{

	@Autowired
	private CmsBannerMapper cmsBannerMapper;
	
	@Autowired
	private OssImgService ossImgService;

	@Autowired
	private AppPackService appPackService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(cmsBannerMapper);
	}

	@Override
	public List<CmsBannerInfo> xSelectCmsBannerList(String app) {
		List<CmsBannerInfo> bannerInfo_list = new ArrayList<>();
		CmsBannerExample cmsBannerExample = new CmsBannerExample();
		Criteria or = cmsBannerExample.or();
		or.andDeletedEqualTo(Status.DeleteStatus.no.value);
		if(app != null) {
			or.andAppEqualTo(app);
		}
		cmsBannerExample.setOrderByClause(CmsBannerExample.C.serial_number + "");
		List<CmsBanner> banner_list = this.selectByExample(cmsBannerExample);
		if(banner_list.size() != 0) {
			bannerInfo_list = banner_list.stream().map(cmsBanner ->{
				CmsBannerInfo cmsBannerInfo = new CmsBannerInfo();
				String img_cover = cmsBanner.getImg_cover();
				OssImg ossImg = ossImgService.selectByPrimaryKey(Long.valueOf(img_cover));
				cmsBannerInfo.setCms_banner_id(cmsBanner.getCms_banner_id());
				cmsBannerInfo.setImg_cover(img_cover);
				cmsBannerInfo.setIntro(cmsBanner.getIntro());
				cmsBannerInfo.setApp(cmsBanner.getApp());
				cmsBannerInfo.setOnline_datetime(cmsBanner.getOnline_datetime());
				cmsBannerInfo.setOffline_datetime(cmsBanner.getOffline_datetime());
				cmsBannerInfo.setSerial_number(cmsBanner.getSerial_number());
				cmsBannerInfo.setSign(cmsBanner.getSign());
				cmsBannerInfo.setTitle(cmsBanner.getTitle());
				cmsBannerInfo.setOssImg(ossImg);
				cmsBannerInfo.setGroup_name(appPackService.selectByPrimaryKey(cmsBanner.getApp()).getApp_pack_name());
				return cmsBannerInfo;
			}).collect(Collectors.toList());
		}
		return bannerInfo_list;
	}

	@Override
	@Transactional(timeout = 5)
	public int xInsertCmsBanner(CmsBanner cmsBanner,String port) {
		//保存内容推荐
		cmsBanner.setCms_banner_id(IdGenerator.generateId());
		//查询并设置要上传的banner的序号
		CmsBannerExample cmsBannerExample = new CmsBannerExample();
		cmsBannerExample.includeColumns(CmsBannerExample.C.serial_number);
		cmsBannerExample.or().andAppEqualTo(cmsBanner.getApp());
		cmsBannerExample.setOrderByClause(CmsBannerExample.C.serial_number + " desc ");
		cmsBannerExample.setLimitStart(0l);
		cmsBannerExample.setLimitEnd(1l);

		String app  = cmsBanner.getApp();
		String[] apps  = app.split("_");
		port  = apps[apps.length-1];
		CmsBanner banner = DataAccessUtils.singleResult(this.selectByExample(cmsBannerExample));
		cmsBanner.setHref("ab"+port+"://productdetail?product_id="+cmsBanner.getHref());
		if(banner != null) {
			cmsBanner.setSerial_number(banner.getSerial_number()+1);
		}else {
			cmsBanner.setSerial_number(1);
		}

		return this.insertSelective(cmsBanner);
	}

	@Override
	@Transactional(timeout = 5)
	public int xUpdateCmsBannerByPrimaryKey(CmsBanner cmsBanner) {
		return this.updateByPrimaryKeySelective(cmsBanner);
	}

	@Override
	@Transactional(timeout = 5)
	public Map<String, Object> changeState(String sign,Long cms_banner_id) {
		Map<String , Object> map = new HashMap<>();
		if ("1".equals(sign)) {
			//修改为下线0
			CmsBanner cmsBanner = new CmsBanner();
			cmsBanner.setCms_banner_id(cms_banner_id);
			cmsBanner.setSign(0);
			int i = this.updateByPrimaryKeySelective(cmsBanner);
			map.put("msg", String.format("banner图信息下线%s!", i > 0 ? "成功" : "失败"));
		} else {
			//修改为上线1
			CmsBanner cmsBanner = new CmsBanner();
			cmsBanner.setCms_banner_id(cms_banner_id);
			cmsBanner.setSign(1);
			int i = this.updateByPrimaryKeySelective(cmsBanner);
			map.put("msg", String.format("banner图信息上线%s!", i > 0 ? "成功" : "失败"));
		}
		return map;
	}

	@Override
	@Transactional(timeout = 5)
	public boolean move_up(String serial_number, Long cms_banner_id, Long pre_cms_id) {
		CmsBanner cmsBanner = this.selectByPrimaryKey(cms_banner_id);
		CmsBanner pre_banner = this.selectByPrimaryKey(pre_cms_id);
		cmsBanner.setSerial_number(pre_banner.getSerial_number());
		pre_banner.setSerial_number(Integer.parseInt(serial_number));
		int i = this.updateByPrimaryKeySelective(pre_banner);
		int j = this.updateByPrimaryKeySelective(cmsBanner);
		if(i > 0 && j > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	@Transactional(timeout = 5)
	public boolean move_down(String serial_number, Long cms_banner_id, Long next_cms_id) {
		CmsBanner cmsBanner = this.selectByPrimaryKey(cms_banner_id);
		CmsBanner next_banner = this.selectByPrimaryKey(next_cms_id);
		cmsBanner.setSerial_number(next_banner.getSerial_number());
		next_banner.setSerial_number(Integer.parseInt(serial_number));
		int i = this.updateByPrimaryKeySelective(next_banner);
		int j = this.updateByPrimaryKeySelective(cmsBanner);
		if(i > 0 && j > 0) {
			return true;
		}else {
			return false;
		}
	}
}