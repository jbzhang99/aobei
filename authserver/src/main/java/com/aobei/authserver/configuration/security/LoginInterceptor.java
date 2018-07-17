package com.aobei.authserver.configuration.security;

import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aobei.authserver.model.Channel;
import com.aobei.authserver.model.LoginLog;
import com.aobei.authserver.repository.ChannelRepository;
import com.aobei.authserver.repository.UserRepository;
import com.aobei.authserver.util.IPUtil;
import com.aobei.authserver.util.IPUtil.IpResult;
import com.aobei.authserver.util.IdGenerator;

/**
 * 登录拦截
 * 
 * @author liyi
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	private static ThreadLocal<LoginLog> threadLocal = new ThreadLocal<>();

	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 保存登录日志
	 * 
	 * @param user_id
	 *            user_id
	 * @param client_type
	 *            系统类型 <br>
	 *            1 后台系统 <br>
	 *            2 微信小程序 学员端 <br>
	 *            3 微信小程序 老师端 <br>
	 *            4 微信小程序 合伙人端 <br>
	 *            5 微信小程序 顾客端
	 * @param login_type
	 *            登入方式 <br>
	 *            1 用户密码 <br>
	 *            2 微信小程序
	 */
	public static void storeLoginLog(Long user_id) {
		LoginLog loginLog = new LoginLog();
		loginLog.setUser_id(user_id);
		threadLocal.set(loginLog);
	}

	/**
	 * 无效的登录 清除登录数据
	 */
	public static void noLogin() {
		threadLocal.remove();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.hasLength(ip) || !ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			ip = request.getRemoteAddr();
			if ("0:0:0:0:0:0:0:1".equals(ip)) {
				ip = "127.0.0.1";
			}
		}
		// 设置ip
		RequestContextHolder.getRequestAttributes().setAttribute("IP", ip, 0);
		
		String channelCode = request.getHeader("channel");
		int channelId = 0;
		if(StringUtils.hasText(channelCode)){
			// 获取渠道信息
			Channel channel = channelRepository.findByCode(channelCode);
			if(Objects.nonNull(channel)){
				channelId = channel.getChannel_id();
			}
		}
		// 设置channelId
		RequestContextHolder.getRequestAttributes().setAttribute("channel", channelId, 0);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 日志处理 <br>
	 * Duuid 网页生成方式 <br>
	 * 引入JS <br>
	 * https://cdnjs.cloudflare.com/ajax/libs/fingerprintjs2/1.6.1/fingerprint2.
	 * min.js <br>
	 * 获取当前浏览器的Duuid <br>
	 * http://valve.github.io/fingerprintjs2/ <br>
	 * 
	 * Nginx 添加配置 <br>
	 * proxy_set_header Host $host;<br>
	 * proxy_set_header X-Real-IP $remote_addr;<br>
	 * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;<br>
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		LoginLog loginLog = threadLocal.get();
		if (ex == null && loginLog != null) {
			String grant_type = request.getParameter("grant_type");
			String ip = RequestContextHolder.getRequestAttributes().getAttribute("IP", 0).toString();
			if (!ip.equals("127.0.0.1")) {
				IpResult ipData = IPUtil.baiduParseIP(ip);
				if (ipData != null && ipData.getContent() != null && ipData.getContent().getAddress_detail() != null) {
					//loginLog.setCountry();
					loginLog.setProvince(ipData.getContent().getAddress_detail().getProvince());
					loginLog.setCity(ipData.getContent().getAddress_detail().getCity());
				}
			}
			loginLog.setLogin_log_id(IdGenerator.generateId());
			loginLog.setIp(ip);
			loginLog.setUser_agent(request.getHeader("User-Agent"));
			loginLog.setDuuid(handlerDuuid(request.getHeader("Duuid")));
			loginLog.setCreate_datetime(new Date());
			loginLog.setActive_time(0);
			loginLog.setLogout(0);

			// 设置客户端类型
			int client_type = 1;
			switch (request.getRemoteUser()) {
			case "wx_m_student":
				client_type = 2;
				break;
			case "wx_m_teacher":
				client_type = 3;
				break;
			case "wx_m_partner":
				client_type = 4;
				break;
			case "wx_m_custom":
				client_type = 5;
				break;
			case "i_custom":
				client_type = 6;
				break;
			case "a_custom":
				client_type = 7;
				break;
			case "i_student":
				client_type = 8;
				break;
			case "a_student":
				client_type = 9;
				break;
			case "i_partner":
				client_type = 10;
				break;
			case "a_partner":
				client_type = 11;
				break;
			case "h5_custom":
				client_type = 12;
				break;
			}

			loginLog.setClient_type(client_type);

			switch (grant_type) {
			case "password":
				loginLog.setLogin_type(1);
				break;
			case "wxm_code":
				loginLog.setLogin_type(2);
				break;
			case "wxa_code":
				loginLog.setLogin_type(3);
				break;
			case "sms_code":
				loginLog.setLogin_type(4);
				break;
			default:
				return;
			}
			userRepository.saveLoginLog(loginLog);
		}

	}

	/**
	 * 生成duuid
	 * 
	 * @param duuid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String handlerDuuid(String duuid) {
		if (duuid != null && duuid.matches("\\s*\\{.*\\}\\s*")) {
			try {
				JSONObject jsonObject = JSON.parseObject(duuid);
				jsonObject.remove("version");
				jsonObject.remove("system");
				return DigestUtils.shaHex(jsonObject.toJSONString());
			} catch (Exception e) {
				logger.error("", e);
			}
			return DigestUtils.shaHex(duuid);
		}
		return duuid;
	}
}
