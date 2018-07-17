package com.aobei.trainconsole.controller;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.sun.tracing.dtrace.ArgsAttributes;
import custom.util.DistanceUtil;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.aobei.train.IdGenerator;
import com.aobei.trainconsole.configuration.CustomProperties;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Constant;
import custom.bean.SmsMessage;
import custom.bean.SmsParams;

@Controller
public class TestController {
	
	@Autowired
	private CustomProperties customProperties;
	
	@Autowired
	private StringRedisTemplate template;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ServiceUnitService serviceUnitService;

	@Autowired
	private RefundService refundService;

	@Autowired
	private CompensationService compensationService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;

	@Autowired
	private FallintoService fallintoService;

	@RequestMapping("/test")
	public String test(
			Authentication authentication,
			HttpServletRequest request,
			HttpSession httpSession ,
			ModelMap modelMap,
			@RequestParam(defaultValue="1") int p){
		modelMap.addAttribute("test", "test");
		
		  if (authentication != null && StringUtils.hasText(authentication.getName())) {
			  modelMap.addAttribute("username", authentication.getName());
			  modelMap.addAttribute("principal", authentication.getPrincipal());
			  modelMap.addAttribute("pgt", getProxyGrantingTicket(authentication).orElse(null));
          }
          System.out.println(JSON.toJSONString(modelMap,true));
		
          Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          
          if (principal instanceof UserDetails) {
        	  String username = ((UserDetails)principal).getUsername();
        	  System.out.println("#1 username " + username);
          } else {
        	  String username = principal.toString();
        	  System.out.println("#2 username " + username);
          }
          
		//分页
		List<Object> list = new ArrayList<>(10);
		modelMap.addAttribute("page", new Page<Object>(list, 512, p, 10));
		return "test";
	}

	@RequestMapping("/testa")
	public String testa(HttpServletRequest request,HttpSession httpSession ,ModelMap modelMap){
		System.out.println("hasRoleA:" + request.isUserInRole("A"));
		return "test_cas";
	}
	
	@RequestMapping("/testb")
	public String testb(HttpServletRequest request,HttpSession httpSession ,ModelMap modelMap){
		System.out.println("hasRoleB:" + request.isUserInRole("B"));
		return "test_cas";
	}
	
	@RequestMapping("/401")
	public String test3(HttpServletRequest request,HttpSession httpSession ,ModelMap modelMap){
		modelMap.addAttribute("test", "401");
		return "test";
	}

	@RequestMapping("/test/ueditor")
	public String ueditor(){
		return "test_ueditor";
	}
	
	@RequestMapping(path="/test/user.json",produces="application/json; charset=UTF-8")
	@ResponseBody
	public String testJson(Authentication authentication){
		System.out.println("###" + authentication.getName());
		return "{\"name\":\"Lee\"}";
	}
	
	@RequestMapping("/test/msg")
	@ResponseBody
	public String msg() {
		SmsParams smsParams = new SmsParams();
		smsParams.setProduct_name("保洁清洗");
		smsParams.setPay_order_id(IdGenerator.generateId()+"");
		sendMsg(Constant.SEND_TO_PARTNER_WHEN_CUSTOMER_PAYED, "15313882039", Constant.SIGN_NAME, smsParams);
		return  "ssssssssssss";
	}
	
	public void sendMsg(String templateCode,String phoneNumber,String signName,SmsParams smsParams) {
		SmsMessage smsMessage  = new SmsMessage();
		smsMessage.setTemplateCode(templateCode);
		smsMessage.setPhoneNumber(phoneNumber);
		smsMessage.setSignName(signName);
		try {
		    smsMessage.setParams(smsParams);
			String json =  JacksonUtil.object_to_json(smsMessage);
		    
			template.convertAndSend(Constant.REDIS_SMS_TOPIC,json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}


	private Optional<String> getProxyGrantingTicket(Authentication authentication) {
        if (!(authentication instanceof CasAuthenticationToken)) {
            return Optional.empty();
        }
        AttributePrincipal principal = ((CasAuthenticationToken) authentication).getAssertion().getPrincipal();
        if (!(principal instanceof AttributePrincipalImpl)) {
            return Optional.empty();
        }
        Field field = ReflectionUtils.findField(AttributePrincipalImpl.class, "proxyGrantingTicket");
        ReflectionUtils.makeAccessible(field);
        return Optional.ofNullable(ReflectionUtils.getField(field, principal)).map(Object::toString);
    }

}
