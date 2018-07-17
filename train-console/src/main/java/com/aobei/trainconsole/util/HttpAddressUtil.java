package com.aobei.trainconsole.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 通过地址解析得到经纬度信息
 * 
 * @author adminL
 *
 */
public class HttpAddressUtil {

	//腾讯地图 API KEY
	private static final String Tencent_KEY = "2MOBZ-VPR6X-K3E4M-7PIE5-EC2Z7-PABQL";

	//高德地图 API KEY
	private static final String GaoDe_KEY = "2892ea3eeb60064cf5a6d6a932646ce6";

	/**
	 * 返回map对象，承载经纬度信息
	 * 请求腾讯地址接口
	 * @param address
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> coordinate_Tencent(String address) throws IOException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			// 创建http GET请求
			HttpUriRequest httpGet = RequestBuilder.get()
									.setUri("http://apis.map.qq.com/ws/geocoder/v1/")
									.addParameter("address", address)
									.addParameter("key", Tencent_KEY)
									.build();
			// 执行请求
			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				// 判断返回状态是否为200
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获取服务端返回的数据
					String content = EntityUtils.toString(response.getEntity(), "UTF-8");
					// FileUtils.writeStringToFile(new File("E:\\baidu.html"),
					// content, "UTF-8");
					if(content!=null){
						JSONObject parseObject = JSONObject.parseObject(content.toString());
						JSONObject jsonObject = parseObject.getJSONObject("result");
						JSONObject jsonObject2 = jsonObject.getJSONObject("location");
						// 经纬度信息存为string类型
						String lng = jsonObject2.getString("lng");
						String lat = jsonObject2.getString("lat");

						HashMap<String, String> map = new HashMap<String, String>();
						// 经度
						map.put("lng_b", lng);
						// 纬度
						map.put("lat_b", lat);
						return map;
					}
				}
			}
		}
		return null;
	}
	/**
	 * 返回map对象，承载经纬度信息
	 * 请求高德地址接口
	 * @param address
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> coordinate_GaoDe(String address,String city) throws IOException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			// 创建http GET请求
			HttpUriRequest httpGet = RequestBuilder.get()
					.setUri("http://restapi.amap.com/v3/geocode/geo")
					.addParameter("address", address)
					.addParameter("city", city)
					.addParameter("key", GaoDe_KEY)
					.build();
			// 执行请求
			try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
				// 判断返回状态是否为200
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获取服务端返回的数据
					String content = EntityUtils.toString(response.getEntity(), "UTF-8");
					// FileUtils.writeStringToFile(new File("E:\\baidu.html"),
					// content, "UTF-8");
					JSONObject parseObject = JSONObject.parseObject(content.toString());
					if (parseObject != null) {
						JSONArray geocodes = parseObject.getJSONArray("geocodes");
						JSONObject o = (JSONObject) geocodes.get(0);
						String location = o.getString("location");
						if (location != null) {
							// 经纬度信息存为string类型
							String[] split = location.split(",");
							String lng = null;
							String lat = null;
							for (int i = 0; i < split.length; i++) {
								lng = split[0];
								lat = split[1];
								break;
							}

							HashMap<String, String> map = new HashMap<String, String>();
							// 经度
							map.put("lng_b", lng);
							// 纬度
							map.put("lat_b", lat);
							return map;
						}
					}
				}
			}
		}
		return null;
	}

	/*public static void main(String args[]){
		try {
			Map<String, String> map = coordinate_GaoDe("北京市北京市海淀区海淀大街", "beijing");
			System.out.println(map.get("lat_b")+"-----纬度"+"       "+ map.get("lng_b")+"------经度     gaode");
			Map<String, String> tencent = coordinate_Tencent("北京市北京市海淀区海淀大街");
			System.out.println(tencent.get("lat_b")+"-----纬度" +"    "+ tencent.get("lng_b")+"-----经度      tencent");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
