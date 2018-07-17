package custom.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mr_bl on 2018/6/5.
 */
public class AliDownloadbill {

    /**
     * 总交易单数
     */
    private String total_trade_num;

    /**
     * 总交易额
     */
    private String total_trade_amount;

    /**
     * 总退款金额
     */
    private String total_refund_amount;

    /**
     * 总企业红包退款金额
     */
    private String total_corporate_red_envelope_refund_amount;

    /**
     * 手续费总金额
     */
    private String total_service_charge;

    /**
     * 获取日期
     */
    private Date getDatetime;

    /**
     * 当日交易单集合
     */
    private List<AliTradeOrder> tradeOrders = new ArrayList<>();

    public String getTotal_trade_num() {
        return total_trade_num;
    }

    public void setTotal_trade_num(String total_trade_num) {
        this.total_trade_num = total_trade_num;
    }

    public String getTotal_trade_amount() {
        return total_trade_amount;
    }

    public void setTotal_trade_amount(String total_trade_amount) {
        this.total_trade_amount = total_trade_amount;
    }

    public String getTotal_refund_amount() {
        return total_refund_amount;
    }

    public void setTotal_refund_amount(String total_refund_amount) {
        this.total_refund_amount = total_refund_amount;
    }

    public String getTotal_corporate_red_envelope_refund_amount() {
        return total_corporate_red_envelope_refund_amount;
    }

    public void setTotal_corporate_red_envelope_refund_amount(String total_corporate_red_envelope_refund_amount) {
        this.total_corporate_red_envelope_refund_amount = total_corporate_red_envelope_refund_amount;
    }

    public String getTotal_service_charge() {
        return total_service_charge;
    }

    public void setTotal_service_charge(String total_service_charge) {
        this.total_service_charge = total_service_charge;
    }

    public Date getGetDatetime() {
        return getDatetime;
    }

    public void setGetDatetime(Date getDatetime) {
        this.getDatetime = getDatetime;
    }

    public List<AliTradeOrder> getTradeOrders() {
        return tradeOrders;
    }

    public void setTradeOrders(List<AliTradeOrder> tradeOrders) {
        this.tradeOrders = tradeOrders;
    }
}
