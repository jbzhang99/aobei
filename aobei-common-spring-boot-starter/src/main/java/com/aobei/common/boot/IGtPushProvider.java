package com.aobei.common.boot;


import com.aobei.common.bean.GTPush;

public class IGtPushProvider {


    private GTPush customerPush;

    private GTPush partnerPush;

    private GTPush studentPush;

    private GTPush teacherPush;

    public IGtPushProvider(GTPush customerPush, GTPush partnerPush, GTPush studentPush, GTPush teacherPush) {
        this.customerPush = customerPush;
        this.partnerPush = partnerPush;
        this.studentPush = studentPush;
        this.teacherPush = teacherPush;
    }

    public GTPush getCustomerPush() {
        return customerPush;
    }

    public void setCustomerPush(GTPush customerPush) {
        this.customerPush = customerPush;
    }

    public GTPush getPartnerPush() {
        return partnerPush;
    }

    public void setPartnerPush(GTPush partnerPush) {
        this.partnerPush = partnerPush;
    }

    public GTPush getStudentPush() {
        return studentPush;
    }

    public void setStudentPush(GTPush studentPush) {
        this.studentPush = studentPush;
    }

    public GTPush getTeacherPush() {
        return teacherPush;
    }

    public void setTeacherPush(GTPush teacherPush) {
        this.teacherPush = teacherPush;
    }
}
