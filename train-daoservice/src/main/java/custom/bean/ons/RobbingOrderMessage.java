package custom.bean.ons;

public class RobbingOrderMessage implements OrderMessage{

    String messageId;
    String pay_order_id ;
    Integer type;  //0 调用发起抢单，1 调用结束抢单

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String getTag() {
        return "robbing";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RobbingOrderMessage{");
        sb.append("messageId='").append(messageId).append('\'');
        sb.append(", pay_order_id='").append(pay_order_id).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
