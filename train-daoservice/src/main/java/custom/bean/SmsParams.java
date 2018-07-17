package custom.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsParams {

    private String product_name;
    private String pay_order_id;
    private Date c_begin_datetime;
    private String cus_address;
    private String cus_username;
    private String cus_phone;
    private String pay_timeout;
    private String worker_name;
    private String code;
    private String timeout;
    private String coupon_price;
    private String coupon_expire;


    public String toJson() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sb.append("{");

        if (product_name != null)
            sb.append("\"product_name\":\"" + product_name + "\",");
        if (pay_order_id != null)
            sb.append("\"pay_order_id\":\"" + pay_order_id + "\",");
        if (c_begin_datetime != null)
            sb.append("\"c_begin_datetime\":\"" + format.format(c_begin_datetime) + "\",");
        if (cus_address != null)
            sb.append("\"cus_address\":\"" + cus_address + "\",");
        if (cus_username != null)
            sb.append("\"cus_username\":\"" + cus_username + "\",");
        if (cus_phone != null)
            sb.append("\"cus_phone\":\"" + cus_phone + "\",");
        if (pay_timeout != null)
            sb.append("\"pay_timeout\":\"" + pay_timeout + "\",");
        if (worker_name != null)
            sb.append("\"worker_name\":\"" + worker_name + "\",");
        if (code != null)
            sb.append("\"code\":\"" + code + "\",");
        if (timeout != null)
            sb.append("\"timeout\":\"" + timeout + "\",");
        if (coupon_price != null)
            sb.append("\"coupon_price\":\"" + coupon_price + "\",");
        if (coupon_expire != null)
            sb.append("\"coupon_expire\":\"" + coupon_expire + "\",");

        if(sb.indexOf(",")>0){
            sb.replace(sb.length()-1,sb.length(),"");
        }

        sb.append("}");
        return sb.toString();


    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    public Date getC_begin_datetime() {
        return c_begin_datetime;
    }

    public void setC_begin_datetime(Date c_begin_datetime) {
        this.c_begin_datetime = c_begin_datetime;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    public String getCus_username() {
        return cus_username;
    }

    public void setCus_username(String cus_username) {
        this.cus_username = cus_username;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public String getPay_timeout() {
        return pay_timeout;
    }

    public void setPay_timeout(String pay_timeout) {
        this.pay_timeout = pay_timeout;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getCoupon_expire() {
        return coupon_expire;
    }

    public void setCoupon_expire(String coupon_expire) {
        this.coupon_expire = coupon_expire;
    }


    public static void main(String[] args) {
        SmsParams params = new SmsParams();
        params.setCode("ddf11");
        params.setProduct_name("fkfk");



        System.out.println(params.toJson());
    }
}
