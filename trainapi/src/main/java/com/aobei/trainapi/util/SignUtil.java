package com.aobei.trainapi.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import weixin.popular.util.MapUtil;

public class SignUtil {

	/**
	 * 生成签名
	 * @param map
	 * @param sign_key
	 * @return
	 */
	public static String generateSign(Map<String, String> map, String sign_key){
		Map<String, String> tmap = MapUtil.order(map);
		if(tmap.containsKey("sign")){
			tmap.remove("sign");
		}
		String str = MapUtil.mapJoin(tmap, false, false);
		return DigestUtils.sha1Hex(str + "&key=" + sign_key).toUpperCase();
	}
	
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<>();
		map.put("query", "{\napicode {\n  code\n  expires_in\n}\n}");
		map.put("nostr", "123");
		map.put("variables", "");
		
		System.out.println(generateSign(map, "bc8b946d-1c3f-47a7-a28f-f692184aef67"));
	}
}
