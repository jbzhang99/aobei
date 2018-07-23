package com.aobei.trainapi.server.bean;

import java.io.Serializable;

/**
 * Created by liqizhen on 2018/7/20.
 */
public class StudentImgInfo implements Serializable {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
