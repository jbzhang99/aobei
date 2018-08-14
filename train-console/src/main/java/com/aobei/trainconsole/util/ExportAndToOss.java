package com.aobei.trainconsole.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Component;

import com.aobei.train.IdGenerator;
import com.aobei.trainconsole.util.PathUtil.PathType;



@Component
public class ExportAndToOss implements Callable<Integer>{
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProSkuService proSkuService;

	@Autowired
	private VOrderUnitService vOrderUnitService;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private DataDownloadService dataDownloadService;
	
	@Autowired
	private OssFileService ossFileService;

	@Autowired
	private OrderItemService orderItemService;

	private VOrderUnitExample orderUnitExample;
	
	private Long data_download_id;
	
	
	public Long getData_download_id() {
		return data_download_id;
	}


	public void setData_download_id(Long data_download_id) {
		this.data_download_id = data_download_id;
	}


	public VOrderUnitExample getOrderUnitExample() {
		return orderUnitExample;
	}


	public void setOrderUnitExample(VOrderUnitExample orderUnitExample) {
		this.orderUnitExample = orderUnitExample;
	}


	@Override
	public Integer call() throws Exception {
		List<VOrderUnit> list = vOrderUnitService.selectByExample(orderUnitExample);
		
		String[] rowsName = new String[]{"订单号", "订单名称","商品件数", "顾客姓名", "顾客电话","订单来源渠道",
										"总价（元）","优惠金额（元）","实际支付金额（元）","支付方式","支付状态",
										"下单时间","支付时间","可退金额（元）","退款状态","退款申请日期",
										"退款完成日期","客户联系人","服务地址","客户联系人电话","用户下单备注",
										"用户取消单备注","系统备注","订单状态","商品","商品单项",
										"服务人员","合伙人","服务站点","预约开始时间","预约结束时间",
										"系统指派合伙人时间","合伙人接单时间","合伙人拒单时间","合伙人分配服务人员时间","服务单完成时间",
										"服务单状态","服务人员状态","工作人员到达时间","工作开始时间","工作结束时间","工作人员离开时间"};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//产品集合
		Set<Long> pro_set = list.stream().map(n -> n.getProduct_id()).collect(Collectors.toSet());
		List<Long> pro_ids = new ArrayList<>(pro_set);
		ProductExample productExample = new ProductExample();
		productExample.or().andProduct_idIn(pro_ids);
		List<Product> products = productService.selectByExample(productExample);
		Map<Long, Product> pro_map = products.stream().collect(Collectors.toMap(Product::getProduct_id, Function.identity()));
		//sku集合
		Set<Long> sku_set = list.stream().map(n -> n.getPsku_id()).collect(Collectors.toSet());
		List<Long> sku_ids = new ArrayList<>(sku_set);
		ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or().andPsku_idIn(sku_ids);
		List<ProSku> proSkus = proSkuService.selectByExample(proSkuExample);
		Map<Long, ProSku> sku_map = proSkus.stream().collect(Collectors.toMap(ProSku::getPsku_id, Function.identity()));
		//合伙人集合
		Set<Long> par_set = list.stream().map(n -> n.getPartner_id()).collect(Collectors.toSet());
		List<Long> par_ids = new ArrayList<>(par_set);
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andPartner_idIn(par_ids);
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		Map<Long, Partner> par_map = partners.stream().collect(Collectors.toMap(Partner::getPartner_id, Function.identity()));
		//站点集合
		Set<Long> sta_set = list.stream().map(n -> n.getStation_id()).collect(Collectors.toSet());
		List<Long> sta_ids = new ArrayList<>(sta_set);
		StationExample stationExample = new StationExample();
		stationExample.or().andStation_idIn(sta_ids);
		List<Station> stations = stationService.selectByExample(stationExample);
		Map<Long, Station> sta_map = stations.stream().collect(Collectors.toMap(Station::getStation_id, Function.identity()));


//		long s = System.currentTimeMillis();
		List<Object[]> dataList = list.stream().map(ou ->{
			OrderItemExample orderItemExample = new OrderItemExample();
			orderItemExample.or().andPay_order_idEqualTo(ou.getPay_order_id());
			OrderItem item = DataAccessUtils.singleResult(orderItemService.selectByExample(orderItemExample));

			Product product = pro_map.get(ou.getProduct_id());
			ProSku  sku = sku_map.get(ou.getPsku_id());
			Partner partner = par_map.get(ou.getPartner_id());
			Station station = sta_map.get(ou.getStation_id());
			Integer r_status = ou.getR_status();
			Integer o_status_active = ou.getO_status_active();
			Integer u_status = ou.getStatus_active();
			Integer w_status = ou.getWork_status();
			Integer pay_type = ou.getPay_type();
			String r_statu = null;
			String o_statu = null;
			String u_statu = null;
			String w_statu = null;
			String pay_type_info = null;

			if (pay_type != null){
				switch (pay_type){
					case 1:
						pay_type_info = "微信";
						break;
					case 2:
						pay_type_info = "支付宝";
						break;
					default:
						pay_type_info = "其他";
						break;
				}
			}else{
				pay_type_info = "其他";
			}

            if(r_status != null) {
                switch (r_status){
                    case 0:
                        r_statu = "待退款";
                        break;
                    case 1:
                        r_statu = "退款中";
                        break;
                    case 2:
                        r_statu = "已退全款";
                        break;
                    case 3:
                        r_statu = "已退部分款";
                        break;
                    default:
                        r_statu = "";
                        break;
                }
            }else {
                r_statu = "";
            }

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

            if(u_status != null) {
                switch (u_status){
                    case 1:
                        u_statu = "待指派合伙人";
                        break;
                    case 2:
                        u_statu = "待合伙人接单";
                        break;
                    case 3:
                        u_statu = "待合伙指派";
                        break;
                    case 4:
                        u_statu = "已指派学员";
                        break;
                    case 5:
                        u_statu = "完成";
                        break;
                    case 6:
                        u_statu = "拒单";
                        break;
                    default:
                        u_statu = "";
                        break;
                }
            }else {
                u_statu = "";
            }

            if(w_status != null) {
                switch (w_status){
                    case 1:
                        w_statu = "到达";
                        break;
                    case 2:
                        w_statu = "开始";
                        break;
                    case 3:
                        w_statu = "结束";
                        break;
                    case 4:
                        w_statu = "离开";
                    default:
                        w_statu = "";
                }
            }else {
                w_statu = "";
            }

			String remark =ou.getRemark() == null ? "":ou.getRemark();
			String remarkCancel = ou.getRemark_cancel() == null ? "": ou.getRemark_cancel();
			String orderName = ou.getUname();
			Object[] obj = {ou.getPay_order_id(),orderName,item.getNum() +"",ou.getCuname(),ou.getUphone(),ou.getChannel(),
					ou.getPrice_total()/100d+"",
					ou.getPrice_discount()/100d+"",
					ou.getPrice_pay()/100d+"",
					pay_type_info,
					"0".equals(ou.getPay_status()+"") ? "待支付" : "已支付",
					sdf.format(ou.getCreate_datetime()),
					ou.getPay_datetime() == null ? "" :sdf.format(ou.getPay_datetime()),
					ou.getR_fee() == null ? "":ou.getR_fee()/100d+"",
					r_statu,ou.getR_datetime() == null ? "":sdf.format(ou.getR_datetime()),
					ou.getR_finish_datetime() == null ? "":sdf.format(ou.getR_finish_datetime()),
					ou.getCus_username(),
					ou.getCus_address(),
					ou.getCus_phone(),
					remark,
					remarkCancel,
					ou.getSys_remark() == null ? "":ou.getSys_remark(),
					o_statu,product == null ? "" : product.getName(),sku == null ? "":sku.getName(),
					ou.getStudent_name() == null ? "":ou.getStudent_name(),
					partner == null ? "" :partner.getName(),
					station == null ? "" :station.getUsername(),
					sdf.format(ou.getC_begin_datetime()),
					ou.getC_end_datetime() == null ? "":sdf.format(ou.getC_end_datetime()),
					ou.getP_assign_datetime() == null ? "" :sdf.format(ou.getP_assign_datetime()),
					ou.getP_confirm_datetime() == null ? "":sdf.format(ou.getP_confirm_datetime()),
					ou.getP_reject_datetime() == null ? "":sdf.format(ou.getP_reject_datetime()),
					ou.getP2s_assign_datetime() == null ? "":sdf.format(ou.getP2s_assign_datetime()),
					ou.getFinish_datetime()==null?"":sdf.format(ou.getFinish_datetime()),
					u_statu,w_statu,
					ou.getWork_1_datetime() == null ?"":sdf.format(ou.getWork_1_datetime()),
					ou.getWork_2_datetime() == null ? "":sdf.format(ou.getWork_2_datetime()),
					ou.getWork_3_datetime() == null ? "" : sdf.format(ou.getWork_3_datetime()),
					ou.getWork_4_datetime() == null ? "":sdf.format(ou.getWork_4_datetime())};
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
