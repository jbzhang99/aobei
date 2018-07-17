package com.aobei.trainapi.server.bean;

import com.aobei.train.model.AppGrowth;

/**
 * Created by 15010 on 2018/6/13.
 */
public class AppVersion extends AppGrowth {
    //可升级
    private  Boolean   canUpgrade;
    //强制升级
    private  Boolean   forcedUpgrade;
    //App下载地址
    private String appUrl;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Boolean getForcedUpgrade() {
        return forcedUpgrade;
    }

    public void setForcedUpgrade(Boolean forcedUpgrade) {
        this.forcedUpgrade = forcedUpgrade;
    }

    public Boolean getCanUpgrade() {
        return canUpgrade;
    }

    public void setCanUpgrade(Boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }
}
