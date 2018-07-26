package com.aobei.trainconsole.util;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.aobei.train.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aobei.train.IdGenerator;
import com.aobei.train.service.DataDownloadService;
import com.aobei.train.service.OssFileService;
import com.aobei.train.service.PartnerService;
import com.aobei.train.service.RefundService;
import com.aobei.train.service.UsersService;
import com.aobei.trainconsole.util.PathUtil.PathType;



@Component
public class ExportRefundsAndToOss implements Callable<Integer>{
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private RefundService refundService;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private DataDownloadService dataDownloadService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private OssFileService ossFileService;

	private RefundExample refundExample;
	
	private Long data_download_id;
	
	
	public Long getData_download_id() {
		return data_download_id;
	}


	public void setData_download_id(Long data_download_id) {
		this.data_download_id = data_download_id;
	}

	public RefundExample getRefundExample() {
		return refundExample;
	}


	public void setRefundExample(RefundExample refundExample) {
		this.refundExample = refundExample;
	}


	@Override
	public Integer call() throws Exception {
		List<Refund> list = refundService.selectByExample(refundExample);
		
		String[] rowsName = new String[]{"订单号", "退款状态", "服务名称", "用户姓名","顾客电话",
										"服务地址","服务价格（元）","实付金额（元）","退款金额（元）","退款原因",
										"退款申请人","合伙人公司名","服务人员","退款申请时间","退款完成时间"};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Set<Long> user_set = list.stream().map(n -> n.getUser_id()).collect(Collectors.toSet());
		List<Long> user_ids = new ArrayList<>(user_set);
		UsersExample usersExample = new UsersExample();
		usersExample.or().andUser_idIn(user_ids);
		List<Users> usersList = usersService.selectByExample(usersExample);
		Map<Long, Users> users_map = usersList.stream().collect(Collectors.toMap(Users::getUser_id, Function.identity()));

		Set<Long> par_set = list.stream().map(n -> n.getPartner_id()).collect(Collectors.toSet());
		List<Long> par_ids = new ArrayList<>(par_set);
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andPartner_idIn(par_ids);
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		Map<Long, Partner> par_map = partners.stream().collect(Collectors.toMap(Partner::getPartner_id, Function.identity()));

		List<Object[]> dataList = list.stream().map(refund -> {
			Integer status = refund.getStatus();
			String statu = null;
			switch (status){
				case 0:
					statu = "待退款";
					break;
				case 1:
					statu = "退款中";
					break;
				case 2:
					statu = "已退款";
					break;
				case 3:
					statu = "退款调度失败";
					break;
				case 4:
					statu = "申请被拒绝";
			}

			Users users = users_map.get(refund.getUser_id());
			Partner partner = par_map.get(refund.getPartner_id());

			Object[] obj = {refund.getPay_order_id(),statu,refund.getOrder_name(),refund.getCuname(),refund.getUphone(),
					refund.getCus_address(),refund.getPrice_total()/100d+"",refund.getPrice_pay()/100d+"",refund.getFee()/100d+"",refund.getInfo(),
					users == null ? "" : users.getUsername(),partner == null ? "" : partner.getName(),
					refund.getStudent_name() == null ? "":refund.getStudent_name(),sdf.format(refund.getCreate_date()),
					refund.getRefund_date() == null ? "" : sdf.format(refund.getRefund_date())};
			return obj;
		}).collect(Collectors.toList());

        
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
