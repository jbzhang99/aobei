package com.aobei.trainconsole.controller;

import java.util.List;
import java.util.Map;

import com.aobei.train.model.*;
import com.aobei.train.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aobei.train.model.CustomerExample.Criteria;
import com.aobei.train.service.CustomerAddressService;
import com.aobei.train.service.CustomerService;
import com.aobei.train.service.OrderService;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Status;

@Controller
@RequestMapping("/customermanager")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService; 	
	
	@Autowired 
	private CustomerAddressService addressService;

	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/goto_customer_list")
	public String goto_customer_list(Model map,
			@RequestParam(defaultValue = "1")Integer p ,
			@RequestParam(defaultValue = "10") Integer ps,
			@RequestParam(required = false) Integer locked,
			@RequestParam(required = false) String phone){
		CustomerExample example = new CustomerExample();
		example.setOrderByClause(CustomerExample.C.create_datetime + " desc");
		Criteria criteria = example.or();
		if (locked != null) {
			criteria.andLockedEqualTo(locked);
		}
		if (!("".equals(phone)) && phone != null) {
			criteria.andPhoneEqualTo(phone);
		}
		Page<Customer> page = customerService.selectByExample(example,p,ps);
		List<Customer> customers = page.getList();
		map.addAttribute("customers", customers);
		map.addAttribute("page", page);
		map.addAttribute("locked", locked);
		map.addAttribute("phone", phone);
		return "customer/customer_list";
	}
	
	@ResponseBody
	@RequestMapping("/change_locked")
	public Object change_locked(Integer locked, Long customer_id, Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[customer] F[change_locked] U[{}] , params locked:{},customer_id:{} .",
				users.getUser_id(),locked,customer_id);
		Map<String, String> map = customerService.change_locked(locked, customer_id);
		logger.info("M[customer] F[change_locked] U[{}] , execute result:{}",
				users.getUser_id(),map.get("msg"));
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/cus_address")
	public Object cus_address(Long customer_id) {
		CustomerAddressExample example = new CustomerAddressExample();
		example.or().andCustomer_idEqualTo(customer_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<CustomerAddress> addresses = addressService.selectByExample(example);
		return addresses;
	}
	
	@RequestMapping("/order_view")
	public String order_view(Long customer_id,@RequestParam(defaultValue = "1")Integer p ,
			@RequestParam(defaultValue = "10") Integer ps,
			Model map) {
		OrderExample orderExample = new OrderExample();
		orderExample.setOrderByClause(OrderExample.C.create_datetime + " desc");
		orderExample.or().andUidEqualTo(customer_id);
		Page<Order> page = orderService.selectByExample(orderExample, p, ps);
		List<Order> list = page.getList();
		map.addAttribute("page", page);
		map.addAttribute("orders", list);
		map.addAttribute("customer_id", customer_id);
		return "customer/customer_orders";
	}
}
