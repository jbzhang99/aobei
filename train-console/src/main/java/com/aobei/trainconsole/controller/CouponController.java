package com.aobei.trainconsole.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.JacksonUtil;
import com.aobei.trainconsole.util.PoiExcelExport;
import com.aobei.trainconsole.util.WriteExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/couponmanager")
public class CouponController {

	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponReceiveService couponReceiveService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UsersService usersService;
	@Autowired
	StringRedisTemplate redisTemplate;
	@Autowired
	private OperateLogService operateLogService;



	private static Logger logger = LoggerFactory.getLogger(CouponController.class);


	/**
	 * 展示优惠卷列表页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/coupon_list")
	public String coupon_list(Model model,
							  @RequestParam(defaultValue = "1") Integer p,
							  @RequestParam(defaultValue = "10") Integer ps,
							  @RequestParam(required = false,value="begin") String begin,
							  @RequestParam(required = false,value="end") String end,
							  @RequestParam(required = false,value="valid_begin") String valid_begin,
							  @RequestParam(required = false,value="valid_end") String valid_end,
							  @RequestParam(required = false,defaultValue = "0") Integer cou_type) {

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		//只用来传递分页参数,降序排列
		CouponExample couponExample = new CouponExample();
		couponExample.setOrderByClause(CouponExample.C.create_time + " desc");
		CouponExample.Criteria or = couponExample.or();
		if(0!=cou_type){
			or.andTypeEqualTo(cou_type);
		}
		if(StringUtils.isNotEmpty(begin) && StringUtils.isNotEmpty(end)){
			Date d_begin = null;
			Date d_end = null;
			try {
				d_begin = sd.parse(begin);
				d_end = sd.parse(end);
				if(StringUtils.isNotEmpty(valid_begin) && StringUtils.isNotEmpty(valid_end)){
					or.andUse_start_datetimeGreaterThanOrEqualTo(sd.parse(valid_begin));
					or.andUse_end_datetimeLessThanOrEqualTo(sd.parse(valid_end));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			or.andCreate_timeBetween(d_begin,d_end);
		}
		Page<CouponList> page = couponService.xSelectCouponList(couponExample, p, ps,1);//1为优惠券运营列表，2为优惠券财务审核列表
		Long number = (long) (page.getPageNo() - 1) * page.getPageSize();

		model.addAttribute("begin",begin);
		model.addAttribute("end",end);
		model.addAttribute("valid_begin",valid_begin);
		model.addAttribute("valid_end",valid_end);
		model.addAttribute("cou_type",cou_type);
		model.addAttribute("number", number);
		model.addAttribute("page", page);
		model.addAttribute("current", p);
		model.addAttribute("list", page.getList());
		return "coupon/coupon_list";
	}

	/**
	 * 传递指定商品数据集合
	 *
	 * @param coupon_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/to_products")
	public Object toProducts(@RequestParam(value = "coupon_id") Long coupon_id) {
		HashMap<String, List<Product>> map = couponService.getProductsList(coupon_id);
		return map;
	}

	/**
	 * 优惠券使用记录列表页
	 *
	 * @param model
	 * @param p
	 * @param ps
	 * @return
	 */
	@RequestMapping("/record_detail")
	public String record_detail(Model model,
								@RequestParam(defaultValue = "1") Integer p,
								@RequestParam(defaultValue = "10") Integer ps,
								@RequestParam(value = "yh_id", required = false) String phone,
								@RequestParam(required = false) Long coupon_id,
								@RequestParam(required = false) Integer type,
								@RequestParam(required = false)String pay_order_id) {
		CouponReceiveExample couponReceiveExample = new CouponReceiveExample();
		couponReceiveExample.setOrderByClause(CouponReceiveExample.C.use_datetime + " desc");
		com.aobei.train.model.CouponReceiveExample.Criteria or = couponReceiveExample.or();
		if (StringUtils.isNotEmpty(phone)) {
			CustomerExample customerExample = new CustomerExample();
			customerExample.or().andPhoneEqualTo(phone);
			Customer customer = DataAccessUtils.singleResult(customerService.selectByExample(customerExample));
			if (customer != null) {
				or.andUidEqualTo(customer.getCustomer_id());
			}else{
				or.andUidEqualTo(1l);
			}
		}
		if(StringUtils.isNotEmpty(pay_order_id)){
			or.andPay_order_idEqualTo(pay_order_id);
		}
		if (coupon_id != null) {
			or.andCoupon_idEqualTo(coupon_id);
		}
		if (type != null) {
			if(type.equals(4)){
				List<Integer> list  = new ArrayList<>();
				list.add(3);
				list.add(4);
				or.andStatusIn(list);
			}else{
				or.andStatusEqualTo(type);
			}
		}
		or.andDeletedEqualTo(Status.DeleteStatus.no.value);
		Page<CouponReceiveList> page = couponService.xSelectRecordDetail(couponReceiveExample, p, ps);
		List<Coupon> list_coupon = couponService.selectByExample(new CouponExample());//检索所有优惠券

		model.addAttribute("page", page);
		model.addAttribute("list", page.getList());
		model.addAttribute("list_coupon", list_coupon);
		model.addAttribute("yh_id", phone);
		model.addAttribute("coupon_id", coupon_id);
		model.addAttribute("type", type);
		model.addAttribute("current", p);
		model.addAttribute("size",page.getList().size());
		model.addAttribute("pay_order_id",pay_order_id);
		return "coupon/record_detail";
	}

	/**
	 * 优惠券启用功能
	 *
	 * @param coupon_id ，参数为优惠券id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/coupon_on")
	public Object coupon_on(Long coupon_id, Authentication authentication) {
		HashMap<String, String> map = new HashMap<String, String>();
		Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
		if (coupon.getUse_end_datetime().before(new Date())) {
			map.put("message", coupon.getName() + "  启用失败,该优惠券已过期!");
			return map;
		}
		coupon.setValid(1);//启用
		int i = couponService.updateByPrimaryKey(coupon);

		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[coupon] F[启用coupon_on] U[{}];param[coupon_id:{}]; coupon:{}; result:{}",
				users.getUser_id(), coupon_id,coupon, i > 0 ? "成功" : "失败");
		map.put("message", String.format(coupon.getName() + "  启用%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 优惠券禁用功能
	 *
	 * @param coupon_id ，参数为优惠券id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/coupon_off")
	public Object coupon_off(Long coupon_id, Model model, Authentication authentication) {
		HashMap<String, String> map = new HashMap<String, String>();
		Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
		coupon.setValid(0);//禁用
		int i = couponService.updateByPrimaryKeySelective(coupon);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[coupon] F[禁用coupon_off] U[{}];param[coupon_id:{}]; coupon:{}; result:{}",
				users.getUser_id(), coupon_id, coupon, i > 0 ? "成功" : "失败");
		map.put("message", String.format(coupon.getName() + "  禁用%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 获取失效时间，在页面提示信息展示
	 *
	 * @param coupon_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getEnd_time")
	public Object getEnd_time(Long coupon_id) {
		HashMap<String, String> map = couponService.getEndTime(coupon_id);
		return map;
	}

	/**
	 * 派发优惠券功能（添加数据到使用记录列表）
	 *
	 * @param request
	 * @param coupon_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/distribute_coupon")
	public Object distribute_coupon(HttpServletRequest request, @RequestParam(required = false) Long coupon_id, Authentication authentication) {
		String ids = request.getParameter("ids");
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		Map<String, String> map = couponService.xDistribute_coupon(coupon_id, ids);
		logger.info("M[coupon] F[coupon_distribute] U[{}];param[用户手机号ids:{};coupon_id:{}] result:{}",
				users.getUser_id(), ids, coupon_id,map.get("message"));
		return map;
	}

	/**
	 * 跳转添加优惠券页面，携带分页参数
	 *
	 * @param p
	 * @param model
	 * @return
	 */
	@RequestMapping("/coupon_goto_add")
	public String coupon_goto_add(@RequestParam(required = false) Integer p, Model model) {

		model.addAttribute("current", p);
		return "coupon/coupon_add";
	}

	/**
	 * 传递页面加载的数据（分类和商品）
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/coupon_reload_pro")
	public Object coupon_reload_pro() {
		HashMap<String, Object> map = couponService.getCategoryProduct();
		return map;
	}

	/**
	 * 优惠券添加方法
	 *
	 * @param model
	 * @param coupon
	 * @param request
	 * @param use_start_datetime
	 * @param use_end_datetime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/coupon_add")
	public Object coupon_add(Model model, Coupon coupon, HttpServletRequest request,
							 @RequestParam(value = "start_datetime") String use_start_datetime,
							 @RequestParam(value = "end_datetime") String use_end_datetime,
							 Authentication authentication) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			//优惠条件模板
			String condition = null;
			switch (coupon.getCondition_type()) {
				case 1:
					//当订单总价满x时，对所有商品优惠
					String strCondition = "condition_" + coupon.getCondition_type();
					String price_1 = request.getParameter(strCondition);
					if (StringUtils.isEmpty(price_1)) {
						map.put("message", "未填写优惠条件金额,添加失败！");
						return map;
					}
					String condition_model_1 = request.getParameter("condition_model_" + coupon.getCondition_type());
					String replace_1 = condition_model_1.replace("x", price_1);

					Condition_type condition_type_1 = new Condition_type();
					condition_type_1.setTitle(replace_1);
					condition_type_1.setValue(formatPrice(price_1));
					//优惠条件类型为 1的优惠条件模板
					condition = JacksonUtil.object_to_json(condition_type_1);
					break;
				case 2:
					//当订单总价满x时，对指定商品优惠
					String strCondition_2 = "condition_" + coupon.getCondition_type();
					String price_2 = request.getParameter(strCondition_2);
					if (StringUtils.isEmpty(price_2)) {
						map.put("message", "未填写条件金额,添加失败！");
						return map;
					}
					//前台获取的指定商品集合
					String product_ids = request.getParameter("data_hidden_ids");
					JSONArray parseArray = JSONObject.parseArray(product_ids);
					List<Long> list_product = parseArray.toJavaList(Long.class);
					//转换模板
					String condition_model_2 = request.getParameter("condition_model_" + coupon.getCondition_type());
					String replace_2 = condition_model_2.replace("x", price_2);
					//转化json数据
					Condition_type condition_type_2 = new Condition_type();
					condition_type_2.setTitle(replace_2);
					condition_type_2.setValue(formatPrice(price_2));
					condition_type_2.setList_product(list_product);
					condition = JacksonUtil.object_to_json(condition_type_2);
					break;
				case 3:
					//对所有订单给予优惠

					//转换模板
					String condition_model_3 = request.getParameter("condition_model_" + coupon.getCondition_type());
					//转化json数据
					Condition_type condition_type_3 = new Condition_type();
					condition_type_3.setTitle(condition_model_3);
					condition = JacksonUtil.object_to_json(condition_type_3);
					break;
				case 4:
					//转换模板
					String condition_model_4 = request.getParameter("condition_model_" + coupon.getCondition_type());

					//前台获取的指定商品集合
					String product_ids_4 = request.getParameter("data_hidden_ids_4");
					JSONArray parseArray_4 = JSONObject.parseArray(product_ids_4);
					List<Long> list_product_4 = parseArray_4.toJavaList(Long.class);
					//转化json数据
					Condition_type condition_type_4 = new Condition_type();
					condition_type_4.setList_product(list_product_4);
					condition_type_4.setTitle(condition_model_4);
					condition = JacksonUtil.object_to_json(condition_type_4);
			/*//转化json数据
			Condition_type condition_type_4 = new Condition_type();
			condition = JacksonUtil.object_to_json(condition_type_4);*/
					break;
				default:
					map.put("message", "添加失败，请重新选择优惠条件！");
					return map;
			}

			//优惠方案模板
			String programme = null;

			//组装name
			String strProgramme = "programme_" + coupon.getProgramme_type();
			//获取到输入的金额
			String discount = request.getParameter(strProgramme);
			if (StringUtils.isEmpty(discount)) {
				map.put("message", "未填写优惠方案金额，添加失败！");
				return map;
			}
			switch (coupon.getProgramme_type()) {
				case 1:
					//订单以固定折扣出售
					String programme_model_1 = request.getParameter("programme_model_" + coupon.getProgramme_type());
					String replace_1 = programme_model_1.replace("x", discount);
					//转换json数据
					Programme_type programme_type_1 = new Programme_type();
					programme_type_1.setTitle(replace_1);
					programme_type_1.setValue(Integer.parseInt(discount));
					programme = JacksonUtil.object_to_json(programme_type_1);
					break;
				case 2:
					//订单以固定折扣出售
					//组装模板
					String programme_model_2 = request.getParameter("programme_model_" + coupon.getProgramme_type());
					String replace_2 = programme_model_2.replace("x", discount);
					//转换json数据
					Programme_type programme_type_2 = new Programme_type();
					programme_type_2.setTitle(replace_2);
					programme_type_2.setValue(formatPrice(discount));
					programme = JacksonUtil.object_to_json(programme_type_2);
					break;
					/*case 3:
					//订单以固定折扣出售
					String strProgramme_3 = "programme_"+coupon.getProgramme_type();
					String discount_3 = request.getParameter(strProgramme_3);
					if(discount_3==""){
						map.put("message", "未填写优惠方案金额");
						return map;
					}

					String programme_model_3 = request.getParameter("programme_model_"+coupon.getProgramme_type());
					String replace_3 = programme_model_3.replace("x", discount);
					//转换json数据
					Programme_type programme_type_3 = new Programme_type();
					programme_type_3.setTitle(replace_3);
					programme_type_3.setValue(Integer.parseInt(discount)*100);
					programme = JacksonUtil.object_to_json(programme_type_3);
					break;*/
				default:
					map.put("message", "添加失败，请重新选择优惠方案！");
					return map;
			}


			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date t_start = null;
			Date t_end = null;
			try {
				t_start = simpleDateFormat.parse(use_start_datetime);
				t_end = simpleDateFormat.parse(use_end_datetime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//开始插入数据
			coupon.setCoupon_id(IdGenerator.generateId());
			coupon.setCondition(condition);
			coupon.setProgramme(programme);
			coupon.setExclusive(1);//默认排他
			//coupon.setNum_able(coupon.getNum_total());
			coupon.setUse_start_datetime(t_start);
			coupon.setUse_end_datetime(t_end);
			coupon.setCreate_time(new Date());//创建时间
			coupon.setReceive_start_datetime(t_start);
			coupon.setReceive_end_datetime(t_end);
			/*if(coupon.getType()==Status.CouponType.exchange_type.value){

				*//*String code = ValidCode();
				coupon.setExchange_code(code);*//*
				GenerateExchangeCode generateExchangeCode = new GenerateExchangeCode();
					List<CouponReceive> list = generateExchangeCode.getExchangeCodeByNumber(coupon.getNum_total(), coupon.getCoupon_id());
					couponReceiveService.batchInsertSelective(list.toArray(new CouponReceive[list.size()]));
				coupon.setNum_able(0);//优惠券为兑换券时，默认全部为待领取状态，已用完。
			}*/
			if(coupon.getType()!=Status.CouponType.compensation_type.value){
				coupon.setValid(0);//无效的
			}
			coupon.setVerify(0);//未审核的
			if (coupon.getNum_limit() == 1) {
				coupon.setNum_able(coupon.getNum_total());
				String key = Constant.getCouponKey(coupon.getCoupon_id());
				//String able_num = "able_"+coupon.getCoupon_id();
				if (!redisTemplate.hasKey(key)) {
					redisTemplate.opsForValue().set(key, coupon.getNum_able().toString());
				}
			}
			Users users = usersService.xSelectUserByUsername(authentication.getName());
			coupon.setCreate_user(users.getUser_id());
			int i = couponService.insertSelective(coupon);

			logger.info("M[coupon] F[add_coupon] U[{}];param[coupon:{}] result:{}",
					users.getUser_id(), coupon, String.format("优惠券添加%s!", i > 0 ? "成功" : "失败"));
			map.put("message", String.format("优惠券添加%s!", i > 0 ? "成功" : "失败"));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return map;
	}
	/*//获取优惠券兑换码并验证是否重复
	private String ValidCode(){
		String code = GenerateExchangeCode.getExchangeCode();
		CouponReceiveExample example = new CouponReceiveExample();
		example.or().andExchange_codeEqualTo(code);
		long l = couponReceiveService.countByExample(example);
		if(l>0){
			ValidCode();
		}else{
			return code;
		}
		return code;
	}*/
	/**
	 * 跳转编辑页面，提供回显数据
	 *
	 * @param model
	 * @param p
	 * @param coupon_id
	 * @return
	 */
	@RequestMapping("/coupon_goto_edit")
	public String coupon_goto_edit(Model model,
								   @RequestParam(required = false) Integer p,
								   @RequestParam(value = "coupon_id") Long coupon_id) {
		Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
		try {
			String condition = coupon.getCondition();
			Condition_type condition_type = JacksonUtil.json_to_object(condition, Condition_type.class);
			String programme = coupon.getProgramme();
			Programme_type programme_type = JacksonUtil.json_to_object(programme, Programme_type.class);
			model.addAttribute("condition_type", condition_type);
			model.addAttribute("programme_type", programme_type);
			//向前台传递商品集合数据
			if (condition_type.getList_product() != null) {
				List<Product> productList = new ArrayList<Product>();
				List<Long> list_product = condition_type.getList_product();
				for (Long product_id : list_product) {
					ProductExample productExample = new ProductExample();
					productExample.or().andProduct_idEqualTo(product_id);
					Product product = productService.selectByPrimaryKey(product_id);
					if(product!=null){
						productList.add(product);
					}
				}
				String string = JacksonUtil.object_to_json(productList);
				model.addAttribute("productList", string);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		model.addAttribute("coupon", coupon);
		model.addAttribute("start_time1",format.format(coupon.getUse_start_datetime()));
		model.addAttribute("end_time1",format.format(coupon.getUse_end_datetime()));
		model.addAttribute("coupon_id", coupon.getCoupon_id());
		model.addAttribute("current", p);
		return "coupon/coupon_edit";
	}

	/**
	 * 优惠券修改方法
	 *
	 * @param model
	 * @param coupon
	 * @param request
	 * @param use_start_datetime
	 * @param use_end_datetime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/coupon_edit")
	public Object coupon_edit(Model model, Coupon coupon, HttpServletRequest request,
							  @RequestParam(value = "start_datetime") String use_start_datetime,
							  @RequestParam(value = "end_datetime") String use_end_datetime,
							  Authentication authentication) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			//优惠条件模板
			String condition = null;
			switch (coupon.getCondition_type()) {
				case 1:
					//当订单总价满x时，对所有商品优惠
					String strCondition = "condition_" + coupon.getCondition_type();
					String price_1 = request.getParameter(strCondition);
					if (StringUtils.isEmpty(price_1)) {
						map.put("message", "未填写优惠条件金额,修改失败！");
						return map;
					}
					String condition_model_1 = request.getParameter("condition_model_" + coupon.getCondition_type());
					String replace_1 = condition_model_1.replace("x", price_1);

					Condition_type condition_type_1 = new Condition_type();
					condition_type_1.setTitle(replace_1);
					condition_type_1.setValue(formatPrice(price_1));
					//优惠条件类型为 1的优惠条件模板
					condition = JacksonUtil.object_to_json(condition_type_1);
					break;
				case 2:
					//当订单总价满x时，对指定商品优惠
					String strCondition_2 = "condition_" + coupon.getCondition_type();
					String price_2 = request.getParameter(strCondition_2);
					if (StringUtils.isEmpty(price_2)) {
						map.put("message", "未填写条件金额，修改失败！");
						return map;
					}
					//前台获取的指定商品集合
					String product_ids = request.getParameter("data_hidden_ids");
					JSONArray parseArray = JSONObject.parseArray(product_ids);
					List<Long> list_product = parseArray.toJavaList(Long.class);
					//转换模板
					String condition_model_2 = request.getParameter("condition_model_" + coupon.getCondition_type());
					String replace_2 = condition_model_2.replace("x", price_2);
					//转化json数据
					Condition_type condition_type_2 = new Condition_type();
					condition_type_2.setTitle(replace_2);
					condition_type_2.setValue(formatPrice(price_2));
					condition_type_2.setList_product(list_product);
					condition = JacksonUtil.object_to_json(condition_type_2);
					break;
				case 3:
					//对所有订单给予优惠

					//转换模板
					String condition_model_3 = request.getParameter("condition_model_" + coupon.getCondition_type());
					//转化json数据
					Condition_type condition_type_3 = new Condition_type();
					condition_type_3.setTitle(condition_model_3);
					condition = JacksonUtil.object_to_json(condition_type_3);
					break;
				case 4:
					//转换模板
					String condition_model_4 = request.getParameter("condition_model_" + coupon.getCondition_type());

					//前台获取的指定商品集合
					String product_ids_4 = request.getParameter("data_hidden_ids_4");
					JSONArray parseArray_4 = JSONObject.parseArray(product_ids_4);
					List<Long> list_product_4 = parseArray_4.toJavaList(Long.class);
					//转化json数据
					Condition_type condition_type_4 = new Condition_type();
					condition_type_4.setList_product(list_product_4);
					condition_type_4.setTitle(condition_model_4);
					condition = JacksonUtil.object_to_json(condition_type_4);
			/*//转化json数据
			Condition_type condition_type_4 = new Condition_type();
			condition = JacksonUtil.object_to_json(condition_type_4);*/
					break;
				default:
					map.put("message", "添加失败，请重新选择优惠条件！");
					return map;
			}

			//优惠方案模板
			String programme = null;

			//组装name
			String strProgramme = "programme_" + coupon.getProgramme_type();
			//获取到输入的金额
			String discount = request.getParameter(strProgramme);
			if (StringUtils.isEmpty(discount)) {
				map.put("message", "未填写优惠方案金额，修改失败！");
				return map;
			}
			String programme_model_1 = request.getParameter("programme_model_" + coupon.getProgramme_type());
			String replace_1 = programme_model_1.replace("x", discount);
			switch (coupon.getProgramme_type()) {
				case 1:
					//转换json数据
					Programme_type programme_type_1 = new Programme_type();
					programme_type_1.setTitle(replace_1);
					programme_type_1.setValue(Integer.parseInt(discount));
					programme = JacksonUtil.object_to_json(programme_type_1);
					break;
				case 2:
			/*//组装模板
			String programme_model_2 = request.getParameter("programme_model_"+coupon.getCondition_type());
			String replace_2 = programme_model_2.replace("x", discount);*/
					//转换json数据
					Programme_type programme_type_2 = new Programme_type();
					programme_type_2.setTitle(replace_1);
					programme_type_2.setValue(formatPrice(discount));
					programme = JacksonUtil.object_to_json(programme_type_2);
					break;
		/*case 3:
			String programme_model_3 = request.getParameter("programme_model_"+coupon.getCondition_type());
			String replace_3 = programme_model_3.replace("x", discount);
			//转换json数据
			Programme_type programme_type_3 = new Programme_type();
			programme_type_3.setTitle(replace_1);
			programme_type_3.setValue(Integer.parseInt(discount)*100);
			programme = JacksonUtil.object_to_json(programme_type_3);
			break;*/
				default:
					map.put("message", "修改失败，请重新选择优惠方案！");
					return map;
			}

			//转换时间类型
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date t_start = simpleDateFormat.parse(use_start_datetime);
			Date t_end = simpleDateFormat.parse(use_end_datetime);
			Coupon resultSingle = couponService.selectByPrimaryKey(coupon.getCoupon_id());
			//开始插入数据
			if (coupon.getNum_limit() == 1) {//有总数量

				//coupon.setNum_able(resultSingle.getNum_able());
				String key = Constant.getCouponKey(coupon.getCoupon_id());
				if (!redisTemplate.hasKey(key)) {
					if(coupon.getNum_able()>0){
						redisTemplate.opsForValue().set(key,coupon.getNum_able()+"");
					}
				}
				coupon.setNum_able(coupon.getNum_total());
				redisTemplate.opsForValue().set(key,coupon.getNum_able()+"");
				if(Status.CouponType.exchange_type.value==coupon.getType()){
					coupon.setNum_able(0);
				}
			}
			coupon.setCoupon_id(Long.parseLong(request.getParameter("coupon_id")));
			coupon.setCondition(condition);
			coupon.setProgramme(programme);
			coupon.setExclusive(1);//默认排他
			coupon.setUse_start_datetime(t_start);
			coupon.setUse_end_datetime(t_end);
			//coupon.setReceive_start_datetime(resultSingle.getUse_start_datetime());
			//coupon.setReceive_end_datetime(resultSingle.getReceive_end_datetime());
			/*if(StringUtils.isEmpty(resultSingle.getExchange_code()) & coupon.getType()==Status.CouponType.exchange_type.value){
				String code = ValidCode();
				coupon.setExchange_code(code);
			}*/
			int i = couponService.updateByPrimaryKeySelective(coupon);
			Users users = usersService.xSelectUserByUsername(authentication.getName());
			logger.info("M[coupon] F[update_coupon] U[{}];param[coupon:{}]; result:{}",
					users.getUser_id(), coupon, String.format("优惠券修改%s!", i > 0 ? "成功" : "失败"));
			map.put("message", String.format("优惠券修改%s!", i > 0 ? "成功" : "失败"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return map;
	}

	public Integer formatPrice(String price) {
		double parseDouble = Double.parseDouble(price) * 100;
		String data = String.valueOf(parseDouble);
		String s = data.substring(0, data.indexOf("."));
		return Integer.parseInt(s);
	}
	/*@RequestMapping("/getCode")
	@ResponseBody
	public Object getCode(Long coupon_id){
		Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
		HashMap<String, String> map = new HashMap<String, String>();
		//map.put("code", coupon.getExchange_code());
		map.put("name",coupon.getName());
		return map;
	}*/

	/**
	 * 优惠券兑换码导出
	 * @param coupon_id
	 * @param response
	 */
	@RequestMapping("export_exchange_code")
	public void export_exchange_code(@RequestParam(value="coupon_id") Long coupon_id, HttpServletResponse response){
		CouponReceiveExample example = new CouponReceiveExample();
		example.or()
				.andCoupon_idEqualTo(coupon_id)
				.andDeletedEqualTo(Status.DeleteStatus.no.value)//未删除
				.andStatusEqualTo(1);//待领取
		List<CouponReceive> list = couponReceiveService.selectByExample(example);
		String name = couponService.selectByPrimaryKey(coupon_id).getName();
		String titleName[] = {"兑换码"};
		int titleSize[] = {20};
		String titleColumn[] = {"exchange_code"};
		PoiExcelExport export = new PoiExcelExport(response, name+"的兑换码", name);
		export.wirteExcel(titleColumn, titleName, titleSize, list);

	}

	/**
	 * 优惠券提审
	 * @param coupon_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("coupon_put")
	public Object coupon_put(long coupon_id){
		HashMap<String, String> map = new HashMap<String, String>();
		Coupon coupon = new Coupon();
		coupon.setCoupon_id(coupon_id);
		coupon.setVerify(1);
		coupon.setDeliver_datetime(new Date());
		int i = couponService.updateByPrimaryKeySelective(coupon);
		map.put("result",String.format("提审%s，请等待结果",i>0?"成功":"失败"));
		return map;
	}

	/**
	 * 获取审核信息内容
	 * @param coupon_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getVerifyCom")
	public Object getVerifyCom(Long coupon_id) {
		HashMap<Object, Object> map = new HashMap<>();
		Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
		map.put("result",coupon.getVerify_comments());
		return map;
	}

	/**
	 * 优惠券信息导出
	 */
	@RequestMapping("/coupon_export")
	public void export_coupon(@RequestParam(required = false,value="begin") String begin,
							  @RequestParam(required = false,value="end") String end,
							  @RequestParam(required = false,value="valid_begin") String valid_begin,
							  @RequestParam(required = false,value="valid_end") String valid_end,
							  @RequestParam(required = false,defaultValue = "0") Integer cou_type,
							  Authentication authentication,HttpServletResponse response){

		String titleName[] ={"优惠券名称","优惠券类型","总数量","已领用","已下单","模板规则","生效时间","失效时间","状态","审核状态","审核人"};
		CouponExample example = new CouponExample();
		CouponExample.Criteria or = example.or();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(0!=cou_type){
			or.andTypeEqualTo(cou_type);
		}
		if(StringUtils.isNotEmpty(begin) && StringUtils.isNotEmpty(end)){
			Date d_begin = null;
			Date d_end = null;
			try {
				d_begin = sd.parse(begin);
				d_end = sd.parse(end);
				if(StringUtils.isNotEmpty(valid_begin) && StringUtils.isNotEmpty(valid_end)){
					or.andUse_start_datetimeGreaterThanOrEqualTo(sd.parse(valid_begin));
					or.andUse_end_datetimeLessThanOrEqualTo(sd.parse(valid_end));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			or.andCreate_timeBetween(d_begin,d_end);
		}
		example.setOrderByClause(CouponExample.C.create_time+" desc");
		List<Object[]> objects = couponService.xSelectCouponList(example,1);//1为优惠券运营列表
		WriteExcel writeExcel = new WriteExcel(titleName,objects);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(new Date());

		try {
			StringBuilder str = new StringBuilder();
			str.append(format);
			str.append("优惠券信息.xls");
			response.setContentType("application/vnd.ms-excel; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(str.toString(), "UTF-8"));
			InputStream export = writeExcel.export();
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			byte[] bytes = new byte[1024];
			int read = export.read(bytes);
			while (read!=-1){
				out.write(bytes,0,read);
				out.flush();
				read = export.read(bytes);
			}
			out.flush();
			export.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[couponInfo] F[优惠券信息导出coupon_export] U[{}];param[创建开始时间begin:{},创建结束时间end:{},有效期开始valid_begin:{},有效期结束valid_end:{},优惠券类型cou_type:{}]; result:{优惠券信息导出成功}",
				users.getUser_id(),begin, end,valid_begin,valid_end,cou_type);
	}
}
