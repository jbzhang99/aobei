package custom.bean;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by mr_bl on 2018/6/4.
 */
public class AliTradeOrder {

    /**
     * 支付宝交易号
     */
    @CsvBindByName(column = "支付宝交易号")
    private String ali_pay_order_id;

    /**
     * 商户订单号
     */
    @CsvBindByName(column = "商户订单号")
    private String pay_order_id;

    /**
     * 业务类型
     */
    @CsvBindByName(column = "业务类型")
    private String trade_type;

    /**
     * 商品名称
     */
    @CsvBindByName(column = "商品名称")
    private String product_sku_descript;

    /**
     * 创建时间
     */
    @CsvBindByName(column = "创建时间")
    private String create_datetime;

    /**
     * 完成时间
     */
    @CsvBindByName(column = "完成时间")
    private String finish_datetime;

    /**
     * 门店编号
     */
    @CsvBindByName(column = "门店编号")
    private String store_number;

    /**
     * 门店名称
     */
    @CsvBindByName(column = "门店名称")
    private String store_name;

    /**
     * 操作员
     */
    @CsvBindByName(column = "操作员")
    private String operator;

    /**
     * 终端号
     */
    @CsvBindByName(column = "终端号")
    private String terminal_no;

    /**
     * 对方账户
     */
    @CsvBindByName(column = "对方账户")
    private String other_account;

    /**
     * 订单金额（元）
     */
    @CsvBindByName(column = "订单金额（元）")
    private String total_amount;

    /**
     * 商家实收（元）
     */
    @CsvBindByName(column = "商家实收（元）")
    private String merchants_get_amount;

    /**
     * 支付宝红包（元）
     */
    @CsvBindByName(column = "支付宝红包（元）")
    private String alipay_red_envelope;

    /**
     * 集分宝（元）
     */
    @CsvBindByName(column = "集分宝（元）")
    private String set_points_treasure;

    /**
     * 支付宝优惠（元）
     */
    @CsvBindByName(column = "支付宝优惠（元）")
    private String alipay_discount;

    /**
     * 商家优惠（元）
     */
    @CsvBindByName(column = "商家优惠（元）")
    private String merchants_discount;

    /**
     * 券核销金额（元）
     */
    @CsvBindByName(column = "券核销金额（元）")
    private String discount_coupon_amount;

    /**
     * 券名称
     */
    @CsvBindByName(column = "券名称")
    private String discount_coupon_name;

    /**
     * 商家红包消费金额（元）
     */
    @CsvBindByName(column = "商家红包消费金额（元）")
    private String merchants_red_envelope;

    /**
     * 卡消费金额（元）
     */
    @CsvBindByName(column = "卡消费金额（元）")
    private String card_amount;

    /**
     * 退款批次号/请求号
     */
    @CsvBindByName(column = "退款批次号/请求号")
    private String refund_id;

    /**
     * 服务费（元）
     */
    @CsvBindByName(column = "服务费（元）")
    private String service_charge;

    /**
     * 分润（元）
     */
    @CsvBindByName(column = "分润（元）")
    private String share_benefit;

    /**
     * 备注
     */
    @CsvBindByName(column = "备注")
    private String remark;

    public String getAli_pay_order_id() {
        return ali_pay_order_id;
    }

    public void setAli_pay_order_id(String ali_pay_order_id) {
        this.ali_pay_order_id = ali_pay_order_id;
    }

    public String getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getProduct_sku_descript() {
        return product_sku_descript;
    }

    public void setProduct_sku_descript(String product_sku_descript) {
        this.product_sku_descript = product_sku_descript;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getFinish_datetime() {
        return finish_datetime;
    }

    public void setFinish_datetime(String finish_datetime) {
        this.finish_datetime = finish_datetime;
    }

    public String getStore_number() {
        return store_number;
    }

    public void setStore_number(String store_number) {
        this.store_number = store_number;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTerminal_no() {
        return terminal_no;
    }

    public void setTerminal_no(String terminal_no) {
        this.terminal_no = terminal_no;
    }

    public String getOther_account() {
        return other_account;
    }

    public void setOther_account(String other_account) {
        this.other_account = other_account;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getMerchants_get_amount() {
        return merchants_get_amount;
    }

    public void setMerchants_get_amount(String merchants_get_amount) {
        this.merchants_get_amount = merchants_get_amount;
    }

    public String getAlipay_red_envelope() {
        return alipay_red_envelope;
    }

    public void setAlipay_red_envelope(String alipay_red_envelope) {
        this.alipay_red_envelope = alipay_red_envelope;
    }

    public String getSet_points_treasure() {
        return set_points_treasure;
    }

    public void setSet_points_treasure(String set_points_treasure) {
        this.set_points_treasure = set_points_treasure;
    }

    public String getAlipay_discount() {
        return alipay_discount;
    }

    public void setAlipay_discount(String alipay_discount) {
        this.alipay_discount = alipay_discount;
    }

    public String getMerchants_discount() {
        return merchants_discount;
    }

    public void setMerchants_discount(String merchants_discount) {
        this.merchants_discount = merchants_discount;
    }

    public String getDiscount_coupon_amount() {
        return discount_coupon_amount;
    }

    public void setDiscount_coupon_amount(String discount_coupon_amount) {
        this.discount_coupon_amount = discount_coupon_amount;
    }

    public String getDiscount_coupon_name() {
        return discount_coupon_name;
    }

    public void setDiscount_coupon_name(String discount_coupon_name) {
        this.discount_coupon_name = discount_coupon_name;
    }

    public String getMerchants_red_envelope() {
        return merchants_red_envelope;
    }

    public void setMerchants_red_envelope(String merchants_red_envelope) {
        this.merchants_red_envelope = merchants_red_envelope;
    }

    public String getCard_amount() {
        return card_amount;
    }

    public void setCard_amount(String card_amount) {
        this.card_amount = card_amount;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getShare_benefit() {
        return share_benefit;
    }

    public void setShare_benefit(String share_benefit) {
        this.share_benefit = share_benefit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

