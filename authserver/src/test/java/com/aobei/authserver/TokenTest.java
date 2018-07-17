package com.aobei.authserver;

import java.util.Base64;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;

public class TokenTest {
	
	private static String BASE_API_URI = "https://test-api.aobei.com";
	
	private static String BASE_URI = "https://test-auth.aobei.com";
	
	private static String client_id = "wx_m_custom";
	
	private static String secret = "4x91b74e-3b7a-bb6x-btv9-qzcio7jk6g7f";
	
	/**
	 * 获取系统时间
	 * @return
	 */
	public static long server_time(){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(RequestBuilder.get().setUri(BASE_API_URI + "/server/time").build());
			String jsondata = EntityUtils.toString(response.getEntity());
			response.close();
			httpClient.close();
			return JSON.parseObject(jsondata).getLong("second");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}
	
	/**
	 * 通过手机号获取 token 信息
	 * @param phone
	 * @return
	 */
	public static JSONObject tokenByPhone(String phone){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestBuilder rb = RequestBuilder.post()
					.addHeader("Authorization" , "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + secret).getBytes()))
					.setUri(BASE_URI + "/oauth/token")
					.addParameter("grant_type", "sms_code")
					.addParameter("phone", phone);
			CloseableHttpResponse response = httpClient.execute(rb.build());
			String jsondata = EntityUtils.toString(response.getEntity());
			response.close();
			String code = JSON.parseObject(jsondata).getString("error_description").split(":")[1];
			rb.addParameter("code", code);
			
			response = httpClient.execute(rb.build());
			jsondata = EntityUtils.toString(response.getEntity());
			response.close();
			httpClient.close();
			return JSON.parseObject(jsondata);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
