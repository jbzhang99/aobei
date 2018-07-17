package custom.bean;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by mr_bl on 2018/5/9.
 */
public class TradeOrder {

    /**
     * 交易时间
     */
    @CsvBindByName(column = "交易时间")
    private String trade_date_time;

    /**
     * 公众账号ID
     */
    @CsvBindByName(column = "公众账号ID")
    private String app_id;

    /**
     * 商户号
     */
    @CsvBindByName(column = "商户号")
    private String mch_id;

    /**
     * 子商户号
     */
    @CsvBindByName(column = "子商户号")
    private String sub_mch_id;

    /**
     * 设备号
     */
    @CsvBindByName(column = "设备号")
    private String device_number;

    /**
     * 微信订单号
     */
    @CsvBindByName(column = "微信订单号")
    private String wx_pay_order_id;

    /**
     * 商户订单号
     */
    @CsvBindByName(column = "商户订单号")
    private String pay_order_id;

    /**
     * 用户标识
     */
    @CsvBindByName(column = "用户标识")
    private String user_sign;

    /**
     * 交易类型
     */
    @CsvBindByName(column = "交易类型")
    private String trade_type;

    /**
     * 交易状态
     */
    @CsvBindByName(column = "交易状态")
    private String trade_status;

    /**
     * 付款银行
     */
    @CsvBindByName(column = "付款银行")
    private String pay_bank;

    /**
     * 货币种类
     */
    @CsvBindByName(column = "货币种类")
    private String currency_type;

    /**
     * 总金额
     */
    @CsvBindByName(column = "总金额")
    private String total_amount;

    /**
     * 企业红包金额
     */
    @CsvBindByName(column = "企业红包金额")
    private String corporate_red_envelope_amount;

    /**
     * 微信退款单号
     */
    @CsvBindByName(column = "微信退款单号")
    private String wx_refund_id;

    /**
     * 商户退款单号
     */
    @CsvBindByName(column = "商户退款单号")
    private String refund_id;

    /**
     * 退款金额
     */
    @CsvBindByName(column = "退款金额")
    private String refund_amount;

    /**
     * 企业红包退款金额
     */
    @CsvBindByName(column = "企业红包退款金额")
    private String corporate_red_envelope_refund_amount;

    /**
     * 退款类型
     */
    @CsvBindByName(column = "退款类型")
    private String refund_type;

    /**
     * 退款状态
     */
    @CsvBindByName(column = "退款状态")
    private String refund_status;

    /**
     * 商品名称
     */
    @CsvBindByName(column = "商品名称")
    private String product_sku_descript;

    /**
     * 商户数据包
     */
    @CsvBindByName(column = "商户数据包")
    private String mch_datas;

    /**
     * 手续费
     */
    @CsvBindByName(column = "手续费")
    private String service_charge;

    /**
     * 费率
     */
    @CsvBindByName(column = "费率")
    private String rate;

    public String getTrade_date_time() {
        return trade_date_time;
    }

    public void setTrade_date_time(String trade_date_time) {
        this.trade_date_time = trade_date_time;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getWx_pay_order_id() {
        return wx_pay_order_id;
    }

    public void setWx_pay_order_id(String wx_pay_order_id) {
        this.wx_pay_order_id = wx_pay_order_id;
    }

    public String getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    public String getUser_sign() {
        return user_sign;
    }

    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getPay_bank() {
        return pay_bank;
    }

    public void setPay_bank(String pay_bank) {
        this.pay_bank = pay_bank;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCorporate_red_envelope_amount() {
        return corporate_red_envelope_amount;
    }

    public void setCorporate_red_envelope_amount(String corporate_red_envelope_amount) {
        this.corporate_red_envelope_amount = corporate_red_envelope_amount;
    }

    public String getWx_refund_id() {
        return wx_refund_id;
    }

    public void setWx_refund_id(String wx_refund_id) {
        this.wx_refund_id = wx_refund_id;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getCorporate_red_envelope_refund_amount() {
        return corporate_red_envelope_refund_amount;
    }

    public void setCorporate_red_envelope_refund_amount(String corporate_red_envelope_refund_amount) {
        this.corporate_red_envelope_refund_amount = corporate_red_envelope_refund_amount;
    }

    public String getRefund_type() {
        return refund_type;
    }

    public void setRefund_type(String refund_type) {
        this.refund_type = refund_type;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getProduct_sku_descript() {
        return product_sku_descript;
    }

    public void setProduct_sku_descript(String product_sku_descript) {
        this.product_sku_descript = product_sku_descript;
    }

    public String getMch_datas() {
        return mch_datas;
    }

    public void setMch_datas(String mch_datas) {
        this.mch_datas = mch_datas;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
