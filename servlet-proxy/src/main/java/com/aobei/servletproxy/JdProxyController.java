package com.aobei.servletproxy;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 京东 接口请求代理类
 * @author liyi
 *
 */
@Controller
public class JdProxyController {

	private static Logger logger = LoggerFactory.getLogger(JdProxyController.class);

	private static final String SERVER_URL = "https://api.jd.com/routerjson";
	
	private static CloseableHttpClient httpClient;

	static {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager.setMaxTotal(20);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(2);
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(8000).build();	// 8 秒超时
		poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
		httpClient = HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager).build();
	}

	
	@PostMapping("/routerjson")
	@ResponseBody
	public String router(HttpServletRequest request){
		RequestBuilder requestBuilder = RequestBuilder.post()
		  .setUri(SERVER_URL)
		  .setHeader("Accept", "text/xml,text/javascript,text/html")
		  .setHeader("User-Agent","360buy-sdk-java")
		  .setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		Enumeration<String> ps = request.getParameterNames();
		while(ps.hasMoreElements()){
			String name = ps.nextElement();
			String value = request.getParameter(name);
			logger.info("PARAM {}:{}", name, value);
			// 添加请求参数
			requestBuilder.addParameter(name, value);
		}
		try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(requestBuilder.build())){
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			String data = EntityUtils.toString(httpEntity, "utf-8");
			logger.info("jdapi return statuscode:{}",closeableHttpResponse.getStatusLine().getStatusCode());
			logger.info("jdapi return data:{}", data);
			return data;
		} catch (Exception e) {
			logger.error("ERROR", e);
			return String.format("{\"code\":\"%s\",\"msg\":\"%s\"}", "error" , "proxy execute exception -- " + e.getMessage());
		}
	}
}
