package com.aobei.trainapi.configuration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.aobei.trainapi.schema.ApiCodeQuery;
import com.aobei.trainapi.util.SignUtil;
import com.vdurmont.emoji.EmojiParser;

/**
 * GraphQL 过滤
 * 
 * @author liyi
 *
 */
@Configuration
public class GraphQLFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(GraphQLFilter.class);

	private static String MTS = "mts";

	private static String SIGN = "sign";

	private static String NOSTR = "nostr";

	private static String QUERY = "query";

	private static String VARIABLES = "variables";
	
	private static String TIMESTAMP = "timestamp";

	private List<FilterWarp> filterWarps;

	@Bean
	public FilterRegistrationBean regGraphQLFilter(StringRedisTemplate redisTemplate,
			@Value("${forceMutation}") boolean forceMutation, 
			@Value("${forceSign}") boolean forceSign,
			@Value("#{${apiExpires}}")int apiExpires,
			@Value("${emojiApis:}") String[] emojiApis) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.addUrlPatterns("/graphql");
		GraphQLFilter graphQLFilter = new GraphQLFilter();
		graphQLFilter.filterWarps = Arrays.asList(
				// 请求数据过滤
				new RequestDataFilter(),
				// 幂等过滤
				new MtsFilter(redisTemplate, forceMutation),
				// apiExpires
				new TimestampFilter(apiExpires),
				// 签名过滤
				new SignFilter(forceSign),
				// Emoji remove
				new EmojiFilter(emojiApis));
		filterRegistrationBean.setFilter(graphQLFilter);
		return filterRegistrationBean;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest rs, ServletResponse rp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = new FilterHttpServletRequestWrapper((HttpServletRequest) rs);
		HttpServletResponse response = (HttpServletResponse) rp;
		Map<String, String> params = apiParams(request);
		Arrays.asList(SIGN, NOSTR, MTS, TIMESTAMP).forEach(t -> {
			if (!StringUtils.hasLength(params.get(t))) {
				// 从请求头中获取
				params.put(t, request.getHeader(t));
			}
		});
		JSONObject jsonObject = parseToken(request);
		if (filterWarps != null && jsonObject != null) {
			String client_id = jsonObject.getString("client_id");
			String uuid = jsonObject.getString("uuid");
			logger.info("[API_REQUEST] client_id:{} uuid:{} mst:{} nostr:{} timestamp:{} query:{} variables:{} sign:{}",
										client_id, 
										uuid,
										params.get(MTS),
										params.get(NOSTR),
										params.get(TIMESTAMP),
										params.get(QUERY),
										params.get(VARIABLES),
										params.get(SIGN));
			for (FilterWarp filter : filterWarps) {
				FilterError filterError = filter.doFilter(client_id, params, request);
				if (filterError != null) {
					String errorJSON = String.format(
							"{\"error\":\"%s\",\"error_description\":\"%s\",\"errcode\":\"%s\"}", filterError.error,
							filterError.error_description, filterError.errcode);
					response.setContentType("application/json;charset=UTF-8");
					response.getOutputStream().write(errorJSON.getBytes());
					response.setStatus(200);
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	/**
	 * 获取请求参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private Map<String, String> apiParams(HttpServletRequest request) throws IOException {
		Map<String, String> params = new LinkedHashMap<>();
		// multipart 模式
		if (ServletFileUpload.isMultipartContent(request)) {
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
			Enumeration<String> enumeration = multipartRequest.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				params.put(key, multipartRequest.getParameter(key));
			}
		} else if (request.getMethod().equalsIgnoreCase("post")) {
			// POST JSON body 模式
			String postData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			try {
				JSONObject object = JSON.parseObject(postData, Feature.OrderedField);
				for (String key : object.keySet()) {
					params.put(key, object.getString(key));
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		} else if (request.getMethod().equalsIgnoreCase("get")) {
			// GET 方式
			Enumeration<String> enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				params.put(key, request.getParameter(key));
			}
		}
		return params;
	}

	/**
	 * 解析token
	 * @param request
	 * @return
	 */
	private JSONObject parseToken(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (StringUtils.hasLength(auth)) {
			try {
				String data = auth.split("\\.")[1];
				String jsonData = new String(Base64.getDecoder().decode(data));
				return JSON.parseObject(jsonData);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return null;
	}

	public class FilterHttpServletRequestWrapper extends HttpServletRequestWrapper {

		private byte[] body; // 报文
		
		private Map<String,String> paramMap;

		public FilterHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			body = StreamUtils.copyToByteArray(request.getInputStream());
		}
		
		public FilterHttpServletRequestWrapper(HttpServletRequest request,Map<String,String> params) throws IOException {
			super(request);
			body = StreamUtils.copyToByteArray(request.getInputStream());
			this.paramMap = params;
		}
		
		public void setBody(byte[] bytes){
			this.body = bytes;
		}
		
		@Override
		public String getParameter(String name) {
			if(paramMap != null && paramMap.containsKey(name)){
				return paramMap.get(name);
			}
			return super.getParameter(name);
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}
		
		

		@Override
		public ServletInputStream getInputStream() throws IOException {
			final ByteArrayInputStream bais = new ByteArrayInputStream(body);
			return new ServletInputStream() {

				@Override
				public int read() throws IOException {
					return bais.read();
				}

				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setReadListener(ReadListener listener) {
				}
			};
		}

	}

	public class FilterError {
		String error;
		String error_description;
		String errcode;

		public FilterError(String error, String error_description, String errcode) {
			super();
			this.error = error;
			this.error_description = error_description;
			this.errcode = errcode;
		}
	}

	public interface FilterWarp {
		FilterError doFilter(String client_id, Map<String, String> params, HttpServletRequest request)
				throws IOException, ServletException;
	}

	public class RequestDataFilter implements FilterWarp {
	
		// 过滤JS IFRAME
		Pattern pattern = Pattern.compile(".*<[^><]*(?i)(script|iframe)[^><]*>.*", Pattern.DOTALL);
	
		@Override
		public FilterError doFilter(String client_id, Map<String, String> params, HttpServletRequest request)
				throws IOException, ServletException {
			String query = params.get(QUERY);
			String variables = params.get(VARIABLES);
			if (StringUtils.hasLength(query)) {
				String data = query + variables;
				if (pattern.matcher(data).find()) {
					return new FilterError("data_error", "malice data", "11005");
				}
			}
			return null;
		}
	
	}

	public class MtsFilter implements FilterWarp {

		private boolean forceMutation; // 强制比对 mutation 方式

		private StringRedisTemplate redisTemplate;

		public MtsFilter(StringRedisTemplate redisTemplate, boolean forceMutation) {
			super();
			this.redisTemplate = redisTemplate;
			this.forceMutation = forceMutation;
		}

		private Pattern pattern = Pattern.compile("\\s*mutation\\s*\\{.*\\}[^\\}]*", Pattern.DOTALL);

		@Override
		public FilterError doFilter(String client_id, Map<String, String> params, HttpServletRequest request)
				throws IOException, ServletException {
			String query = params.get(QUERY);
			String mts = params.get(MTS);
			// 强制 mutation 检查 & 无 mts 请求头
			if (forceMutation && !StringUtils.hasLength(mts)) {
				// 检查是否为 mutation 请求
				if (pattern.matcher(query).find()) {
					return new FilterError("no_mts_head", "mts head is require", "11001");
				}
			}

			if (StringUtils.hasLength(mts)) {
				String key = ApiCodeQuery.codeKey(mts);
				// code缓存检查
				if (redisTemplate.hasKey(key)) {
					redisTemplate.delete(key);
				} else {
					logger.info("Graphql invalid request {}", query);
					return new FilterError("invalid_request", "invalid mts head", "11002");
				}
			}
			return null;
		}
	}
	
	/**
	 * 
	 * Timestamp 过滤
	 * @author liyi
	 *
	 */
	public class TimestampFilter implements FilterWarp {

		private int expires;

		public TimestampFilter(int expires) {
			this.expires = expires;
		}

		@Override
		public FilterError doFilter(String client_id, Map<String, String> params, HttpServletRequest request)
				throws IOException, ServletException {
			String timestamp = params.get(TIMESTAMP);
			if (StringUtils.hasLength(timestamp)) {
				// 请求时间不匹配
				try {
					if (Math.abs(System.currentTimeMillis() / 1000 - Long.valueOf(timestamp)) > expires) {
						return new FilterError("invalid_timestamp", "invalid timestamp", "11009");
					}
				} catch (NumberFormatException e) {
					logger.error("",e);
					return new FilterError("invalid_timestamp", "invalid timestamp", "11009");
				}
			}
			return null;
		}
	}

	public class SignFilter implements FilterWarp {

		private boolean forceSign; // 强制签名

		private Map<String, String> signKeyMap;
		{
			signKeyMap = new HashMap<>();
			signKeyMap.put("wx_m_student", "bc8b946d-1c3f-47a7-a28f-f692184aef67");
			signKeyMap.put("wx_m_teacher", "cce38319-116b-404d-b69a-dc2fdd36941b");
			signKeyMap.put("wx_m_partner", "e2cb8f3c-987a-4a1f-896e-124664ba1137");
			signKeyMap.put("wx_m_custom", "ac928fb7-f11a-4d9f-894c-8283859bc914");
			signKeyMap.put("i_custom", "8dd2596c-4155-49e0-ba9e-29be82db07a9");
			signKeyMap.put("a_custom", "766bad8c-bfb1-4fa1-a425-c03697f8684d");
			signKeyMap.put("i_student", "3605d025-a91e-41e8-a54b-2ba42437343b");
			signKeyMap.put("a_student", "51827038-b4fe-4aa4-85d9-8ecee01bb11c");
			signKeyMap.put("i_partner", "ff2245c6-94eb-47f6-8dc2-7d5989529434");
			signKeyMap.put("a_partner", "47ec0a5d-ecd8-48e7-a294-f10de8b7ac36");
			signKeyMap.put("i_teacher", "9048c3bf-760c-47af-aa50-af9bdcd70fc3");
			signKeyMap.put("a_teacher", "40443706-ecc9-430e-9259-0973d6ec628a");
			signKeyMap.put("h5_custom", "40890fdd-862f-c5c6-3df0-d0ede77500a9");
		}

		public SignFilter(boolean forceSign) {
			super();
			this.forceSign = forceSign;
		}

		@Override
		public FilterError doFilter(String client_id, Map<String, String> params, HttpServletRequest request)
				throws IOException, ServletException {
			String sign = params.get(SIGN);
			String nostr = params.get(NOSTR);
			// 强制 sign 检查
			if (forceSign && (!StringUtils.hasLength(sign) || !StringUtils.hasLength(nostr))) {
				if (!StringUtils.hasLength(sign)) {
					return new FilterError("no_sign_param", "sign param is require", "11003");
				} else {
					return new FilterError("no_nostr_param", "nostr param is require", "11004");
				}
			}
			if (StringUtils.hasLength(sign) && StringUtils.hasLength(nostr)) {
				// 无client_id
				if (client_id == null) {
					return new FilterError("client_id_error", "client id error", "11007");
				}
				String sign_key = signKeyMap.get(client_id);
				// client_id 无效
				if (sign_key == null) {
					return new FilterError("client_id_invalid", "client id invalid", "11008");
				}
				// 签名校验
				if (!sign.equals(SignUtil.generateSign(params, sign_key))) {
					return new FilterError("sign_error", "sign error", "11006");
				}
			}
			return null;
		}

	}
	
	/**
	 * 过滤表情符
	 * @author liyi
	 *
	 */
	public class EmojiFilter implements FilterWarp {
		
		private String[] emojiApis;
		
		public EmojiFilter(String[] emojiApis) {
			super();
			this.emojiApis = emojiApis;
		}
		
		@Override
		public FilterError doFilter(String client_id, Map<String, String> params, HttpServletRequest request)
				throws IOException, ServletException {
			String query = params.get(QUERY);
			String variables = params.get(VARIABLES);
			if(StringUtils.hasLength(query)){
				// 检查是否在允许的API列表中
				if(emojiApis != null){
					for(String api : emojiApis){
						if(query.matches("(?s)\\s*mutation\\s*\\{\\s*" + api + "\\s*\\(.+\\}\\s*")){
							return null;
						}
					}
				}
				String temp1 = EmojiParser.removeAllEmojis(query);
				boolean reset = false;
				// query 检查
				if(!query.equals(temp1)){
					params.put(QUERY, temp1);
					reset = true;
				}
				// variables 检查
				if(StringUtils.hasLength(variables)){
					String temp2 = EmojiParser.removeAllEmojis(variables);
					if(!variables.equals(temp2)){
						params.put(VARIABLES, temp2);
						reset = true;
					}
				}
				if(reset){
					if (request.getMethod().equalsIgnoreCase("get")){
						// 设置get 方式参数
						request = new FilterHttpServletRequestWrapper(request,params);
					}else{
						String bodyStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
						String temp = EmojiParser.removeAllEmojis(bodyStr);
						FilterHttpServletRequestWrapper warp = (FilterHttpServletRequestWrapper) request;
						// 设置 body
						warp.setBody(temp.getBytes());
					}
				}
			}
			return null;
		}
		
	}

}
