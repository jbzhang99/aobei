package com.aobei.trainapi.server.bean;

import com.alipay.api.AlipayClient;

import java.util.Map;

public class AliPayClientMap {

    Map<String ,AlipayClient> map ;

    public AliPayClientMap(Map<String, AlipayClient> map) {
        this.map = map;
    }

    public Map<String, AlipayClient> getMap() {
        return map;
    }

    public void setMap(Map<String, AlipayClient> map) {
        this.map = map;
    }

    public AlipayClient getClient(String appid){
        if(this.map==null){
            return null;
        }
       return this.map.get(appid);
    }
}
