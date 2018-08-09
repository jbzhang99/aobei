package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aobei.common.boot.EventPublisher;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.model.ServiceUnitExample.Criteria;
import com.aobei.train.service.*;
import com.aobei.train.service.impl.StoreServiceImpl;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.ApiService;
import com.aobei.trainapi.server.PartnerApiService;
import com.aobei.trainapi.server.bean.*;
import com.aobei.trainapi.server.bean.Img;
import com.aobei.trainapi.server.bean.MessageContent;
import com.aobei.trainapi.server.handler.InStationHandler;
import com.aobei.trainapi.server.handler.PushHandler;
import com.aobei.trainapi.server.handler.SmsHandler;
import com.aobei.trainapi.server.listener.AutoRobbingOrderListener;
import com.aobei.trainapi.util.JacksonUtil;
import com.aobei.trainapi.util.MyFileHandleUtil;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.*;
import custom.bean.OrderInfo.OrderStatus;
import custom.util.DateUtil;
import custom.util.ParamsCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
public class PartnerApiServiceImpl implements PartnerApiService {

	@Autowired
	private PartnerService partnerService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private StationService stationService;

	@Autowired
	private OssImgService ossImgService;
	@Autowired
	private ServiceUnitService serviceUnitService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerAddressService customerAddressService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	StringRedisTemplate redisTemplate;
	@Autowired
	MessageService messageService;
	@Autowired
	RedisService redisService;
	@Autowired
	StudentServiceitemService studentServiceitemService;
	@Autowired
	ServiceitemService serviceitemService;
	@Autowired
	CustomProperties properties;
	@Autowired
	EventPublisher publisher;
	@Autowired
	MetadataService metadataService;
	@Autowired
	ServiceunitPersonService serviceunitPersonService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	RobbingService robbingService;
	@Autowired
	StoreService storeService;
	@Autowired
	AutoRobbingOrderListener autoRobbingOrderListener;
	@Autowired
	ProductSoleService productSoleService;
	@Autowired
	SmsHandler smsHandler;
	@Autowired
    CacheReloadHandler cacheReloadHandler;
	@Autowired
	OutOfServiceService outOfServiceService;
	@Autowired
	MyFileHandleUtil myFileHandleUtil;
	@Autowired
	ApiService apiService;
	@Autowired
	PushHandler pushHandler;
	@Autowired
    ApiOrderService apiOrderService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    RejectRecordService rejectRecordService;

    @Autowired
    InStationHandler inStationHandler;

	Logger logger = LoggerFactory.getLogger(PartnerApiServiceImpl.class);


	/**
	 * 获取合伙人信息
	 *
	 * @param user_id
	 * @return
	 */
	@Cacheable(value = "partnerInfoByUserId", key = "'user_id:'+#user_id", unless = "#result == null")
	@Override
	public Partner partnerInfoByUserId(Long user_id) {
	    try{
            logger.info("api-method:partnerInfoByUserId:params user_id:{}", user_id);
            if (user_id == 0 || user_id == null) {
                Errors._40111.throwError();
            }
            PartnerExample partnerExample = new PartnerExample();
            partnerExample.or().andUser_idEqualTo(user_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
            List<Partner> example = partnerService.selectByExample(partnerExample);
            logger.info("api-method:partnerInfoByUserId:process example:{}", example);
            Partner partner = singleResult(example);
            if(partner!=null && partner.getLogo_img() != null){
                OssImg ossImg = ossImgService.selectByPrimaryKey(Long.valueOf(partner.getLogo_img()));
                if(ossImg!=null) {
                    String logUrl = myFileHandleUtil.get_signature_url(ossImg.getUrl(),
                            3600l);
                    partner.setLogo_img(logUrl);
                }
            }
            return partner;
        }catch (Exception e ){
	        logger.error("ERROR api-method:partnerInfoByUserId",e);
	        return  null;
        }

	}

	/**
	 * 绑定合伙人
	 */
	@Override
	public Partner partnerByPhoneAndLinkman(String phone, String linkman) {
	    try{
            logger.info("api-method:partnerByPhoneAndLinkman:params phone:{},linkman:{}", phone, linkman);
            if (StringUtils.isEmpty(phone)) {
                Errors._42015.throwError();
            }
            if (StringUtils.isEmpty(linkman)) {
                Errors._42016.throwError();
            }
            PartnerExample partnerExample = new PartnerExample();
            partnerExample.or().andPhoneEqualTo(phone).andLinkmanEqualTo(linkman)
                    .andDeletedEqualTo(Status.DeleteStatus.no.value);
            Partner partner = singleResult(partnerService.selectByExample(partnerExample));
            if(partner==null){
                return null;
            }
            // 更新合伙人缓存
            cacheReloadHandler.partnerInfoByUserIdReload(partner.getUser_id());
            logger.info("api-method:partnerByPhoneAndLinkman:process partner:{}", partner);
            return partner;
        }catch (Exception e ){
            logger.error("ERROR api-method:partnerByPhoneAndLinkman",e);
	         return null;
        }

	}

	/**
	 * 合伙人
	 */
	@Transactional(timeout = 2)
	public int bindPartner(Long user_id, Long partner_id) {
		logger.info("api-method:bindPartner:params user_id:{},partner_id:{}", user_id, partner_id);
		Partner partner = new Partner();
		partner.setPartner_id(partner_id);
		partner.setUser_id(user_id);
		logger.info("api-method:bindPartner:process partner:{}", partner);
		int count = partnerService.updateByPrimaryKeySelective(partner);
		if (count > 0) {
			userAddRole(user_id, Roles.PARTNER_USER.roleName());
		}
		return count;
	}

	/**
	 * 合伙人用户
	 */
	@Override
	public int userAddRole(Long user_id, String roleName) {
		logger.info("api-method:userAddRole:params user_id:{},roleName:{}", user_id, roleName);
		UsersExample usersExample = new UsersExample();
		usersExample.or().andUser_idEqualTo(user_id);
		usersExample.includeColumns(UsersExample.C.user_id, UsersExample.C.roles);
		// 更新用户角色
		Users users = singleResult(usersService.selectByExample(usersExample));
		String[] roles = (users.getRoles() == null ? roleName : users.getRoles()).split(",");
		Optional<String> optional = Stream.of(roles).filter(n -> n.equals(roleName)).findAny();
		if (!optional.isPresent()) {
			roles = StringUtils.addStringToArray(roles, roleName);
		}
		users.setRoles(StringUtils.arrayToCommaDelimitedString(roles));
		return usersService.updateByPrimaryKeySelective(users);
	}

	/**
	 * 员工信息管理
	 */
	@Cacheable(value = "my_employeeManagement", key = "'partner_id:'+#partner.partner_id+':'+#page_index+':'+#count", unless = "#result == null")
	@Override
	public List<EmployeeManagement> my_employeeManagement(Partner partner, int page_index, int count) {
		logger.info("api-method:my_employeeManagement:params partner:{},page_index:{},count:{}", partner, page_index,
				count);
		// 根据合伙人id查询学员
		StudentExample studentExample = new StudentExample();
		studentExample.or().andPartner_idEqualTo(partner.getPartner_id()).andStateEqualTo(1);
		Page<Student> page = studentService.selectByExample(studentExample, page_index, count);
		List<Student> list = page.getList();
		return list.stream().map(n -> {
			EmployeeManagement employeeManagement = new EmployeeManagement();
			//StudentInfo studentInfo = apiService.studentInfoByUserId(n.getUser_id());
			StudentInfo info =new StudentInfo();
			// 头像url
			if (n.getLogo_img() != null) {
				OssImg ossImg = ossImgService.selectByPrimaryKey(Long.valueOf(n.getLogo_img()));
				String logUrl = myFileHandleUtil.get_signature_url(ossImg.getUrl(),
						3600l);
				n.setLogo_img(logUrl);
			}
			BeanUtils.copyProperties(n,info);
			employeeManagement.setStudent(info);
			employeeManagement.setPartner(partner);
			StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
			studentServiceitemExample.or().andStudent_idEqualTo(n.getStudent_id()).andStatusEqualTo(1);
			List<StudentServiceitem> itemList = studentServiceitemService.selectByExample(studentServiceitemExample);

			List<Long> ids = new ArrayList<>();
			itemList.stream().forEach(t->{
				ids.add(t.getServiceitem_id());
			});
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			ServiceitemExample.Criteria criter = serviceitemExample.or();
			if (ids.size() != 0) {
				criter.andServiceitem_idIn(ids);
			} else {
				criter.andServiceitem_idIsNull();
			}
			criter.andStateEqualTo(1);
			List<Serviceitem> serviceitems = serviceitemService.selectByExample(serviceitemExample);
			if (serviceitems.size() == 0) {
				List<Serviceitem> list4 = new ArrayList<>();
				employeeManagement.setServiceitem(list4);
			} else {
				employeeManagement.setServiceitem(serviceitems);
			}

			StationExample stationExample = new StationExample();
			StationExample.Criteria criteria = stationExample.or();
			if (n.getStation_id() != null) {
				criteria.andStation_idEqualTo(n.getStation_id());
				criteria.andDeletedEqualTo(Status.DeleteStatus.no.value);
				List<Station> example = stationService.selectByExample(stationExample);
				Station station = singleResult(example);
				employeeManagement.setStation(station);
			} else {
				employeeManagement.setStation(new Station());
			}
			// 根据学员头像id查询路径
			employeeManagement.setOssImage(getImage(n.getLogo_img()));
			logger.info("api-method:my_employeeManagement:process employeeManagement:{}", employeeManagement);
			return employeeManagement;
		}).collect(Collectors.toList());

	}

    /**
     * 员工详情
     */
    // @Cacheable(value = "partnerEmployeeDetail",key =
    // "'partner_id:'+#partner.partner_id+':student_id:'+#student_id",unless =
    // "#result == null")
    @Override
    public EmployeeManagement partnerEmployeeDetail(Partner partner, Long student_id) {
        logger.info("api-method:partnerEmployeeDetail:params student_id:{}", student_id);
        EmployeeManagement emp = new EmployeeManagement();
        Student student = studentService.selectByPrimaryKey(student_id);
        StudentInfo info = new StudentInfo();
        // 头像url
        if (student.getLogo_img() != null) {
            OssImg ossImg = ossImgService.selectByPrimaryKey(Long.valueOf(student.getLogo_img()));
            String logUrl = myFileHandleUtil.get_signature_url(ossImg.getUrl(),
                    3600l);
            student.setLogo_img(logUrl);
        }
        BeanUtils.copyProperties(student, info);
        emp.setStudent(info);
        Long partner_id = student.getPartner_id();
        emp.setPartner(partner);
        StationExample stationExample = new StationExample();
        if (student.getStation_id() != null) {
            stationExample.or().andStation_idEqualTo(student.getStation_id());
            Station station = singleResult(stationService.selectByExample(stationExample));
            emp.setStation(station);
        }
        StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
        studentServiceitemExample.or().andStudent_idEqualTo(student.getStudent_id())
                .andStatusEqualTo(Status.OnlineStatus.online.value);
        List<StudentServiceitem> list2 = studentServiceitemService.selectByExample(studentServiceitemExample);
        List<Long> ids = new ArrayList<>();
        for (StudentServiceitem studentitem : list2) {
            ids.add(studentitem.getServiceitem_id());
        }
        ServiceitemExample serviceitemExample = new ServiceitemExample();
        ServiceitemExample.Criteria criteria = serviceitemExample.or();
        if (ids.size() != 0) {
            criteria.andServiceitem_idIn(ids);
        } else {
            criteria.andServiceitem_idIsNull();
        }
        criteria.andStateEqualTo(1);
        List<Serviceitem> list3 = serviceitemService.selectByExample(serviceitemExample);
        if (list3.size() == 0) {
            List<Serviceitem> list4 = new ArrayList<>();
            emp.setServiceitem(list4);
        } else {
            emp.setServiceitem(list3);
        }
        // 根据学员头像id查询路径
        emp.setOssImage(getImage(student.getLogo_img()));
        //学员是否可以修改服务站
        ServiceunitPersonExample personExample = new ServiceunitPersonExample();
        personExample.or().andStudent_idEqualTo(student_id).andStatus_activeEqualTo(Status.OrderStatus.wait_service.value);
        List<ServiceunitPerson> list = serviceunitPersonService.selectByExample(personExample);
        if (list.size() > 0){
            emp.setStationStatus(1);
        }else {
            emp.setStationStatus(0);
        }
        logger.info("api-method:partnerEmployeeDetail:process emp:{}", emp);
        return emp;
    }

    private OssImg getImage(String imgId) {
        logger.info("api-method:getImage:params imgId:{}", imgId);
        if (org.apache.commons.lang3.StringUtils.isNumeric(imgId)) {
            long img_id = Long.parseLong(imgId);
            OssImg ossImg = ossImgService.selectByPrimaryKey(img_id);
            logger.info("api-method:getImage:process ossImg:{}", ossImg);
            return ossImg;
        }
        return new OssImg();
    }

    /**
     * 任务排期
     */
    //@Cacheable(value = "my_mission_scheduled_information", key = "'partner_id:'+#partner_id+':datevalue:'+#dataValue+':'+#page_idex+':'+#count", unless = "#result == null")
    @Override
    public List<StudentServiceunit> my_mission_scheduled_information(Long partner_id, Date dataValue, int page_index,
                                                                     int count) {
        logger.info(
                "api-method:my_mission_scheduled_information:params partner_id:{},dataValue:{},page_index:{},count:{}",
                partner_id, dataValue, page_index, count);
        List<StudentServiceunit> orderList = new ArrayList<>();
        // 所有该合伙人下的学员信息
        StudentExample studentExample = new StudentExample();
        studentExample.or().andPartner_idEqualTo(partner_id).andStateEqualTo(Status.OnlineStatus.online.value)
                .andDeletedEqualTo(Status.DeleteStatus.no.value);
        Page<Student> page = studentService.selectByExample(studentExample, page_index, count);
        List<Student> studentList = page.getList();
        if (studentList.size() == 0) {
            return new ArrayList<StudentServiceunit>();
        }
        for (Student student : studentList) {
            StudentServiceunit studentUnit = new StudentServiceunit();
            List<Integer> ids = new ArrayList<>();
            ids.add(Status.ServiceStatus.assign_worker.value);
            ids.add(Status.ServiceStatus.done.value);

            List<Integer> statusIds = new ArrayList<>();
            statusIds.add(Status.OrderStatus.wait_service.value);
            statusIds.add(Status.OrderStatus.done.value);
            // 查询学员服务单
            ServiceunitPersonExample personExample = new ServiceunitPersonExample();
            personExample.or().andStudent_idEqualTo(student.getStudent_id()).andStatus_activeIn(statusIds);
            List<ServiceunitPerson> list = serviceunitPersonService.selectByExample(personExample);
            List<Long> unitIds = new ArrayList<>();
            for (ServiceunitPerson person : list) {
                unitIds.add(person.getServiceunit_id());
            }
            if (unitIds.size() == 0) {
                studentUnit.setOrderList(new ArrayList<OrderInfo>());
                studentUnit.setStudent(student);
                orderList.add(studentUnit);
            } else {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andServiceunit_idIn(unitIds).andActiveEqualTo(Status.PayStatus.payed.value)
                        .andPidEqualTo(0l).andStatus_activeIn(ids);
                List<ServiceUnit> serviceUnitList = serviceUnitService.selectByExample(serviceUnitExample);
                if (serviceUnitList.size() == 0) {
                    studentUnit.setOrderList(new ArrayList<OrderInfo>());
                } else {
                    List<OrderInfo> orderInfos = new ArrayList<>();
                    for (ServiceUnit unit : serviceUnitList) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String dateStr = format.format(dataValue);
                        Date beginValue = unit.getC_begin_datetime();
                        String beginStr = format.format(beginValue);
                        if (dateStr.equals(beginStr)) {
                            OrderInfo orderInfo = new OrderInfo(Roles.STUDENT);
                            Order order = orderService.selectByPrimaryKey(unit.getPay_order_id());
                            orderInfo.setOrder(order);
                            orderInfo.setServiceUnit(unit);
                            orderInfo.getdServiceStartTime();
                            orderInfos.add(orderInfo);
                        }
                    }
                    studentUnit.setOrderList(orderInfos);
                }
                studentUnit.setStudent(student);
                orderList.add(studentUnit);
            }

        }
        logger.info("api-method:my_mission_scheduled_information:process orderList:{}", orderList.size());
        return orderList;
    }

    /**
     * 本月累计订单
     */
    @Override
    public AccumulatedOrdersMonth Accumulated_orders_month(Long partner_id) {
        logger.info("api-method:Accumulated_orders_month:params partner_id:{}", partner_id);
        AccumulatedOrdersMonth accumulated = new AccumulatedOrdersMonth();
        LocalDateTime today = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localFirstTime = today.with(TemporalAdjusters.firstDayOfMonth()).plusDays(0).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime localLastTime = today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zonedFirstTime = localFirstTime.atZone(zoneId);
        ZonedDateTime zonedLastTime = localLastTime.atZone(zoneId);
        Date first = Date.from(zonedFirstTime.toInstant());
        Date last = Date.from(zonedLastTime.toInstant());
        // 查询 服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPartner_idEqualTo(partner_id).andActiveEqualTo(Status.ServiceUnitActive.active.value)
                .andStatus_activeEqualTo(Status.ServiceStatus.done.value).andPidEqualTo(0l)
                .andFinish_datetimeLessThanOrEqualTo(last).andFinish_datetimeGreaterThanOrEqualTo(first);
        List<ServiceUnit> list = serviceUnitService.selectByExample(serviceUnitExample);
        Long count = (long) list.size();
        accumulated.setCount(count);
        logger.info("api-method:Accumulated_orders_month:process accumulated:{}", accumulated.getCount());
        return accumulated;
    }

    /**
     * 店铺信息(服务站点)
     */
    @Cacheable(value = "Store_information", key = "'partner_id:'+#partner_id", unless = "#result == null")
    @Override
    public List<Station> Store_information(Long partner_id) {
        logger.info("api-method:Store_information:params partner:{}", partner_id);
        // 服务站
        StationExample stationExample = new StationExample();
        stationExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andOnlinedEqualTo(Status.OnlineStatus.online.value).andPartner_idEqualTo(partner_id);
        List<Station> stationList = stationService.selectByExample(stationExample);
        logger.info("api-method:Store_information:process stationList:{}", stationList.size());
        return stationList;
    }

    /**
     * 订单列表
     */
    @SuppressWarnings("incomplete-switch")
    @Cacheable(value = "partner_order_list", key = "'partner_id:'+#partner_id+':status:'+#status+#page_index+':'+#count", unless = "#result == null")
    @Override
    public List<OrderInfo> partner_order_list(String status, Long partner_id, int page_index, int count) {
        logger.info("api-method:partner_order_list:params status:{},partner_id:{},page_index:{},count:{}", status,
                partner_id, page_index, count);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        Criteria unitExample = serviceUnitExample.or();
        unitExample.andPartner_idEqualTo(partner_id);
        List<Integer> ids = new ArrayList<>();
        ids.add(Status.ServiceStatus.wait_partner_confirm.value);
        ids.add(Status.ServiceStatus.wait_assign_worker.value);
        ids.add(Status.ServiceStatus.assign_worker.value);
        // 组装条件
        OrderStatus orderStatus = OrderStatus.get(status);
        if (orderStatus != null) {
            switch (orderStatus) {
                case PAYED:
                    unitExample.andPidNotEqualTo(0l);
                    unitExample.andActiveEqualTo(Status.PayStatus.payed.value);
                    unitExample.andStatus_activeEqualTo(Status.ServiceStatus.wait_partner_confirm.value);
                    serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " asc");
                    break;
                case WAIT_SERVICE:
                    unitExample.andPidEqualTo(0l);
                    unitExample.andActiveEqualTo(Status.PayStatus.payed.value);
                    unitExample.andStatus_activeEqualTo(Status.ServiceStatus.assign_worker.value);
                    serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " asc");
                    break;
                case CANCEL:
                    unitExample.andPidNotEqualTo(0l);
                    unitExample.andStatus_activeIn(ids);
                    unitExample.andActiveEqualTo(Status.ServiceUnitActive.unactive.value);
                    serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " desc");
                    break;
                case DONE:
                    unitExample.andPidEqualTo(0l);
                    unitExample.andActiveEqualTo(Status.PayStatus.payed.value);
                    unitExample.andStatus_activeEqualTo(Status.ServiceStatus.done.value);
                    serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " desc");
                    break;
                case REFUSED:
                    unitExample.andPidNotEqualTo(0l);
                    unitExample.andActiveEqualTo(Status.ServiceUnitActive.unactive.value);
                    unitExample.andStatus_activeEqualTo(Status.ServiceStatus.reject.value);
                    serviceUnitExample.setOrderByClause(ServiceUnitExample.C.c_begin_datetime + " desc");
                    break;
            }
        } else {
            unitExample.andPidNotEqualTo(0l);
        }
        List<OrderInfo> list = orderService.orderInfoList(Roles.PARTNER_USER, serviceUnitExample, page_index, count)
                .getList();
        logger.info("api-method:partner_order_list:process list:{}", list.size());
        return list;
    }

    /**
     * 订单详情
     *
     * @param pay_order_id
     * @return
     */
    @Cacheable(value = "partner_order_detail", key = "'pay_order_id:'+#pay_order_id", unless = "#result == null")
    @Override
    public OrderInfo partner_order_detail(Partner partner, String pay_order_id) {
        logger.info("api-method:partner_order_detail:params pay_order_id:{}", pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        // 已拒单.andGroup_tagNotEqualTo(value)
        serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidNotEqualTo(0l)
                .andPartner_idEqualTo(partner.getPartner_id());

        List<ServiceUnit> serviceUnits = serviceUnitService.selectByExample(serviceUnitExample);
        ServiceUnit serviceUnit = new ServiceUnit();
        if (serviceUnits.size() > 1) {
            // 兼容脏数据
            serviceUnits = serviceUnits.stream().filter(t -> t.getActive() == 1).collect(Collectors.toList());
            if (serviceUnits.size() > 0) {
                serviceUnit = serviceUnits.get(0);
            }
        } else {
            serviceUnit = singleResult(serviceUnits);
        }
        if (serviceUnit == null) {
            // 如果是抢单的合伙人进入，只查看主单，
            ServiceUnitExample serviceUnitExample1 = new ServiceUnitExample();
            // 已拒单.andGroup_tagNotEqualTo(value)
            serviceUnitExample1.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
            serviceUnit = singleResult(serviceUnitService.selectByExample(serviceUnitExample1));
        }
        OrderInfo orderInfo = orderService.orderInfoDetail(Roles.PARTNER_USER, serviceUnit);
        logger.info("api-method:partner_order_detail:process orderInfo:{}", orderInfo);
        return orderInfo;
    }

    /**
     * 查看可进行指派的服务人员信息
     *
     * @param pay_order_id
     */
    @Override
    public List<StudentType> partnerFindAvailableStudent(Partner partner, String pay_order_id) {
        logger.info("api-method:partnerFindAvailableStudent:params pay_order_id:{}", pay_order_id);
        List<StudentType> stuType = new ArrayList<>();
        // 订单详情
        OrderInfo orderInfo = partner_order_detail(partner, pay_order_id);
        Product product = productService.selectByPrimaryKey(orderInfo.getProduct_id());
        Station station = null;
        if (!partner.getPartner_id().equals(orderInfo.getPartner_id())) {
            CustomerAddress customerAddress =new CustomerAddress();
            customerAddress.setCity(orderInfo.getOrder().getCus_city());
            customerAddress.setLbs_lat(orderInfo.getOrder().getLbs_lat());
            customerAddress.setLbs_lng(orderInfo.getOrder().getLbs_lng());
            Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
            Integer integer = metadata == null ? 5000 : Integer.parseInt(metadata.getMeta_value() == null ? "5000" : metadata.getMeta_value());
            List<Station> stations = stationService.findNearbyStation(customerAddress, integer);
            stations = stations.stream().filter(t -> partner.getPartner_id().equals(t.getPartner_id())).collect(Collectors.toList());
            station = stationService.randomAStation(stations);
        } else {
            station = stationService.selectByPrimaryKey(orderInfo.getStation_id());
        }
        List<Student> studentList2 = new ArrayList<>();
        if (station != null && product != null) {
            studentList2 = studentService.getStudentByStationAndProduct(station, product);
        }
        if (studentList2.size() == 0) {
            return stuType;
        }
        List<Student> listStudent = new ArrayList<>();
        if (orderInfo.getOrder().getProxyed() != 1) {
            // 预约时间和结束时间处理
            ServiceUnitExample example2 = new ServiceUnitExample();
            example2.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
            ServiceUnit unit2 = singleResult(serviceUnitService.selectByExample(example2));
            Date startDate = unit2.getC_begin_datetime();
            Date endDate = unit2.getC_end_datetime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String startTime = format.format(startDate);
            String dateTime = startTime.substring(0, 10);
            String startStr = startTime.substring(11, 16);
            Integer start = Constant.timeUnisMap.get(startStr);
            Integer end = 48;
            if (endDate != null) {
                String endTime = format.format(endDate);
                String endStr = endTime.substring(11, 16);
                end = Constant.timeUnisMap.get(endStr);
            }
            listStudent = storeService.serviceProviderList(studentList2, dateTime, start, end, 1);
            logger.info("api-method:partnerFindAvailableStudent:process listStudent:{}", listStudent);
        }
        // 查询该订单的服务人员
        Set<Long> studentIdSet = listStudent.stream().map(t->t.getStudent_id()).collect(Collectors.toSet());
        Set<Long> studentIdSetResult  = new HashSet<>();
        if (partner.getPartner_id().equals(orderInfo.getPartner_id())) {
            List<ServiceunitPerson> students = orderInfo.getStudents();
            for (ServiceunitPerson serviceunitPerson : students) {
                StudentType studentType = new StudentType();
                Student student = studentService.selectByPrimaryKey(serviceunitPerson.getStudent_id());
                if(studentIdSet.contains(student.getStudent_id())){
                    studentIdSetResult.add(student.getStudent_id());
                }
                studentType.setStudent(student);
                studentType.setType(0);
                stuType.add(studentType);
                logger.info("api-method:partnerFindAvailableStudent:process student:{}", student);
            }
        }
        for (Student student : listStudent) {
            if(studentIdSetResult.contains(student.getStudent_id())){
                continue;
            }
            StudentType studentType = new StudentType();
            studentType.setStudent(student);
            studentType.setType(1);
            stuType.add(studentType);
        }
        Collections.sort(stuType);
        logger.info("api-method:partnerFindAvailableStudent:process stuType:{}", stuType.size());
        return stuType;
    }


	/**
	 * 合伙人拒绝接单
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public ApiResponse partnerRefusedOrder(Partner partner, String pay_order_id, String orderStr) {
		logger.info("api-method:partnerRefusedOrder:params pay_order_id:{},orderStr:{}", pay_order_id, orderStr);
		ApiResponse response = new ApiResponse();
		try {
			// 订单详情
			OrderInfo orderInfo = partner_order_detail(partner, pay_order_id);
			if (orderInfo == null) {
				response.setErrors(Errors._42008);
				return response;
			}
			String status = orderInfo.getOrderStatus();
			if ("payed".equals(status)) {
				if (partner == null) {
					response.setErrors(Errors._42001);
					return response;
				}

				// 拒单方法
				orderService.xRejectOrder(pay_order_id, partner.getPartner_id(), orderStr,1);
				// 对产生变化的当前订单，进行缓存的更新
				cacheReloadHandler.partner_order_detailReload(pay_order_id);
				// 刷新合伙人订单列表
				cacheReloadHandler.partner_order_listReload(partner.getPartner_id());

				String pname = partner.getName() == null ? partner.getPhone() : partner.getName();
				// 订单日志
				orderLogService.xInsert(pname, partner.getUser_id(), pay_order_id,
						"合伙人【" + pname + "】拒绝了该订单，拒单原因：" + orderStr);

                Order order = orderService.selectByPrimaryKey(pay_order_id);
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id)
                        .andPidEqualTo(0l);
                ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
                RejectRecord rejectRecord = new RejectRecord();
                rejectRecord.setReject_record_id(IdGenerator.generateId());
                rejectRecord.setServer_name(order.getName());
                rejectRecord.setPay_order_id(pay_order_id);
                rejectRecord.setServiceunit_id(serviceUnit.getServiceunit_id());
                rejectRecord.setCreate_datetime(new Date());
                rejectRecord.setServer_name(order.getName());
                rejectRecord.setCus_username(order.getCus_username());
                rejectRecord.setCus_phone(order.getCus_phone());
                rejectRecord.setCus_address(order.getCus_address());
                rejectRecord.setPrice_total(order.getPrice_total());
                rejectRecord.setPrice_pay(order.getPrice_pay());
                rejectRecord.setReject_type(1);
                rejectRecord.setReject_info(serviceUnit.getP_reject_remark() == null ? "":serviceUnit.getP_reject_remark());
                rejectRecord.setPartner_id(serviceUnit.getPartner_id());
                rejectRecord.setServer_datetime(serviceUnit.getC_begin_datetime());
                rejectRecordService.insert(rejectRecord);

				ProductSoleExample soleExample = new ProductSoleExample();
				soleExample.or().andProduct_idEqualTo(orderInfo.getProduct_id())
						.andPartner_idEqualTo(partner.getPartner_id());
				ProductSole sole = singleResult(productSoleService.selectByExample(soleExample));
                // 订单信息（如果为代下单或抢单,不发起抢单）
				if (order.getProxyed() != 1 && sole == null
						&& (orderInfo.getServiceUnit().getRobbing() == null
						|| new Integer(0).equals(orderInfo.getServiceUnit().getRobbing()))) {
					// 拒单,发起抢单
					apiOrderService.startRobbing(orderService.selectByPrimaryKey(pay_order_id));
					// 刷新抢单列表缓存
					cacheReloadHandler.partner_order_listReload(partner.getPartner_id());
				}
			} else {
				response.setErrors(Errors._42008);
				return response;
			}
		} catch (Exception e) {
            logger.error("api-method:partnerRefusedOrder ERROR e",e);
			response.setErrors(Errors._42008);
		}
		response.setMutationResult(new MutationResult());
		return response;
	}

    /**
     * 待服务状态下修改
     */
    @Transactional(timeout = 3)
    @SuppressWarnings("rawtypes")
    @Override
    public ApiResponse partnerAlterOrder(Partner partner, String pay_order_id, List<Long> student_ids) {
        logger.info("api-method:partnerAlterOrder:params pay_order_id:{},student_ids:{}", pay_order_id, student_ids);
        ApiResponse response = new ApiResponse();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 订单详情
        OrderInfo orderInfo = partner_order_detail(partner, pay_order_id);
        if (orderInfo == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        logger.info("api-method:partnerAlterOrder:process orderInfo:{}", orderInfo);
        ServiceUnitExample example2 = new ServiceUnitExample();
        example2.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
        ServiceUnit unit = singleResult(serviceUnitService.selectByExample(example2));
        if (unit == null) {
            response.setErrors(Errors._41013);
            return response;
        }
        logger.info("api-method:partnerAlterOrder:process partner:{}", partner);
        Customer customer = customerService.selectByPrimaryKey(unit.getCustomer_id());
        logger.info("api-method:partnerAlterOrder:process customer:{}", customer);
        if (customer == null) {
            response.setErrors(Errors._41003);
            return response;
        }
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if (order == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or().andPay_order_idEqualTo(pay_order_id);
        OrderItem item = singleResult(orderItemService.selectByExample(orderItemExample));
        if (orderInfo.getBuy_multiple_o2o() == 0) {
            if (student_ids.size() > 1 || student_ids.size() == 0) {
                response.setErrors(Errors._42027);
                return response;
            }
        } else {
            if (item.getNum() != student_ids.size()) {
                response.setErrors(Errors._42021);
                return response;
            }
        }
        // 删除之前的服务记录
        ServiceunitPersonExample personExam = new ServiceunitPersonExample();
        personExam.or().andServiceunit_idEqualTo(unit.getServiceunit_id());
        List<ServiceunitPerson> listPerson = serviceunitPersonService.selectByExample(personExam);

		// 查询之前的学员 ,然后给替换掉的学员和新服务人员发送短信
		List<Long> oldIds = listPerson.stream().map(c -> c.getStudent_id()).collect(Collectors.toList());
		// 修改之前的服务人员缓存更新
		for (Long stuId : oldIds) {
			// 更新服务人员缓存
			cacheReloadHandler.student_order_listReload(stuId);
			cacheReloadHandler.selectStuShowTaskdetailReload(stuId, pay_order_id);
			cacheReloadHandler.selectStuUndoneOrderReload(stuId);
		}

		Collection exists = new ArrayList(student_ids);
		exists.removeAll(oldIds);
		// 新分配的服务人员(存在多个)
		for (Object id : exists) {
			// 您的有一个新的${product_name}任务，于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}进行服务。
			Student student = studentService.selectByPrimaryKey((Long) id);
			smsHandler.sendToWorkWhenOrderAssign(order.getName(), order.getCus_username(), order.getCus_address(),
					format.format(unit.getC_begin_datetime()), order.getCus_phone(), student.getPhone());
			//推送消息  新服务人员新订单通知
			pushHandler.pushOrderMessageToStudent(orderInfo,student.getStudent_id().toString());
			//站内消息(学员)
            inStationHandler.sentToStudentOrder(student.getStudent_id(),pay_order_id);
		}
		// 原来的服务人员
		Collection oldexists = new ArrayList(oldIds);
		oldexists.removeAll(student_ids);
		for (Object id : oldexists) {
			Student student = studentService.selectByPrimaryKey((Long) id);
			// 您于${c_begin_datetime}去${cus_address}，为${cus_username}，进行${product_name}服务的订单已取消。
			smsHandler.sendToPartnerWhenOrderCancel(order.getName(), format.format(unit.getC_begin_datetime()),
					order.getCus_username(), order.getCus_address(), student.getPhone());
			//推送消息  原服务人员取消订单
			pushHandler.pushCancelOrderMessageToStudent(orderInfo,student.getStudent_id().toString());
			//站内消息(学员)
            inStationHandler.sentToStudentCancleOrder(pay_order_id,student);
        }

		// 您的${produce_name}订单服务人员变为${worker_name}，服务时间变更为${c_begin_datetime}去${cus_address}为您进行服务。
		Student randomStudent = studentService.selectByPrimaryKey(student_ids.get(0));
		smsHandler.sendToCustomerWhenOrderAlter(order.getName(), randomStudent.getName(),
				format.format(unit.getC_begin_datetime()), order.getCus_address(), customer.getPhone());

		listPerson.forEach(t -> {
			storeService.updateAvilableTimeUnits(t.getStudent_id(), unit.getC_begin_datetime(),
					unit.getC_end_datetime(), StoreServiceImpl.RELEASE);
		});
		serviceunitPersonService.deleteByExample(personExam);
		// 记录 name
		List<String> StrList = new ArrayList<>();
		// 添加服务记录
		student_ids.stream().forEach(t -> {

			Student student = studentService.selectByPrimaryKey(t);
			logger.info("api-method:partnerAlterOrder:process [student=" + student + "]");
			if (student == null) {
				response.setErrors(Errors._42011);
				return;
			}
			StrList.add(student.getName());

			ServiceunitPerson person = new ServiceunitPerson();
			person.setServiceunit_person_id(IdGenerator.generateId());
			person.setServiceunit_id(unit.getServiceunit_id());
			person.setStudent_id(student.getStudent_id());
			person.setStudent_name(student.getName());
			person.setStatus_active(3);
			serviceunitPersonService.insert(person);

			// 员工接单占库存
			storeService.updateAvilableTimeUnits(student.getStudent_id(), unit.getC_begin_datetime(),
					unit.getC_end_datetime(), StoreServiceImpl.TAKEN);

			// 更新服务人员缓存
			cacheReloadHandler.student_order_listReload(t);
			cacheReloadHandler.selectStuShowTaskdetailReload(t, pay_order_id);
			cacheReloadHandler.selectStuUndoneOrderReload(t);
			cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
		});

		// 修改服务单
		ServiceUnitExample exam = new ServiceUnitExample();
		exam.or().andPay_order_idEqualTo(pay_order_id).andGroup_tagEqualTo(unit.getGroup_tag());
		List<ServiceUnit> list = serviceUnitService.selectByExample(exam);
		if (list.size() <= 0) {
			response.setErrors(Errors._41008);
			return response;
		}
		for (ServiceUnit serviceUnit : list) {
			serviceUnit.setP2s_assign_datetime(new Date());
			StudentExample studentExample = new StudentExample();
			studentExample.or().andStudent_idIn(student_ids);
			List<Student> students = studentService.selectByExample(studentExample);
			StringBuffer stringBuffer = new StringBuffer();
			students.forEach(s -> {
				stringBuffer.append(s.getName() + ",");
			});
			serviceUnit.setStudent_name(stringBuffer.toString());
			serviceUnitService.updateByPrimaryKey(serviceUnit);
		}
		String pname = partner.getName() == null ? partner.getPhone() : partner.getName();
		orderLogService.xInsert(pname, partner.getUser_id(), pay_order_id,
				"合伙人【" + pname + "】修改了该订单，将服务人员改为:" + StrList.toString());

		// 更新顾客端缓存
		cacheReloadHandler.orderListReload(order.getUid());
		cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);
		// 更新合伙人订单缓存
		cacheReloadHandler.partner_order_detailReload(pay_order_id);
		cacheReloadHandler.partner_order_listReload(partner.getPartner_id());
		cacheReloadHandler.my_mission_scheduled_informationReload(partner.getPartner_id());
        //推送消息 服务人员进行变更，发送给顾客
        pushHandler.pushOrderMessageWhenStudentChangeToCustomer(orderInfo,customer.getCustomer_id().toString());
        //站内消息，服务人员变更（通知顾客）
        inStationHandler.sentToCustomerChangeOrder(orderInfo,student_ids,pay_order_id);
        Message msg = new Message();
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
        param.put("orderStatus",orderInfo.getOrderStatus());
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.PARTNER,TransmissionContent.ORDER_DETAIL,param);
        content.setHref(tContent.getHrefNotEncode());
        content.setTitle("服务变更通知");
        content.setTypes(1);
        content.setNoticeTypes(2);
        if (content.getContent() != null && !ParamsCheck.checkStrAndLength(content.getContent(),200)){
            Errors._41040.throwError("消息长度过长");
            return response;
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
        msg.setApp_type(3);
        msg.setSend_type(1);
        msg.setApp_platform(0);
        messageService.insertSelective(msg);

        response.setMutationResult(new MutationResult());
		return response;
	}

	/**
	 * 待确认下接单
	 */
	@Transactional(timeout = 3)
	@SuppressWarnings("rawtypes")
	@Override
	public ApiResponse partnerConfirmOrder(Partner partner, String pay_order_id, List<Long> student_ids) {
        ApiResponse response = new ApiResponse();
	    try {
            logger.info("api-method:partnerConfirmOrder:params pay_order_id:{},student_ids:{}", pay_order_id, student_ids);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // 订单详情
            OrderInfo orderInfo = partner_order_detail(partner, pay_order_id);
            ServiceUnitExample example2 = new ServiceUnitExample();
            example2.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
            ServiceUnit unit = singleResult(serviceUnitService.selectByExample(example2));
            // ServiceUnit unit =
            // serviceUnitService.selectByPrimaryKey(orderInfo.getServiceunit_id());
            Order order = orderService.selectByPrimaryKey(pay_order_id);
            logger.info("api-method:partnerConfirmOrder:process orderInfo:{}", orderInfo);
            String status = orderInfo.getOrderStatus();
            if (status.equals("payed")) {
                if (order.getStatus_active() >= 3) {
                    response.setErrors(Errors._42018);
                    response.setMutationResult(new MutationResult());
                    return response;
                } else {
                    OrderItemExample orderItem = new OrderItemExample();
                    orderItem.or().andPay_order_idEqualTo(pay_order_id);
                    OrderItem item = singleResult(orderItemService.selectByExample(orderItem));

                    if (orderInfo.getBuy_multiple_o2o() == 0) {
                        if (student_ids.size() > 1 || student_ids.size() == 0) {
                            response.setErrors(Errors._42027);
                            return response;
                        }
                    } else {
                        if (item.getNum() != student_ids.size()) {
                            response.setErrors(Errors._42021);
                            return response;
                        }
                    }

                    for (Long student_id : student_ids) {
                        Student student = studentService.selectByPrimaryKey(student_id);
                        if (student == null) {
                            response.setErrors(Errors._42011);
                            return response;
                        }

                        ServiceunitPerson person = new ServiceunitPerson();
                        person.setServiceunit_person_id(IdGenerator.generateId());
                        person.setServiceunit_id(unit.getServiceunit_id());
                        person.setStudent_id(student.getStudent_id());
                        person.setStudent_name(student.getName());
                        person.setStatus_active(3);
                        serviceunitPersonService.insert(person);

                        storeService.updateAvilableTimeUnits(student_id, unit.getC_begin_datetime(),
                                unit.getC_end_datetime(), StoreServiceImpl.TAKEN);
                        //LocalDateTime today = LocalDateTime.now();
                        //DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

                        // 您的${product_name}订单已确认，服务人员${worker_name}将于${c_begin_datetime}，去${cus_address}为您进行服务。
                        Customer customer = customerService.selectByPrimaryKey(unit.getCustomer_id());
                        // 向顾客发送短信确认已接单
                        if (customer != null) {
                            smsHandler.sendToCustomerWhenOrderAssignWork(order.getName(), student.getName(),
                                    format.format(unit.getC_begin_datetime()), order.getCus_address(), customer.getPhone());
                            //推送新订单 通知到顾客
                            pushHandler.pushOrderMessageToCustomer(order,customer.getCustomer_id().toString());
                            //pushHandler.pushOrderMessageBeforServiceToCustomer(orderInfo,customer.getCustomer_id().toString());
                        }

                        // 向学员发送短息确认
                        if (student != null) {
                            smsHandler.sendToWorkWhenOrderAssign(order.getName(), order.getCus_username(),
                                    order.getCus_address(), format.format(unit.getC_begin_datetime()), order.getCus_phone(),
                                    student.getPhone());

                            //推送新订单 通知到服务人员0_0
                            pushHandler.pushOrderMessageToStudent(orderInfo,student.getStudent_id().toString());
                            //发送站内消息
                            inStationHandler.sentToStudentOrder(student.getStudent_id(),pay_order_id);
                            //暂无 服务时间提醒类型
                            //inStationHandler.sentToStudentRemindService(orderInfo);//前一天八点提醒
                            //inStationHandler.sentToCustomerRemindService(orderInfo);//前一天9点整
                        }

                        // 接单,更新服务人员缓存//服务人员订单列表//服务人员当前订单
                        cacheReloadHandler.student_order_listReload(student_id);
                        cacheReloadHandler.selectStuShowTaskdetailReload(student_id, pay_order_id);
                        cacheReloadHandler.selectStuUndoneOrderReload(student_id);
                        cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
                    }
                    ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                    serviceUnitExample.or().andGroup_tagEqualTo(unit.getGroup_tag()).andPay_order_idEqualTo(pay_order_id);
                    ServiceUnit unit1 = new ServiceUnit();
                    unit1.setStatus_active(4);
                    unit1.setP_confirm_datetime(new Date());
                    unit1.setP2s_assign_datetime(new Date());
                    StudentExample studentExample = new StudentExample();
                    studentExample.or().andStudent_idIn(student_ids);
                    List<Student> students = studentService.selectByExample(studentExample);
                    StringBuffer stringBuffer = new StringBuffer();
                    students.forEach(s -> {
                        stringBuffer.append(s.getName() + ",");
                    });
                    unit1.setStudent_name(stringBuffer.toString());
                    serviceUnitService.updateByExampleSelective(unit1, serviceUnitExample);

                    if (order == null) {
                        response.setErrors(Errors._41007);
                        return response;
                    }
                    order.setStatus_active(3);
                    orderService.updateByPrimaryKey(order);
                    String pname = partner.getName() == null ? partner.getPhone() : partner.getName();
                    // 日志
                    orderLogService.xInsert(pname, partner.getUser_id(), pay_order_id,
                            "合伙人【" + pname + "】接了" + pay_order_id + "订单");
                }

                // 更新合伙人订单缓存//当前订单
                cacheReloadHandler.partner_order_detailReload(pay_order_id);
                cacheReloadHandler.partner_order_listReload(partner.getPartner_id());
                // 顾客订单列表//顾客当前订单
                cacheReloadHandler.orderListReload(order.getUid());
                cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);

                // 接单删除虚拟库存
                Set<String> set = redisService.sMembers(pay_order_id);
                if (set != null) {
                    redisService.delete(pay_order_id);
                    redisService.delete(set);
                }

            } else {
                response.setErrors(Errors._42008);
                return response;
            }
        }catch (Exception e){
	        logger.error("ERROR partnerConfirmOrder  ",e);
            response.setErrors(Errors._42008);
            return response;
        }

		response.setMutationResult(new MutationResult());
		return response;
	}

	/**
	 * 消息列表
	 */
	@Override
	public List<MessageContent> partnerMessageInfo(Partner partner, int page_index, int count) {
		logger.info("api-method:partnerMessageInfo:params partner:{},page_index:{},count:{}", partner, page_index,
				count);
		List<MessageContent> messageList = new ArrayList<>();
		MessageExample messageExample = new MessageExample();
		List<Long> ids = new ArrayList<>();
		ids.add(partner.getUser_id());
		ids.add(0l);
		messageExample.or()
						.andUser_idIn(ids)
						.andDeletedEqualTo(Status.DeleteStatus.no.value)
						.andNotify_datetimeLessThanOrEqualTo(new Date())
						.andSend_typeEqualTo(1)
						.andApp_typeEqualTo(3)
						.andApp_platformEqualTo(0);
		messageExample.setOrderByClause(MessageExample.C.notify_datetime + " DESC");
		List<Message> messages = messageService.selectByExample(messageExample, page_index, count).getList();
		if(messages.size() != 0){
			messageList = messages.stream().map((Message message) -> {
				MessageContent messageContent = new MessageContent();
				String content = message.getMsg_content();
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(content)){
					MessageContent.ContentMsg msg =  JSON.parseObject(content, MessageContent.ContentMsg.class);
                    try {
                        if (msg != null && msg.getHref() != null) {
                            msg.setHref(URLEncoder.encode(msg.getHref(),"UTF-8"));
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    messageContent.setContentMsg(msg);
				}
				messageContent.setNotifyDateTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(message.getNotify_datetime()));
				BeanUtils.copyProperties(message, messageContent);
				return messageContent;
			}).collect(Collectors.toList());
		}
		logger.info("api-method:partnerMessageInfo:process messageList:{}", messageList);
		return messageList;
	}

	/**
	 * 消息详情
	 */
	@Override
	public Message partnerMessageDetail(Long id) {
		return messageService.selectByPrimaryKey(id);
	}

	/**
	 * 修改服务站
	 */
	@Override
	public ApiResponse partnerUpdateStation(Partner partner, Long station_id, Long student_id) {
		logger.info("api-method:partnerUpdateStation:params station_id:{},student_id:{}", station_id, student_id);
		ApiResponse response = new ApiResponse();
		try {
            if (StringUtils.isEmpty(station_id)){
                response.setErrors(Errors._42032);
                return response;
            }
            // 学员待服务的服务单
            ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
            ServiceunitPersonExample personExample = new ServiceunitPersonExample();
            personExample.or().andStudent_idEqualTo(student_id).andStatus_activeEqualTo(3);
            List<ServiceunitPerson> list = serviceunitPersonService.selectByExample(personExample);
            logger.info("api-method:partnerUpdateStation:process list:{}", list);
            if (list.size() <= 0) {
                Student student = studentService.selectByPrimaryKey(student_id);
                student.setStation_id(station_id);
                studentService.updateByPrimaryKey(student);
            } else {
                response.setErrors(Errors._42019);
                return response;
            }
            // 更新服务站缓存
            cacheReloadHandler.studentInfoByUserIdReload(partner.getUser_id());
            cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
            cacheReloadHandler.store_informationReload(partner.getPartner_id());
        }catch (Exception e){
            logger.error("ERROR api-method:partnerUpdateStation",e);
		    return  response;
        }
		response.setMutationResult(new MutationResult());
		return response;
	}

    /**
     * 学员停止接单
     */
    @Override
    public ApiResponse partnerStopOrder(Partner partner, Long student_id, String date, String start, String end,
                                        Boolean statu) {
        logger.info("api-method:partnerStopOrder:params student_id:{},date:{},start:{},end:{},statu:{}", student_id,
                date, start, end, statu);
        ApiResponse response = new ApiResponse();
        // 开始结束时间
        int intStart = 0;
        int intEnd = 0;
        if (StringUtils.isEmpty(start)) {
            start = "00:00";
        }
        if (StringUtils.isEmpty(end)) {
            end = "23:30";
        }
        //正则验证参数值是否正确
        Pattern pattern = Pattern.compile("^([0-1]\\d|[2][0-3])(\\:)([0-5]\\d)$", Pattern.DOTALL);
        if (!(pattern.matcher(start).find() && pattern.matcher(end).find())) {
            response.setErrors(Errors._41040);
            return response;
        }
        intStart = Constant.timeUnisMap.get(start);
        intEnd = Constant.timeUnisMap.get(end);
        //清除缓存
        cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
        // 判断请假还是取消
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date day = null;
        try {
            day = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (statu == true) {
            Student student = studentService.selectByPrimaryKey(student_id);
            String key = Constant.getStudentTaskScheduleKey(student.getStudent_id(), date);
            String schedule = redisService.getStringValue(key);

            if (schedule==null || !schedule.contains("2")) {
                storeService.updateAvilableTimeUnits(student_id, date, intStart, intEnd, StoreServiceImpl.SORDER);
                //插入一条请假记录
                OutOfService outOfService = new OutOfService();
                outOfService.setOut_of_service_id(IdGenerator.generateId());
                outOfService.setMonth(date.substring(0, 7));
                outOfService.setDay(day);
                outOfService.setStart_time(start);
                outOfService.setEnd_time(end);
                outOfService.setStudent_id(student_id);
                outOfService.setDuration(Math.round((intEnd - intStart) / 2.0F));
                outOfService.setCreate_datetime(new Date());
                outOfService.setPartner_id(partner.getPartner_id());
                outOfServiceService.insert(outOfService);
                response.setMutationResult(new MutationResult());
                return response;
            } else {
                response.setErrors(Errors._42019);
                return response;
            }
        } else {
            storeService.updateAvilableTimeUnits(student_id, date, intStart, intEnd, StoreServiceImpl.RELEASE);
            //删除一条请假记录
            OutOfServiceExample outOfServiceExample = new OutOfServiceExample();
            outOfServiceExample.or()
                    .andPartner_idEqualTo(partner.getPartner_id())
                    .andStudent_idEqualTo(student_id)
                    .andDayEqualTo(day)
                    .andStart_timeEqualTo(start)
                    .andEnd_timeEqualTo(end);
            outOfServiceService.deleteByExample(outOfServiceExample);
        }
        response.setMutationResult(new MutationResult());
        return response;
    }

    /**
     * 停单日期当前日期7天
     */
    @Override
    public List<StudentStopDate> partnerStopDate(Long student_id,String clientId) {
        logger.info("api-method:partnerStopDate:params student_id:{}", student_id);
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_LEAVE_DAYS_SPAN);
        Integer span = metadata.getMeta_value() == null ? Constant.MAXNUM_STUDENT_STOP_SPAN
                : Integer.parseInt(metadata.getMeta_value());
        List<String> list = DateUtil.daysSpan(span, true);
        List<String> filterList = new ArrayList<>();
        logger.info("api-method:partnerStopDate:process list:{}", list);
        Student student = studentService.selectByPrimaryKey(student_id);
        List<StudentStopDate> studentStopDates = new ArrayList<>();
        if (clientId.equals("wx_m_partner") || clientId.equals("wx_m_student") || clientId.equals("wx_m_custom") || clientId.equals("wx_m_teacher")){
            try {
                String times = student.getService_times();
                if (times != null) {
                    String weeks = student.getService_cycle();
                    Set<Integer> set = JSON.parseObject(weeks, new TypeReference<Set<Integer>>() {});
                    //筛选后的集合日期
                    list = list.stream()
                            .filter(t -> set.contains(LocalDate.parse(t, DateTimeFormatter.ISO_LOCAL_DATE).getDayOfWeek().getValue()))
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                logger.error("api-method:partnerStopDate    student info is illegal ERROR :{}", e.getMessage());
            }
        }
        //被筛选掉的集合日期
        Set<Integer> set1 = JSON.parseObject(student.getService_cycle(), new TypeReference<Set<Integer>>() {});
        filterList = list.stream()
                .filter(t -> !set1.contains(LocalDate.parse(t, DateTimeFormatter.ISO_LOCAL_DATE).getDayOfWeek().getValue()))
                .collect(Collectors.toList());

        for (String n: list) {
            StudentStopDate date = new StudentStopDate();
            if (filterList.size() == 0){
                String key = Constant.getStudentTaskScheduleKey(student_id, n);
                String schedule = redisService.getStringValue(key);
                Integer[] integers = Constant.defaultStudentsStopSingle;
                String StopSingle = null;
                try {
                    StopSingle = JacksonUtil.object_to_json(integers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                date.setLeaveDate(n);
                if (StopSingle.equals(schedule)) {
                    date.setStatu(1);
                } else {
                    date.setStatu(0);
                }
            }else {
                for (String t: filterList) {
                    if(n.equals(t)){
                        date.setLeaveDate(t);
                        date.setStatu(2);
                        break;
                    }else{
                        String key = Constant.getStudentTaskScheduleKey(student_id, n);
                        String schedule = redisService.getStringValue(key);
                        Integer[] integers = Constant.defaultStudentsStopSingle;
                        String StopSingle = null;
                        try {
                            StopSingle = JacksonUtil.object_to_json(integers);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        date.setLeaveDate(n);
                        if (StopSingle.equals(schedule)) {
                            date.setStatu(1);
                        } else {
                            date.setStatu(0);
                        }
                    }
                }
            }
            studentStopDates.add(date);
        }
        return studentStopDates;
    }

    /**
     * 查询可抢单的订单列表
     */
    @Override
    public List<OrderInfo> partnerRobberies(Long partner_id, int page_index, int count) {
        logger.info("api-method:partnerRobberies:params partner_id:{},page_index:{},count:{}", partner_id, page_index,
                count);
        List<OrderInfo> infoList = new ArrayList<>();
        RobbingExample robbingExample = new RobbingExample();
        robbingExample.or().andPartner_idEqualTo(partner_id).andStatusEqualTo(1)
                .andExpire_datetimeGreaterThan(new Date());
        List<Robbing> robbingList = robbingService.selectByExample(robbingExample);
        if (robbingList.size() <= 0) {
            return infoList;
        }
        // 查询所有可抢服务单
        List<Long> unitIds = new ArrayList<>();
        for (Robbing robbing : robbingList) {
            unitIds.add(robbing.getServiceunit_id());
        }
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andServiceunit_idIn(unitIds);
        if (unitIds.size() == 0) {
            return infoList;
        }
        infoList = orderService.orderInfoList(Roles.PARTNER_USER, serviceUnitExample, page_index, count).getList();
        if (infoList.size() == 0) {
            infoList = new ArrayList<>();
        }
        logger.info("api-method:partnerRobberies:process list:{}", infoList.size());
        return infoList;
    }

    /**
     * 抢单
     */
    @Transactional(timeout = 3)
    @Override
    public ApiResponse partnerOrderRobbing(Partner partner, String pay_order_id, List<Long> student_ids) {
        logger.info("api-method:partnerOrderRobbing:params pay_order_id:{},student_ids:{}", pay_order_id, student_ids);
        ApiResponse response = new ApiResponse();
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Order order = orderService.selectByPrimaryKey(pay_order_id);
            OrderItemExample orderItemExample = new OrderItemExample();
            orderItemExample.or().andPay_order_idEqualTo(pay_order_id);
            OrderItem item = singleResult(orderItemService.selectByExample(orderItemExample));
            if (order == null) {
                response.setErrors(Errors._41007);
                return response;
            }
            if (order.getStatus_active() >= 3) {
                response.setErrors(Errors._42026);
                return response;
            }
            //service
            ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
            serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id);
            List<ServiceUnit> list = serviceUnitService.selectByExample(serviceUnitExample);
            ServiceUnit master = null;
            ServiceUnit child = null;
            Long groupTag  = IdGenerator.generateId();
            for (ServiceUnit serviceUnit : list) {
                if (partner.getPartner_id().equals(serviceUnit.getPartner_id()) && serviceUnit.getPid()!=0) {
                    child = serviceUnit;
                }
                if (serviceUnit.getPid()==0) {
                    master = serviceUnit;
                    master.setActive(1);
                    master.setStatus_active(4);
                    master.setRobbing(2);
                    master.setGroup_tag(groupTag);
                    master.setP_confirm_datetime(new Date());
                    master.setPartner_id(partner.getPartner_id());

                }
            }

            ProSku proSku = proSkuService.selectByPrimaryKey(master.getPsku_id());
            if (proSku.getBuy_multiple_o2o() == 0) {
                if (student_ids.size() > 1 || student_ids.size() == 0) {
                    response.setErrors(Errors._42027);
                    return response;
                }
            } else {
                if (item.getNum() != student_ids.size()) {
                    response.setErrors(Errors._42021);
                    return response;
                }
            }

            RobbingExample robbingExample = new RobbingExample();
            robbingExample.or().andServiceunit_idEqualTo(master.getServiceunit_id())
                    .andPartner_idEqualTo(partner.getPartner_id());
            Robbing robbing = singleResult(robbingService.selectByExample(robbingExample));

            // 订单详情
            Long station_id = 0L;
            if (robbing != null && robbing.getStatus() == 1 && robbing.getExpire_datetime().after(new Date())) {
                // 生成一个抢单key
                String key = Constant.getRobbingOrderKey(robbing.getServiceunit_id());
                if (!redisService.exits(key)) {
                    redisService.setStringValue(key, "true", 2, TimeUnit.SECONDS);

                    StudentExample studentExample = new StudentExample();
                    studentExample.or().andStudent_idIn(student_ids);
                    List<Student> students = studentService.selectByExample(studentExample);
                    StringBuilder builder = new StringBuilder();
                    for (Student student:students){
                        builder.append(student.getName());
                        builder.append(",");
                    }
                    if (students.size()>0){
                        station_id=students.get(0).getStation_id();
                    }
                    //主单先更新了
                    master.setStation_id(station_id);
                    master.setStudent_name(builder.toString());
                    serviceUnitService.updateByPrimaryKeySelective(master);
                    if(child==null){
                        child  = new ServiceUnit();
                        BeanUtils.copyProperties(master,child);
                        child.setServiceunit_id(IdGenerator.generateId());
                        child.setPid(master.getServiceunit_id());
                    }else {
                        child.setActive(1);
                        child.setStatus_active(4);
                        child.setRobbing(2);
                        child.setGroup_tag(groupTag);
                        child.setPartner_id(partner.getPartner_id());
                        child.setStation_id(station_id);
                        child.setStudent_name(builder.toString());
                    }
                    serviceUnitService.upsertSelective(child);

                    for (Student student:students) {
                        ServiceunitPerson person = new ServiceunitPerson();
                        person.setServiceunit_person_id(IdGenerator.generateId());
                        person.setServiceunit_id(master.getServiceunit_id());
                        person.setStudent_id(student.getStudent_id());
                        person.setStudent_name(student.getName());
                        person.setStatus_active(3);
                        serviceunitPersonService.insert(person);
                        logger.info("api-method:partnerOrderRobbing:process person:{}", person);
                        // 占服务人员库存
                        storeService.updateAvilableTimeUnits(student.getStudent_id(), master.getC_begin_datetime(),
                                master.getC_end_datetime(), StoreServiceImpl.TAKEN);
                        // 您的${product_name}订单已确认，服务人员${worker_name}将于${c_begin_datetime}，去${cus_address}为您进行服务。
                        Customer customer = customerService.selectByPrimaryKey(master.getCustomer_id());
                        // 向顾客发送短信确认已接单
                        if (customer != null) {
                            smsHandler.sendToCustomerWhenOrderAssignWork(order.getName(), student.getName(),
                                    format.format(master.getC_begin_datetime()), order.getCus_address(), customer.getPhone());
                        }
                        // 向学员发送短息确认
                        if (student != null) {
                            smsHandler.sendToWorkWhenOrderAssign(order.getName(), order.getCus_username(),
                                    order.getCus_address(), format.format(master.getC_begin_datetime()), order.getCus_phone(),
                                    student.getPhone());
                        }
                        // 更新服务人员缓存
                        cacheReloadHandler.student_order_listReload(student.getStudent_id());
                        cacheReloadHandler.selectStuShowTaskdetailReload(student.getStudent_id(), pay_order_id);
                        cacheReloadHandler.selectStuUndoneOrderReload(student.getStudent_id());
                        cacheReloadHandler.my_employeeManagementReload(partner.getPartner_id());
                    }

                    String pname = partner.getName() == null ? partner.getPhone() : partner.getName();
                    // 订单日志
                    orderLogService.xInsert(pname, partner.getUser_id(), pay_order_id,
                            "合伙人【" + pname + "】抢了" + pay_order_id + "订单");
                    // 修改订单状态
                    order.setStatus_active(3);
                    orderService.updateByPrimaryKeySelective(order);
                    // 修改抢单记录
                    RobbingExample updateRobbingExample = new RobbingExample();
                    updateRobbingExample.or().andServiceunit_idEqualTo(master.getServiceunit_id());
                    Robbing updateobbing = new Robbing();
                    updateobbing.setStatus(0);
                    robbingService.updateByExampleSelective(updateobbing,updateRobbingExample);
                    robbing.setStatus(0);
                    robbing.setActived(1);
                    robbingService.updateByPrimaryKey(robbing);
                    // 接单删除虚拟库存
                    Set<String> set = redisService.sMembers(pay_order_id);
                    if (set != null) {
                        redisService.delete(pay_order_id);
                        redisService.delete(set);
                    }
                    // 更新顾客缓存
                    cacheReloadHandler.orderListReload(order.getUid());
                    cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);
                    // 更新合伙人订单缓存
                    cacheReloadHandler.partner_order_detailReload(pay_order_id);
                    cacheReloadHandler.partner_order_listReload(partner.getPartner_id());
                } else {
                    response.setErrors(Errors._42024);
                    return response;
                }
            }else {
                response.setErrors(Errors._42026);
                return response;
            }
        }catch (Exception e){
            logger.error("ERROR partnerOrderRobbing",e);
            response.setErrors(Errors._42026);
        }

        response.setMutationResult(new MutationResult());
        return response;
    }


    /**
     * 本月订单统计
     *
     * @param partner
     * @return
     */
    @Override
    public MonthOrderStatistics monthOrderStatistics(Partner partner) {
        LocalDateTime today = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localFirstTime = today.with(TemporalAdjusters.firstDayOfMonth()).plusDays(0).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime localLastTime = today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zonedFirstTime = localFirstTime.atZone(zoneId);
        ZonedDateTime zonedLastTime = localLastTime.atZone(zoneId);
        Date first = Date.from(zonedFirstTime.toInstant());
        Date last = Date.from(zonedLastTime.toInstant());
        MonthOrderStatistics orderStatistics = new MonthOrderStatistics();
        //已完成统计
        ServiceUnitExample serviceUnitExample1 = new ServiceUnitExample();
        serviceUnitExample1.or()
                .andPartner_idEqualTo(partner.getPartner_id())
                .andPidEqualTo(0l)
                .andActiveEqualTo(Status.ServiceUnitActive.active.value)
                .andStatus_activeEqualTo(Status.ServiceStatus.done.value)
                .andFinish_datetimeGreaterThanOrEqualTo(first)
                .andFinish_datetimeLessThan(last);
        long doneNum = serviceUnitService.countByExample(serviceUnitExample1);
        logger.info("api-method:monthOrderStatistics:process serviceUnits1:{}", doneNum);
        orderStatistics.setDoneNum((int) doneNum);
        //已拒单统计
        ServiceUnitExample serviceUnitExample2 = new ServiceUnitExample();
        serviceUnitExample2.or()
                .andPartner_idEqualTo(partner.getPartner_id())
                .andPidNotEqualTo(0l)
                .andActiveEqualTo(Status.ServiceUnitActive.unactive.value)
                .andStatus_activeEqualTo(Status.ServiceStatus.reject.value)
                .andP_reject_datetimeGreaterThanOrEqualTo(first)
                .andP_reject_datetimeLessThan(last);
        long refusedNum = serviceUnitService.countByExample(serviceUnitExample2);
        logger.info("api-method:monthOrderStatistics:process serviceUnits2:{}", refusedNum);
        orderStatistics.setRefusedNum((int) refusedNum);
        //已取消统计
        List<Integer> ids = new ArrayList<>();
        ids.add(Status.ServiceStatus.wait_partner_confirm.value);
        ids.add(Status.ServiceStatus.wait_assign_worker.value);
        ids.add(Status.ServiceStatus.assign_worker.value);
        ServiceUnitExample serviceUnitExample3 = new ServiceUnitExample();
        serviceUnitExample3.or()
                .andPartner_idEqualTo(partner.getPartner_id())
                .andPidEqualTo(0l)
                .andActiveEqualTo(Status.ServiceUnitActive.unactive.value)
                .andStatus_activeIn(ids)
                .andC_begin_datetimeGreaterThanOrEqualTo(first)
                .andC_begin_datetimeLessThan(last);
        long cancelNum = serviceUnitService.countByExample(serviceUnitExample3);
        logger.info("api-method:monthOrderStatistics:process serviceUnits3:{}", cancelNum);
        orderStatistics.setCancelNum((int) cancelNum);
        //待服务统计
        ServiceUnitExample serviceUnitExample4 = new ServiceUnitExample();
        serviceUnitExample4.or()
                .andPartner_idEqualTo(partner.getPartner_id())
                .andPidEqualTo(0l)
                .andActiveEqualTo(Status.ServiceUnitActive.active.value)
                .andStatus_activeEqualTo(Status.ServiceStatus.assign_worker.value)
                .andC_begin_datetimeGreaterThanOrEqualTo(first)
                .andC_begin_datetimeLessThan(last);
        long waitServiceNum = serviceUnitService.countByExample(serviceUnitExample4);
        logger.info("api-method:monthOrderStatistics:process serviceUnits4:{}",waitServiceNum);
        orderStatistics.setWaitServiceNum((int) waitServiceNum);
        return orderStatistics;
    }

    /**
     * 上传头像
     *
     * @param partner,logoImg
     * @return
     */
    @Override
    public MutationResult uploadTheLogimg(Partner partner, String logoImg) {
        logger.info("api-method:uploadTheLogimg:params partner:{},logoImg:{}", partner, logoImg);
        if (!StringUtils.isEmpty(logoImg)) {
            OssImg ossImg = new OssImg();
            ossImg.setOss_img_id(IdGenerator.generateId());
            ossImg.setName(logoImg.substring(logoImg.lastIndexOf("/") + 1));
            ossImg.setUrl(logoImg);
            ossImg.setEffect("合伙人头像");
            ossImg.setCreate_time(new Date());
            ossImg.setBucket(logoImg.substring(logoImg.indexOf("//") + 2, logoImg.indexOf(".")));
            ossImg.setAccess_permissions("public");
            ossImg.setFormat(logoImg.substring(logoImg.lastIndexOf(".") + 1));
            logger.info("api-method:uploadTheLogimg:process ossImg:{}", ossImg);
            ossImgService.insertSelective(ossImg);
            Img img = new Img();
            img.setId(ossImg.getOss_img_id());
            img.setUrl(logoImg);
            partner.setLogo_img(JSON.toJSONString(img));
            partnerService.updateByPrimaryKeySelective(partner);
        }
        return new MutationResult();
    }

    /**
     * 获取一个学员的出勤统计情况。
     *
     * @param partner
     * @param key
     * @param student_id
     * @return
     */
    @Override
    public ApiResponse<OutOfServiceStatistics> outOfServiceStatistics(Partner partner, String key, Long student_id) {
        logger.info("api-method:outOfServiceStatistics:params partner_id:{},key:{},student_id:{}", partner.getPartner_id(), key, student_id);
        //验证参数是否正确
        ApiResponse<OutOfServiceStatistics> response = new ApiResponse<>();
        Pattern pattern = Pattern.compile("^([2-3])\\d{3}[-]([0]\\d|[1][0-2])$", Pattern.DOTALL);
        LocalDateTime local = LocalDateTime.now();
        //key 不存在返回当前月份数据
        if (org.apache.commons.lang3.StringUtils.isEmpty(key)) {
            StringBuilder builder = new StringBuilder();
            builder.append(local.getYear());
            builder.append("-");
            int month = local.getMonth().getValue();
            if (month < 10) {
                builder.append(0);
            }
            builder.append(month);
            key = builder.toString();
        }
        if (!pattern.matcher(key).find() || partner == null || student_id == null) {
            response.setErrors(Errors._41040);
            return response;
        }
        //验证传入的学员是否存在
        if (studentService.selectByPrimaryKey(student_id) == null) {
            response.setErrors(Errors._41040);
            return response;
        }
        OutOfServiceExample outOfServiceExample = new OutOfServiceExample();
        outOfServiceExample.or()
                .andPartner_idEqualTo(partner.getPartner_id())
                .andMonthEqualTo(key)
                .andStudent_idEqualTo(student_id);
        List<OutOfService> outOfServices = outOfServiceService.selectByExample(outOfServiceExample);
        //分组，如果需要按照工作小时数进行计算，此步骤不能省略
        Map<Date, Integer> map = new HashMap<>();
        outOfServices.stream().forEach(t -> {
            Date k = t.getDay();
            if (map.get(k) == null) {
                map.put(k, t.getDuration());
            } else {
                map.put(k, map.get(k) + t.getDuration());
            }
        });
        //需要准确的计算出一个月的最大值。
        String[] arrays = key.split("-");
        int yearInt = Integer.parseInt(arrays[0]);
        int monthInt = Integer.parseInt(arrays[1]);
        Month month = local
                .withYear(yearInt)
                .withMonth(monthInt)
                .getMonth();
        //设置服务人员的出勤，缺勤天数
        int totalDays = month.maxLength();
        int outDays = map.size();
        int workingDays = totalDays - outDays;
        OutOfServiceStatistics statistics = new OutOfServiceStatistics();
        statistics.setTotalDays(totalDays);
        statistics.setOutDays(outDays);
        statistics.setWorkingDays(workingDays);
        statistics.setYearAndMonth(key);
        statistics.setOutOfServices(outOfServices);
        response.setT(statistics);
        return response;
    }

    /**
     * 消息状态修改
     *
     * @param message_id
     * @return
     */
    @Override
    public MutationResult messageStatusAlter(Long message_id) {
        Message message = messageService.selectByPrimaryKey(message_id);
        if (message != null && message.getStatus() == 0) {
            message.setStatus(1);
            messageService.updateByPrimaryKeySelective(message);
        }
        return new MutationResult();
    }

    /**
     * 抢单详情
     *
     * @param partner
     * @param pay_order_id
     * @return
     */
    @Override
    public OrderInfo partnerRobbingDetail(Partner partner, String pay_order_id) {
        logger.info("api-method:partnerRobbingDetail:params partner,pay_order_id:{}", partner, pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
        ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        OrderInfo orderInfo = orderService.orderInfoDetail(Roles.PARTNER_USER, serviceUnit);
        orderInfo.getdServiceStartTime();
        logger.info("api-method:partner_order_detail:process orderInfo:{}", orderInfo);
        return orderInfo;
    }

    /**
     * 学员停单(多个日期)
     *
     * @param partner
     * @param student_id
     * @param dates
     * @param start
     * @param end
     * @param statu
     * @return
     */
    @Override
    public ApiResponse partnerStopOrderDates(Partner partner, Long student_id, List<String> dates, String start, String end, Integer statu) {
        ApiResponse response = new ApiResponse();
        Boolean status = true;
        if (statu != 1 ){
            status = false;
        }
        for (String str:dates) {
            response = partnerStopOrder(partner, student_id, str, start, end, status);
        }
        response.setMutationResult(new MutationResult());
        return response;
    }


}
