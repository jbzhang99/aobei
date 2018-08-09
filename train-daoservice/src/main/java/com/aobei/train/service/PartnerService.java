package com.aobei.train.service;

import com.aobei.train.model.PartnerExample;

import java.util.Map;

import com.aobei.train.model.Users;

import com.aobei.train.model.Partner;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PartnerService extends MbgReadService<Long, Partner, Partner, PartnerExample>,MbgWriteService<Long, Partner, Partner, PartnerExample>,MbgUpsertService<Long, Partner, Partner, PartnerExample>{


	int xDelPartner(Long partner_id);

	void xPackNext(Partner partner, Map<String, String> justp, Map<String, String> againstp,
			Map<String, String> licensep, String ope_start, String ope_end, String coo_start, String coo_end,Map<String, String> partnerImg);

	Partner xAddPartner(Map<String, Object> map, Users users);

	Partner xUpdatePartner(Map<String, Object> map);

    int xAddPartnerToFallino(String str,Long partnerid);
}