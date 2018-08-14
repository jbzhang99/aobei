package com.aobei.trainapi.server.bean;

/**
 * Created by adminL on 2018/7/18.
 */
public class StudentServiceOrderStatistics {
    private Integer servicedOrder;
    private Integer doneOrder;
    private Integer allWaitServiceOrder;
    private Integer todayWaitServiceOrder;

    public Integer getServicedOrder() {
        return servicedOrder;
    }

    public void setServicedOrder(Integer servicedOrder) {
        this.servicedOrder = servicedOrder;
    }

    public Integer getDoneOrder() {
        return doneOrder;
    }

    public void setDoneOrder(Integer doneOrder) {
        this.doneOrder = doneOrder;
    }

    public Integer getAllWaitServiceOrder() {
        return allWaitServiceOrder;
    }

    public void setAllWaitServiceOrder(Integer allWaitServiceOrder) {
        this.allWaitServiceOrder = allWaitServiceOrder;
    }

    public Integer getTodayWaitServiceOrder() {
        return todayWaitServiceOrder;
    }

    public void setTodayWaitServiceOrder(Integer todayWaitServiceOrder) {
        this.todayWaitServiceOrder = todayWaitServiceOrder;
    }
}
