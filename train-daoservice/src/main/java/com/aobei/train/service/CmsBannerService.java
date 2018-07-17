package com.aobei.train.service;

import com.aobei.train.model.CmsBannerExample;
import com.aobei.train.model.CmsBannerInfo;

import java.util.List;
import java.util.Map;

import com.aobei.train.model.CmsBanner;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CmsBannerService extends MbgReadService<Long, CmsBanner, CmsBanner, CmsBannerExample>,MbgWriteService<Long, CmsBanner, CmsBanner, CmsBannerExample>,MbgUpsertService<Long, CmsBanner, CmsBanner, CmsBannerExample>{
	
	/**
	 * 根据要查询的端的参数来查询并封装banner信息
	 * @param app
	 * @return
	 */
	List<CmsBannerInfo> xSelectCmsBannerList(String app);
	
	/**
	 * 插入新的banner
	 * @param cmsBanner
	 * @return
	 */
	int xInsertCmsBanner(CmsBanner cmsBanner,String port);
	
	/**
	 * 更新banner信息
	 * @param cmsBanner
	 * @return
	 */
	int xUpdateCmsBannerByPrimaryKey(CmsBanner cmsBanner);
	
	/**
	 * 改变banner的上下线状态
	 * @param sign
	 * @param cms_banner_id
	 * @return
	 */
	Map<String , Object> changeState(String sign,Long cms_banner_id);
	
	/**
	 * 上移banner图，改变与前一张banner的序号
	 * @param serial_number
	 * @param cms_banner_id
	 * @param pre_cms_id
	 * @return
	 */
	boolean move_up(String serial_number,Long cms_banner_id,Long pre_cms_id);
	
	/**
	 * 下移banner图，改变与后一张banner的序号
	 * @param serial_number
	 * @param cms_banner_id
	 * @param pre_cms_id
	 * @return
	 */
	boolean move_down(String serial_number,Long cms_banner_id,Long next_cms_id);
}