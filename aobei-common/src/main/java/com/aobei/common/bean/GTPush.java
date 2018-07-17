package com.aobei.common.bean;

import com.gexin.rp.sdk.http.IGtPush;

public class GTPush extends IGtPush{
    String appId;
    String appKey;
    public GTPush(String host, String appKey, String masterSecret,String appId) {
        super(host, appKey, masterSecret);
        this.appId=appId;
        this.appKey=appKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppkey() {
        return appKey;
    }

    public void setAppkey(String appKey) {
        this.appKey = appKey;
    }
}
