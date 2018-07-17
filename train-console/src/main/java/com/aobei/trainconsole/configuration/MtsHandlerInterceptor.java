package com.aobei.trainconsole.configuration;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 重复提交过滤
 * 
 * @author liyi
 *
 */

public class MtsHandlerInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(MtsHandlerInterceptor.class);

	private StringRedisTemplate redisTemplate;

	private ValueOperations<String, String> opsValue;
	
	private boolean inited;
	
	public MtsHandlerInterceptor(StringRedisTemplate redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		this.opsValue = redisTemplate.opsForValue();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(!inited){
			request.getSession().getServletContext().setAttribute("mts", new MTS());
			inited = true;
		}

		String mts = request.getParameter("mts");
		String key = codeKey(mts);
		if (StringUtils.hasLength(mts)) {
			// code缓存检查
			if (redisTemplate.hasKey(key)) {
				logger.info("[repeat_request] uri:{}", request.getRequestURI());
				// ajax 请求
				if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
					response.setHeader("Content-Type","application/json;charset=UTF-8");
					response.getOutputStream().write("{\"error\":\"repeat_request\"}".getBytes());
				}else{
					response.sendRedirect("/repeat_request");
				}
				return false;
			} else {
				opsValue.set(key, "1", 60 * 30, TimeUnit.SECONDS);
			}
		}
		return true;
	}

	private String codeKey(String code) {
		return "CONSOLE_MTS:" + code;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
	public class MTS{
		
		public String getCode(){
			return System.currentTimeMillis() + "_" + RandomStringUtils.randomNumeric(2);
		}

		@Override
		public String toString() {
			return getCode();
		}
		
	}

}
