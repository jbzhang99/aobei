package custom.bean;

import com.aobei.train.model.PayAliNotify;
import com.aobei.train.model.PayWxNotify;
import com.aobei.train.model.RefundWxNotify;

/**
 * Created by mr_bl on 2018/5/16.
 */
public class BillTemp {

    private TradeOrder tradeOrder;

    private PayWxNotify wxNotify;

    private RefundWxNotify refundWxNotify;

    private AliTradeOrder aliTradeOrder;

    private PayAliNotify payAliNotify;

    private PayAliNotify refundAliNotify;

    public TradeOrder getTradeOrder() {
        return tradeOrder;
    }

    public void setTradeOrder(TradeOrder tradeOrder) {
        this.tradeOrder = tradeOrder;
    }

    public PayWxNotify getWxNotify() {
        return wxNotify;
    }

    public void setWxNotify(PayWxNotify wxNotify) {
        this.wxNotify = wxNotify;
    }

    public RefundWxNotify getRefundWxNotify() {
        return refundWxNotify;
    }

    public void setRefundWxNotify(RefundWxNotify refundWxNotify) {
        this.refundWxNotify = refundWxNotify;
    }

    public AliTradeOrder getAliTradeOrder() {
        return aliTradeOrder;
    }

    public void setAliTradeOrder(AliTradeOrder aliTradeOrder) {
        this.aliTradeOrder = aliTradeOrder;
    }

    public PayAliNotify getPayAliNotify() {
        return payAliNotify;
    }

    public void setPayAliNotify(PayAliNotify payAliNotify) {
        this.payAliNotify = payAliNotify;
    }

    public PayAliNotify getRefundAliNotify() {
        return refundAliNotify;
    }

    public void setRefundAliNotify(PayAliNotify refundAliNotify) {
        this.refundAliNotify = refundAliNotify;
    }
}
