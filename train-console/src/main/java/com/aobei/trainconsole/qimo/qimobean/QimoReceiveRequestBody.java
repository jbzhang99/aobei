package com.aobei.trainconsole.qimo.qimobean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr_bl on 2018/7/6.
 */
public class QimoReceiveRequestBody implements Serializable{

    /**
     * 工单id
     */
    @JSONField(name="_id")
    private String _id;
    /**
     * 客户ID
     */
    private String customerId;
    /**
     * 工单步骤动作字段(name:字段名称 type:字段类型value:字段值)
     */
    private List<QimoFiled> fields = new ArrayList<>();
    /**
     * 处理坐席的工号
     */
    private String user;
    /**
     * 工单创建时间
     */
    private String createTime;
    /**
     * 当前步骤的的步骤信息
     */
    private String flowInfo;
    /**
     * 当前的步骤界面信息
     */
    private List<QimoFiled> stepFileds = new ArrayList<>();
    /**
     * 执行的动作名称
     */
    private String action;
    /**
     * 当前步骤的名称
     */
    private String stepName;
    /**
     * 工单编号
     */
    private String businessNumber;
    /**
     * 创建时间毫秒数
     */
    private String creatTimestamp;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<QimoFiled> getFields() {
        return fields;
    }

    public void setFields(List<QimoFiled> fields) {
        this.fields = fields;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFlowInfo() {
        return flowInfo;
    }

    public void setFlowInfo(String flowInfo) {
        this.flowInfo = flowInfo;
    }

    public List<QimoFiled> getStepFileds() {
        return stepFileds;
    }

    public void setStepFileds(List<QimoFiled> stepFileds) {
        this.stepFileds = stepFileds;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getCreatTimestamp() {
        return creatTimestamp;
    }

    public void setCreatTimestamp(String creatTimestamp) {
        this.creatTimestamp = creatTimestamp;
    }
}
