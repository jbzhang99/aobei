package custom.bean.ons;

public class CancelOrderMessage implements OrderMessage {

    String messageId;
    String pay_order_id;

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

    @Override
    public String getTag() {
        return "cancelOrder";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CancelOrderMessage{");
        sb.append("messageId='").append(messageId).append('\'');
        sb.append(", pay_order_id='").append(pay_order_id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
