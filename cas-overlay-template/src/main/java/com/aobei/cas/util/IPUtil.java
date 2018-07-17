package com.aobei.cas.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IPUtil {

	/**
	 * 解析IP 地址
	 * 
	 * @param ip
	 * @return
	 */
	public static IPData parseIP(String ip) {
		HttpUriRequest httpUriRequest = RequestBuilder.get()
				.setUri("https://int.dpool.sina.com.cn/iplookup/iplookup.php").addParameter("format", "json")
				.addParameter("ip", ip).build();

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			return httpClient.execute(httpUriRequest, new ResponseHandler<IPData>() {

				@Override
				public IPData handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					String data = EntityUtils.toString(response.getEntity());
					ObjectMapper objectMapper = new ObjectMapper();
					return objectMapper.readValue(data, IPData.class);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class IPData {
		private Integer ret;
		private Integer start;
		private Integer end;
		private String country;
		private String province;
		private String city;
		private String district;
		private String isp;
		private String type;
		private String desc;

		public Integer getRet() {
			return ret;
		}

		public void setRet(Integer ret) {
			this.ret = ret;
		}

		public Integer getStart() {
			return start;
		}

		public void setStart(Integer start) {
			this.start = start;
		}

		public Integer getEnd() {
			return end;
		}

		public void setEnd(Integer end) {
			this.end = end;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public String getIsp() {
			return isp;
		}

		public void setIsp(String isp) {
			this.isp = isp;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

	}
}
