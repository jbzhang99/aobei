package custom.bean.ons;

public class RejectOrderMessage implements  OrderMessage{

    String messageId;
    String pay_order_id;
    Long partner_id;
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

    public Long getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    @Override
    public String getTag(){
        return  "rejectOrder";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RejectOrderMessage{");
        sb.append("messageId='").append(messageId).append('\'');
        sb.append(", pay_order_id='").append(pay_order_id).append('\'');
        sb.append(", partner_id=").append(partner_id);
        sb.append('}');
        return sb.toString();
    }
}
