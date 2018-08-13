package com.aobei.trainconsole.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.SmsSendEvent;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.model.ServiceUnitExample.Criteria;
import com.aobei.train.service.*;
import com.aobei.trainconsole.configuration.CustomProperties;
import com.aobei.trainconsole.handler.OnsHandler;
import com.aobei.trainconsole.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.*;
import custom.bean.ons.RefundOrderMessage;
import custom.util.DistanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Controller
@RequestMapping("/ordermanager")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private UsersService usersService;

	@Autowired
	private VOrderUnitService vOrderUnitService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private ServiceUnitService serviceUnitService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private StationService stationService;

	@Autowired
	private CustomerAddressService customerAddressService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProSkuService proSkuService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private CompletionService<Integer> completionService;

	@Autowired
	private ExportAndToOss ex;

	@Autowired
	private ExportRefundsAndToOss exportRefundsAndToOss;

	@Autowired
	private ExportCompensationsAndToOss exportCompensationsAndToOss;

	@Autowired
	private DataDownloadService dataDownloadService;

	@Autowired
	private RefundService refundService;

	@Autowired
	private PayWxNotifyService wxNotifyService;

	@Autowired
	private EventPublisher publisher;

	@Autowired
    private CustomProperties properties;

	@Autowired
	private Producer producer;

	@Autowired
	private MetadataService metadataService;

	@Autowired
	private ServiceunitPersonService serviceunitPersonService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private CompensationService compensationService;
	@Autowired
	private PayAliNotifyService payAliNotifyService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CompleteApplyService completeApplyService;

	@Autowired
	private RobbingService robbingService;

	@Autowired
	private ProductSoleService productSoleService;

	@Autowired
	private DeductMoneyService deductMoneyService;

	@Autowired
	private FineMoneyService fineMoneyService;

	@Autowired
	private ExportCompleteApplyToOss exportCompleteApplyToOss;

	@Autowired
	private ExportDeductMoneyToOss exportDeductMoneyToOss;

	@Autowired
	private ExportRejectUnitToOss exportRejectUnitToOss;

	@Autowired
	private ExportFineMoneyToOss exportFineMoneyToOss;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private RejectRecordService rejectRecordService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private OnsHandler onsHandler;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private StoreService storeService;


	/**
	 * 跳转到订单列表页
	 * @param map
	 * @param p 查询起始条数
	 * @param ps 查询条目数
	 * @param uname 服务名称
	 * @param pay_order_id 订单号
	 * @param c_begin_datetime 预约开始的服务时间
	 * @param c_end_datetime 预约解释的服务时间
	 * @param cuname 用户名
	 * @param uphone 用户联系电话
	 * @param student_name 培训人员名称
	 * @param partner_id 合伙人
	 * @param statu 订单状态-----和服务单的组合状态
	 * @return
	 */
	@RequestMapping("/goto_order_list")
	public String order_list(Model map, @RequestParam(defaultValue = "1") Integer p,
							 @RequestParam(defaultValue = "10") Integer ps, @RequestParam(required = false) String uname,
							 @RequestParam(required = false) String pay_order_id, @RequestParam(required = false) String c_begin_datetime,
							 @RequestParam(required = false) String c_end_datetime, @RequestParam(required = false) String cuname,
							 @RequestParam(required = false) String uphone, @RequestParam(required = false) String student_name,
							 @RequestParam(required = false) Long partner_id, @RequestParam(required = false) Integer statu,
							 @RequestParam(required = false) String qs_create_time,@RequestParam(required = false) String qe_create_time,
							 @RequestParam(required = false) String qs_pay_time,@RequestParam(required = false) String qe_pay_time,
							 @RequestParam(required = false,defaultValue = "") String channel_code,
							 @RequestParam(required = false) Integer assign_state) {
		VOrderUnitExample orderUnitExample = new VOrderUnitExample();
		orderUnitExample.setOrderByClause(VOrderUnitExample.C.create_datetime + " desc");
		//查询条件的对象
		VOrderUnitExample.Criteria or = orderUnitExample.or();
		if (!"".equals(channel_code) && channel_code != null){
			or.andChannelEqualTo(channel_code);
		}
		or.andPay_order_idLessThan(Integer.MAX_VALUE + "");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//拼接查询参数
		if (statu != null) {
			if (statu < 10) {
				if(statu.equals(Status.OrderStatus.wait_confirm.value)) {
					or.andStatus_activeNotEqualTo(Status.ServiceStatus.reject.value);
				}
				if(statu.equals(Status.OrderStatus.cancel.value)) {
					or.andR_statusIn(Arrays.asList(new Integer[]{-1,0,1,3}));
				}
				or.andO_status_activeEqualTo(statu);
			} else {
				Integer o_statu = statu / 10;
				if (statu < 30) {
					Integer s_statu = statu % 10;
					or.andStatus_activeEqualTo(s_statu);
					or.andO_status_activeEqualTo(o_statu);
				} else {
					Integer r_statu = statu % 10;
					or.andO_status_activeEqualTo(o_statu);
					or.andR_statusEqualTo(r_statu);
				}
			}
			map.addAttribute("statu", statu);
		}
		if (!("".equals(uname)) && uname != null) {
			uname = uname.trim();
			or.andUnameLike("%" + uname + "%");
			map.addAttribute("uname", uname);
		}
		if (!("".equals(pay_order_id)) && pay_order_id != null) {
			pay_order_id = pay_order_id.trim();
			or.andPay_order_idEqualTo(pay_order_id);
			map.addAttribute("pay_order_id", pay_order_id);
		}
		if (!("".equals(cuname)) && cuname != null) {
			cuname = cuname.trim();
			or.andCus_usernameLike("%" + cuname + "%");
			map.addAttribute("cuname", cuname);
		}
		if (!("".equals(uphone)) && uphone != null) {
			uphone = uphone.trim();
			or.andCus_phoneLike("%" + uphone + "%");
			map.addAttribute("uphone", uphone);
		}

		if (!("".equals(c_begin_datetime)) && c_begin_datetime != null) {
			try {
				or.andC_begin_datetimeGreaterThanOrEqualTo(sdf.parse(c_begin_datetime));
				map.addAttribute("c_begin_datetime", sdf.parse(c_begin_datetime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!("".equals(c_end_datetime)) && c_end_datetime != null) {
			try {
				or.andC_end_datetimeLessThanOrEqualTo(sdfhms.parse(c_end_datetime + " 23:59:59"));
				map.addAttribute("c_end_datetime", sdf.parse(c_end_datetime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!("".equals(student_name)) && student_name != null) {
			student_name = student_name.trim();
			or.andStudent_nameLike("%" + student_name + "%");
			map.addAttribute("student_name", student_name);
		}
		if (partner_id != null) {
			or.andPartner_idEqualTo(partner_id);
			map.addAttribute("partner_id", partner_id);
		}
		if (!("".equals(qe_create_time)) && qe_create_time != null) {
			try {
				or.andCreate_datetimeBetween(sdf.parse(qs_create_time),sdfhms.parse(qe_create_time + " 23:59:59"));
				map.addAttribute("qs_create_time",sdf.parse(qs_create_time));
				map.addAttribute("qe_create_time",sdf.parse(qe_create_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!("".equals(qe_pay_time)) && qe_pay_time != null) {
			try {
				or.andPay_datetimeBetween(sdf.parse(qs_pay_time),sdfhms.parse(qe_pay_time + " 23:59:59"));
				map.addAttribute("qs_pay_time",sdf.parse(qs_pay_time));
				map.addAttribute("qe_pay_time",sdf.parse(qe_pay_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (assign_state != null){
			if (assign_state == 0){
				or.andPartner_idIsNull();
			}
			if (assign_state == 1){
				or.andPartner_idIsNotNull();
			}
			map.addAttribute("assign_state",assign_state);
		}

		Page<VOrderUnit> page = vOrderUnitService.selectByExample(orderUnitExample, p, ps);
		List<VOrderUnit> list = page.getList();

        ChannelExample channelExample = new ChannelExample();
        channelExample.or().andDeletedEqualTo(0);
        List<Channel> channels = channelService.selectByExample(channelExample);

		PartnerExample partnerExample = new PartnerExample();
		//partnerExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andStateEqualTo(1);  08/06 update 合伙人条件无需根据上、下线状态过滤
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		map.addAttribute("list", list);
		map.addAttribute("page", page);
		map.addAttribute("partners", partners);
		map.addAttribute("channels",channels);
		map.addAttribute("channel_code",channel_code);
		return "order/order_list";
	}

	/**
	 * 跳转到订单详情页面
	 * @param map
	 * @param page_current_page 页面参数
	 * @param pay_order_id
	 * @return
	 */
	@RequestMapping("/order_detail")
	public String order_detail(Model map, Integer page_current_page, String pay_order_id,HttpServletRequest request) {
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		List<ServiceUnit> list = serviceUnitService.selectByExample(sue);
		ServiceUnit serviceUnit = singleResult(list);

		Order order = orderService.selectByPrimaryKey(pay_order_id);

		// 查询日志
		OrderLogExample orderLogExample = new OrderLogExample();
		orderLogExample.or().andPay_order_idEqualTo(pay_order_id);
		List<OrderLog> logs = orderLogService.selectByExample(orderLogExample);

		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andStateEqualTo(1);
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		try {
			String json = JacksonUtil.object_to_json(serviceUnit);
			map.addAttribute("su_p", json);
		} catch (JsonProcessingException e) {
			logger.error("Json解析异常,{}",e);
		}
		if (order.getStatus_active().equals(Status.OrderStatus.done.value) ||
				order.getStatus_active().equals(Status.OrderStatus.wait_service.value)){
			ServiceunitPersonExample example = new ServiceunitPersonExample();
			example.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id()).andStatus_activeIn(Arrays.asList(new Integer[]{3,5}));
			List<ServiceunitPerson> persons_done = serviceunitPersonService.selectByExample(example);
			map.addAttribute("persons",persons_done);
		}

		OrderItemExample orderItemExample = new OrderItemExample();
		orderItemExample.or().andPay_order_idEqualTo(pay_order_id);
		OrderItem item = singleResult(orderItemService.selectByExample(orderItemExample));

		String Referer = request.getHeader("Referer");
		map.addAttribute("Referer",Referer);
		map.addAttribute("order", order);
		map.addAttribute("num",item.getNum());
		map.addAttribute("su", serviceUnit);
		map.addAttribute("page_current_page", page_current_page);
		map.addAttribute("pay_order_id", pay_order_id);
		map.addAttribute("logs", logs);
		map.addAttribute("partners", partners);
		return "order/order_detail";
	}

	/**
	 * 为顾客取消订单
	 * @param authentication
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancel_order")
	@Transactional(timeout = 5)
	public Object cancel_order(Authentication authentication, HttpServletRequest request) {
		String param = request.getParameter("result");
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[cancel_order] U[{}] , params param:{} .",
				users.getUser_id(),param);
		JSONObject resultJson = JSONObject.parseObject(param);
		ServiceUnit unit = resultJson.toJavaObject(ServiceUnit.class);
		Order order_db = orderService.selectByPrimaryKey(unit.getPay_order_id());
		Order order = new Order();
		order.setPay_order_id(unit.getPay_order_id());
		// 让当前订单状态变为已取消
		order.setStatus_active(Status.OrderStatus.cancel.value);
		int i = orderService.xCancelOrderBackstage(order);
		logger.info("M[order] F[cancel_order] U[{}] , execute result{} .",
				users.getUser_id(),String.format("订单取消%s!", i > 0 ? "成功" : "失败"));
		if(i > 0) {
			// 生成日志
			logger.info("M[order] F[cancel_order] U[{}] , execute log insert when cancel order success.",users.getUser_id());
			int log_insert_result = orderLogService.xInsert(authentication.getName(), users.getUser_id(), unit.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户取消了订单");
			logger.info("M[order] F[cancel_orbder] U[{}] , execute log insert result:{} .",
					users.getUser_id(),String.format("订单取消操作日志插入%s!", log_insert_result > 0 ? "成功" : "失败"));
			// 发送短信给用户
			// 您预约的${product_name}，${c_begin_datetime}去${cus_address}的订单已经自动取消。
			Map<String, String> params = new HashMap<>();
			params.put("product_name", order_db.getName());
			params.put("c_begin_datetime", (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(unit.getC_begin_datetime()));
			params.put("cus_address",
					order_db.getCus_address().length() > 20 ?
							order_db.getCus_address().substring(0,20) : order_db.getCus_address());
			params.put("cus_username",order_db.getCus_username());
			//发送给合伙人的订单取消短信
			//您于${c_begin_datetime}去${cus_address}，为${cus_username}，进行${product_name}服务的订单已取消。
			sendMsg(Constant.SEND_TO_PARTNER_WHEN_ORDER_CANCEL, order_db.getCus_phone(), Constant.SIGN_NAME, params);
			//取消不用发送短信给用户
			//sendMsg(Constant.SEND_TO_CUSTOMER_WHEN_ORDER_CANCEL, order_db.getCus_phone(), Constant.SIGN_NAME, params);
			//发送个推消息到客户和合伙人
			pushHandler.pushCancelOrderMessageToCustomer(order_db,order_db.getUid().toString());
			pushHandler.pushCancelOrderMessageToPartner(order_db,unit.getPartner_id().toString());

			// 发送短信给服务人员
			// 您于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}，进行${product_name}服务的订单已取消。
			if (order_db.getStatus_active().equals(Status.OrderStatus.wait_service.value) &&
					unit.getStatus_active().equals(Status.ServiceStatus.assign_worker.value)) {
				cancelOrderWhenWaitService(params,unit,order_db);
			}
		}

		cacheReloadHandler.orderListReload(order_db.getUid());
		cacheReloadHandler.orderDetailReload(order_db.getUid(),order_db.getPay_order_id());
		cacheReloadHandler.partner_order_detailReload(order_db.getPay_order_id());
		cacheReloadHandler.partner_order_listReload(unit.getPartner_id());
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("为顾客取消订单%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 申请退款
	 * @param authentication
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/refund_order")
	@Transactional(timeout = 5)
	public Object refund_order(Authentication authentication, HttpServletRequest request) {
		String param = request.getParameter("result");
		String fee = request.getParameter("fee");
		String info = request.getParameter("info");
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[refund_order] U[{}] , params param:{},fee:{},info:{} .",
				users.getUser_id(),param,fee,info);
		JSONObject resultJson = JSONObject.parseObject(param);
		ServiceUnit unit = resultJson.toJavaObject(ServiceUnit.class);
		String pay_order_id = unit.getPay_order_id();
		Order order_db = orderService.selectByPrimaryKey(pay_order_id);

		int i = orderService.applyRefund(fee,info,unit,users);


		logger.info("M[order] F[refund_order] U[{}] , execute result{} .",
				users.getUser_id(),String.format("订单申请退款并生成退款单%s!", i > 0 ? "成功" : "失败"));
		if(i > 0) {
			if(order_db.getStatus_active() != 4 && order_db.getStatus_active()!=5) {
				// 发送短信给用户
				// 您预约的${product_name}，${c_begin_datetime}去${cus_address}的订单已经自动取消。
				Map<String, String> params = new HashMap<>();
				params.put("product_name", order_db.getName());
				params.put("c_begin_datetime", (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(unit.getC_begin_datetime()));
				params.put("cus_address",
						order_db.getCus_address().length() > 20 ?
								order_db.getCus_address().substring(0,20) : order_db.getCus_address());

				sendMsg(Constant.SEND_TO_CUSTOMER_WHEN_ORDER_CANCEL, order_db.getCus_phone(), Constant.SIGN_NAME, params);

				// 发送短信给服务人员
				// 您于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}，进行${product_name}服务的订单已取消。
				if (order_db.getStatus_active().equals(Status.OrderStatus.wait_service.value) &&
						unit.getStatus_active().equals(Status.ServiceStatus.assign_worker.value)) {
					cancelOrderWhenWaitService(params,unit,order_db);
				}
			}
			logger.info("M[order] F[refund_order] U[{}] , execute log insert when refund order success.",users.getUser_id());
			// 生成日志
			int log_insert_result = orderLogService.xInsert(authentication.getName(), users.getUser_id(), pay_order_id,
					"用户[" + authentication.getName() + "] 为客户进行了申请退款操作！");
			logger.info("M[order] F[refund_order] U[{}] , execute log insert result:{} .",
					users.getUser_id(),String.format("订单申请退款操作日志插入%s!", log_insert_result > 0 ? "成功" : "失败"));
		}

		cacheReloadHandler.orderListReload(order_db.getUid());
		cacheReloadHandler.orderDetailReload(order_db.getUid(),order_db.getPay_order_id());
		cacheReloadHandler.partner_order_detailReload(order_db.getPay_order_id());
		cacheReloadHandler.partner_order_listReload(unit.getPartner_id());
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("申请退款操作%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 ************* 我是宇宙无敌分割线 *******************************************************************
	 *                                        服务完成申请申请
	 **************************************************************************************************
	 */

	/**
	 * 服务完成申请
	 * @param completeApply
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/complete_apply")
	public Object complete_apply(CompleteApply completeApply,Authentication authentication){
		CompleteApplyExample completeApplyExample = new CompleteApplyExample();
		completeApplyExample.or()
				.andPay_order_idEqualTo(completeApply.getPay_order_id() == null ? "" :completeApply.getPay_order_id())
				.andApply_statusIn(Arrays.asList(1,2));
		List<CompleteApply> completeApplyList = completeApplyService.selectByExample(completeApplyExample);
		Map<String, String> map = new HashMap<>();
		if (completeApplyList.size() > 0){
			map.put("error", "服务完成申请已存在！");
			return map;
		}
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[complete_apply] U[{}] , params completeApply:{} .",
				users.getUser_id(),completeApply);
		completeApply.setComplete_apply_id(IdGenerator.generateId());
		completeApply.setApply_status(1);
		completeApply.setOperator_apply(users.getUser_id());
		completeApply.setCreate_datetime(new Date());
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(completeApply.getPay_order_id()).andPidEqualTo(0l);
		List<ServiceUnit> list = serviceUnitService.selectByExample(sue);
		ServiceUnit serviceUnit = singleResult(list);
		completeApply.setServiceunit_id(serviceUnit.getServiceunit_id());
		int i = orderService.xInsertCompleteApply(completeApply);
		if (i > 0){
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), completeApply.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户发起了订单确认完成申请！");
		}
		logger.info("M[order] F[complete_apply] U[{}] , execute complete_apply insert result:{} .",
				users.getUser_id(),String.format("发起确认服务完成申请操作%s!", i > 0 ? "成功" : "失败"));
		map.put("msg", String.format("发起确认服务完成申请操作%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 服务完成申请列表
	 * @param map
	 * @param p
	 * @param ps
	 * @param pay_order_id
	 * @param apply_status
	 * @return
	 */
	@RequestMapping("/complete_apply_list")
	public String complete_apply_list(Model map,
									  @RequestParam(defaultValue = "1") Integer p,
									  @RequestParam(defaultValue = "10") Integer ps,
									  @RequestParam(required = false) String pay_order_id,
									  @RequestParam(required = false) Integer apply_status){
		CompleteApplyExample completeApplyExample = new CompleteApplyExample();
		completeApplyExample.setOrderByClause(CompleteApplyExample.C.create_datetime + " desc");
		CompleteApplyExample.Criteria criteria = completeApplyExample.or();
		if (!"".equals(pay_order_id) && pay_order_id != null){
			criteria.andPay_order_idEqualTo(pay_order_id);
			map.addAttribute("pay_order_id",pay_order_id);
		}
		if (apply_status != null){
			criteria.andApply_statusEqualTo(apply_status);
			map.addAttribute("apply_status",apply_status);
		}
		Page<CompleteApply> applyPage = completeApplyService.selectByExample(completeApplyExample, p, ps);
		List<CompleteApplyInfo> infos = applyPage.getList().stream().map(n -> {
			Order order = orderService.selectByPrimaryKey(n.getPay_order_id());
			ServiceUnit unit = serviceUnitService.selectByPrimaryKey(n.getServiceunit_id());
			Users users = usersService.selectByPrimaryKey(n.getOperator_apply());
			CompleteApplyInfo completeApplyInfo = new CompleteApplyInfo();
			if (n.getConfirm_operator()!= null){
				completeApplyInfo.setConfirm_operators(usersService.selectByPrimaryKey(n.getConfirm_operator()));
			}
			completeApplyInfo.setOrder(order);
			completeApplyInfo.setUnit(unit);
			completeApplyInfo.setApply_operator(users);
			BeanUtils.copyProperties(n, completeApplyInfo);
			return completeApplyInfo;
		}).collect(Collectors.toList());
		Page<CompleteApplyInfo> page = new Page<>(infos,applyPage.getTotal(),p,ps);
		map.addAttribute("page",page);
		map.addAttribute("infos",infos);
		return "order/complete_apply";
	}

	/**
	 * 申请服务单确认完成
	 * @param complete_apply_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/complete_apply_confirm")
	public Object complete_apply_confirm(Long complete_apply_id,Authentication authentication){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[complete_apply_confirm] U[{}] , params complete_apply_id:{} .",
				users.getUser_id(),complete_apply_id);
		CompleteApply completeApply = completeApplyService.selectByPrimaryKey(complete_apply_id);
		ServiceUnit serviceUnit = serviceUnitService.selectByPrimaryKey(completeApply.getServiceunit_id());
		Date c_begin_datetime = serviceUnit.getC_begin_datetime();
		Date c_end_datetime = serviceUnit.getC_end_datetime();
		String endString;
		if (c_end_datetime != null){
			endString = hm.format(c_end_datetime);
		}else{
			endString = "none";
		}
		int i = orderService.xUpdateComfirmCompleteApply(completeApply, users);
		logger.info("M[order] F[complete_apply_confirm] U[{}] , execute complete_apply update result:{} .",
				users.getUser_id(),String.format("申请服务单确认完成%s!", i > 0 ? "成功" : "失败"));
		if (i > 0){
			ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
			serviceunitPersonExample.or().andServiceunit_idEqualTo(completeApply.getServiceunit_id());
			List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(serviceunitPersonExample);
			personList.forEach(n -> {
				cacheReloadHandler.student_order_listReload(n.getStudent_id());
				cacheReloadHandler.selectStuShowTaskdetailReload(n.getStudent_id(), completeApply.getPay_order_id());
				cacheReloadHandler.selectStuUndoneOrderReload(n.getStudent_id());
			});

			personList.forEach(n -> {
				Integer begin = Constant.timeUnisMap.get(hm.format(c_begin_datetime));
				Integer end = Constant.timeUnisMap.get(endString);
				if (begin != null) {
					end = end == null ? 47 : end;
					updateAvilableTimeUnits(n.getStudent_id(), sdf.format(c_begin_datetime), begin, end, 1);
				}
			});
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), completeApply.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户进行了订单确认完成操作！");
		}
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("申请服务单确认完成%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 申请服务单确认完成驳回
	 * @param complete_apply_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/complete_apply_reject")
	public Object complete_apply_reject(Long complete_apply_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[complete_apply_reject] U[{}] , params complete_apply_id:{} .",
				users.getUser_id(),complete_apply_id);
		CompleteApply completeApply = completeApplyService.selectByPrimaryKey(complete_apply_id);
		int i = orderService.xUpdateRejectCompleteApply(completeApply, users);
		if (i > 0){
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), completeApply.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户进行了订单确认完成驳回操作！");
		}
		logger.info("M[order] F[complete_apply_reject] U[{}] , execute complete_apply update result:{} .",
				users.getUser_id(),String.format("申请服务单确认完成驳回%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("申请服务单确认完成驳回%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 导出excel futuertask
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportCompleteApplyExcel")
	public Object exportCompleteApplyExcel(HttpServletRequest request, Authentication authentication) {
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		CompleteApplyExample completeApplyExample = completeApplyService.generateDownloadTaskAndPottingParam(str,username,id);
		exportCompleteApplyToOss.setCompleteApplyExample(completeApplyExample);
		exportCompleteApplyToOss.setData_download_id(id);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(exportCompleteApplyToOss);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}

	/**
	 ************* 我是宇宙无敌分割线 *******************************************************************
	 *                                        订单扣款申请
	 **************************************************************************************************
	 */

	/**
	 * 检查该订单pay_order_id下是否存在扣款申请
	 * @param pay_order_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deduct_money_check_exist")
	public Object deduct_money_check_exist(String pay_order_id){
		DeductMoneyExample deductMoneyExample = new DeductMoneyExample();
		deductMoneyExample.or().andPay_order_idEqualTo(pay_order_id).andDeduct_statusIn(Arrays.asList(1,2));
		DeductMoney deductMoney = singleResult(deductMoneyService.selectByExample(deductMoneyExample));
		return deductMoney;
	}

	/**
	 * 插入新的扣款单
	 * @param deductMoney
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deduct_money_add")
	public Object deduct_money_add(DeductMoney deductMoney,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		serviceUnitExample.or().andPay_order_idEqualTo(deductMoney.getPay_order_id()).andPidEqualTo(0l);
		ServiceUnit unit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
		deductMoney.setServiceunit_id(unit.getServiceunit_id());
        deductMoney.setDeduct_operator(users.getUser_id());
        deductMoney.setDeduct_money_id(IdGenerator.generateId());
        deductMoney.setCreate_datetime(new Date());
        deductMoney.setDeduct_status(1);
		logger.info("M[order] F[deduct_money_add] U[{}] , params param:{} .",
				users.getUser_id(),deductMoney);
		int i = orderService.xInsertDeductMoney(deductMoney);
		logger.info("M[order] F[deduct_money_add] U[{}] , execute result{} .",
				users.getUser_id(),String.format("发起扣款单添加%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("发起扣款单添加%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 扣款申请列表
	 * @param map
	 * @param p
	 * @param ps
	 * @param pay_order_id
	 * @param deduct_status
	 * @return
	 */
	@RequestMapping("/deduct_money_list")
	public String deduct_money_list(Model map,
									  @RequestParam(defaultValue = "1") Integer p,
									  @RequestParam(defaultValue = "10") Integer ps,
									  @RequestParam(required = false) String pay_order_id,
									  @RequestParam(required = false) Integer deduct_status){
		DeductMoneyExample deductMoneyExample = new DeductMoneyExample();
		deductMoneyExample.setOrderByClause(DeductMoneyExample.C.create_datetime + " desc");
		DeductMoneyExample.Criteria criteria = deductMoneyExample.or();
		if (!"".equals(pay_order_id) && pay_order_id != null){
			criteria.andPay_order_idEqualTo(pay_order_id);
			map.addAttribute("pay_order_id",pay_order_id);
		}
		if (deduct_status != null){
			criteria.andDeduct_statusEqualTo(deduct_status);
			map.addAttribute("deduct_status",deduct_status);
		}
		Page<DeductMoney> deductMoneyPage = deductMoneyService.selectByExample(deductMoneyExample, p, ps);
		List<DeductMoneyInfo> infos = deductMoneyPage.getList().stream().map(n -> {
			Order order = orderService.selectByPrimaryKey(n.getPay_order_id());
			ServiceUnit unit = serviceUnitService.selectByPrimaryKey(n.getServiceunit_id());
			Users users = usersService.selectByPrimaryKey(n.getDeduct_operator());
			DeductMoneyInfo deductMoneyInfo = new DeductMoneyInfo();
			if (n.getConfirm_operator()!= null){
				deductMoneyInfo.setConfirm_operators(usersService.selectByPrimaryKey(n.getConfirm_operator()));
			}
			deductMoneyInfo.setOrder(order);
			deductMoneyInfo.setUnit(unit);
			deductMoneyInfo.setApply_operator(users);
			BeanUtils.copyProperties(n, deductMoneyInfo);
			return deductMoneyInfo;
		}).collect(Collectors.toList());
		List<Partner> partners = partnerService.selectByExample(new PartnerExample());
		Page<DeductMoneyInfo> page = new Page<>(infos,deductMoneyPage.getTotal(),p,ps);
		map.addAttribute("partners",partners);
		map.addAttribute("page",page);
		map.addAttribute("infos",infos);
		return "order/deduct_money_list";
	}

	/**
	 * 申请扣款单确认操作
	 * @param deduct_money_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deduct_money_confirm")
	public Object deduct_money_confirm(Long deduct_money_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[deduct_money_confirm] U[{}] , params deduct_money_id:{} .",
				users.getUser_id(),deduct_money_id);
		DeductMoney deductMoney = deductMoneyService.selectByPrimaryKey(deduct_money_id);
		int i = orderService.xUpdateComfirmDeductMoney(deductMoney,users);
		if (i > 0){
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), deductMoney.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户进行了订单扣款申请确认操作！");
		}
		logger.info("M[order] F[deduct_money_confirm] U[{}] , execute deduct_money update result:{} .",
				users.getUser_id(),String.format("为客户进行订单扣款申请确认操作%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("为客户进行订单扣款申请确认操作%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 申请扣款单驳回操作
	 * @param deduct_money_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deduct_money_reject")
	public Object deduct_money_reject(Long deduct_money_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[deduct_money_reject] U[{}] , params deduct_money_id:{} .",
				users.getUser_id(),deduct_money_id);
		DeductMoney deductMoney = deductMoneyService.selectByPrimaryKey(deduct_money_id);
		int i = orderService.xUpdateRejectDeductMoney(deductMoney,users);
		if (i > 0){
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), deductMoney.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户进行了订单扣款申请驳回操作！");
		}
		logger.info("M[order] F[deduct_money_reject] U[{}] , execute deduct_money update result:{} .",
				users.getUser_id(),String.format("为客户进行订单扣款申请驳回操作%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("为客户进行订单扣款申请驳回操作%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 导出excel futuertask
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportDeductMoneyExcel")
	public Object exportDeductMoneyExcel(HttpServletRequest request, Authentication authentication) {
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		DeductMoneyExample deductMoneyExample = deductMoneyService.generateDownloadTaskAndPottingParam(str, username, id);
		exportDeductMoneyToOss.setData_download_id(id);
		exportDeductMoneyToOss.setDeductMoneyExample(deductMoneyExample);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(exportDeductMoneyToOss);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}

	/**
	 * ************************************************************************************
	 * 							拒单列表查询
	 * ************************************************************************************
	 */

	/**
	 * 拒单列表查询
	 * @param map
	 * @param p
	 * @param ps
	 * @param partner_id
	 * @param qs_create_time
	 * @param qe_create_time
	 * @return
	 */
	@RequestMapping("/reject_unit_list")
	public String reject_unit_list(Model map,
								   @RequestParam(defaultValue = "1") Integer p,
								   @RequestParam(defaultValue = "10") Integer ps,
								   @RequestParam(required = false) Long partner_id,
								   @RequestParam(required = false) String qs_create_time,@RequestParam(required = false) String qe_create_time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		RejectRecordExample recordExample = new RejectRecordExample();
		recordExample.setOrderByClause(RejectRecordExample.C.create_datetime + " desc");
		RejectRecordExample.Criteria criteria = recordExample.or();
		try {
			if (!("".equals(qe_create_time)) && qe_create_time != null) {
				criteria.andCreate_datetimeBetween(sdf.parse(qs_create_time),sdfhms.parse(qe_create_time + " 23:59:59"));
				map.addAttribute("qs_create_time",sdf.parse(qs_create_time));
				map.addAttribute("qe_create_time",sdf.parse(qe_create_time));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (partner_id != null) {
			criteria.andPartner_idEqualTo(partner_id);
			map.addAttribute("partner_id", partner_id);
		}
		Page<RejectRecord> page = rejectRecordService.selectByExample(recordExample, p, ps);
		List<RejectRecord> list = page.getList();
		List<Partner> partners = partnerService.selectByExample(new PartnerExample());
		map.addAttribute("page",page);
		map.addAttribute("partners",partners);
		map.addAttribute("list",list);
		return "order/reject_unit_list";
	}

	/**
	 * 导出excel futuertask
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportRejectUnitExcel")
	public Object exportRejectUnitExcel(HttpServletRequest request, Authentication authentication) {
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		RejectRecordExample rejectRecordExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, RejectRecordExample.class);
		exportRejectUnitToOss.setData_download_id(id);
		exportRejectUnitToOss.setRejectRecordExample(rejectRecordExample);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(exportRejectUnitToOss);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}

	/**
	 ************* 我是宇宙无敌分割线 *******************************************************************
	 *                                        订单罚款申请
	 **************************************************************************************************
	 */

	/**
	 * 检查该订单pay_order_id,partner_id下是否存在罚款申请   fine_money_list
	 * @param pay_order_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fine_money_check_exist")
	public Object fine_money_check_exist(String pay_order_id,Long partner_id){
		FineMoneyExample fineMoneyExample = new FineMoneyExample();
		fineMoneyExample.or()
				.andPay_order_idEqualTo(pay_order_id)
				.andPartner_idEqualTo(partner_id)
				.andFine_statusIn(Arrays.asList(1,2));
		FineMoney fineMoney = singleResult(fineMoneyService.selectByExample(fineMoneyExample));
		return fineMoney;
	}

	/**
	 * 插入新的罚款单
	 * @param fineMoney
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fine_money_add")
	public Object fine_money_add(FineMoney fineMoney,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		serviceUnitExample.or().andPay_order_idEqualTo(fineMoney.getPay_order_id()).andPidEqualTo(0l);
		ServiceUnit unit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
		fineMoney.setServiceunit_id(unit.getServiceunit_id());
		fineMoney.setFine_operator(users.getUser_id());
		fineMoney.setFine_money_id(IdGenerator.generateId());
		fineMoney.setCreate_datetime(new Date());
		fineMoney.setFine_status(1);
		logger.info("M[order] F[fine_money_add] U[{}] , params param:{} .",
				users.getUser_id(),fineMoney);
		int i = orderService.xInsertFineMoney(fineMoney);
		logger.info("M[order] F[fine_money_add] U[{}] , execute result{} .",
				users.getUser_id(),String.format("发起罚款单添加%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("发起罚款单添加%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 罚款单列表页
	 * @param map
	 * @param p
	 * @param ps
	 * @param pay_order_id
	 * @param fine_status
	 * @return
	 */
	@RequestMapping("/fine_money_list")
	public String fine_money_list(Model map,
									@RequestParam(defaultValue = "1") Integer p,
									@RequestParam(defaultValue = "10") Integer ps,
									@RequestParam(required = false) String pay_order_id,
									@RequestParam(required = false) Integer fine_status){
		FineMoneyExample fineMoneyExample = new FineMoneyExample();
		fineMoneyExample.setOrderByClause(DeductMoneyExample.C.create_datetime + " desc");
		FineMoneyExample.Criteria criteria = fineMoneyExample.or();
		if (!"".equals(pay_order_id) && pay_order_id != null){
			criteria.andPay_order_idEqualTo(pay_order_id);
			map.addAttribute("pay_order_id",pay_order_id);
		}
		if (fine_status != null){
			criteria.andFine_statusEqualTo(fine_status);
			map.addAttribute("fine_status",fine_status);
		}
		Page<FineMoney> fineMoneyPage = fineMoneyService.selectByExample(fineMoneyExample, p, ps);
		List<FineMoneyInfo> infos = fineMoneyPage.getList().stream().map(n -> {
			Order order = orderService.selectByPrimaryKey(n.getPay_order_id());
			ServiceUnit unit = serviceUnitService.selectByPrimaryKey(n.getServiceunit_id());
			Users users = usersService.selectByPrimaryKey(n.getFine_operator());
			FineMoneyInfo fineMoneyInfo = new FineMoneyInfo();
			if (n.getConfirm_operator()!= null){
				fineMoneyInfo.setConfirm_operators(usersService.selectByPrimaryKey(n.getConfirm_operator()));
			}
			fineMoneyInfo.setOrder(order);
			fineMoneyInfo.setUnit(unit);
			fineMoneyInfo.setApply_operator(users);
			BeanUtils.copyProperties(n, fineMoneyInfo);
			return fineMoneyInfo;
		}).collect(Collectors.toList());
		List<Partner> partners = partnerService.selectByExample(new PartnerExample());
		Page<FineMoneyInfo> page = new Page<>(infos,fineMoneyPage.getTotal(),p,ps);
		map.addAttribute("partners",partners);
		map.addAttribute("page",page);
		map.addAttribute("infos",infos);
		return "order/fine_money_list";
	}

	/**
	 * 申请罚款单确认操作
	 * @param fine_money_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fine_money_confirm")
	public Object fine_money_confirm(Long fine_money_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[fine_money_confirm] U[{}] , params fine_money_id:{} .",
				users.getUser_id(),fine_money_id);
		FineMoney fineMoney = fineMoneyService.selectByPrimaryKey(fine_money_id);
		int i = orderService.xUpdateComfirmFineMoney(fineMoney,users);
		if (i > 0){
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), fineMoney.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户进行了订单罚款申请确认操作！");
		}
		logger.info("M[order] F[fine_money_confirm] U[{}] , execute fine_money update result:{} .",
				users.getUser_id(),String.format("为客户进行订单罚款申请确认操作%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("为客户进行订单罚款申请确认操作%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 申请罚款单驳回操作
	 * @param fine_money_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fine_money_reject")
	public Object fine_money_reject(Long fine_money_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[fine_money_reject] U[{}] , params fine_money_id:{} .",
				users.getUser_id(),fine_money_id);
		FineMoney fineMoney = fineMoneyService.selectByPrimaryKey(fine_money_id);
		int i = orderService.xUpdateRejectFineMoney(fineMoney,users);
		if (i > 0){
			orderLogService.xInsert(authentication.getName(), users.getUser_id(), fineMoney.getPay_order_id(),
					"用户[" + authentication.getName() + "] 为客户进行了订单罚款申请驳回操作！");
		}
		logger.info("M[order] F[fine_money_reject] U[{}] , execute fine_money update result:{} .",
				users.getUser_id(),String.format("为客户进行订单罚款申请驳回操作%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("为客户进行订单罚款申请驳回操作%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 导出excel futuertask
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportFineMoneyExcel")
	public Object exportFineMoneyExcel(HttpServletRequest request, Authentication authentication) {
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		FineMoneyExample fineMoneyExample = fineMoneyService.generateDownloadTaskAndPottingParam(str, username, id);
		exportFineMoneyToOss.setData_download_id(id);
		exportFineMoneyToOss.setFineMoneyExample(fineMoneyExample);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(exportFineMoneyToOss);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}

	/**
	 * ****************************************************************************************************
	 * 								到上个面的帅气分割线是新加的功能
	 * ****************************************************************************************************
	 */


	/**
	 * 更改订单的系统备注
	 * @param pay_order_id
	 * @param sys_remark
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update_sys_remark")
	@Transactional(timeout = 5)
	public Object update_sys_remark(String pay_order_id, String sys_remark,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[update_sys_remark] U[{}] , params pay_order_id:{},sys_remark:{} .",
				users.getUser_id(),pay_order_id,sys_remark);
		Order order = new Order();
		order.setPay_order_id(pay_order_id);
		order.setSys_remark(sys_remark);
		int i = orderService.updateByPrimaryKeySelective(order);
		logger.info("M[order] F[update_sys_remark] U[{}] , execute result:{} .",
				users.getUser_id(),String.format("修改备注%s!", i > 0 ? "成功" : "失败"));
		//生成日志
		logger.info("M[order] F[update_sys_remark] U[{}] , execute log insert when update order remark success.",users.getUser_id());
		int log_insert_result = orderLogService.xInsert(users.getUsername(), users.getUser_id(), pay_order_id,
				"[备注] ：用户[" + users.getUsername() + "] 更新了订单的系统备注：" + sys_remark);
		logger.info("M[order] F[update_sys_remark] U[{}] , execute log insert result:{} .",
				users.getUser_id(),String.format("更改订单的系统备注的操作日志插入%s!", log_insert_result > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("修改备注%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 跳转到订单变更的页面
	 * @param map
	 * @param pay_order_id
	 * @return
	 */
	@RequestMapping("/order_change")
	public String order_change(Model map, String pay_order_id) {
		Order order = orderService.selectByPrimaryKey(pay_order_id);
		//根据订单id查询的服务单
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(sue));
		//判断该单是否处于抢单中，抢单中则不能变更，返回 无后续操作
		RobbingExample robbingExample = new RobbingExample();
		robbingExample.or()
				.andServiceunit_idEqualTo(serviceUnit.getServiceunit_id())
				.andExpire_datetimeGreaterThan(new Date())
				.andStatusEqualTo(1);
		long robbing_count = robbingService.countByExample(robbingExample);
		if (robbing_count > 0){
			//抢单中
			map.addAttribute("msg","当前订单正在抢单中，订单无法变更！");
			map.addAttribute("su", serviceUnit);
			map.addAttribute("order",order);
			return "order/order_change";
		}

		Product product = productService.selectByPrimaryKey(serviceUnit.getProduct_id());
		CustomerAddress customerAddress =new CustomerAddress();
		customerAddress.setCity(order.getCus_city());
		customerAddress.setLbs_lat(order.getLbs_lat());
		customerAddress.setLbs_lng(order.getLbs_lng());

		Metadata metadata  = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
		Integer integer  = metadata.getMeta_value()==null?Constant.SEARCH_RADIUS:Integer.parseInt(metadata.getMeta_value());

		List<Station> stationList = null;
		ProductSoleExample soleExample = new ProductSoleExample();
		soleExample.or().andProduct_idEqualTo(product.getProduct_id());
		List<ProductSole> productSoles = productSoleService.selectByExample(soleExample);
		if (productSoles.size() != 0) {//绑定合伙人的产品
			List<Long> partner_ids = productSoles.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
			StationExample stationExample = new StationExample();
			stationExample.or().andPartner_idIn(partner_ids).andCityEqualTo(customerAddress.getCity());
			stationList = stationService.selectByExample(stationExample);
		} else {//未绑定的
			stationList = stationService.findNearbyStation(customerAddress, integer);
			stationList = stationService.filterByProduct(product, stationList);
		}
//		List<Station> listStation = new ArrayList<>();
//		if (customerAddress != null){
//			Metadata metadata  = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
//			Integer integer  = metadata.getMeta_value()==null?Constant.SEARCH_RADIUS:Integer.parseInt(metadata.getMeta_value());
//
//			listStation = stationService.findNearbyStation(customerAddress,integer);
//		}
//		List<Station> listByPro = stationService.filterByProduct(product, listStation);
		//紧急需求变更，可选原派单合伙人和站点
//		Iterator<Station> iterator = list2.iterator();
//		while (iterator.hasNext()) {
//			Station station = iterator.next();
//			if (station.getPartner_id().equals(serviceUnit.getPartner_id())) {
//				iterator.remove();
//			}
//		}
//		if(product.getSole()==1){
//			StationExample example  = new StationExample();
//			example.or().andPartner_idEqualTo(serviceUnit.getPartner_id());
//			Set<Long>  set = listByPro.stream().map(t->t.getStation_id()).collect(Collectors.toSet());
//
//            listByPro.addAll(stationService.selectByExample(example).stream()
//					.filter(t->!set.contains(t.getStation_id())).collect(Collectors.toList()));
//		}
		String stations = null;
		try {
			stations = JacksonUtil.object_to_json(stationList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		List<Long> ids = stationList.stream().map(station -> station.getPartner_id()).collect(Collectors.toList());

		PartnerExample example = new PartnerExample();
		if (ids.size() > 0) {
			example.or().andPartner_idIn(ids);
		} else {
			ids.add(0l);
			example.or().andPartner_idIn(ids);
		}
		// 根据顾客地址得到的可以选择的就近的合伙人公司
		List<Partner> optional_partners = partnerService.selectByExample(example);
		// 页面展示系统分配的合伙人需要的list
		List<Partner> partners = partnerService.selectByExample(new PartnerExample());
		map.addAttribute("order", order);
		map.addAttribute("su", serviceUnit);
		map.addAttribute("pay_order_id", pay_order_id);
		map.addAttribute("optional_partners", optional_partners);
		map.addAttribute("partners", partners);
		map.addAttribute("stations", stationList);
		map.addAttribute("ss", stations);
		return "order/order_change";
	}

	/**
	 * 订单变更
	 * @param authentication
	 * @param partner_id
	 * @param station_id
	 * @param change_intro
	 * @param pay_order_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/change_partner")
	@Transactional
	public Object change_partner(Authentication authentication, Long partner_id, Long station_id, String change_intro,
								 String pay_order_id, String c_begin_datetime,
								 String time) {
		//更新服务时间到服务单上
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SkuTime skuTime = null;
        String begins = null;
        String ends = null;
        if (!"".equals(time) && time != null){
            try {
                skuTime =JacksonUtil.json_to_object(time,SkuTime.class);
                int s = skuTime.getS();
                int e = skuTime.getE();
                Map<String, Integer> timeUnisMap = Constant.timeUnisMap;
                Iterator<Map.Entry<String, Integer>> iterator = timeUnisMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, Integer> entry = iterator.next();
                    if (entry.getValue().equals(s)){
                        begins = c_begin_datetime + " " + entry.getKey();
                    }
                    if (entry.getValue().equals(e)){
                        ends = c_begin_datetime + " " + entry.getKey();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		Date cBeginDatetime = null;
		Date c_end_datetime = null;

		try {
		    if (!"".equals(begins) && begins != null){
                cBeginDatetime = sdf.parse(begins);
                c_end_datetime = sdf.parse(ends);
            }
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[change_partner] U[{}] , params partner_id:{},station_id:{},change_info:{}," +
						"pay_order_id:{},c_begin_datetime:{},c_end_datetime:{} .",
				users.getUser_id(),partner_id,station_id,change_intro,pay_order_id,begins,ends);
		Order order = orderService.selectByPrimaryKey(pay_order_id);
		Partner partner = partnerService.selectByPrimaryKey(partner_id);
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		ServiceUnit unit = singleResult(serviceUnitService.selectByExample(sue));

		ServiceUnitExample outExample = new ServiceUnitExample();
		ServiceUnitExample.Criteria criteria = outExample.or();
		criteria.andPay_order_idEqualTo(pay_order_id);
		criteria.andPartner_idEqualTo(partner_id);
		criteria.andStation_idEqualTo(station_id);
		criteria.andPidEqualTo(0l);
		if(cBeginDatetime!=null) {
			criteria.andC_begin_datetimeEqualTo(cBeginDatetime);
			criteria.andC_end_datetimeEqualTo(c_end_datetime);
		}
		if (serviceUnitService.countByExample(outExample) > 0 && unit.getStatus_active() != 6) {
			Map<String, String> map = new HashMap<>();
			map.put("msg", String.format("订单信息变更，订单变更条件没有发生变化，不进行变更！"));
			return map;
		}

		if (unit.getStatus_active()!=6 && unit.getStatus_active()!=3 && unit.getStatus_active()!=4){
			Map<String, String> map = new HashMap<>();
			map.put("msg", String.format("订单信息变更，该订单不满足变更条件，不能变更！"));
			return map;
		}

		SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = ymd.format(unit.getC_begin_datetime());
		String beginString = hm.format(unit.getC_begin_datetime());
		String endString;
		if(unit.getC_end_datetime()==null){
			endString = "none";
		}else {
			endString = hm.format(unit.getC_end_datetime());
		}
		ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
		serviceunitPersonExample.or().andServiceunit_idEqualTo(unit.getServiceunit_id()).andStatus_activeEqualTo(Status.OrderStatus.wait_service.value);
		List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(serviceunitPersonExample);
		//只要变更就变成待确认
		personList.forEach(n -> {
			Integer begin = Constant.timeUnisMap.get(beginString);
			Integer end = Constant.timeUnisMap.get(endString);
			if (begin != null) {
				end = end == null ? 47 : end;
				updateAvilableTimeUnits(n.getStudent_id(), dateTime, begin, end, 1);
			}
			cacheReloadHandler.student_order_listReload(n.getStudent_id());
			cacheReloadHandler.selectStuShowTaskdetailReload(n.getStudent_id(), pay_order_id);
			cacheReloadHandler.selectStuUndoneOrderReload(n.getStudent_id());
		});

		serviceunitPersonService.deleteByExample(serviceunitPersonExample);

		Order up_order = new Order();
		up_order.setPay_order_id(pay_order_id);
		up_order.setStatus_active(Status.OrderStatus.wait_confirm.value);
		orderService.updateByPrimaryKeySelective(up_order);

		//变更前原合伙人缓存清除
		cacheReloadHandler.partner_order_listReload(unit.getPartner_id());
		int i = orderService.changeOrder(authentication.getName(),partner_id,station_id,change_intro,order,cBeginDatetime,c_end_datetime,unit);
		cacheReloadHandler.orderListReload(order.getUid());
		cacheReloadHandler.orderDetailReload(order.getUid(),order.getPay_order_id());
		cacheReloadHandler.partner_order_detailReload(order.getPay_order_id());
		//变更后合伙人的缓存更新
		cacheReloadHandler.partner_order_listReload(unit.getPartner_id());

		if (i > 0){
			// 发送站内消息
			com.aobei.train.model.Message msg = new com.aobei.train.model.Message();
			msg.setId(IdGenerator.generateId());
			msg.setType(2);
			msg.setBis_type(3);
			msg.setUser_id(partner.getUser_id());
			msg.setUid(partner.getPartner_id());
			msg.setMsg_title("服务变更通知");
			// 服务人员修改
			MessageContent.ContentMsg content = new MessageContent.ContentMsg();
			content.setMsgtype("native");
			content.setContent(pay_order_id+"订单变更");
			Map<String,String> param = new HashMap<>();
			param.put("pay_order_id",order.getPay_order_id());
			param.put("orderStatus","payed");
			TransmissionContent tContent = new TransmissionContent(TransmissionContent.PARTNER,TransmissionContent.ORDER_DETAIL,param);
			content.setHref(tContent.getHrefNotEncode());
			content.setTitle("服务变更通知");
			content.setTypes(1);
			content.setNoticeTypes(2);
			String object_to_json = null;
			try {
				object_to_json = JSON.toJSONString(content);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			msg.setMsg_content(object_to_json);
			msg.setCreate_datetime(new Date());
			msg.setNotify_datetime(new Date());
			msg.setGroup_id(order.getPay_order_id());
			msg.setApp_type(3);
			msg.setSend_type(1);
			msg.setApp_platform(0);
			messageService.insertSelective(msg);
		}
		logger.info("M[order] F[change_partner] U[{}] , execute result:{} .",
				users.getUser_id(),String.format("订单信息变更%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("订单信息变更%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 地区商家的列表
	 * @param map
	 * @param p
	 * @param ps
	 * @param province
	 * @param city
	 * @param area
	 * @return
	 */
	@RequestMapping("/area_partner_info")
	public String area_partner_info(Model map, @RequestParam(defaultValue = "1") Integer p,
									@RequestParam(defaultValue = "10") Integer ps, @RequestParam(required = false) Integer province,
									@RequestParam(required = false) Integer city, @RequestParam(required = false) Integer area) {
		PartnerExample example = new PartnerExample();
		PartnerExample.Criteria or = example.or();
		or.andDeletedEqualTo(Status.DeleteStatus.no.value).andStateEqualTo(1);
		List<Long> ids = new ArrayList<>();
		if (area != null) {
			StationExample stationExample = new StationExample();
			StationExample.Criteria criteria = stationExample.or();
			criteria.andProvinceEqualTo(province);
			criteria.andCityEqualTo(city);
			criteria.andAreaEqualTo(area);
			criteria.andDeletedEqualTo(Status.DeleteStatus.no.value);
			List<Station> list = stationService.selectByExample(stationExample);
			/*for (Station station : list) {
				ids.add(station.getPartner_id());
			}*/
			ids = list.stream().map(station -> station.getPartner_id()).collect(Collectors.toList());
			if (ids.size() != 0) {
				or.andPartner_idIn(ids);
			} else {
				or.andPartner_idEqualTo(0l);
			}
		}

		Page<Partner> page = partnerService.selectByExample(example, p, ps);
		map.addAttribute("partners", page.getList());
		map.addAttribute("page", page);
		map.addAttribute("province", province);
		map.addAttribute("city", city);
		map.addAttribute("area", area);
		return "order/area_partners";
	}

	/**
	 * 某商户下的服务人员排期列表--服务单的数据
	 * @param partner_id
	 * @param map
	 * @param p
	 * @param ps
	 * @param product_id
	 * @param psku_id
	 * @param c_begin_datetime
	 * @param c_end_datetime
	 * @return
	 */
	@RequestMapping("/scheduling_detail")
	public String scheduling_detail(Long partner_id, Model map,
			@RequestParam(defaultValue = "1") Integer p,
			@RequestParam(defaultValue = "10") Integer ps,
			@RequestParam(required = false) Long product_id,
			@RequestParam(required = false) Long psku_id,
			@RequestParam(required = false) String c_begin_datetime,
			@RequestParam(required = false) String c_end_datetime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime +" desc");
		Criteria criteria = serviceUnitExample.or();
		criteria.andPartner_idEqualTo(partner_id);
		criteria.andActiveEqualTo(1);
		criteria.andPidEqualTo(0l);
		criteria.andStatus_activeIn(Arrays.asList(4,5));
		if (product_id != null) {
			criteria.andProduct_idEqualTo(product_id);
			map.addAttribute("product_id", product_id);
		}
		if (psku_id != null) {
			criteria.andPsku_idEqualTo(psku_id);
			map.addAttribute("psku_id", psku_id);
		}
		if (!("".equals(c_end_datetime)) && c_end_datetime != null) {
			try {
				criteria.andC_begin_datetimeGreaterThanOrEqualTo(sdf.parse(c_begin_datetime));
				criteria.andC_end_datetimeLessThanOrEqualTo(sdfhms.parse(c_end_datetime + " 23:59:59"));
				map.addAttribute("c_begin_datetime", sdf.parse(c_begin_datetime));
				map.addAttribute("c_end_datetime", sdf.parse(c_end_datetime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Page<ServiceUnit> page = serviceUnitService.selectByExample(serviceUnitExample, p, ps);
		List<Schduling> schdulings = page.getList().stream().map(serviceUnit ->{
			String pay_order_id = serviceUnit.getPay_order_id();
			Order order = orderService.selectByPrimaryKey(pay_order_id);
			Schduling schduling = new Schduling();
			schduling.setOrder(order);
			schduling.setServiceUnit(serviceUnit);
			return schduling;
		}).collect(Collectors.toList());

		Partner partner = partnerService.selectByPrimaryKey(partner_id);
        ProductExample productExample = new ProductExample();
        productExample.or().andDeletedEqualTo(0).andOnlineEqualTo(1);
        List<Product> products = productService.selectByExample(productExample);
        ProSkuExample proSkuExample = new ProSkuExample();
        proSkuExample.or().andDeletedEqualTo(0);
        List<ProSku> skus = proSkuService.selectByExample(proSkuExample);
		String json = null;
		try {
			json = JacksonUtil.object_to_json(skus);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		map.addAttribute("partner", partner);
		map.addAttribute("schdulings", schdulings);
		map.addAttribute("page", page);
		map.addAttribute("products", products);
		map.addAttribute("skus", json);
		map.addAttribute("partner_id", partner_id + "");
		return "order/scheduling_detail";
	}

	/**
	 * 某商户某站点下的服务人员库存
	 * @param partner_id
	 * @param map
	 * @param p
	 * @param ps
	 * @param product_id
	 * @param psku_id
	 * @param c_begin_date
	 * @param c_end_date
	 * @return
	 */
	@RequestMapping("/scheduling_task")
	public String scheduling_task(Long partner_id, Model map,
									@RequestParam(defaultValue = "1") Integer p,
									@RequestParam(defaultValue = "10") Integer ps,
									@RequestParam(required = false) Long station_id,
									@RequestParam(required = false) Long product_id,
									@RequestParam(required = false) Long psku_id,
									@RequestParam(required = false) String c_begin_date,
									@RequestParam(required = false) String c_end_date) {
		StationExample example = new StationExample();
		example.or().andPartner_idEqualTo(partner_id).andOnlinedEqualTo(1).andDeletedEqualTo(0);
		List<Station> stations = stationService.selectByExample(example);
		Partner partner = partnerService.selectByPrimaryKey(partner_id);

		List<OneDayStore> stores = new ArrayList<>();
		if (station_id != null && product_id != null && psku_id != null &&
				(!"".equals(c_begin_date) || c_begin_date != null) &&
				(!"".equals(c_end_date) || c_end_date != null)){
			ProSkuExample skuExample = new ProSkuExample();
			skuExample.or().andProduct_idEqualTo(product_id).andPsku_idEqualTo(psku_id);
			ProSku proSku = DataAccessUtils.singleResult(proSkuService.selectByExample(skuExample));
			String times = proSku.getService_times();
			List<SkuTime> timesList = JSON.parseArray(times, SkuTime.class);

			StudentExample studentExample = new StudentExample();
			studentExample.or().andStation_idEqualTo(station_id).andStateEqualTo(1).andDeletedEqualTo(0);
			List<Student> students = studentService.selectByExample(studentExample);

			LocalDate begin_date = LocalDate.parse(c_begin_date);
			LocalDate end_date = LocalDate.parse(c_end_date);
			LocalDate date = LocalDate.parse(c_begin_date);
			long days = begin_date.until(end_date, ChronoUnit.DAYS);
			while (days >= 0){
				//填充库存类
				OneDayStore oneDayStore = getStudentsStroe(timesList, students, date.toString());
				stores.add(oneDayStore);
				date = date.plusDays(1L);
				days--;
			}
			map.addAttribute("product_id", product_id);
			map.addAttribute("psku_id", psku_id);
			map.addAttribute("c_begin_date", c_begin_date);
			map.addAttribute("c_end_date", c_end_date);
		}
        ProductExample productExample = new ProductExample();
        productExample.or().andDeletedEqualTo(0).andOnlineEqualTo(1);
        List<Product> products = productService.selectByExample(productExample);
        ProSkuExample proSkuExample = new ProSkuExample();
        proSkuExample.or().andDeletedEqualTo(0);
        List<ProSku> skus = proSkuService.selectByExample(proSkuExample);
		String json = null;
		try {
			json = JacksonUtil.object_to_json(skus);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		int size = stores.size();
		List<OneDayStore> storesReturn = new ArrayList<>();
		if (size > ps){
			if (p == 1){
				storesReturn = stores.subList(0,ps);
			} else {
				if (stores.subList(ps*(p-1),stores.size()).size()>ps){
					storesReturn = stores.subList(ps*(p-1),ps*p);
				}else{
					storesReturn = stores.subList(ps*(p-1),stores.size());
				}
			}
		}else{
			storesReturn = stores;
		}
		Page<OneDayStore> page = new Page<>(stores,size,p,ps);
		Map<String, Integer> timeUnisMap = Constant.timeUnisMap;
		map.addAttribute("timeUnisMap",timeUnisMap);
		map.addAttribute("stores",storesReturn);
		map.addAttribute("products", products);
		map.addAttribute("skus", json);
		map.addAttribute("station_id",station_id + "");
		map.addAttribute("partner", partner);
		map.addAttribute("stations",stations);
		map.addAttribute("partner_id", partner_id + "");
		map.addAttribute("page",page);
		return "order/scheduling_task";
	}

	/**
	 * ******************************************************************************************************************
	 *      以下：          JD订单派发模块
	 * ******************************************************************************************************************
	 */

	/**
	 * JD订单派发前，查询可用的合伙人和站点信息
	 * @param pay_order_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assign_order_pre")
	public Object assign_order_pre(String pay_order_id){
		Order order = orderService.selectByPrimaryKey(pay_order_id);
		//根据订单id查询的服务单
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(sue));
		/*Product product = productService.selectByPrimaryKey(serviceUnit.getProduct_id());
		CustomerAddress customerAddress =new CustomerAddress();
		customerAddress.setCity(order.getCus_city());
		customerAddress.setLbs_lat(order.getLbs_lat());
		customerAddress.setLbs_lng(order.getLbs_lng());

		Metadata metadata  = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
		Integer integer  = metadata.getMeta_value()==null?Constant.SEARCH_RADIUS:Integer.parseInt(metadata.getMeta_value());

		List<Station> stations = null;
		ProductSoleExample soleExample = new ProductSoleExample();
		soleExample.or().andProduct_idEqualTo(product.getProduct_id());
		List<ProductSole> productSoles = productSoleService.selectByExample(soleExample);
		if (productSoles.size() != 0) {//绑定合伙人的产品
			List<Long> partner_ids = productSoles.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
			StationExample stationExample = new StationExample();
			stationExample.or().andPartner_idIn(partner_ids);
			stations = stationService.selectByExample(stationExample);
			//将来多个城市了需要的话再拿findNearByStation筛选一遍
		} else {//未绑定的
			stations = stationService.findNearByStation(customerAddress, integer,product);
		}
		List<Long> ids = stations.stream().map(station -> station.getPartner_id()).collect(Collectors.toList());

		PartnerExample example = new PartnerExample();
		if (ids.size() > 0) {
			example.or().andPartner_idIn(ids);
		} else {
			ids.add(0l);
			example.or().andPartner_idIn(ids);
		}
		// 根据顾客地址得到的可以选择的就近的合伙人公司
		List<Partner> partners = partnerService.selectByExample(example);
		String stationsJson = null;
		try {
			stationsJson = JacksonUtil.object_to_json(stations);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("partners",partners);
		map.put("stationsJson",stationsJson);*/

		ProSku proSku = proSkuService.selectByPrimaryKey(serviceUnit.getPsku_id());
		String serviceTimes = proSku.getService_times();
		List<SkuTime> timesList = JSON.parseArray(serviceTimes, SkuTime.class);
		Map<String,Object> map = new HashMap<>();
		map.put("timesList",timesList);
		return map;
	}

	/**
	 * 派单前查看选择日期是否又库存，且订单
	 * @param pay_order_id
	 * @param address
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_has_store_time_scope")
	public Object get_has_store_time_scope(String pay_order_id,String address){
		Order order = orderService.selectByPrimaryKey(pay_order_id);

		OrderItemExample orderItemExample = new OrderItemExample();
		orderItemExample.or().andPay_order_idEqualTo(pay_order_id);
		OrderItem orderItem = singleResult(orderItemService.selectByExample(orderItemExample));

		//根据pay_order_id查询的服务单
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(sue));
		ProSku proSku = proSkuService.selectByPrimaryKey(serviceUnit.getPsku_id());
		String times = proSku.getService_times();
		List<SkuTime> timesList = JSON.parseArray(times, SkuTime.class);

		Map<String,Object> map = new HashMap<>();

		Product product = productService.selectByPrimaryKey(serviceUnit.getProduct_id());
		CustomerAddress customerAddress =new CustomerAddress();
		String lat = "";
		String lng = "";
		if (!"".equals(address) && address != null){
			try {
				Map<String, String> addressMap = HttpAddressUtil.coordinate_GaoDe(address, "");
				if(addressMap!=null){
					//经度
					lng = addressMap.get("lng_b");
					//纬度
					lat = addressMap.get("lat_b");
				}
			} catch (Exception e) {
				logger.info("M[order] F[get_has_store_time_scope] error[{}].",e.toString());
				map.put("msg","地址信息解析出错！");
				return map;
			}
			customerAddress.setCity(null);
			customerAddress.setLbs_lat(lat);
			customerAddress.setLbs_lng(lng);
		}else{
			customerAddress.setCity(order.getCus_city());
			customerAddress.setLbs_lat(order.getLbs_lat());
			customerAddress.setLbs_lng(order.getLbs_lng());
		}


		Metadata metadata  = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
		Integer integer  = metadata.getMeta_value()==null?Constant.SEARCH_RADIUS:Integer.parseInt(metadata.getMeta_value());

		List<Station> stationList = null;
		ProductSoleExample soleExample = new ProductSoleExample();
		soleExample.or().andProduct_idEqualTo(product.getProduct_id());
		List<ProductSole> productSoles = productSoleService.selectByExample(soleExample);
		if (productSoles.size() != 0) {//绑定合伙人的产品
			List<Long> partner_ids = productSoles.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
			StationExample stationExample = new StationExample();
			StationExample.Criteria or = stationExample.or();
			or.andPartner_idIn(partner_ids);
			or.andOnlinedEqualTo(1);
			or.andDeletedEqualTo(Status.DeleteStatus.no.value);
			if (customerAddress.getCity() != null){
				or.andCityEqualTo(customerAddress.getCity());
			}
			List<Station> stations = stationService.selectByExample(stationExample);
			stationList = stations.stream().filter(t -> DistanceUtil.GetDistance(
					new Double(t.getLbs_lat() == null ? "0" : t.getLbs_lat()),
					new Double(t.getLbs_lng() == null ? "0" : t.getLbs_lng()),
					new Double(customerAddress.getLbs_lat()),
					new Double(customerAddress.getLbs_lng())) <= integer).collect(Collectors.toList());
		} else {//未绑定的
			stationList = stationService.findNearbyStation(customerAddress, integer);
			stationList = stationService.filterByProduct(product, stationList);
		}

		List<TimeModel> timeModels = storeService.stationsTimeModel(stationList, 15, proSku,
				proSku.getBuy_multiple_o2o() == 1 ? orderItem.getNum() : 1);

		timeModels = timeModels.stream().filter(n -> n.isActive() == true).collect(Collectors.toList());
		timeModels = timeModels.stream().map(n ->{
			Set<TimeModel.Model> models = n.getModels().stream().filter(m -> m.isActive() == true).collect(Collectors.toSet());
			n.setModels(models);
			return n;
		}).collect(Collectors.toList());
		if (timeModels.size() <= 0){
			map.put("msg","无可供选择的服务时间段！");
		}else{
			map.put("timeModels",timeModels);
		}
		return map;
	}

	/**
	 * 查询指定 server_date 下的对应订单sku服务时间段的库存足够的可选的服务时间段信息assign_order
	 * @param pay_order_id
	 * @param station_id
	 * @param server_date
	 * @param num
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_optional_time_scope")
	public Object get_optional_time_scope(String pay_order_id,
										  Long station_id,
										  String server_date,
										  Integer num){
		//根据pay_order_id查询的服务单
		ServiceUnitExample sue = new ServiceUnitExample();
		sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(sue));
		ProSku proSku = proSkuService.selectByPrimaryKey(serviceUnit.getPsku_id());
		String times = proSku.getService_times();
		List<SkuTime> timesList = JSON.parseArray(times, SkuTime.class);

		Map<String,Object> map = new HashMap<>();
		StudentExample studentExample = new StudentExample();
		studentExample.or().andStation_idEqualTo(station_id).andStateEqualTo(1).andDeletedEqualTo(0);
		List<Student> students = studentService.selectByExample(studentExample);

		OneDayStore oneDayStore = getStudentsStroe(timesList, students, server_date);
		List<TimeScopeStore> scopeStores = oneDayStore.getScopeStores();
		scopeStores = scopeStores.stream().filter(n->n.getStore() >= num).collect(Collectors.toList());
		if (scopeStores.size() <= 0){
			map.put("msg","该站点下服务人员数量不足！");
		}else{
			map.put("scopeStores",scopeStores);
		}
		return map;
	}

	/**
	 * 分配订单
	 * @param select_date
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assign_order")
	public Object assign_order(String select_date,
							   String time,
							   String pay_order_id,String address){
		Order original_order = orderService.selectByPrimaryKey(pay_order_id);
		Map<String,Object> map = new HashMap<>();
		String lat = "";
		String lng = "";
		if (!"".equals(address) && address != null){
			try {
				Map<String, String> addressMap = HttpAddressUtil.coordinate_GaoDe(address, "");
				if(addressMap!=null){
					//经度
					lng = addressMap.get("lng_b");
					//纬度
					lat = addressMap.get("lat_b");
				}
			} catch (Exception e) {
				logger.info("M[order] F[assign_order] error[{}].",e.toString());
				map.put("msg","地址信息解析出错！");
				return map;
			}
			Order upOrder = new Order();
			upOrder.setPay_order_id(pay_order_id);
			upOrder.setCus_address(address);
			upOrder.setLbs_lat(lat);
			upOrder.setLbs_lng(lng);
			orderService.updateByPrimaryKeySelective(upOrder);
			CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(original_order.getCustomer_address_id());
			CustomerAddress upCustomerAddress = new CustomerAddress();
			upCustomerAddress.setCustomer_address_id(customerAddress.getCustomer_address_id());
			upCustomerAddress.setAddress(address);
			upCustomerAddress.setLbs_lat(lat);
			upCustomerAddress.setLbs_lng(lng);
			customerAddressService.updateByPrimaryKeySelective(upCustomerAddress);
		}
		//更新服务时间到服务单上
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] split = null;
		if (!"".equals(time) && time != null){
			split = time.split(",");
		}
		String s = split[0];
		String e = split[1];
		String begin = null;
		String end = null;
		begin = select_date + " " + s;
		if (!"untime".equals(e)){
			end = select_date + " " + e;
		}
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		Criteria criteria = serviceUnitExample.or();
		criteria.andPay_order_idEqualTo(pay_order_id);
		ServiceUnit upUnit = new ServiceUnit();
		try {
			upUnit.setC_begin_datetime(sdf.parse(begin));
			if (end != null){
				upUnit.setC_end_datetime(sdf.parse(end));
			}
			serviceUnitService.updateByExampleSelective(upUnit,serviceUnitExample);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		//派单
		criteria.andPidEqualTo(0l);
		ServiceUnit unit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
		Order order = orderService.selectByPrimaryKey(pay_order_id);
		Station station = orderService.dispatchOrder(order,unit);
		Partner partner = partnerService.selectByPrimaryKey(station == null ? 0l : station.getPartner_id());
		if (partner != null) {
			// 发送短信
			Map<String, String> params = new HashMap<>();
			params.put("product_name", order.getName());
			sendMsg(Constant.SEND_TO_PARTNER_WHEN_CUSTOMER_PAYED,partner.getPhone(),Constant.SIGN_NAME,params);
			//推送消息 新订单 通知到合伙人
			pushHandler.pushOrderMessageToPartner(order, partner.getPartner_id().toString());

			// 发送自动拒单命令，晚上18点至早8:00份的订单 第二日早8点半取消
			Metadata metadata = metadataService.selectByPrimaryKey("max_pay_time");
			Metadata metadata1 = metadataService.selectByPrimaryKey("min_pay_time");
			Metadata metadata2 = metadataService.selectByPrimaryKey("default_reject_time");
			LocalDateTime local = LocalDateTime.now();
			Integer times = local.getHour();

			String max = metadata == null ? "20" : metadata.getMeta_value();
			String min = metadata1 == null ? "8" : metadata1.getMeta_value();
			String rejectTime = metadata2 == null ? "30" : metadata2.getMeta_value();
			Long delayTime = System.currentTimeMillis() + Integer.parseInt(rejectTime) * 60 * 1000;
			if (times <= Integer.parseInt(min)) {
				delayTime = local.withHour(8).withMinute(30).toInstant(ZoneOffset.of("+8")).toEpochMilli();
			} else if (times >= Integer.parseInt(max)) {
				delayTime = local.plusDays(1).withHour(8).withMinute(30).toInstant(ZoneOffset.of("+8")).toEpochMilli();
			}
			// 发mq消息
			try {
				SendResult sendRejectMessage = onsHandler.sendRejectMessage(order.getPay_order_id(), partner.getPartner_id(),
						delayTime);
				//拒单和抢单是同时进行的，防止冲突。延迟3秒进行抢单的发起
				delayTime = delayTime + 3000;
				SendResult sendRobbingMessage = onsHandler.sendRobbingMessage(order.getPay_order_id(), 0, delayTime);
				logger.info("console-method:assign_order:sendRejectMessage:{},sendRobbingMessage:{}", sendRejectMessage.toString(), sendRobbingMessage.toString());
			} catch (Exception e3) {
				logger.error("ONS ERROR {} sendRejectMessage sendRobbingMessage orderID{}", e3.getMessage(), order.getPay_order_id());
			}

			cacheReloadHandler.partner_order_listReload(partner.getPartner_id());
			cacheReloadHandler.partner_order_detailReload(pay_order_id);
			cacheReloadHandler.partner_order_listReload(unit.getPartner_id());
		}
		//更新缓存
		cacheReloadHandler.orderListReload(order.getUid());
		cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);


		map.put("msg",String.format("订单派发%s!", partner != null ? "成功" : "失败"));
		return map;
	}


	/**
	 * ******************************************************************************************************************
	 *      以上：          JD订单派发模块
	 *
	 *      																							I am '分割线'
	 * ******************************************************************************************************************
	 */

	/**
	 * 发送短信
	 * @param templateCode
	 * @param phoneNumber
	 * @param signName
	 * @param params
	 */
	public void sendMsg(String templateCode, String phoneNumber, String signName, Map<String, String> params) {
		SmsData data  = new SmsData();
        data.setSignName(signName);
        String[] phone = new String[]{phoneNumber};
        data.setPhoneNumber(phone);
        data.setTemplateCode(templateCode);
        data.setParams(params);
        SmsSendEvent event  = new SmsSendEvent(this,data);
        publisher.publish(event);
	}	
	
	/**
	 * 导出excel futuertask 
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportExcel")
	public Object exportExcel(HttpServletRequest request, Authentication authentication) {
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		VOrderUnitExample orderUnitExample = orderService.generateDownloadTaskAndPottingParam(str,username,id,VOrderUnitExample.class);
		ex.setOrderUnitExample(orderUnitExample);
		ex.setData_download_id(id);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(ex);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}

//	@Async
//	private void todo(VOrderUnitExample e) {
//		//
//	}

	/**
	 * 跳转到下载页面
	 * @param authentication
	 * @param map
	 * @return
	 */
	@RequestMapping("/download_task")
	public String download_task(Authentication authentication, Model map) {
		String username = authentication.getName();
		UsersExample usersExample = new UsersExample();
		usersExample.or().andUsernameEqualTo(username);
		Users user = singleResult(usersService.selectByExample(usersExample));
		DataDownloadExample dde = new DataDownloadExample();
		dde.setOrderByClause(DataDownloadExample.C.create_datetime + " desc");
		dde.or().andUser_idEqualTo(user.getUser_id());
		List<DataDownload> list = dataDownloadService.selectByExample(dde);
		map.addAttribute("task_list", list);
		return "order/download_task";
	}

	/**
	 * 下载
	 * @param data_download_id
	 */
	@ResponseBody
	@RequestMapping("/download")
	@Transactional(timeout = 5)
	public void download(Long data_download_id,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[order] F[download] U[{}] , params data_download_id:{} .",
				users.getUser_id(),data_download_id);
		//下载任务的状态更改为已下载
		DataDownload dataDownload = new DataDownload();
		dataDownload.setData_download_id(data_download_id);
		dataDownload.setFinish_datetime(new Date());
		// dataDownload.setStatus(2);
		int i = dataDownloadService.updateByPrimaryKeySelective(dataDownload);
		logger.info("M[order] F[download] U[{}] , execute result:{}",
				users.getUser_id(),String.format("文件下载%s!", i> 0 ? "成功" : "失败"));
	}

	/**
	 * 订单退款单列表
	 * @return
	 */
	@RequestMapping("/goto_order_refunds")
	public String goto_order_refunds(Model map, @RequestParam(defaultValue = "1") Integer p,
			@RequestParam(defaultValue = "10") Integer ps,
			@RequestParam(required = false) String pay_order_id, @RequestParam(required = false) String begin_date,
			@RequestParam(required = false) String end_date, @RequestParam(required = false) String cuname,
			@RequestParam(required = false) String uphone, @RequestParam(required = false) String student_name,
			@RequestParam(required = false) Long partner_id, @RequestParam(required = false) Integer status) {
		RefundExample refundExample = new RefundExample();
		refundExample.setOrderByClause(RefundExample.C.create_date + " desc");
		RefundExample.Criteria or = refundExample.or();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//拼接查询参数
		if (status != null) {
			or.andStatusEqualTo(status);
			map.addAttribute("status", status);
		}
		if (!("".equals(pay_order_id)) && pay_order_id != null) {
			pay_order_id = pay_order_id.trim();
			or.andPay_order_idEqualTo(pay_order_id);
			map.addAttribute("pay_order_id", pay_order_id);
		}
		if (!("".equals(cuname)) && cuname != null) {
			cuname = cuname.trim();
			or.andCunameLike("%" + cuname + "%");
			map.addAttribute("cuname", cuname);
		}
		if (!("".equals(uphone)) && uphone != null) {
			uphone = uphone.trim();
			or.andUphoneLike("%" + uphone + "%");
			map.addAttribute("uphone", uphone);
		}

		if (!("".equals(begin_date)) && begin_date != null) {
			try {
				or.andCreate_dateGreaterThanOrEqualTo(sdf.parse(begin_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			map.addAttribute("begin_date", begin_date);
		}
		if (!("".equals(end_date)) && end_date != null) {
			try {
				or.andCreate_dateLessThanOrEqualTo(sdf.parse(end_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			map.addAttribute("end_date", end_date);
		}
		if (!("".equals(student_name)) && student_name != null) {
			student_name = student_name.trim();
			or.andStudent_nameLike("%" + student_name + "%");
			map.addAttribute("student_name", student_name);
		}
		if (partner_id != null) {
			or.andPartner_idEqualTo(partner_id);
			map.addAttribute("partner_id", partner_id);
		}
		Page<Refund> page = refundService.selectByExample(refundExample, p, ps);
		List<Refund> refunds = page.getList();
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andStateEqualTo(1);
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		List<Users> userses = usersService.selectByExample(new UsersExample());
		map.addAttribute("page", page);
		map.addAttribute("refunds", refunds);
		map.addAttribute("partners", partners);
		map.addAttribute("userses", userses);
		return "order/refunds";
	}
	
	/**
	 * 导出订单退款单excel futuertask 
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportRefundsExcel")
	public Object exportRefundsExcel(HttpServletRequest request, Authentication authentication) {
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		RefundExample refundExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, RefundExample.class);
		exportRefundsAndToOss.setRefundExample(refundExample);
		exportRefundsAndToOss.setData_download_id(id);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(exportRefundsAndToOss);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}
	
	/**
	 * 提交退款申请
	 * @param refund_id
	 * @param pay_order_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/refund")
	public Object refund(Long refund_id,String pay_order_id,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		Order order  = orderService.selectByPrimaryKey(pay_order_id);
		String appid ="";
        if(Type.PayType.wxpay.value.equals(order.getPay_type())){
			PayWxNotifyExample example = new PayWxNotifyExample();
			example.or().andOut_trade_noEqualTo(pay_order_id);
			PayWxNotify payWxNotify = singleResult(wxNotifyService.selectByExample(example));
            appid = payWxNotify.getAppid();
		}else if(Type.PayType.alipay.value.equals(order.getPay_type())){
        	PayAliNotifyExample example = new PayAliNotifyExample();
        	example.or().andOut_trade_noEqualTo(pay_order_id);
        	example.limit(0L,1L);
        	PayAliNotify payAliNotify  = singleResult(payAliNotifyService.selectByExample(example));
        	appid =payAliNotify.getApp_id();
        }

		RefundOrderMessage refundMsg = new RefundOrderMessage();
		refundMsg.setAppid(appid);
		refundMsg.setMessageId(IdGenerator.generateId() + "");
		refundMsg.setPay_order_id(pay_order_id);
		refundMsg.setRefund_id(refund_id);
		Message message = new Message();
		message.setTopic(properties.getAliyun().getOns().getTopic());
		message.setTag(refundMsg.getTag());
		message.setBody(JSON.toJSONBytes(refundMsg));
		producer.send(message);
		//更新退款单到退款中状态
		Refund refund = new Refund();
		refund.setRefund_id(refund_id);
		refund.setStatus(Status.RefundStatus.refunding.value);
		refundService.updateByPrimaryKeySelective(refund);

		Map<String, String> map = new HashMap<>();
		map.put("msg", "退款申请已提交，请静心等待处理结果！");
		orderLogService.xInsert(authentication.getName(), users.getUser_id(), pay_order_id,
				"用户[" + authentication.getName() + "] 为客户进行了确认退款操作！");
        return map;
	}

	/**
	 * 检查赔偿单是否存在
	 * @param pay_order_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/compensation_check_exist")
	public Object compensation_check_exist(String pay_order_id){
		CouponExample couponExample = new CouponExample();
		couponExample.or()
				.andValidEqualTo(1)//有效
				.andTypeEqualTo(2);//赔偿券
		List<Coupon> coupons = couponService.selectByExample(couponExample);
		List<Programme_type> programmes = coupons.stream().map(n -> {
			String programme = n.getProgramme();
			Programme_type programmeType = JSONObject.parseObject(programme, Programme_type.class);
			programmeType.setCoupon_id(n.getCoupon_id());
			return programmeType;
		}).collect(Collectors.toList());
		CompensationExample compensationExample = new CompensationExample();
		compensationExample.or().andPay_order_idEqualTo(pay_order_id).andCompensation_statusIn(Arrays.asList(new Integer[]{1,2}));
		Compensation compensation = singleResult(compensationService.selectByExample(compensationExample));
		Map<String,Object> map = new HashMap<>();
		map.put("programmes",programmes);
		map.put("compensation",compensation);
		return map;
	}

	/**
	 * 添加赔偿单
	 * @param compensation
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/compensation_add")
	public Object compensation_add(Compensation compensation,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		compensation.setOperator_create(users.getUser_id());
		logger.info("M[order] F[compensation_add] U[{}] , params param:{} .",
				users.getUser_id(),compensation);
		int i = orderService.xInsertCompensation(compensation);
		logger.info("M[order] F[compensation_add] U[{}] , execute result{} .",
				users.getUser_id(),String.format("发起赔偿单添加%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("发起赔偿单添加%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 跳转到赔偿单列表页
	 * @param map
	 * @param p
	 * @param ps
	 * @param pay_order_id
	 * @param q_begin_datetime
	 * @param q_end_datetime
	 * @param cuname
	 * @param uphone
	 * @param student_name
	 * @param partner_id
	 * @param compensation_status
	 * @return
	 */
	@RequestMapping("/compensations")
	public String compensations(Model map,
								@RequestParam(defaultValue = "1") Integer p,
								@RequestParam(defaultValue = "10") Integer ps,
								@RequestParam(required = false) String pay_order_id,
								@RequestParam(required = false) Date q_begin_datetime,
								@RequestParam(required = false) Date q_end_datetime,
								@RequestParam(required = false) String cuname,
								@RequestParam(required = false) String uphone,
								@RequestParam(required = false) String student_name,
								@RequestParam(required = false) Long partner_id,
								@RequestParam(required = false) Integer compensation_status){
		VOrderUnitExample orderUnitExample = new VOrderUnitExample();
		orderUnitExample.setOrderByClause(VOrderUnitExample.C.create_datetime + " desc");
		Map<String,String> params = new HashMap<>();
		if (!("".equals(pay_order_id)) && pay_order_id != null) {
			pay_order_id = pay_order_id.trim();
			params.put("pay_order_id",pay_order_id);
			map.addAttribute("pay_order_id", pay_order_id);
		}
		if (!("".equals(cuname)) && cuname != null) {
			cuname = cuname.trim();
			params.put("cuname",cuname);
			map.addAttribute("cuname", cuname);
		}
		if (!("".equals(uphone)) && uphone != null) {
			uphone = uphone.trim();
			params.put("uphone",uphone);
			map.addAttribute("uphone", uphone);
		}
		if (partner_id != null) {
			params.put("partner_id",partner_id.toString());
			map.addAttribute("partner_id", partner_id);
		}
		if (!("".equals(student_name)) && student_name != null) {
			student_name = student_name.trim();
			params.put("student_name",student_name);
			map.addAttribute("student_name", student_name);
		}


		CompensationExample compensationExample = new CompensationExample();
		compensationExample.setOrderByClause(CompensationExample.C.create_datetime + " desc");
		CompensationExample.Criteria criteria = compensationExample.or();
		if (q_begin_datetime != null && q_end_datetime != null) {
			criteria.andCreate_datetimeBetween(q_begin_datetime,q_end_datetime);
			map.addAttribute("q_begin_datetime", q_begin_datetime);
			map.addAttribute("q_end_datetime", q_end_datetime);
		}
		if (partner_id != null) {
			criteria.andPartner_idEqualTo(partner_id);
		}
		if (compensation_status != null) {
			criteria.andCompensation_statusEqualTo(compensation_status);
			map.addAttribute("compensation_status", compensation_status);
		}
		if (!("".equals(pay_order_id)) && pay_order_id != null) {
			pay_order_id = pay_order_id.trim();
			criteria.andPay_order_idEqualTo(pay_order_id);
		}

		Page<CompensationInfo> page = compensationService.compensationInfoList(compensationExample,orderUnitExample,params, p, ps);
		List<CompensationInfo> list = page.getList();
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andStateEqualTo(1);
		List<Partner> partners = partnerService.selectByExample(partnerExample);
		map.addAttribute("list", list);
		map.addAttribute("page", page);
		map.addAttribute("partners", partners);
		return "order/compensations";
	}

	/**
	 * 赔偿单确认赔偿
	 * @param compensation_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/compensation_confirm")
	public Object compensation_confirm(Long compensation_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		Compensation compensation = new Compensation();
		compensation.setOperator_confirm(users.getUser_id());
		compensation.setCompensation_status(2);
		compensation.setCompensation_id(compensation_id);
		compensation.setConfirm_datetime(new Date());
		logger.info("M[order] F[compensation_confirm] U[{}] , params param:{} .",
				users.getUser_id(),compensation);
		int i = compensationService.xUpdateConfirm(compensation);
		logger.info("M[order] F[compensation_confirm] U[{}] , execute result{} .",
				users.getUser_id(),String.format("赔偿单确认%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("赔偿单确认%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 赔偿单拒绝赔偿
	 * @param compensation_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/compensation_reject")
	public Object compensation_reject(Long compensation_id,Authentication authentication){
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		Compensation compensation = new Compensation();
		compensation.setCompensation_status(3);
		compensation.setCompensation_id(compensation_id);
		compensation.setOperator_confirm(users.getUser_id());
		compensation.setConfirm_datetime(new Date());
		logger.info("M[order] F[compensation_reject] U[{}] , params param:{} .",
				users.getUser_id(),compensation);
		int i = compensationService.xUpdateReject(compensation);
		logger.info("M[order] F[compensation_reject] U[{}] , execute result{} .",
				users.getUser_id(),String.format("赔偿单驳回%s!", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<>();
		map.put("msg",String.format("赔偿单驳回%s!", i> 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 导出赔偿单列表信息
	 * @param request
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportCompensations")
	public Object exportCompensations(HttpServletRequest request, Authentication authentication){
		// 获取前台传到的json数据
		String str = request.getParameter("params");
		String username = authentication.getName();
		long id = IdGenerator.generateId();
		CompensationExample compensationExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, CompensationExample.class);
		exportCompensationsAndToOss.setCompensationExample(compensationExample);
		exportCompensationsAndToOss.setData_download_id(id);
		// 生成excel并将之上传到阿里云OSS
		completionService.submit(exportCompensationsAndToOss);
		Map<String, String> result = new HashMap<>();
		result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
		return result;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	public void updateAvilableTimeUnits(Long student_id, String dateTime, int begin, int end, int upType) {
		if (upType != 1 && upType != 2 && upType != 3)
			return;
		String key = Constant.getStudentTaskScheduleKey(student_id, dateTime);
		String schedule = redisTemplate.opsForValue().get(key);
		Integer[] integers = null;
		try {
			Student student  = studentService.selectByPrimaryKey(student_id);
			integers =scheduleStr(schedule,dateTime,student);
			for (int i = begin; i <= end; i++) {
				if (i < 0 || i > integers.length)
					continue;
				integers[i] = upType;
			}
			schedule = JacksonUtil.object_to_json(integers);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = format.parse(dateTime + " 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			redisTemplate.opsForValue().set(key,schedule);
			redisTemplate.opsForValue().getOperations().expireAt(key,date);

			cacheReloadHandler.selectStuUndoneOrderReload(student_id);
			cacheReloadHandler.student_order_listReload(student_id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cancelOrderWhenWaitService(Map<String,String> params,ServiceUnit unit,Order order_db){
		//清除服务人员时间调度副本
		Set<String> set = redisService.sMembers(order_db.getPay_order_id());
		if (set!=null){
			redisService.delete(order_db.getPay_order_id());
			redisService.delete(set);
		}

		ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
		ServiceunitPersonExample.Criteria criteria = serviceunitPersonExample.or();
		criteria.andServiceunit_idEqualTo(unit.getServiceunit_id());
		criteria.andStatus_activeEqualTo(Status.OrderStatus.wait_service.value);
		List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(serviceunitPersonExample);
		SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = ymd.format(unit.getC_begin_datetime());
		String beginString = hm.format(unit.getC_begin_datetime());
		String endString;
		if(unit.getC_end_datetime()==null){
			endString = "none";
		}else {
			endString = hm.format(unit.getC_end_datetime());
		}
		OrderInfo orderInfo = orderService.orderInfoDetail(Roles.CUSTOMER, order_db);
		personList.forEach(n ->{
			Student student = studentService.selectByPrimaryKey(n.getStudent_id());
			params.put("cus_username", order_db.getCus_username());
			params.put("cus_phone", order_db.getCus_phone());
			sendMsg(Constant.SEND_TO_WORK_WHEN_ORDER_CANCEL, student.getPhone(), Constant.SIGN_NAME, params);

			Integer begin = Constant.timeUnisMap.get(beginString);
			Integer end = Constant.timeUnisMap.get(endString);
			if (begin != null) {
				end = end == null ? 47 : end;
				updateAvilableTimeUnits(n.getStudent_id(), dateTime, begin, end, 1);
			}
			cacheReloadHandler.selectStuUndoneOrderReload(n.getStudent_id());
			cacheReloadHandler.student_order_listReload(n.getStudent_id());
			cacheReloadHandler.selectStuShowTaskdetailReload(n.getStudent_id(),order_db.getPay_order_id());
			//订单取消，发送个推消息
			//pushHandler.pushCancelOrderMessageToStudent(orderInfo,n.getStudent_id().toString());
		});
		ServiceunitPerson sp = new ServiceunitPerson();
		sp.setStatus_active(Status.OrderStatus.cancel.value);
		serviceunitPersonService.updateByExampleSelective(sp,serviceunitPersonExample);
		cacheReloadHandler.my_mission_scheduled_informationReload(unit.getPartner_id());
	}

	public OneDayStore getStudentsStroe(List<SkuTime> timesList,List<Student> students,String dateTime){
		OneDayStore oneDayStore = new OneDayStore();
		List<TimeScopeStore> scopeStores = timesList.stream().map(n -> {
			int store = 0;
			for (Student student : students) {
				String key = Constant.getStudentTaskScheduleKey(student.getStudent_id(), dateTime);
				String schedule = redisTemplate.opsForValue().get(key);
				Integer[] integers = null;
				try {
					integers = scheduleStr(schedule, dateTime, student);
				} catch (Exception e) {
					integers = Constant.defaultAvailableTimeArrar;
					logger.error("data of student is  illegal  studentId:{}", student.getStudent_id());
				}
				if (isHaveStore(integers, n.getS(), n.getE(), Constant.SERVICE_INTERVAL_UNIT)) {
					store++;
				}

			}
			TimeScopeStore scopeStore = new TimeScopeStore();
			scopeStore.setSkuTime(n);
			scopeStore.setStore(store);
			return scopeStore;
		}).collect(Collectors.toList());
		oneDayStore.setDatetime(dateTime);
		oneDayStore.setScopeStores(scopeStores);
		return oneDayStore;
	}

	private boolean isHaveStore(Integer[] arr, int begin, int end, int restUnit) {
		//边界值检测 如果开始时间是在00:00,00:30,10:00 ，结束时间在23:00,23:30 都属于非法劳动时间。
		//去掉非法劳动时间。
		if (end < begin || begin < 3) {
			return false;
		}
		if (end > 45) {
			restUnit = 0;
		}
		if (end > 42) {
			end = 42;
		}
		//连续时间：如果不可使用，就代表不能提供服务；
		for (int i = begin - (restUnit-1); i < end + restUnit; i++) {
			if (arr[i] != 1) {
				if ((i < begin && arr[i] == 0) || (i > end && arr[i] == 0))
					continue;
				return false;
			}
		}
		return true;
	}
	/**
	 * 如果服务人员设定了上班时间。服务人员调度时间的计算
	 * @param student
	 * @return
	 */
	private Integer[] scheduleStr(String schedule,String dateTime ,Student student){

		String times = student.getService_times();
		if(times==null) {
			schedule = Constant.defaultAvailableTime;
			return JSON.parseObject(schedule, Integer[].class);
		}
		Integer[] orderArray = null;

		String weeks =student.getService_cycle();
		Map<String,Integer>  map = JSON.parseObject(times, new TypeReference<Map<String, Integer>>() {});
		Set<Integer>set  =  JSON.parseObject(weeks,new TypeReference<Set<Integer>>() {});
		Integer[] arrars = JSON.parseObject(Constant.defaultNoTime,Integer[].class);
		if (schedule!=null){
			orderArray  = JSON.parseObject(schedule,Integer[].class);
		}
		int begin  = map.get("s");
		int end    = map.get("e");
		LocalDate date =  LocalDate.parse(dateTime,DateTimeFormatter.ISO_LOCAL_DATE);
		if(set.contains(date.getDayOfWeek().getValue())){
			for (int i=0;i<arrars.length;i++){
				if (orderArray!=null&&orderArray[i]>1){
					arrars[i]=orderArray[i];
				}else {
					if (i >= begin && i <= end) {
						arrars[i] = 1;
					} else {
						arrars[i] = 0;
					}
				}
			}
		}
		return  arrars;
	}
}
