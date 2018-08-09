package com.aobei.trainapi.server.bean;

import java.io.Serializable;

/**
 * Created by liqizhen on 2018/7/26.
 */
public class MessageState implements Serializable{
    //是否有新增消息
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
