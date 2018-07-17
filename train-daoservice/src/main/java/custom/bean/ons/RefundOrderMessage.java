package custom.bean.ons;

public class RefundOrderMessage implements OrderMessage {

    String messageId;
    String appid;
    Long refund_id;
    String pay_order_id;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Long getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(Long refund_id) {
        this.refund_id = refund_id;
    }

    public String getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    @Override
    public String getTag() {
        return "refundOrder";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RefundOrderMessage{");
        sb.append("messageId='").append(messageId).append('\'');
        sb.append(", appid='").append(appid).append('\'');
        sb.append(", refund_id=").append(refund_id);
        sb.append(", pay_order_id='").append(pay_order_id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
