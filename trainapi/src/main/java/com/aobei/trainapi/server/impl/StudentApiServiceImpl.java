package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.openservices.ons.api.SendResult;
import com.aobei.common.boot.RedisIdGenerator;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.model.ServiceUnitExample.Criteria;
import com.aobei.train.service.*;
import com.aobei.train.service.impl.StoreServiceImpl;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.TokenUtil;
import com.aobei.trainapi.schema.input.StudentOrderInput;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.StudentApiService;
import com.aobei.trainapi.server.bean.*;
import com.aobei.trainapi.server.bean.Img;
import com.aobei.trainapi.server.bean.MessageContent;
import com.aobei.trainapi.server.bean.StudentServiceOrderStatistics;
import com.aobei.trainapi.server.bean.StudentInfo;
import com.aobei.trainapi.server.handler.OnsHandler;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.*;
import custom.bean.OrderInfo.OrderStatus;
import custom.bean.OrderInfo.ServiceStatus;
import custom.util.DistanceUtil;
import custom.util.ParamsCheck;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
public class StudentApiServiceImpl implements StudentApiService
{
	@Autowired
	ServiceUnitService serviceUnitService;

	@Autowired
	OrderService orderService;

	@Autowired
	StudentService studentService;

	@Autowired
	ProductService productService;

	@Autowired
	ProSkuService proSkuService;

	@Autowired
	CustomerService customerService;

	@Autowired
	MessageService messageService;
	@Autowired
	MetadataService metadataService;
	@Autowired
	OnsHandler onsHandler;
	@Autowired
	ServiceunitPersonService serviceunitPersonService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	CustomerAddressService customerAddressService;
	@Autowired
	StoreService storeService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	OrderLogService orderLogService;
	@Autowired
    CacheReloadHandler cacheReloadHandler;
	@Autowired
	Environment env;
	@Autowired
	TokenUtil TOKEN;
	@Autowired
	VideoContentService videoContentService;
	@Autowired
	CustomerApiService customerApiService;
	Logger logger = LoggerFactory.getLogger(StudentApiServiceImpl.class);

	/**
	 * 服务人员未完成订单
	 */
	@Cacheable(value = "selectStuUndoneOrder", key = "'student_id:'+#student_id+':'+#page_index+':'+#count", unless = "#result == null")
	@Override
	public List<OrderInfo> selectStuUndoneOrder(Long student_id, int page_index, int count) {
		logger.info("api-method:selectStuUndoneOrder:params student_id:{},page_index:{},count:{}", student_id,
				page_index, count);
		List<OrderInfo> orderInfoList = new ArrayList<>();
		// 根据学员找到对应的已指派的服务单
		List<Long> unitIds = partnerLongIds(student_id);
		if (unitIds.size() == 0) {
			return orderInfoList;
		}
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		Criteria criteria = serviceUnitExample.or();
		criteria.andServiceunit_idIn(unitIds)
				.andStatus_activeEqualTo(Status.ServiceStatus.assign_worker.value)// 订单状态为已指派
				.andPidEqualTo(0L)// 父单
				.andActiveEqualTo(Status.ServiceUnitActive.active.value)
				.andWork_statusEqualTo(2);
		List<OrderInfo> orderInfoList1 = orderService.orderInfoList(Roles.STUDENT, serviceUnitExample, page_index, count).getList();
		ServiceUnitExample example = new ServiceUnitExample();
		Criteria or = example.or();
		or.andServiceunit_idIn(unitIds)
				.andStatus_activeEqualTo(Status.ServiceStatus.assign_worker.value)// 订单状态为已指派
				.andPidEqualTo(0L)// 父单
				.andActiveEqualTo(Status.ServiceUnitActive.active.value)
				.andWork_statusIsNull();
		orderInfoList = orderService.orderInfoList(Roles.STUDENT, example, page_index, count).getList();
		orderInfoList.addAll(orderInfoList1);
		logger.info("api-method:selectStuUndoneOrder:process orderInfoList:{}", orderInfoList.size());
		return orderInfoList;
	}

	/**
	 * 重用方法
	 */
	public List<Long> partnerLongIds(Long student_id) {
		logger.info("api-method:partnerLongIds:params student_id:{}", student_id);
		ServiceunitPersonExample personExample = new ServiceunitPersonExample();
		personExample.or().andStudent_idEqualTo(student_id);
		List<ServiceunitPerson> list = serviceunitPersonService.selectByExample(personExample);
		List<Long> unitIds = list.stream().map(n -> n.getServiceunit_id()).collect(Collectors.toList());
		logger.info("api-method:partnerLongIds:process unitIds:{}", unitIds.size());
		return unitIds;
	}

	/**
	 * 根据选择的日期进行查询指定日期服务人员完成订单
	 */
	@Cacheable(value = "selectStuCompleteOrder", key = "'student_id:'+#student_id+':dateValue:'+#dateValue+':'+#page_index+':'+#count", unless = "#result == null")
	@Override
	public List<OrderInfo> selectStuCompleteOrder(Long student_id, Date dateValue, int page_index, int count) {
		logger.info("api-method:selectStuCompleteOrder:params student_id:{},dateValue:{},page_index:{},count:{}",
				student_id, dateValue, page_index, count);
		List<OrderInfo> orderInfoList = new ArrayList<>();
		// 根据学员找到对应的已完成的服务单
		List<Long> unitIds = partnerLongIds(student_id);
		if (unitIds.size() == 0) {
			return orderInfoList;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateValue);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		Date end = calendar.getTime();
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		Criteria criteria = serviceUnitExample.or();
		serviceUnitExample.or().andServiceunit_idIn(unitIds).andPidEqualTo(0L)// 父单
				.andStatus_activeEqualTo(Status.ServiceStatus.done.value)// 已完成
				.andActiveEqualTo(Status.PayStatus.payed.value).andC_begin_datetimeBetween(dateValue, end);// 有效的
		orderInfoList = orderService.orderInfoList(Roles.STUDENT, serviceUnitExample, page_index, count).getList();
		if (orderInfoList.size() == 0) {
			return new ArrayList<OrderInfo>();
		}
		logger.info("api-method:selectStuCompleteOrder:process orderInfoList:{}", orderInfoList.size());
		return orderInfoList;
	}

	/**
	 * 修改学员的4个状态（到达，开始，结束，离开）和是否备注，是否在顾客下单地址附近
	 */
	@Override
	@Transactional(timeout = 3)
	public ApiResponse<WorkStatusResult> student_update_work_status(Long student_id, String pay_order_id,
			String serviceStatus, String remarkStr, String lbs_lat, String lbs_lng) {
		logger.info(
				"api-method:student_update_work_status:params student_id:{},pay_order_id:{},serviceStatus:{},remarkStr:{}"
						+ "lbs_lat:{},lbs_lng:{}",
				student_id, pay_order_id, serviceStatus, remarkStr, lbs_lat, lbs_lng);
		ApiResponse<WorkStatusResult> apiResponse = new ApiResponse<>();
		WorkStatusResult result = new WorkStatusResult();
		// 获取到顾客下单地址的经度 纬度
		Order order = this.orderService.selectByPrimaryKey(pay_order_id);
		Customer customer = customerService.selectByPrimaryKey(order.getUid());
		Double cusLat = Double.valueOf(order.getLbs_lat());// 顾客纬度
		Double cusLng = Double.valueOf(order.getLbs_lng());// 顾客经度
		// 定位学员地址的经度 纬度
		Double stuLat = Double.valueOf(lbs_lat);// 学员纬度
		Double stuLng = Double.valueOf(lbs_lng);// 学员经度
		Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SIGN_RADIUS);
		Integer radiu = metadata.getMeta_value() == null ? Constant.SERVICE_STATUS_UPDATE_RADIUS
				: Integer.parseInt(metadata.getMeta_value());
		double getDistance = DistanceUtil.GetDistance(cusLat, cusLng, stuLat, stuLng);
		Map<String, Object> map = new HashMap<>();
		// 判断是否在1公里之内
		if (radiu < getDistance) {
			result.setServiceStatus(serviceStatus);
			result.setOutPostion(true);// 不在服务范围
			apiResponse.setT(result);
			return apiResponse;
		}
		Student student = this.studentService.selectByPrimaryKey(student_id);
		logger.info("api-method:student_update_work_status:process student:{}", student);
		// 根据订单找出来服务单
		ServiceUnitExample unitExample = new ServiceUnitExample();
		unitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0L).andActiveEqualTo(1);
		ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(unitExample));
		logger.info("api-method:student_update_work_status:process serviceUnit:{}", serviceUnit);
		// 验证服务单是否存在
		if (serviceUnit == null) {
			apiResponse.setErrors(Errors._41008);
			return apiResponse;
		}
		ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
		serviceunitPersonExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id())
				.andStudent_idEqualTo(student_id);
		ServiceunitPerson serviceunitPerson = singleResult(
				serviceunitPersonService.selectByExample(serviceunitPersonExample));

		ServiceStatus status = ServiceStatus.get(serviceStatus);
        String sname = student.getName() == null ? student.getPhone() : student.getName();
		logger.info("api-method:student_update_work_status:process status:{}", status);
		if (serviceStatus != null) {
			ServiceUnit unit = new ServiceUnit();
			switch (status) {
			case START:
			case ARRIVE:// 到达
				unit.setWork_status(2);
				unit.setWork_1_datetime(new Date());
				unit.setWork_2_datetime(new Date());
				serviceunitPerson.setWork_status(2);
				serviceunitPerson.setWork_1_datetime(new Date());
				serviceunitPerson.setWork_2_datetime(new Date());
				result.setServiceStatus(ServiceStatus.LEAVE.getDescr());
				apiResponse.setT(result);
                orderLogService.xInsert(sname, student.getUser_id(), pay_order_id,
                        "服务人员【" + sname + "】已到达，开始进行服务!!!" );
				break;
			case LEAVE:
			case DONE:// 离开
				unit.setWork_status(4);
				unit.setWork_3_datetime(new Date());
				unit.setWork_4_datetime(new Date());
				unit.setWork_remark(remarkStr);

				serviceunitPerson.setWork_status(4);
				serviceunitPerson.setWork_remark(remarkStr);
				serviceunitPerson.setWork_3_datetime(new Date());
				serviceunitPerson.setWork_4_datetime(new Date());

				Message msg = new Message();
				msg.setId(IdGenerator.generateId());
				msg.setType(2);
				msg.setBis_type(4);
				msg.setUser_id(customer.getUser_id());
				msg.setUid(customer.getCustomer_id());
				msg.setMsg_title("订单完成通知");
				// 服务人员{服务人员名}于{预约时间}，在{服务地址}为您进行服务已完成，请您确认，满意请给我们点亮五颗星星哦。
				MessageContent.ContentMsg content = new MessageContent.ContentMsg();
				content.setMsgtype("native");
				content.setV("1");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String beginString = format.format(serviceUnit.getC_begin_datetime());
				String studentName  = serviceUnit.getStudent_name()==null?"":serviceUnit.getStudent_name();
				studentName = studentName.endsWith(",")?studentName.substring(0,serviceUnit.getStudent_name().length()-1):studentName;

				content.setContent("服务人员" + studentName+","+ beginString + "在"
						+ order.getCus_address() + "为您进行的服务已完成，请您确认，满意请给我们点亮五颗星星哦。");
				Map<String,String> param = new HashMap<>();
				param.put("pay_order_id",order.getPay_order_id());
				TransmissionContent tContent = new TransmissionContent(TransmissionContent.CUSTOM,TransmissionContent.ORDER_DETAIL,param);
				content.setHref(tContent.getHrefNotEncode());
				content.setTitle("订单完成通知");
				content.setTypes(1);
				if (content.getContent() != null && !ParamsCheck.checkStrAndLength(content.getContent(),500)){
					Errors._41040.throwError("消息长度过长");
					return apiResponse;
				}
				String object_to_json = null;
				try {
					object_to_json = JSON.toJSONString(content);
				} catch (Exception e) {
					e.printStackTrace();
				}
				msg.setMsg_content(object_to_json);
				msg.setCreate_datetime(new Date());
				msg.setNotify_datetime(new Date());
				msg.setGroup_id(order.getPay_order_id());
				msg.setApp_type(4);
				msg.setSend_type(1);
				msg.setApp_platform(0);
				messageService.insertSelective(msg);

				// 72小时发送MQ
				metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_AUTO_COMPLETE_TIME);
				Integer timeOut = metadata.getMeta_value() == null ? 72 : Integer.parseInt(metadata.getMeta_value());
				Long delayTime = System.currentTimeMillis() + timeOut * 60 * 60 * 1000;
				onsHandler.sendCompleteMessage(order.getPay_order_id(), delayTime);
				result.setServiceStatus("over");
				apiResponse.setT(result);
                orderLogService.xInsert(sname, student.getUser_id(), pay_order_id,
                        "服务人员【" + sname + "】完成服务，已离开!!!" );
				break;
			}
			ServiceUnitExample serviceUnitExample1 = new ServiceUnitExample();
			serviceUnitExample1.or().andGroup_tagEqualTo(serviceUnit.getGroup_tag());
			serviceUnitService.updateByExampleSelective(unit, serviceUnitExample1);
			serviceunitPersonService.updateByPrimaryKeySelective(serviceunitPerson);
			// 更新顾客端缓存
			cacheReloadHandler.orderListReload(order.getUid());
			cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);
			// 更新服务人员缓存
			cacheReloadHandler.student_order_listReload(serviceunitPerson.getStudent_id());
			cacheReloadHandler.selectStuShowTaskdetailReload(serviceunitPerson.getStudent_id(), pay_order_id);
			cacheReloadHandler.selectStuUndoneOrderReload(serviceunitPerson.getStudent_id());
			cacheReloadHandler.my_employeeManagementReload(student.getPartner_id());
			// 更新合伙人订单缓存
			cacheReloadHandler.partner_order_detailReload(pay_order_id);
			cacheReloadHandler.partner_order_listReload(serviceUnit.getPartner_id());
		}
		return apiResponse;
	}

	/**
	 * 服务人员端 任务详情
	 */
	@Cacheable(value = "selectStuShowTaskdetail", key = "'student_id:'+#student_id+':pay_order_id:'+#pay_order_id", unless = "#result == null")
	@Override
	public OrderInfo selectStuShowTaskdetail(Long student_id, String pay_order_id) {
		logger.info("api-method:selectStuShowTaskdetail:params [student_id:{},pay_order_id:{}", student_id,
				pay_order_id);
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0L);
		List<ServiceUnit> selectByExample = this.serviceUnitService.selectByExample(serviceUnitExample);
		ServiceUnit serviceUnit = singleResult(selectByExample);
		logger.info("api-method:selectStuShowTaskdetail:process serviceUnit:{}", serviceUnit);
		OrderInfo orderInfo = orderService.orderInfoDetail(Roles.STUDENT, serviceUnit);
		if ("waitService".equals(orderInfo.getOrderStatus())) {
			if (serviceUnit.getWork_status() == null) {
				serviceUnit.setWork_status(2);
			} else if (serviceUnit.getWork_status() == 2) {
				serviceUnit.setWork_status(4);
			} else if (serviceUnit.getWork_status() == 4) {
				serviceUnit.setWork_status(5);
			}
		}
		orderInfo.setServiceUnit(serviceUnit);
		//服务人员是否可以续单
		ProSkuExample skuExample = new ProSkuExample();
		skuExample.or().andProduct_idEqualTo(serviceUnit.getProduct_id()).andDispalyEqualTo(1);
		long skuNum = proSkuService.countByExample(skuExample);
		orderInfo.setWhetherCanContinue(1);
		if (skuNum == 0){
			orderInfo.setWhetherCanContinue(0);
		}
		//是否支持取消订单（true支持,false不支持）
		switch (orderInfo.getOrder().getStatus_active()){
			case 1:
				orderInfo.setAllowedToCancel(true);
				break;
			case 2:
			case 3:
				if (StringUtils.isEmpty(serviceUnit.getC_end_datetime())){
					orderInfo.setAllowedToCancel(false);
					break;
				}
				if ("JD-001".equals(orderInfo.getOrder().getChannel())){
					orderInfo.setAllowedToCancel(false);
				}else {
					CancleStrategyMethod cancleStrategyMethod = customerApiService.cancleStrategyMethod(orderInfo.getCustomer(), pay_order_id).getT();
					if (cancleStrategyMethod == null) {
						orderInfo.setAllowedToCancel(false);
					} else {
						orderInfo.setAllowedToCancel(true);
					}
				}
				break;
			case 4:
			case 5:
				orderInfo.setAllowedToCancel(false);
				break;
		}
		logger.info("api-method:selectStuShowTaskdetail:process orderInfo:{}", orderInfo);
		return orderInfo;
	}

	/**
	 * 学员的消息列表
	 *
	 * @param page_index
	 * @param count
	 * @return
	 */
	@Override
	public List<MessageContent> selectStuMessage(Student student,int page_index, int count) {
		logger.info("api-method:selectStuMessage:params student:{},date:{},page_index:{},count:{}", student,
				page_index, count);
		List<MessageContent> messageList = new ArrayList<>();
		MessageExample messageExample = new MessageExample();
		List<Long> ids = new ArrayList<>();
		ids.add(student.getUser_id());
		ids.add(0l);
		messageExample.or()
				.andUser_idIn(ids)
				.andDeletedEqualTo(Status.DeleteStatus.no.value)
				.andNotify_datetimeLessThanOrEqualTo(new Date())
				.andSend_typeEqualTo(1)
				.andApp_typeEqualTo(1)
				.andApp_platformEqualTo(0);
		messageExample.setOrderByClause(MessageExample.C.notify_datetime + " DESC");
		List<Message> messages = messageService.selectByExample(messageExample, page_index, count).getList();
		if(messages.size() != 0){
			messageList = messages.stream().map((Message message) -> {
				MessageContent messageContent = new MessageContent();
				String content = message.getMsg_content();
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(content)){
					MessageContent.ContentMsg msg =  JSON.parseObject(content, MessageContent.ContentMsg.class);
					messageContent.setContentMsg(msg);
					try {
						if (msg != null && msg.getHref() != null) {
							msg.setHref(URLEncoder.encode(msg.getHref(),"UTF-8"));
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				messageContent.setNotifyDateTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(message.getNotify_datetime()));
				BeanUtils.copyProperties(message, messageContent);
				return messageContent;
			}).collect(Collectors.toList());
		}
		logger.info("api-method:selectStuMessage:process messageList:{}", messageList.size());
		return messageList;
	}

	/**
	 * 单个消息详情
	 *
	 * @param student_id
	 * @param id
	 * @return
	 */
	@Override
	public Message selectMessageDetail(Long student_id, Long id) {
		logger.info("api-method:selectMessageDetail:params student_id:{},id:{}", student_id, id);
		MessageExample messageExample = new MessageExample();
		messageExample.or().andUser_idEqualTo(student_id).andIdEqualTo(id);
		List<Message> selectByExample = this.messageService.selectByExample(messageExample);
		return singleResult(selectByExample);
	}

	/**
	 * 订单列表
	 */
	@Cacheable(value = "student_order_list", key = "'student_id:'+#student_id+':status:'+#status+#page_index+':'+#count", unless = "#result == null")
	@Override
	public List<OrderInfo> student_order_list(String status, Long student_id, int page_index, int count) {
		logger.info("api-method:student_order_list:params status:{},student_id:{},page_index:{},count:{}", status,
				student_id, page_index, count);
		// 该学员的服务单id
		ServiceunitPersonExample personExample = new ServiceunitPersonExample();
		ServiceunitPersonExample.Criteria personCriteria = personExample.or();
		personCriteria.andStudent_idEqualTo(student_id);
		List<ServiceunitPerson> personList3 = serviceunitPersonService.selectByExample(personExample);
		List<Long> Unit_ids3 = personList3.stream().map(n -> n.getServiceunit_id()).collect(Collectors.toList());
		// 学员服务单
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		Criteria unitCriteria = serviceUnitExample.or();
		// 根据服务单id查询订单
		OrderExample orderExample = new OrderExample();
		OrderExample.Criteria orderCriteria = orderExample.or();
		List<OrderInfo> list = new ArrayList<>();
		// 组装条件
		OrderStatus orderStatus = OrderStatus.get(status);
		logger.info("api-method:student_order_list:process orderStatus:{}", orderStatus);
		if (orderStatus != null) {
			switch (orderStatus) {
			case WAIT_PAY:
				orderCriteria.andStatus_activeEqualTo(Status.OrderStatus.wait_pay.value);
				orderCriteria.andPay_statusEqualTo(Status.PayStatus.wait_pay.value);
				orderCriteria.andProxyed_uidEqualTo(student_id);
				orderCriteria.andProxyedEqualTo(1);
				orderExample.setOrderByClause(OrderExample.C.create_datetime + " DESC");
				list = orderService.orderInfoList(Roles.STUDENT, orderExample, page_index, count).getList();
				break;
			case CANCEL:
				personCriteria.andStatus_activeEqualTo(Status.OrderStatus.cancel.value);
				List<ServiceunitPerson> perList = serviceunitPersonService.selectByExample(personExample);
				if (perList.size() == 0) {
					return list;
				}
				List<Long> unitids = perList.stream().map(a -> a.getServiceunit_id()).collect(Collectors.toList());
				unitCriteria.andServiceunit_idIn(unitids);
				serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " DESC");
				list = orderService.orderInfoList(Roles.STUDENT, serviceUnitExample, page_index, count).getList();
				break;
			case DONE:
				personCriteria.andStatus_activeEqualTo(Status.ServiceStatus.done.value);
				List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(personExample);
				if (personList.size() == 0) {
					return list;
				}
				List<Long> Unit_ids1 = personList.stream().map(n -> n.getServiceunit_id()).collect(Collectors.toList());
				unitCriteria.andServiceunit_idIn(Unit_ids1);
				unitCriteria.andActiveEqualTo(Status.PayStatus.payed.value);
				serviceUnitExample.setOrderByClause(ServiceUnitExample.C.finish_datetime + " DESC");
				list = orderService.orderInfoList(Roles.STUDENT, serviceUnitExample, page_index, count).getList();
				break;
			case WAIT_SERVICE:
				personCriteria.andStatus_activeEqualTo(Status.OrderStatus.wait_service.value);
				List<ServiceunitPerson> personList1 = serviceunitPersonService.selectByExample(personExample);
				List<Long> Unit_ids2 = personList1.stream().map(n -> n.getServiceunit_id())
						.collect(Collectors.toList());
				if (Unit_ids2.size() == 0)
					break;
				unitCriteria.andServiceunit_idIn(Unit_ids2);
				unitCriteria.andActiveEqualTo(Status.ServiceUnitActive.active.value);
				serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " ASC");
				list = orderService.orderInfoList(Roles.STUDENT, serviceUnitExample, page_index, count).getList();
				break;
			}
		} else {
			if (Unit_ids3.size() == 0)
				return new ArrayList<OrderInfo>();
			unitCriteria.andServiceunit_idIn(Unit_ids3);
			serviceUnitExample.setOrderByClause(ServiceUnitExample.C.p_confirm_datetime + " DESC");
			list = orderService.orderInfoList(Roles.STUDENT, serviceUnitExample, page_index, count).getList();
		}
		logger.info("api-method:student_order_list:process listSize:{}", list.size());
		return list;
	}

	/**
	 * 获取产品的sku列表
	 *
	 * @param product_id
	 *            产品ID
	 * @param page_index
	 *            页码 从1开始
	 * @param count
	 *            每页显示条数
	 * @return list
	 */
	public List<ProSku> proSkuList(Long product_id, int page_index, int count) {
		logger.info("api-method:proSkuList:params product_id:{},page_index:{},count:{}", product_id, page_index, count);
		ProSkuExample example = new ProSkuExample();
		example.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andProduct_idEqualTo(product_id)
				.andDispalyEqualTo(1);
		example.setOrderByClause(ProSkuExample.C.sort_num + " ASC");
		Page<ProSku> proSkus = proSkuService.selectByExample(example, page_index, count);
		logger.info("api-method:proSkuList:process proSkus:{}", proSkus);
		return proSkus.getList();
	}

	/**
	 * 商品详情
	 */
	@Cacheable(value = "productDetail2", key = "'product_id:'+#product_id")
	@Override
	public ProductInfo productDetail(Long product_id) {
		logger.info("api-method:productDetail:params product_id:{}", product_id);
        Product product = productService.selectByPrimaryKey(product_id);
        if (product == null) {
           Errors._41006.throwError("商品信息不存在");
        }
		ProductInfo productInfo = new ProductInfo();
		logger.info("api-method:productDetail:process product:{}", product);
		OrderItemExample orderItemExample = new OrderItemExample();
		orderItemExample.or().andProduct_idEqualTo(product_id);
		int num = orderItemService.xSumNum(orderItemExample);
		product.setBase_buyed(product.getBase_buyed() + num);
		List<ProSku> proSkus = proSkuList(product.getProduct_id(), 1, 20);
		productInfo.setProduct(product);
		productInfo.setProSkus(proSkus);
        String tag_images = product.getTag_images();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(tag_images)) {
            List<ProductTag> productTags = JSONArray.parseArray(tag_images, ProductTag.class);
            productInfo.setProductTags(productTags);
        } else {
            productInfo.setProductTags(new ArrayList<>());
        }
        List<CouponResponse> couponResponses= new ArrayList<>();
        productInfo.setCouponResponse(couponResponses);
        String imgstring = product.getLite_image();
        try {
            custom.bean.Img img = JSON.parseObject(imgstring, custom.bean.Img.class);
            product.setLite_image(img.getUrl());
        } catch (Exception e) {
            logger.warn("product{} liteImage not exits", product_id);
        }
        productInfo.setCouponResponse(new ArrayList<>());
        logger.info("api-method:productDetail:process productInfo:{}", productInfo);
		return productInfo;
	}

	/**
	 * 查询顾客信息
	 */
	@Override
	public CustomerDetail studentCustomerDetail(Student student, String pay_order_id) {
		logger.info("api-method:studentCustomerDetail:params student:{},pay_order_id:{}", student, pay_order_id);
		CustomerDetail customerDetail = new CustomerDetail();
		Order order = orderService.selectByPrimaryKey(pay_order_id);
		logger.info("api-method:studentCustomerDetail:process order:{}", order);
		if (StringUtils.isEmpty(order)) {
			Errors._41007.throwError();
		}
		Customer customer = customerService.selectByPrimaryKey(order.getUid());
		customerDetail.setCustomer(customer);
		CustomerAddressExample addressExample = new CustomerAddressExample();
		addressExample.or().andCustomer_idEqualTo(customer.getCustomer_id());
		List<CustomerAddress> addressList = customerAddressService.selectByExample(addressExample);
		customerDetail.setCustomerAddress(addressList);
		logger.info("api-method:studentCustomerDetail:process customerDetail:{}", customerDetail);
		return customerDetail;
	}

	/**
	 * 创建订单:
	 */
	@Transactional(timeout = 5)
	@Override
	public ApiResponse<Order> create_order(Student student,String channelId, StudentOrderInput input) {
		logger.info("api-method:create_order:params student:{},input:{}", student, input);
		ApiResponse<Order> response = new ApiResponse<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
            boolean flag = true;
            Date begin = null;
            Date end = null;
            try {
				if (org.apache.commons.lang3.StringUtils.isEmpty(input.getBegin_datetime()) || org.apache.commons.lang3.StringUtils.equals(input.getBegin_datetime(), "null")) {
					begin = null;
				} else {
					begin = format.parse(input.getBegin_datetime());
				}

                if (org.apache.commons.lang3.StringUtils.isEmpty(input.getEnd_datatime()) || org.apache.commons.lang3.StringUtils.equals(input.getEnd_datatime(), "null")) {
                    end = null;
                } else {
                    end = format.parse(input.getEnd_datatime());
                }
                Date now = new Date();
				if (!StringUtils.isEmpty(begin)){
					if (begin.before(now)) {
						flag = false;
					}
				}
            } catch (ParseException e) {
                flag = false;
            }

			Order order = new Order();
			Order oldOlder = orderService.selectByPrimaryKey(input.getPay_order_id());
			logger.info("api-method:create_order:process oldOlder:{}", oldOlder);
			Product product = productService.selectByPrimaryKey(input.getProduct_id());
			OrderItem orderItem = new OrderItem();
			//服务单1  0元服务,下单支付成功   付费单 2  正常流程,跟小程序一样  其他值都是走正常流程
			if (input.getContinuousSingleState() == 1){
				CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(oldOlder.getCustomer_address_id());
				if (customerAddress == null) {
					response.setErrors(Errors._41004);
					return response;
				}
				// 开始生成订单，不计算价格
				ProSku sku = new ProSku();
				sku.setName("服务单");
				sku.setPrice(0);
				sku.setProduct_id(input.getProduct_id());
				order = orderService.initOrder(product, sku, customerAddress, input.getNum());
				String envname = env.getActiveProfiles()[0];
				String pay_order_id = redisIdGenerator.generatorId("pay_order_id", 1000) + "";
				if ("dev".equals(envname)) {
					pay_order_id = pay_order_id + "_1";
				} else if ("test".equals(envname)) {
					pay_order_id = pay_order_id + "_2";
				}
				order.setPay_order_id(pay_order_id);
				order.setPrice_discount(0);
				order.setPrice_pay(0);
				order.setPrice_total(0);
				order.setChannel(channelId);
				order.setPay_type(3);
				order.setPay_status(StatusConstant.PAY_STATUS_PAYED);
				order.setRemark(input.getRemark());
				order.setStatus_active(StatusConstant.ORDER_STATUS_WAITSERVICE);
				order.setProxyed(1);
				order.setProxyed_uid(student.getStudent_id());
				order.setGroup_tag(oldOlder.getGroup_tag());
				//订单包含商品
				orderItem = orderItemService.initOrderItem(order, sku, input.getNum());
				// 进行存储,成功和失败的判断
				orderService.insertSelective(order);
				orderItemService.insertSelective(orderItem);

				Customer customer = customerService.selectByPrimaryKey(customerAddress.getCustomer_id());
				ServiceUnit[] serviceUnits = initServiceUnit(customer, order, input);
				Long serviceUnit_id = 0l;
				for (ServiceUnit serviceUnit : serviceUnits) {
					serviceUnit.setStudent_id(student.getStudent_id());
					serviceUnit.setStudent_name(student.getName());
					serviceUnit.setC_begin_datetime(begin);
					serviceUnit.setC_end_datetime(end);
					serviceUnit.setPartner_id(student.getPartner_id());
					serviceUnit.setStation_id(student.getStation_id());
					serviceUnit.setActive(StatusConstant.SERVICEUNIT_STATUS_ACTIVE);
					serviceUnit.setStatus_active(StatusConstant.SERVICEUNIT_STATUS_ASSIGNWORKE);
					serviceUnitService.insertSelective(serviceUnit);
					if (serviceUnit.getPid() == 0) {
						serviceUnit_id = serviceUnit.getServiceunit_id();
					}
				}
				// 服务单记录
				ServiceunitPerson person = new ServiceunitPerson();
				person.setServiceunit_person_id(IdGenerator.generateId());
				person.setServiceunit_id(serviceUnit_id);
				person.setStudent_id(student.getStudent_id());
				person.setStudent_name(student.getName());
				person.setStatus_active(StatusConstant.ORDER_STATUS_WAITSERVICE);
				serviceunitPersonService.insertSelective(person);
				logger.info("api-method:create_order:process [person = " + person + "]");

				// 占服务人员库存
                if (input.getType() == 1){
                    if (!storeService.isStudentHasStore(student, input.getBegin_datetime().substring(0, 10),begin, end)) {
                        response.setErrors(Errors._41021);
                        return response;
                    }
                    storeService.updateAvilableTimeUnits(student.getStudent_id(), begin, end, StoreServiceImpl.TAKEN);
                }

			}else {
				ProSku sku = proSkuService.selectByPrimaryKey(input.getPsku_id());
				logger.info("api-method:create_order:process sku:{}", sku);
				// --------------------------------判断参数值对否-------------------------------------
				Integer allow_mutiple = sku.getBuy_multiple();
                String strBegin = null;
                if (!StringUtils.isEmpty(begin)){
                    strBegin = format.format(begin);
                }
				Integer num = input.getNum();
				if (allow_mutiple == 1) {// 允许购买多件
					Integer max = sku.getBuy_multiple_max();
					Integer min = sku.getBuy_multiple_min();
					if (max == 0) {// 不限制购买数量
						if (num >= min) {
							flag = true;
						}
					} else {
						if (num >= min && num <= max) {
							flag = true;
						}
					}
				} else if (num > 1) {
					flag = false;
				}

				if (!flag) {
					response.setErrors(Errors._41040);
					return response;
				}
				// 用户下单地址校验
				CustomerAddressExample example = new CustomerAddressExample();
				example.or().andCustomer_address_idEqualTo(input.getCustomer_address_id());
				CustomerAddress customerAddress = singleResult(customerAddressService.selectByExample(example));
				logger.info("api-method:create_order:process customerAddress:{}", customerAddress);
				if (customerAddress == null) {
					response.setErrors(Errors._41004);
					return response;
				}

				// 要购买的产品的验证
				logger.info("api-method:create_order:process product:{}", product);
				if (product == null || sku == null) {
					response.setErrors(Errors._41006);
					return response;
				}
				Integer buy_multiple_o2o = sku.getBuy_multiple_o2o();
				if (input.getType() == 1) {
					if (!storeService.isStudentHasStore(student, strBegin.substring(0, 10), begin, end)) {
						response.setErrors(Errors._41021);
						return response;
					}
				}

				// 开始生成订单，并计算价格
				int totalPrice = sku.getPrice() * num;
				int payPrice = totalPrice;
				order = orderService.initOrder(product, sku, customerAddress, num);
				String envname = env.getActiveProfiles()[0];
				String pay_order_id = redisIdGenerator.generatorId("pay_order_id", 1000) + "";
				if ("dev".equals(envname)) {
					pay_order_id = pay_order_id + "_1";
				} else if ("test".equals(envname)) {
					pay_order_id = pay_order_id + "_2";
				}
				order.setPay_order_id(pay_order_id);
				order.setProxyed(1);
				order.setProxyed_uid(student.getStudent_id());
				order.setGroup_tag(oldOlder.getGroup_tag());
				order.setPrice_discount(0);
				order.setPrice_pay(payPrice);
				order.setPrice_total(totalPrice);
				order.setRemark(input.getRemark());
				order.setName(product.getName() + sku.getName());
				order.setClient_id(TOKEN.getClientId());
				orderItem = orderItemService.initOrderItem(order, sku, num);
				Customer customer = customerService.selectByPrimaryKey(customerAddress.getCustomer_id());
				ServiceUnit[] serviceUnits = initServiceUnit(customer, order, input);
				// 进行存储,成功和失败的判断
				orderService.insertSelective(order);
				orderItemService.insertSelective(orderItem);
				Long serviceUnit_id = 0l;
				for (ServiceUnit serviceUnit : serviceUnits) {
					serviceUnit.setStudent_name(student.getName());
					serviceUnit.setC_begin_datetime(begin);
					serviceUnit.setC_end_datetime(end);
					serviceUnit.setPartner_id(student.getPartner_id());
					serviceUnit.setStation_id(student.getStation_id());
					serviceUnitService.insertSelective(serviceUnit);
					if (serviceUnit.getPid() == 0) {
						serviceUnit_id = serviceUnit.getServiceunit_id();
					}
				}

				// 服务单记录
				ServiceunitPerson person = new ServiceunitPerson();
				person.setServiceunit_person_id(IdGenerator.generateId());
				person.setServiceunit_id(serviceUnit_id);
				person.setStudent_id(student.getStudent_id());
				person.setStudent_name(student.getName());
				person.setStatus_active(1);
				serviceunitPersonService.insertSelective(person);
				logger.info("api-method:create_order:process [person = " + person + "]");

				// 发送自动取消的命令
				Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_PAY_TIMEOUT);
				Integer timeOut = metadata.getMeta_value() == null ? Constant.ORDER_PAY_TIMEOUT
						: Integer.parseInt(metadata.getMeta_value());
				Long delayTime = System.currentTimeMillis() + timeOut * 60 * 1000;
				SendResult sendResult = onsHandler.sendCancelMessage(order.getPay_order_id(), delayTime);

				// 占服务人员库存
				if (input.getType() == 1)
					storeService.updateAvilableTimeUnits(student.getStudent_id(), begin, end, StoreServiceImpl.TAKEN);

			}

			// 更新顾客端缓存
			cacheReloadHandler.orderListReload(order.getUid());
			cacheReloadHandler.orderDetailReload(order.getUid(), orderItem.getPay_order_id());

			String sname = student.getName() == null ? student.getPhone() : student.getName();
			// 订单日志
			orderLogService.xInsert(sname, student.getStudent_id(), order.getPay_order_id(), "服务人员【" + sname + "进行了下单操作");
			response.setT(order);

			// 更新服务人员缓存
			cacheReloadHandler.student_order_listReload(student.getStudent_id());
			cacheReloadHandler.selectStuShowTaskdetailReload(student.getStudent_id(), orderItem.getPay_order_id());
			cacheReloadHandler.selectStuUndoneOrderReload(student.getStudent_id());
			cacheReloadHandler.my_employeeManagementReload(student.getPartner_id());
			cacheReloadHandler.productDetailReload(input.getProduct_id());
			// 更新合伙人订单缓存
			cacheReloadHandler.partner_order_detailReload(orderItem.getPay_order_id());
			cacheReloadHandler.partner_order_listReload(student.getPartner_id());
		}catch (Exception e){
			logger.error("ERROR create order error", e);
			response.setErrors(Errors._41040);
		}
		return response;
	}

	/**
	 * 创建订单需要同时创建两个服务单，初始化两个服务单
	 *
	 * @param customer
	 *            顾客对象
	 * @param order
	 *            订单对象
	 * @param input
	 *            订单输入项
	 * @return Array
	 */
	private ServiceUnit[] initServiceUnit(Customer customer, Order order, StudentOrderInput input) {
		Long customer_id = customer.getCustomer_id();
		ServiceUnit serviceUnit = serviceUnit(customer_id, order, input);
		long groupTag = IdGenerator.generateId();
		serviceUnit.setGroup_tag(groupTag);
		ServiceUnit subServiceUnit = serviceUnit(customer_id, order, input);
		subServiceUnit.setGroup_tag(groupTag);
		subServiceUnit.setPid(serviceUnit.getServiceunit_id());
		List<ServiceUnit> list = new ArrayList<>();
		list.add(serviceUnit);
		list.add(subServiceUnit);
		return list.toArray(new ServiceUnit[list.size()]);

	}

	/**
	 * 组装一个服务单
	 *
	 * @param customer_id
	 *            顾客id
	 * @param order
	 *            订单对象
	 * @param orderInput
	 *            订单输入项目
	 * @return ServiceUnit
	 */
	private ServiceUnit serviceUnit(Long customer_id, Order order, StudentOrderInput orderInput) {
		ServiceUnit serviceUnit = new ServiceUnit();
		long serveicUnitId = IdGenerator.generateId();
		serviceUnit.setServiceunit_id(serveicUnitId);
		serviceUnit.setCustomer_id(customer_id);
		serviceUnit.setPay_order_id(order.getPay_order_id());
		serviceUnit.setProduct_id(orderInput.getProduct_id());
		if (!StringUtils.isEmpty(orderInput.getPsku_id())){
			serviceUnit.setPsku_id(orderInput.getPsku_id());
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		serviceUnit.setActive(0);
		serviceUnit.setPid(0l);
		serviceUnit.setStatus_active(1);
		return serviceUnit;

	}

	/**
	 * 服务人员的可预约时间
	 */
	@Override
	public List<TimeModel> productAvailableTimes(Student student, Long psku_id, int num) {
		logger.info("api-method:productAvailableTimes:params student:{},psku_id:{},num:{}", student, psku_id, num);
		ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
		logger.info("api-method:productAvailableTimes:process proSku:{}", proSku);
		// 基础数据不存在，不能计算可用时间
		Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_RESERVE_DAYS_SPAN);
		Integer span = metadata.getMeta_value() == null ? Constant.MAXNUM_RESERVATION_SPAN
				: Integer.parseInt(metadata.getMeta_value());
		List<TimeModel> studentTimeModel = storeService.studentTimeModel(student, span, proSku,
				proSku.getBuy_multiple_o2o() == 1 ? num : 1);
		logger.info("api-method:productAvailableTimes:process studentTimeModel:{}", studentTimeModel.size());
		return studentTimeModel;
	}

	/**
	 * 服务人员取消订单
	 */
	@Transactional(timeout = 3)
	@Override
	public ApiResponse cancelOrder(Student student, String pay_order_id) {
		logger.info("api-method:cancelOrder:params student:{},pay_order_id:{}", student, pay_order_id);
		ApiResponse response = new ApiResponse();
		orderService.xCancelOrderWithoutPay(pay_order_id, null);
		ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
		serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
		ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
		logger.info("api-method:cancelOrder:process serviceUnit:{}", serviceUnit);
		// 删除该学员的库存
		if (!StringUtils.isEmpty(serviceUnit.getC_begin_datetime())){
			storeService.updateAvilableTimeUnits(student.getStudent_id(), serviceUnit.getC_begin_datetime(),
					serviceUnit.getC_end_datetime(), StoreServiceImpl.RELEASE);
		}
		Order order = orderService.selectByPrimaryKey(pay_order_id);
		logger.info("api-method:cancelOrder:process order:{}", order);
		order.setStatus_active(Status.OrderStatus.cancel.value);
		orderService.updateByPrimaryKey(order);

		// 更新缓存
		cacheReloadHandler.orderListReload(order.getUid());
		cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);
		// 更新服务人员缓存
		cacheReloadHandler.student_order_listReload(student.getStudent_id());
		cacheReloadHandler.selectStuShowTaskdetailReload(student.getStudent_id(), pay_order_id);
		cacheReloadHandler.selectStuUndoneOrderReload(student.getStudent_id());
		// 更新合伙人订单缓存
		cacheReloadHandler.partner_order_detailReload(pay_order_id);
		cacheReloadHandler.partner_order_listReload(serviceUnit.getPartner_id());
		cacheReloadHandler.my_mission_scheduled_informationReload(serviceUnit.getPartner_id());
		cacheReloadHandler.my_employeeManagementReload(student.getPartner_id());
		response.setMutationResult(new MutationResult());
		return response;
	}

	/**
	 * 是否有新的消息
	 * @param studentInfo
	 * @return
	 */
	@Override
	public MessageState whetherHaveNewMessages(StudentInfo studentInfo) {
	    MessageState messageState = new MessageState();
		try {
			MessageExample messageExample = new MessageExample();
			List<Long> ids = new ArrayList<>();
			ids.add(studentInfo.getUser_id());
			ids.add(0l);
			messageExample.or()
					.andUser_idIn(ids)
					.andDeletedEqualTo(Status.DeleteStatus.no.value)
					.andNotify_datetimeLessThanOrEqualTo(new Date())
					.andSend_typeEqualTo(1)
					.andApp_typeEqualTo(1)
					.andStatusEqualTo(0);
			long messageNum = messageService.countByExample(messageExample);
            logger.info("api-method:whetherHaveNewMessages:process messageNum:{}", messageNum);
			if (messageNum > 0){
				messageState.setState(1);
			}else {
			    messageState.setState(0);
            }
		}catch (Exception e){
			logger.error("ERROR api-method:whetherHaveNewMessages",e);
		}
		return messageState;
	}

	/**
	 * 统计服务人员订单数
	 * @return
	 */
	@Override
	public StudentServiceOrderStatistics studentStatisticsOrder(StudentInfo studentInfo) {
		try{
			StudentServiceOrderStatistics studentServiceOrderStatistics = new StudentServiceOrderStatistics();
			Long student_id = studentInfo.getStudent_id();
			if(student_id!=null){
				studentServiceOrderStatistics
						.setServicedOrder(getServiceUnitPersons(2, student_id,0,false));//本月已服务订单数
				studentServiceOrderStatistics
						.setDoneOrder(getServiceUnitPersons(4, student_id,Status.ServiceStatus.done.value,false));//本月服务完成订单
				studentServiceOrderStatistics
						.setTodayWaitServiceOrder(getServiceUnitPersons(0, student_id, Status.ServiceStatus.wait_assign_worker.value , true));//今日待服务订单
				studentServiceOrderStatistics
						.setAllWaitServiceOrder(getServiceUnitPersons(0, student_id, Status.ServiceStatus.wait_assign_worker.value,false));//全部待服务订单
				return studentServiceOrderStatistics;
			}
			logger.info("api-method studentStatisticsOrder");
		}catch(Exception e){
			logger.error("error api-method:studentStatisticsOrder",e);
		}
		return null;
	}

	/**
	 * 统计学员订单数
	 * @param work_status
	 * @param student_id
	 * @param status_active
	 * @param is_day
	 * @return
	 */
	private Integer getServiceUnitPersons(int work_status,Long student_id,int status_active,boolean is_day){
		Set<Long> set = null;
		List<ServiceUnit> serviceUnits = null;
		try{
			serviceUnits = new ArrayList<ServiceUnit>();
			ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
			ServiceunitPersonExample.Criteria or = serviceunitPersonExample.or();
			ServiceunitPersonExample serviceunitPersonExample1 = new ServiceunitPersonExample();
			ServiceunitPersonExample.Criteria or1 = serviceunitPersonExample1.or();
			List<ServiceunitPerson> serviceunitPeople1 = new ArrayList<>();
			if(work_status==2){
				int[] status = {2,4};
				or.andWork_statusIn(Arrays.asList(status.length));
			}
			if(work_status==4){
				or.andStatus_activeEqualTo(status_active);
			}
			if(status_active==3){
				or.andStatus_activeEqualTo(status_active);
				or1.andWork_statusIsNull()
						.andStatus_activeEqualTo(status_active)
						.andStudent_idEqualTo(student_id);
				serviceunitPeople1 = serviceunitPersonService.selectByExample(serviceunitPersonExample1);
			}
			or.andStudent_idEqualTo(student_id);
			List<ServiceunitPerson> serviceUnitPersons = serviceunitPersonService.selectByExample(serviceunitPersonExample);
			if(serviceUnitPersons.size()==0){
				return 0;
			}
			set = new HashSet<Long>();
			for (ServiceunitPerson snp:serviceUnitPersons) {
				if(snp!=null)
					set.add(snp.getServiceunit_id());
			}
			if(work_status==0 && !is_day){
				if(serviceunitPeople1.size()!=0){
					return serviceUnitPersons.size()+serviceunitPeople1.size();
				}
				return serviceUnitPersons.size();
			}
			Calendar instance = Calendar.getInstance();
			for (Long serviceunit_id:set) {
				ServiceUnit serviceUnit = serviceUnitService.selectByPrimaryKey(serviceunit_id);
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat sdh = new SimpleDateFormat("yyyy-MM-dd");
				if(serviceUnit!=null){
					if(serviceUnit.getC_begin_datetime()!=null){
						if(is_day) {
							String c_begin_time_sdh = sdh.format(serviceUnit.getC_begin_datetime());
							String local_date_sdh = sdh.format(new Date());
							if (c_begin_time_sdh.equals(local_date_sdh)) {
								instance.setTime(sd.parse(local_date_sdh));
								int day_local = instance.get(Calendar.DAY_OF_MONTH) + 1;//当前时间 /天
								instance.setTime(sd.parse(c_begin_time_sdh));
								int day_service = instance.get(Calendar.DAY_OF_MONTH) + 1;//服务时间 /天
								if (day_local == day_service)
									serviceUnits.add(serviceUnit);
							}
						}else{
							String c_begin_time = sd.format(serviceUnit.getC_begin_datetime());
							String local_date = sd.format(new Date());
							if(local_date.equals(c_begin_time)){
								instance.setTime(sd.parse(local_date));
								int month_local = instance.get(Calendar.MONTH) + 1;//当前时间 /月
								instance.setTime(sd.parse(c_begin_time));
								int month_service = instance.get(Calendar.MONTH)+1;//服务时间 /月
								if(month_local==month_service){
									serviceUnits.add(serviceUnit);
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("error api-method : getServiceUnitPersons",e);
		}
		return serviceUnits.size();
	}


	/**
	 * 视频列表
	 * @param clientId
	 * @return
	 */
	@Override
	public List<VideoContent> studentVideoList(String clientId,int page_index,int count) {
        logger.info("api-method:studentVideoList:params clientId:{}", clientId);
		List<VideoContent> videoContents = new ArrayList<>();
		try {
			String[] split = clientId.split("_");
			String clientIdnew = split[split.length - 1];
			VideoContentExample videoContentExample = new VideoContentExample();
			videoContentExample.or().andClient_typeEqualTo(clientIdnew)
					.andVideo_upload_statusEqualTo(2)
					.andOnlineEqualTo(1);
			videoContentExample.setOrderByClause(VideoContentExample.C.order_num + " DESC");
			//查询该人员的视频列表
            videoContents = videoContentService.selectByExample(videoContentExample,page_index,count).getList();
            logger.info("api-method:studentVideoList:process videoContents:{}", videoContents.size());
			videoContents.stream().forEach(t ->{
                Img imgUrl = JSON.parseObject(t.getImg_url(), Img.class);
                t.setImg_url(imgUrl.getUrl());
                Img videoUrl = JSON.parseObject(t.getVideo_url(), Img.class);
                t.setVideo_url(videoUrl.getUrl());
            });
		}catch (Exception e){
			logger.error("ERROR api-method:studentVideoList",e);
		}
		return videoContents;
	}

    /**
     * 服务人员解绑
     * @param studentInfo
     * @return
     */
    @Override
    public ApiResponse removeTheBing(StudentInfo studentInfo) {
        ApiResponse response = new ApiResponse();
        if (!StringUtils.isEmpty(studentInfo)){
            cacheReloadHandler.customerInfoReload(studentInfo.getUser_id());
            StudentInfo info = new StudentInfo();
            info.setStudent_id(studentInfo.getStudent_id());
            info.setUser_id(0l);
            studentService.updateByPrimaryKeySelective(info);
        }else {
            response.setErrors(Errors._40111);
            return response;
        }
        response.setMutationResult(new MutationResult());
        return response;
    }

}
