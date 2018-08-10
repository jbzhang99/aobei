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
public class ExportCompensationBalanceAndToOss implements Callable<Integer>{
	
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

	@Autowired
	private FallintoCompensationService fallintoCompensationService;

	private CompensationExample compensationExample;
	
	private Long data_download_id;

	private int type;
	private FallintoCompensationExample fallintoCompensationExample;
	
	public Long getData_download_id() {
		return data_download_id;
	}


	public void setData_download_id(Long data_download_id) {
		this.data_download_id = data_download_id;
	}


	public FallintoCompensationExample getFallintoCompensationExample() {
		return fallintoCompensationExample;
	}

	public void setFallintoCompensationExample(FallintoCompensationExample fallintoCompensationExample) {
		this.fallintoCompensationExample = fallintoCompensationExample;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public Integer call() throws Exception {

		String titleName[] ={type==1?"结算期":"","订单号","服务单号","赔偿单号","类型",
				"订单生成时间","赔偿发起时间","赔偿完成时间","订单金额","赔偿金额","赔偿合伙人承担金额","赔偿合伙人承担券额",
				"合伙人","合伙人ID","合伙人级别","合作起日期","合作止日期","状态"};


		List<FallintoCompensation> list = fallintoCompensationService.selectByExample(fallintoCompensationExample);

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		List<Object[]> dataList = list.stream().map( n->{
			//Product product = this.productService.selectByPrimaryKey(n.getProduct_id());
			//Refund refund = refundService.selectByPrimaryKey(n.getRefund_id());
			Object[] obj = {n.getStatus()==1?n.getBalance_cycle():"",
					n.getPay_order_id()==null?"":n.getPay_order_id().toString(),
					n.getServiceunit_id()==null?"":n.getServiceunit_id().toString(),
					n.getCompensation_id()==null?"":n.getCompensation_id().toString(),
					"赔偿",
					n.getOrder_create_datetime()==null?"":sd.format(n.getOrder_create_datetime()),
					n.getCompensation_create_datetime()==null?"":sd.format(n.getCompensation_create_datetime()),
					n.getCompensation_confirm_datetime()==null?"":sd.format(n.getCompensation_confirm_datetime()),
					n.getPrice_pay()==null?"":(n.getPrice_pay()*0.01),
					n.getAmount()==null?"":(n.getAmount()*0.01),
					n.getPartner_bear_amount()==null?"":(n.getPartner_bear_amount()*0.01),
					n.getPartner_bear_coupon_amount()==null?"":(n.getPartner_bear_coupon_amount()*0.01),
					n.getPartner_name()==null?"":n.getPartner_name().toString(),
					n.getPartner_id()==null?"":n.getPartner_id().toString(),
					n.getPartner_level()==null?"":n.getPartner_level(),
					n.getCooperation_start()==null?"":sd.format(n.getCooperation_start()),//合伙人合作开始时间
					n.getCooperation_end()==null?"":sd.format(n.getCooperation_end()),
					n.getStatus()==1?"待结算":"挂起"
			};//合伙人合作结束时间
			return obj;
		}).collect(Collectors.toList());


		WriteExcel ex = new WriteExcel(titleName, dataList);
		Map<String, String> file_map = myFileHandleUtil.file_upload(ex.export(), PathUtil.PathType.file_order_excel);
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
