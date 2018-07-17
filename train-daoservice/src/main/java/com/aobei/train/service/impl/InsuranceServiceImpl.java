package com.aobei.train.service.impl;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.InsuranceRegister;
import com.aobei.train.model.InsuranceRegisterExample;
import com.aobei.train.service.InsuranceRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Insurance;
import com.aobei.train.mapper.InsuranceMapper;
import com.aobei.train.model.InsuranceExample;import com.aobei.train.service.InsuranceService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InsuranceServiceImpl extends MbgServiceSupport<InsuranceMapper, Long, Insurance, Insurance, InsuranceExample> implements InsuranceService{

	@Autowired
	private InsuranceMapper insuranceMapper;

	@Autowired
	private InsuranceRegisterService insuranceRegisterService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(insuranceMapper);
	}

	@Override
	public int xAddInsurance(Insurance insurance, String startDatetime, String endDatetime) {
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		int num=0;
		try {
			//购买日期
			Date startDate = sf.parse(startDatetime);
			//失效日期
			Date endDate = sf.parse(endDatetime);
			insurance.setInsurance_id(IdGenerator.generateId());
			insurance.setCreate_datetime(new Date());
			insurance.setStart_datetime(startDate);
			insurance.setEnd_datetime(endDate);
			insurance.setType(1);
			num = this.insuranceMapper.insert(insurance);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return num;
	}

	@Override
	public int xEditInsurance(Insurance insurance, String startDatetime, String endDatetime) {
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

		int num=0;
		try {
			//购买日期
			Date startDate = sf.parse(startDatetime);
			//失效日期
			Date endDate = sf.parse(endDatetime);
			insurance.setStart_datetime(startDate);
			insurance.setEnd_datetime(endDate);
			num=this.insuranceMapper.updateByPrimaryKeySelective(insurance);

			//更改保险登记里面的保险开始，结束时间
            InsuranceRegisterExample insuranceRegisterExample = new InsuranceRegisterExample();
            insuranceRegisterExample.or().andInsurance_idEqualTo(insurance.getInsurance_id());
            List<InsuranceRegister> insuranceRegisters = this.insuranceRegisterService.selectByExample(insuranceRegisterExample);
            //不为空
            if(insuranceRegisters.size()!=0){
                insuranceRegisters.stream().forEach(insuranceRegister ->{
                    insuranceRegister.setInsurance_name(insurance.getInsurance_name());
                    insuranceRegister.setStart_datetime(startDate);
                    insuranceRegister.setEnd_datetime(endDate);
                    this.insuranceRegisterService.updateByPrimaryKeySelective(insuranceRegister);
                });
            }
        } catch (ParseException e) {
			e.printStackTrace();
		}
		return num;
	}
}