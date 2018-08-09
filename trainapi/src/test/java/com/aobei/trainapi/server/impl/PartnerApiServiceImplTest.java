package com.aobei.trainapi.server.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.util.JsonUtil;
import custom.bean.ProductTag;
import custom.bean.TransmissionContent;
import custom.util.ParamsCheck;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import com.aliyun.openservices.ons.api.SendResult;
import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.SmsSendEvent;
import com.aobei.train.IdGenerator;
import com.aobei.trainapi.server.PartnerApiService;
import com.aobei.trainapi.server.bean.AccumulatedOrdersMonth;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.EmployeeManagement;
import com.aobei.trainapi.server.bean.MessageContent;
import com.aobei.trainapi.server.bean.StudentServiceunit;
import com.aobei.trainapi.server.bean.StudentType;
import com.aobei.trainapi.server.handler.OnsHandler;
import com.aobei.trainapi.server.handler.SmsHandler;
import com.aobei.trainapi.util.JacksonUtil;

import custom.bean.Constant;
import custom.bean.OrderInfo;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class PartnerApiServiceImplTest {

	@Autowired
	PartnerApiService partnerApiService;
	@Autowired
	EventPublisher publisher;
	@Autowired
	SmsHandler smsHandLer;
	@Autowired
	ServiceUnitService serviceUnitService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderLogService orderLogService;
	@Autowired
	RedisService redisService;
	 @Autowired
	 MetadataService metadataService;
	 @Autowired
	 OnsHandler onsHandler;
	 @Autowired
	 MessageService messageService;
	 @Autowired
	ProductService productService;
	 @Autowired
	 AppGrowthService appGrowthService;
	 @Autowired
	 PartnerService partnerService;
	 @Autowired
	StringRedisTemplate stringRedisTemplate;
	 

	@Test
	public void partnerInfoByUserId() {
		Partner partner1 = partnerApiService.partnerInfoByUserId(100000000000000l);
		Assert.assertNull(partner1);
		Partner partner2 = partnerApiService.partnerInfoByUserId(1075168748123299840l);
		Assert.assertNotNull(partner2);
		Assert.assertEquals(partner2.getName(), "经海路锋创家政");
		Assert.assertEquals(partner2.getAddress(), "经海路地铁站右转200m");
		Assert.assertEquals(partner2.getCode(), "432423423423423333");
	}

	/**
	 * 获取员工信息
	 */
	@Test
	public void my_employeeManagement() {
		Long partner_id = 1067671915911208960l;
		Partner partner = partnerService.selectByPrimaryKey(partner_id);
		List<EmployeeManagement> list = partnerApiService.my_employeeManagement(partner, 1, 100);
		Assert.assertEquals(list.size(), 10);
		EmployeeManagement employeeManagement = list.get(0);
		// Assert.assertNull(employeeManagement.getStation());
		/*
		 * Station station = employeeManagement.getStation();
		 * Assert.assertEquals(station.getPhone(),"15313882039");
		 */
		Assert.assertNotNull(employeeManagement.getStudent());
		Student student = employeeManagement.getStudent();
		Assert.assertEquals(student.getName(), "赵丽");
		List<Serviceitem> list2 = employeeManagement.getServiceitem();
		Assert.assertEquals(list2.size(), 4);
	}

	/**
	 * 员工详情
	 * 
	 * @param
	 * @return
	 */
	@Test
	public void partnerEmployeeDetail() {
		Long student_id = 1067677206111346688l;
		Partner p = new Partner();
		EmployeeManagement emp = partnerApiService.partnerEmployeeDetail(p, student_id);
		Assert.assertNotNull(emp);
		Assert.assertNotNull(emp.getStudent());
		Assert.assertEquals(emp.getStudent().getName(), "王美兰");
		Assert.assertNotNull(emp.getStation());
		Assert.assertEquals(emp.getStation().getUsername(), "刘永强");
		Assert.assertNotNull(emp.getServiceitem());
		List<Serviceitem> list = emp.getServiceitem();
		Serviceitem serviceitem = list.get(0);
		String name = serviceitem.getName();
		Assert.assertEquals(name, "地毯清洗");

	}

	/**
	 * 我的任务排期
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Test
	public void my_mission_scheduled_information() throws Exception {
		Long partner_id = 1067670088150966272l;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = "2018-03-15 10:00:00";
		Date date = sdf.parse(str);
		int page_index = 1;
		int count = 10;
		List<StudentServiceunit> list = partnerApiService.my_mission_scheduled_information(partner_id, date, page_index,
				count);
		Assert.assertNotNull(list);
		StudentServiceunit serviceunit = list.get(0);
		Student student = serviceunit.getStudent();
		Assert.assertNotNull(student);
		Assert.assertEquals(student.getName(), "毛老汉");
		List<OrderInfo> orderList = serviceunit.getOrderList();
		Assert.assertEquals(orderList.size(), 0);
	}

	/**
	 * 本月累计订单
	 */
	@Test
	public void Accumulated_orders_month() {
		Long partner_id = 1067670088150966272l;
		AccumulatedOrdersMonth ao = partnerApiService.Accumulated_orders_month(partner_id);
		Long count2 = 0l;
		Long count1 = ao.getCount();
		Assert.assertNotNull(count1);
		Assert.assertEquals(count1, count2);
	}

	/**
	 * 店铺信息(虚拟地址)
	 * 
	 * @param
	 * @return
	 */
	@Test
	public void Store_information() {
		Long partner_id = 1067670088150966272l;
		List<Station> list = partnerApiService.Store_information(partner_id);
		/* Assert.assertNotNull(list); */
		Assert.assertEquals(list.size(), 2);
		Station station = list.get(0);
		String address = "北京市 北京市 大兴区 34";
		Integer city = 110100;
		Assert.assertEquals(station.getAddress(), address);
		Assert.assertEquals(station.getCity(), city);
	}

	/**
	 * 获取订单列表
	 * 
	 * @return
	 */
	@Test
	public void partner_order_list() {
		String status = null;
		Long partner_id = 1067671915911208960l;
		Integer page_index = 1;
		Integer count = 10;
		List<OrderInfo> list = partnerApiService.partner_order_list(status, partner_id, page_index, count);
		Assert.assertEquals(list.size(), 10);
		OrderInfo info = list.get(0);
		Assert.assertEquals(info.getPay_order_id(), "1068641304966258688699648");
		Assert.assertEquals(info.getName(), "地毯清洗纯棉地毯");
	}

	/**
	 * 获取订单详情
	 */
	@Test
	public void partner_order_detail() {
		String pay_order_id = "1068641304966258688699648";
		Partner partner = new Partner();
		OrderInfo info = partnerApiService.partner_order_detail(partner,pay_order_id);
		Assert.assertNotNull(info);
		Assert.assertEquals(info.getName(), "地毯清洗纯棉地毯");
		Long cus = 1068620482469879808l;
		Assert.assertEquals(info.getCustomer_address_id(), cus);
		Assert.assertEquals(info.getCustomer_address(), "北京市 北京市 大兴区 科创十三街18号院5号楼13层");
		Assert.assertEquals(info.getPartner_name(), "经海路锋创家政");
		Assert.assertEquals(info.getStudent_name(), "王美兰");

	}

	/**
	 * 查看可进行指派的服务人员信息
	 * 
	 * @param
	 * @param
	 */
	@Test
	public void partnerFindAvailableStudent() {
		String pay_order_id = "1072879497335644160668224";
		Partner p = new Partner();
		List<StudentType> list = partnerApiService.partnerFindAvailableStudent(p, pay_order_id);
		Assert.assertEquals(list.size(), 4);
		StudentType student = list.get(0);
		Assert.assertEquals(student.getStudent().getPhone(), "15313890729");
		Assert.assertEquals(student.getStudent().getName(), "毛老汉");
		Long student_id = 1055478991391072256l;
		Assert.assertEquals(student.getStudent().getStudent_id(), student_id);
	}

	/**
	 * 拒绝接单
	 * 
	 * @param
	 * @return
	 */
	@Test
	public void partnerRefusedOrder() {
		String pay_order_id = "1068643681671421952668224";
		String orderStr = "身体不适";
		Partner p = new Partner();
		ApiResponse res = partnerApiService.partnerRefusedOrder(p, pay_order_id, orderStr);
		if (res.getMutationResult() == null) {
			Assert.assertNull(res.getMutationResult());
		} else {
			int i = res.getMutationResult().getStatus();
			Assert.assertEquals(i, 0);
		}
	}

	/**
	 * 待服务状态下修改
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@Test
	public void partnerAlterOrder() {
		String pay_order_id = "1068643681671421952668224";
		List<Long> student_id = new ArrayList<>();
		student_id.add(1067677206111346688l);
		Partner p = new Partner();
		ApiResponse api = partnerApiService.partnerAlterOrder(p, pay_order_id, student_id);
		if (api.getMutationResult() == null) {
			Assert.assertNull(api.getMutationResult());
		} else {
			int i = api.getMutationResult().getStatus();
			Assert.assertEquals(i, 0);
		}

	}

	/**
	 * 合伙人接单
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@Test
	public void partnerConfirmOrder() {
		String pay_order_id = "1068641304966258688699648";
		List<Long> student_id = new ArrayList<>();
		student_id.add(1067677206111346688l);
		Partner p = new Partner();
		ApiResponse api = partnerApiService.partnerConfirmOrder(p, pay_order_id, student_id);
		if (api.getMutationResult() == null) {
			Assert.assertNull(api.getMutationResult());
		} else {
			int i = api.getMutationResult().getStatus();
			Assert.assertEquals(i, 0);
			OrderInfo info = partnerApiService.partner_order_detail(p,pay_order_id);
			String name = info.getStudent_name();
			Assert.assertEquals(name, "王美兰");
		}
	}

	/**
	 * 查询消息列表
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@Test
	public void partnerMessageInfo() {
		Long partner_id = 1067671915911208960l;
		Partner partner = partnerService.selectByPrimaryKey(partner_id);
		Integer page_index = 1;
		Integer count = 10;
		List<MessageContent> list = partnerApiService.partnerMessageInfo(partner, page_index, count);
		Assert.assertEquals(list.size(), 1);
		Message message = list.get(0);
		Long id = message.getId();
		Assert.assertEquals(message.getId(), id);
		Assert.assertEquals(message.getMsg_title(), "订单通知");
		Assert.assertEquals(message.getGroup_id(), "1072060843582218240699648-1067671915911208960-1072060843708047361");
	}

	/**
	 * 学员停止接单
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	@Test
	public void partnerStopOrder() {
		Long student_id = 1067677206111346688l;
		String dateTime = "2018-03-17";
		Long station_id = 1l;
		String start = null;
		String end = null;
		Partner p = new Partner();
		ApiResponse result = partnerApiService.partnerStopOrder(p, student_id, dateTime, start, end, true);

	}

	@Test
	public void partnerSend() {
		SmsData data = new SmsData();
		data.setPhoneNumber(new String[] { "17635065357" });
		data.setSignName(Constant.SIGN_NAME);
		data.setTemplateCode(Constant.SEND_TO_WORK_WHEN_ORDER_ASSIGN);
		data.addParam("product_name", "生孩子");
		data.addParam("cus_username", "非洲老黑");
		data.addParam("cus_address", "浪漫的土耳其");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		data.addParam("c_begin_datetime", format.format(new Date()));
		data.addParam("cus_phone", "17635065357");
		SmsSendEvent event = new SmsSendEvent(this, data);
		publisher.publish(event);
	}

	@Test
	public void partnerSendTo() {
		String name = "XXOO";
		String username = "你吃了吗?";
		String address = "东京";
		String date = "2018-05-02 05:02";
		String cusphone = "15010543876";
		String phone = "17635065357";
		String stuName = "狗蛋";
		smsHandLer.sendToWorkWhenOrderAssign(name, stuName, address, date, cusphone, phone);
	}
	
	@Test
	public void orderlog(){
		ServiceUnit unit = serviceUnitService.selectByPrimaryKey(1068643682308956160l);
		//unit.setServiceunit_id(IdGenerator.generateId());
		unit.setCustomer_id(31231242112411l);
		unit.setPay_order_id("432423423452");
		unit.setProduct_id(4242343242342l);
		unit.setPsku_id(42343243245325233l);
		unit.setPid(0l);
		unit.setGroup_tag(IdGenerator.generateId());
		unit.setStatus_active(2);
		serviceUnitService.updateByPrimaryKeySelective(unit);
		unit.setPid(unit.getServiceunit_id());
		serviceUnitService.insertSelective(unit);
	}
	
	@Test
	public void partnerRobberies(){
		Long partner_id = 1067670088150966272l;
		Integer page_index = 1;
		Integer count = 10;
		List<OrderInfo> list = partnerApiService.partnerRobberies(partner_id, page_index, count);
		Assert.assertNotNull(list);
		
	}
	
	@Test
	public void partnerOrderRobbing(){
		Long partner_id = 1067670088150966272l;
		Partner partner = partnerService.selectByPrimaryKey(partner_id);
		String pay_order_id= "14710784668224";
		List<Long> student_ids = new ArrayList<>();
		student_ids.add(1040850221208985600l);
		ApiResponse response = partnerApiService.partnerOrderRobbing(partner, pay_order_id, student_ids);
		int status = response.getMutationResult().getStatus();
		Assert.assertEquals(status, 0);
	}
	
	@Test
	public void haha(){
		Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_PAY_TIMEOUT);
        Integer timeOut = metadata.getMeta_value() == null ? Constant.ORDER_PAY_TIMEOUT
                : Integer.parseInt(metadata.getMeta_value());
        Long delayTime = System.currentTimeMillis() + timeOut * 60 * 1000;
        SendResult sendResult = onsHandler.sendCancelMessage("52807680716160", delayTime);
        if(sendResult.getMessageId() == null){
        	System.out.println("yes");
        }
	}
	
	@Test
	public void test2(){
		Message mes = new Message();
		mes.setId(IdGenerator.generateId());
		mes.setType(2);
		mes.setBis_type(3);//合伙人端
		mes.setUser_id(1140145971255091200l);
		mes.setUid(1135912592396460032l);
		mes.setMsg_title("订单取消通知哈哈");
		MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginTimeStr = "2018-08-10 10:00:00";
		contentMsg.setMsgtype("native");
		contentMsg.setContent("您于"+beginTimeStr+"进行服务的订单已取消");
		Map<String,String> param = new HashMap<>();
		param.put("pay_order_id","1536159070_2");
		param.put("orderStatus","cancel");
		TransmissionContent tContent = new TransmissionContent(TransmissionContent.PARTNER,TransmissionContent.ORDER_DETAIL,param);
		contentMsg.setHref(tContent.getHrefNotEncode());
		contentMsg.setTypes(1);//纯文本
		contentMsg.setNoticeTypes(3);//订单取消类型
		contentMsg.setTitle("订单取消通知");
		if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
			Errors._41040.throwError("消息长度过长");
		}
		String json = JSON.toJSONString(contentMsg);
		mes.setMsg_content(json);
		mes.setCreate_datetime(new Date());
		mes.setNotify_datetime(new Date());
		mes.setGroup_id("1536159070_2");
		mes.setSend_type(1);//站内类型
		mes.setApp_type(3);//合伙人员端
		mes.setApp_platform(0);//所有平台
		messageService.insertSelective(mes);
		
	}

	@Test
	public void tagImagesTest(){
		Product pro = productService.selectByPrimaryKey(1059702439282499584l);
		ProductTag images = new ProductTag();
        ProductTag images2 = new ProductTag();
        images2.setId("1055603515533844480");
        images2.setTag("测试");
        images2.setUrl("http://aobei-test-public.oss-cn-beijing.aliyuncs.com/image/product/logo/2018/2/24/1055603513159868416.jpg");
		List<ProductTag> is = new ArrayList<>();
		images.setId("1055603515533844480");
		images.setTag("测试");
		images.setUrl("http://aobei-test-public.oss-cn-beijing.aliyuncs.com/image/product/logo/2018/2/24/1055603513159868416.jpg");
		is.add(images);
		is.add(images2);
        String s = JsonUtil.toJSONString(is);
        pro.setTag_images(s);
		productService.updateByPrimaryKey(pro);
	}

	@Test
	public void app_growthtest(){
		AppGrowth app =new AppGrowth();
		app.setId(1111111111);
		app.setApp_pack_id("wx_m_custom");
		app.setCurrent_version("1.5");
		app.setGrowth_version("1.2");
		app.setCreate_datetime(new Date());
		app.setDescr_version("你升级呀,升级呀,升级呀升级呀,升级呀");
		appGrowthService.insertSelective(app);
	}

	@Test
	public void robbingDetail(){
		String pay_order_id = "1527602394_1";
		Partner partner = partnerService.selectByPrimaryKey(1067670088150966272l);
		OrderInfo orderInfo = partnerApiService.partnerRobbingDetail(partner, pay_order_id);
		Assert.assertNotNull(orderInfo);
		Assert.assertEquals(orderInfo.getOrderStatus(),"waitRobbing");
	}

	@Test
	public void redisTest(){
		/*final String REDIS_KEY_COUPONDIS = "couponList"+3;
		//stringRedisTemplate.opsForSet().add(REDIS_KEY_COUPONDIS,"123");
		stringRedisTemplate.opsForList().leftPush(REDIS_KEY_COUPONDIS,"987");
		stringRedisTemplate.opsForList().leftPush(REDIS_KEY_COUPONDIS,String.valueOf(32423423));*/
		final String REDIS_KEY_COUPONDIS = "couponDistributed_userId";
		Long user_id = 1169135518328332288l;
		Long lo = stringRedisTemplate.opsForList().leftPush(REDIS_KEY_COUPONDIS, String.valueOf(user_id));
		System.out.println(lo);

	}

}
