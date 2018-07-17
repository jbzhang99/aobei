package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.PathUtil.PathType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class ExportBillAndToOss implements Callable<Integer>{
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private DataDownloadService dataDownloadService;
	
	@Autowired
	private OssFileService ossFileService;

	private BillExample billExample;
	
	private Long data_download_id;
	
	
	public Long getData_download_id() {
		return data_download_id;
	}


	public void setData_download_id(Long data_download_id) {
		this.data_download_id = data_download_id;
	}


	public BillExample getBillExample() {
		return billExample;
	}

	public void setBillExample(BillExample billExample) {
		this.billExample = billExample;
	}

	@Override
	public Integer call() throws Exception {
		List<Bill> list = billService.selectByExample(billExample);

		String[] rowsName = new String[]{"对账批次号", "平台订单号", "平台订单生成时间", "平台订单支付时间","平台订单金额（元）",
										"应付金额（元）","用户支付金额（元）","平台交易类型","平台交易状态","平台退款单号",
										"平台退款金额（元）","平台退款类型","平台退款状态","支付方式","交易时间",
										"交易流水号","交易类型","交易状态","付款银行","货币种类",
										"第三方总金额（元）","企业红包金额（元）","第三方退款单号","wx退款金额（元）","企业红包退款金额（元）",
										"wx退款类型","wx退款状态","手续费（元）","费率","平账金额（元）",
										"手动平账原因","手动平账备注","对账完成时间","对账方式"};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		List<Object[]> dataList = list.stream().map(n ->{
			Object[] obj = {n.getBill_batch_id(),n.getPay_order_id(),n.getOrder_create_datetime() == null ? "":sdf.format(n.getOrder_create_datetime()),
			n.getPay_datetime() == null ? "" : sdf.format(n.getPay_datetime()),n.getPrice_total() == null ? "" : n.getPrice_total()/100d +"",
			n.getPrice_pay() == null ? "" : n.getPrice_pay()/100d + "",n.getPrice_pay() == null ? "" : n.getPrice_pay()/100d + "",
			n.getRefund_id_three() == null ? "支付" : "",n.getPay_status() == null ? "" :(n.getPay_status() == 0 ? "待支付" : "已支付"),
			n.getRefund_id() == null ? "" : n.getRefund_id(),n.getR_fee() == null ? "" : n.getR_fee()/100d+"",n.getRefund_id_three() != null ? "退款" : "",
			n.getR_status() == null ? "" : (n.getR_status() == 2 ? "已退全款" : ""),n.getPay_type() == null ? "" : (n.getPay_type() == 1 ? "微信" : ""),
			n.getTransaction_datetime() == null ? "" : sdf.format(n.getTransaction_datetime()),n.getTransaction_id(),"JSAPI",
			n.getTransaction_status() == null ? "" : (n.getTransaction_status() == 0 ? "未支付" : "已支付"),
			n.getBank_name() == null ? "" : n.getBank_name(),"CNY",n.getTransaction_fee() == null ? "" : n.getTransaction_fee()/100d+"",
			"0.00",n.getRefund_id_three() == null ? "" : n.getRefund_id_three(),n.getRefund_fee() == null ? "": n.getRefund_fee()/100d+"",
			"0.00",n.getRefund_id_three() == null ? "":"ORIGINAL",n.getRefund_id_three() == null ? "":"wx已退款",n.getService_charge() == null ? "": n.getService_charge()/100d+"",
			n.getRate() == null ? "" : n.getRate()/100d + "%",n.getBill_fee() == null ? "":n.getBill_fee()/100d+"",
			n.getManual_info() == null ? "" : n.getManual_info(),n.getManual_remark() == null ? "" : n.getManual_remark(),
			n.getCreate_datetime() == null ? "" : sdf.format(n.getCreate_datetime()),n.getBill_type() == null ? "" : (n.getBill_type() == 1 ? "自动" : "手动")};
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
