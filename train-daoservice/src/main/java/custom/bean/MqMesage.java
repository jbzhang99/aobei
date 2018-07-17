package custom.bean;

import java.io.Serializable;

public class MqMesage implements Serializable {
    private static final long serialVersionUID = 7310581186763903075L;

    SmsMessage smsMessage;
    String orderId;
    Long partnerId;
    Long customerId;
    Long studentId;
    Action action;
    Long sendTime;

    public SmsMessage getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(SmsMessage smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Action getAction() {
        return action;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        order_cancel("取消订单"),
        order_confirm("提醒订单确认"),
        order_pay_success("订单支付成功"),
        order_recject("不接单，自动拒单"),
        order_wait_service("等待服务")
       ;
       private String action;

       private String descr;

       private Action(String descr) {
           this.action = name();
           this.descr = descr;
       }

   }

}
