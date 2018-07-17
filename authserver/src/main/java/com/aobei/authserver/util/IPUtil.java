package com.aobei.authserver.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IPUtil {
	
	private static CloseableHttpClient httpClient;

	static {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager.setMaxTotal(20);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(2);
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(3000).build();
		poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
		httpClient = HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager).build();
	}


	/**
	 * 百度 IP定位API <br>
	 * 每个key每天支持10万次调用，超过限制不返回数据。<br>
	 * IP定位的结果精度较差，主要应用获取省份或者城市的位置信息。移动平台的APP建议使用百度定位SDK 。
	 * 
	 * @param ip
	 *            ip地址
	 * @param ak
	 *            用户密钥 (必选，在lbs云官网注册的access key，作为访问的依据)
	 * @param sn
	 *            用户的权限签名 (可选，若用户所用ak的校验方式为sn校验时该参数必须。)
	 * @param coor
	 *            输出的坐标格式 (可选，coor不出现时，默认为百度墨卡托坐标；coor=bd09ll时，返回为百度经纬度坐标)
	 * @return 地址解析
	 */
	public static IpResult baiduParseIP(String ip, String ak, String sn, String coor) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri("http://api.map.baidu.com/location/ip")
				.addParameter("ip", ip).addParameter("ak", ak == null ? "" : ak)
				.addParameter("sn", sn == null ? "" : sn).addParameter("coor", coor == null ? "" : coor).build();

		try {
			CloseableHttpResponse response = httpClient.execute(httpUriRequest);
			String json = EntityUtils.toString(response.getEntity(), "utf-8");
			response.close();
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, IpResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 百度 IP定位API
	 * @param ip
	 * @return
	 */
	public static IpResult baiduParseIP(String ip) {
		return baiduParseIP(ip, "TBkRu9XDlwqs57QK1pON1gVsUuUkdkO3", null, null);
	}

	public static class IpResult {

		private String status;

		private String address;

		private Content content;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public boolean isSuccess() {
			return "0".equals(status);
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Content getContent() {
			return content;
		}

		public void setContent(Content content) {
			this.content = content;
		}

	}

	public static class Content {
		private String address;

		private AddressDetail address_detail;

		private Point point;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public AddressDetail getAddress_detail() {
			return address_detail;
		}

		public void setAddress_detail(AddressDetail address_detail) {
			this.address_detail = address_detail;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

	}

	public static class AddressDetail {

		private String city;
		private String city_code;
		private String district;
		private String province;
		private String street;
		private String street_number;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCity_code() {
			return city_code;
		}

		public void setCity_code(String city_code) {
			this.city_code = city_code;
		}

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getStreet_number() {
			return street_number;
		}

		public void setStreet_number(String street_number) {
			this.street_number = street_number;
		}

	}

	public static class Point {

		private String x;

		private String y;

		public String getX() {
			return x;
		}

		public void setX(String x) {
			this.x = x;
		}

		public String getY() {
			return y;
		}

		public void setY(String y) {
			this.y = y;
		}

	}

	public static void main(String[] args) {
		IpResult ipResult = baiduParseIP("124.64.77.142");
		System.out.println(JSON.toJSONString(ipResult, true));
	}
}
