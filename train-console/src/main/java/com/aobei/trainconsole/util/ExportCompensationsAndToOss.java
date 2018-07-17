package com.aobei.trainconsole.util;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.PathUtil.PathType;
import custom.bean.Programme_type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class ExportCompensationsAndToOss implements Callable<Integer>{
	
	@Autowired
	private PartnerService partnerService;

	@Autowired
	private VOrderUnitService vOrderUnitService;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private DataDownloadService dataDownloadService;
	
	@Autowired
	private OssFileService ossFileService;

	@Autowired
	private CompensationService compensationService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CouponService couponService;

	private CompensationExample compensationExample;
	
	private Long data_download_id;
	
	
	public Long getData_download_id() {
		return data_download_id;
	}


	public void setData_download_id(Long data_download_id) {
		this.data_download_id = data_download_id;
	}

	public CompensationExample getCompensationExample() {
		return compensationExample;
	}

	public void setCompensationExample(CompensationExample compensationExample) {
		this.compensationExample = compensationExample;
	}

	@Override
	public Integer call() throws Exception {
		List<Compensation> compensations = compensationService.selectByExample(compensationExample);
		List<String> orderids = compensations.stream().map(n -> n.getPay_order_id()).collect(Collectors.toList());
		List<Long> creators = compensations.stream().map(n -> n.getOperator_create()).collect(Collectors.toList());
		List<Long> confirmers = compensations.stream().map(n -> n.getOperator_confirm()).collect(Collectors.toList());
		VOrderUnitExample example = new VOrderUnitExample();
		if (orderids.size() > 0){
			example.or().andPay_order_idIn(orderids);
		} else {
			example.or().andPay_order_idEqualTo("00");
		}
		List<VOrderUnit> vOrderUnits = vOrderUnitService.selectByExample(example);
		Map<String, VOrderUnit> ou_map = vOrderUnits.stream().collect(Collectors.toMap(VOrderUnit::getPay_order_id, Function.identity()));
		UsersExample usersExample = new UsersExample();
		usersExample.setDistinct(true);
		if (confirmers.size() > 0){
			usersExample.or().andUser_idIn(confirmers);
		} else {
			usersExample.or().andUser_idEqualTo(0l);
		}
		if (creators.size() > 0){
			usersExample.or().andUser_idIn(creators);
		} else {
			usersExample.or().andUser_idEqualTo(0l);
		}

		List<Users> users = usersService.selectByExample(usersExample);
		Map<Long, Users> usersMap = users.stream().collect(Collectors.toMap(Users::getUser_id, Function.identity()));
		List<CompensationInfo> list = compensations.stream().map(n -> {
			Programme_type programmeType = null;
			if (n.getCoupon_id() != null){
				Coupon coupon = couponService.selectByPrimaryKey(n.getCoupon_id());
				if (coupon != null){
					programmeType = JSONObject.parseObject(coupon.getProgramme(), Programme_type.class);
				}
			}
			CompensationInfo compensationInfo = new CompensationInfo();
			compensationInfo.setCompensation(n);
			BeanUtils.copyProperties(ou_map.get(n.getPay_order_id()), compensationInfo);
			compensationInfo.setOperator_create(usersMap.get(n.getOperator_create()));
			compensationInfo.setOperator_confirm(usersMap.get(n.getOperator_confirm()));
			compensationInfo.setProgrammeType(programmeType);
			return compensationInfo;
		}).collect(Collectors.toList());

		String[] rowsName = new String[]{"赔偿单号","订单号","订单状态","服务名称", "用户姓名",
									    "顾客电话","服务地址","服务价格（元）","实际金额（元）","赔偿金额（元）","赔偿券额（元）",
										"赔偿原因","合伙人承担金额（元）","合伙人承担券金额（元）","赔偿申请人","收款人姓名","收款人卡号",
										"收款银行","合伙人公司名","服务人员","赔偿申请时间","赔偿状态",
										"确认时间","确认人"};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//合伙人集合
		Set<Long> par_set = list.stream().map(n -> n.getCompensation().getPartner_id()).collect(Collectors.toSet());
		List<Long> par_ids = new ArrayList<>(par_set);
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andPartner_idIn(par_ids);
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		Map<Long, Partner> par_map = partners.stream().collect(Collectors.toMap(Partner::getPartner_id, Function.identity()));

//		long s = System.currentTimeMillis();
		List<Object[]> dataList = list.stream().map(ou ->{
			Partner partner = par_map.get(ou.getCompensation().getPartner_id());
			Integer o_status_active = ou.getO_status_active();
			String o_statu = null;
			Integer compensation_status = ou.getCompensation().getCompensation_status();
			String compensation_statu = null;

			switch (o_status_active){
                case 1:
                    o_statu = "待付款";
                    break;
                case 2:
                    o_statu = "待确认";
                    break;
                case 3:
                    o_statu = "待服务";
                    break;
                case 4:
                    o_statu = "已取消";
                    break;
                case 5:
                    o_statu = "已完成";
                    break;
            }

            switch (compensation_status){
				case 1:
					compensation_statu = "待处理";
					break;
				case 2:
					compensation_statu = "完成";
					break;
				case 3:
					compensation_statu = "驳回";
					break;
			}

			String orderName = ou.getUname();
			Object[] obj = {ou.getCompensation().getCompensation_id(),
							ou.getPay_order_id(),
							o_statu,
							orderName,
							ou.getCus_username(),
							ou.getCus_phone(),
							ou.getCus_address(),
							ou.getPrice_total()/100d + "",
							ou.getPrice_pay()/100d + "",
							ou.getCompensation().getAmount()/100d + "",
							ou.getProgrammeType() == null ? "" : ou.getProgrammeType().getValue()/100d + "",
							ou.getCompensation().getCompensation_info(),
							ou.getCompensation().getPartner_bear_amount()/100d + "",
							ou.getCompensation().getPartner_bear_coupon_amount()== null?"":ou.getCompensation().getPartner_bear_coupon_amount()/100d + "",
							ou.getOperator_create().getUsername(),
							ou.getCompensation().getPayee(),
							ou.getCompensation().getPayee_card(),
							ou.getCompensation().getReceiving_bank(),
							partner.getName(),
							ou.getStudent_name(),
							sdf.format(ou.getCompensation().getCreate_datetime()),
							compensation_statu};
			return obj;
		}).collect(Collectors.toList());

//		long e = System.currentTimeMillis();
//		System.out.println("xxxxxxxxxxxxxxx=====########################################=========" + (e-s));

		WriteExcel ex = new WriteExcel(rowsName, dataList);
		Map<String, String> file_map = myFileHandleUtil.file_upload(ex.export(), PathType.file_order_excel);
		OssFile ossFile = new OssFile();
		ossFile.setOss_file_id(IdGenerator.generateId());
		ossFile.setName(file_map.get("file_name"));
		ossFile.setUrl(file_map.get("file_url"));
		ossFile.setFormat(file_map.get("file_format"));
		ossFile.setCreate_time(new Date());
		ossFile.setBucket(file_map.get("bucketName"));
		ossFile.setAccess_permissions("private");
		ossFileService.insertSelective(ossFile);
//		Thread.sleep(30000);
		DataDownload dataDownload = new DataDownload();
		dataDownload.setData_download_id(data_download_id);
		dataDownload.setOss_file(file_map.get("file_url"));
		dataDownload.setStatus(1);
		int i = dataDownloadService.updateByPrimaryKeySelective(dataDownload);
		return i;
	}
	
	
}
