package com.aobei.common.bean;

import com.gexin.rp.sdk.template.style.Style0;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IGtPushData implements Serializable {

    private static final long serialVersionUID = 5964343650256057498L;
    //要发送的样式表
    public static final String Style0 = "com.gexin.rp.sdk.template.style.Style0";
    public static final String Style1 = "com.gexin.rp.sdk.template.style.Style1";
    public static final String Style4 = "com.gexin.rp.sdk.template.style.Style4";
    public static final String Style6 = "com.gexin.rp.sdk.template.style.Style6";

    public static final int SINGLE = 1;
    public static final int GROUP = 2;
    public static final int APP = 3;
    public static final int BIND=4;
    public static final int UNBIND=5;

    public static final String SOUND_NEW_ORDER ="newOrder.wav";

    private String messageId;
    private String title;
    private String text;
    private String durationStart;
    private String durationend;//消息保留时间 格式  "2015-01-16 11:40:00,2015-01-16 12:24:00"
    private String transmissionContent;//透传内容
    private int setTransmissionType;
    private String style;
    private List<String> alias;
    private int type; //操作类型，1 单个发送。2列表发送，3app 群组发送,4 绑定别名
    private String pushTime;//"201710261050" 代表延时发送
    private Client client;
    private String cid;
    private String alia;
    private String sound; //推送声音
    //发送到APP群组专用
    //手机类型
    List<String> phoneTypeList ;
    //省份
    List<String> provinceList;
    //自定义tag
    List<String> tagList;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(String durationStart) {
        this.durationStart = durationStart;
    }

    public String getDurationend() {
        return durationend;
    }

    public void setDurationend(String durationend) {
        this.durationend = durationend;
    }

    public String getTransmissionContent() {
        return transmissionContent;
    }

    public void setTransmissionContent(String transmissionContent) {
        this.transmissionContent = transmissionContent;
    }

    public int getSetTransmissionType() {
        return setTransmissionType;
    }

    public void setSetTransmissionType(int setTransmissionType) {
        this.setTransmissionType = setTransmissionType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<String> getPhoneTypeList() {
        return phoneTypeList;
    }

    public void setPhoneTypeList(List<String> phoneTypeList) {
        this.phoneTypeList = phoneTypeList;
    }

    public List<String> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<String> provinceList) {
        this.provinceList = provinceList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public List<String> getAlias() {
        if (alias == null) {
            alias = new ArrayList<>();
        }
        return alias;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAlia() {
        return alia;
    }

    public void setAlia(String alia) {
        this.alia = alia;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public enum Client{

        customer,
        partner,
        student,
        teacher


    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IGtPushData{");
        sb.append("messageId='").append(messageId).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", durationStart='").append(durationStart).append('\'');
        sb.append(", durationend='").append(durationend).append('\'');
        sb.append(", transmissionContent='").append(transmissionContent).append('\'');
        sb.append(", setTransmissionType=").append(setTransmissionType);
        sb.append(", style='").append(style).append('\'');
        sb.append(", alias=").append(alias);
        sb.append(", type=").append(type);
        sb.append(", pushTime='").append(pushTime).append('\'');
        sb.append(", client=").append(client);
        sb.append(", cid='").append(cid).append('\'');
        sb.append(", alia='").append(alia).append('\'');
        sb.append(", phoneTypeList=").append(phoneTypeList);
        sb.append(", provinceList=").append(provinceList);
        sb.append(", tagList=").append(tagList);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {
        //通过类名反射获取实例
        try {
            Class c = Class.forName(Style0);
            try {
                Style0 style0 = (Style0) c.newInstance();
                style0.setLogo("8888888888");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


}
