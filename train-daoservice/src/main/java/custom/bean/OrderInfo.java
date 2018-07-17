package custom.bean;

import com.alibaba.fastjson.JSON;
import com.aobei.train.Roles;
import com.aobei.train.model.*;
import custom.util.DateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderInfo implements Serializable {
    //payOrder中的必须字段
    /**
     * 订单id
     */
    private String pay_order_id;

    /**
     * 订单是否允许取消(true:代表可以取消, false：代表不能取消)

     */
    private Boolean allowedToCancel;

    /**
     * 服务人员离开备注
     */
    private String work_remark;

    /**
     * 合伙人拒单原因
     */
    private String p_reject_remark;

    /**
     * 服务名称（订单名称）
     */
    private String name;
    /**
     * 总价（单位分）
     */
    private Integer price_total;
    /**
     * 优惠金额
     */
    private Integer price_discount;
    /**
     * 实际支付金额
     */
    private Integer price_pay;
    /**
     * 支付方式
     */
    private Integer pay_type;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 订单表的用户下单备注
     */
    private String remark;
    /**
     * 支付时间
     */
    private Date pay_datetime;

    /**
     * 创建时间
     */
    private Date create_datetime;
    /**
     * 退款状态
     */
    private String refundStatus;
    //customer_address中的必要字段
    /**
     * 顾客姓名
     */
    private String user_username;
    /**
     * 顾客手机号
     */
    private String user_phone;
    /**
     * 客户联系人
     */
    private String cus_username;
    /**
     * 服务地址联系人手机号
     */
    private String cus_phone;
    /**
     * 客户完整的服务地址
     */
    private String customer_address;
    /**
     * 客户服务地址的id
     */
    private Long customer_address_id;

    private Long customer_id;
    //产品相关的字段
    /**
     * 商品id
     */
    private Long product_id;
    /**
     * 商品sku  id
     */
    private Long proSku_id;
    /**
     * 商品介绍图片4：3大图
     */
    private String image_first;
    /**
     * 商品列表图片 放图
     */
    private String lite_image;
    /**
     * 图片JSON 数据 [{id:图片id,url:“图片访问url”,first:1}]
     * 需要peoduct对象
     */
    private String images;
    /**
     * 商品描述
     * 需要product对象
     */
    private String descript;
    //服务单中的必要字段
    /**
     * 服务单id
     */
    private Long serviceunit_id;
    /**
     * 预约开始时间
     */
    private Date c_begin_datetime;
    /**
     * 预约结束时间
     */
    private Date c_end_datetime;
    /**
     * 系统指派给合伙人的时间
     */
    private Date p_assign_datetime;
    /**
     * 合伙人确认接单时间
     */
    private Date p_confirm_datetime;
    /**
     * 合伙人拒绝单时间
     */
    private Date p_reject_datetime;
    /**
     * 合伙人分配给学员的时间
     */
    private Date p2s_assign_datetime;

    /**
     * 服务单完成时间
     */
    private Date finish_datetime;
    /**
     * 服务状态
     */
    private String serviceStatus;
    /**
     * JSON 优惠数据 [{id:优惠id,descript:优惠说明,price:优惠金额}]
     */
    private String discount_data;
    //服务人员和合伙人的相关信息
    /**
     * 服务人员id
     */
    private Long student_id;
    /**
     * 服务人员姓名
     */
    private String student_name;
    /**
     * 服务人员电话
     * 如果需要服务人员电话，必须要注入Student对象
     */
    private String student_phone;

    private List<ServiceunitPerson> students;
    /**
     * 商家合伙人id
     */
    private Long partner_id;
    /**
     * 服务站点id
     */
    private Long station_id;
    /**
     * 商家合伙人名称
     * 需要partner对象
     */
    private String partner_name;
    /**
     * 商家合伙人联系电话
     * 需要partner 对象
     */
    private String partner_phone;

    /**
     * 是否已经点评过
     */
    boolean isEvaluate;
    /**
     * sku数量
     */
    Integer num;

    /**
     * j计量单位
     */
    String unit;

    /**
     * 是否1对1
     *
     * @return
     */
    private Integer buy_multiple_o2o;

    public String getUnit() {
        if (proSku != null) {
            this.unit = proSku.getUnit();
        }
        return unit;
    }

    /**
     * 抢单记录id
     *
     * @return
     */
    private Long robbing_id;

    public boolean isEvaluate() {
        return isEvaluate;
    }

    public void setEvaluate(boolean evaluate) {
        isEvaluate = evaluate;
    }

    /**
     * 用来展示专用
     */
    private String dateTime;
    private String startTime;
    private String endTime;


    //需要的实体类型
    List<Remark> remarkList;
    /**
     * 订单
     */
    private Order order;
    /**
     * 订单包含的商品sku
     */
    private OrderItem orderItem;
    /**
     * 客户地址
     */
    private CustomerAddress customerAddress;
    /**
     * 服务单
     */
    private ServiceUnit serviceUnit;
    /**
     * 商品
     */
    private Product product;
    /**
     * 商品单项
     */
    private ProSku proSku;
    /**
     * 服务人员
     */
    private Student student;
    /**
     * 商家合伙人
     */
    private Partner partner;
    /**
     * 服务单,抢单
     */
    private Robbing robbing;
    /**
     * 顾客
     */
    private Customer customer;

    /**
     * 用户角色
     */
    private Roles roles;

    private String dServiceStartTime;
    /**
     * 通用构造方法，自己指定需要传进来的实体类型。Roles是必须的。
     *
     * @param roles
     */
    public OrderInfo(Roles roles) {
        this.roles = roles;
    }
    public  OrderInfo(){}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getUser_username() {
        if (customer == null)
            return null;
        return customer.getName();
    }

    public String getUser_phone() {
        if (customer == null)
            return null;
        return customer.getPhone();
    }

    public List<Remark> getRemarkList() {
        if (serviceUnit == null) {
            return new ArrayList<Remark>();
        }
        String remarks = this.serviceUnit.getRemark();

        remarkList = (remarks == null || remarks.length()==0) ? new ArrayList<Remark>() : JSON.parseArray(remarks, Remark.class);
        switch (roles) {
            case STUDENT:
                remarkList.stream().filter(t -> t.getUser_id() == this.student_id);
                break;
            case PARTNER_USER:
                remarkList.stream().filter(t -> t.getUser_id() == this.partner_id);
                break;
            case CUSTOMER:
                remarkList = null;
                break;
        }

        return remarkList;
    }

    public Integer getBuy_multiple_o2o() {
        if (proSku == null)
            return 0;
        this.buy_multiple_o2o = proSku.getBuy_multiple_o2o();
        return buy_multiple_o2o;
    }


    public Integer getNum() {
        if (orderItem == null) {
            this.num = 1;
        } else {
            this.num = orderItem.getNum();
        }


        return num;
    }

    public Long getStation_id() {
        if (serviceUnit == null)
            return null;
        this.station_id = serviceUnit.getStation_id();
        return station_id;
    }

    public String getPay_order_id() {
        if (this.order == null)
            return null;
        this.pay_order_id = order.getPay_order_id();
        return pay_order_id;
    }


    public String getWork_remark() {
        if (serviceUnit == null)
            return null;
        this.work_remark = serviceUnit.getWork_remark();
        return work_remark;
    }

    public String getP_reject_remark() {
        if (serviceUnit == null)
            return null;
        this.p_reject_remark = serviceUnit.getP_reject_remark();
        return p_reject_remark;
    }


    public String getName() {
        if (this.order == null)
            return null;
        this.name = order.getName();
        return name;
    }

    public Integer getPrice_total() {
        if (this.order == null)
            return null;
        this.price_total = order.getPrice_total();
        return price_total;
    }

    public Integer getPrice_discount() {
        if (this.order == null)
            return null;
        this.price_discount = order.getPrice_discount();
        return price_discount;
    }

    public Integer getPrice_pay() {
        if (this.order == null)
            return null;
        this.price_pay = order.getPrice_pay();
        return price_pay;
    }

    public Integer getPay_type() {
        if (this.order == null)
            return null;
        this.pay_type = order.getPay_type();
        return pay_type;
    }

    public String getOrderStatus() {
        if (order == null)
            return null;
        switch (roles) {
            case CUSTOMER:

                if (Status.OrderStatus.wait_pay.value.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.WAIT_PAY.descr;
                }
                if (StatusConstant.ORDER_STATUS_WAITCONFIRM.equals(order.getStatus_active())
                        && StatusConstant.PAY_STATUS_PAYED.equals(order.getPay_status())) {
                    orderStatus = OrderStatus.WAIT_SERVICE.descr;
                }
                if (StatusConstant.ORDER_STATUS_WAITSERVICE.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.WAIT_SERVICE.descr;
                }
                if (StatusConstant.ORDER_STATUS_CANCLE.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.CANCEL.descr;
                }
                if (StatusConstant.ORDER_STATUS_DONE.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.DONE.descr;
                }
                /**
                 if (StatusConstant.ORDER_REFUND_STATUS_WAITREFUND.equals(order.getR_status())) {
                 refundStatus = OrderStatus.WAIT_REFUND.descr;
                 }
                 if (StatusConstant.ORDER_REFUND_STATUS_REFUNDED.equals(order.getR_status())) {
                 refundStatus = OrderStatus.REFUNDED.descr;
                 }
                 if (StatusConstant.ORDER_REFUND_STATUS_PARTREFUNDED.equals(order.getR_status())) {
                 refundStatus = OrderStatus.PART_REFUND.descr;
                 }**/
                break;
            case PARTNER_USER:
                if (StatusConstant.SERVICEUNIT_STATUS_ACTIVE.equals(serviceUnit.getActive())) {
                    if (StatusConstant.SERVICEUNIT_STATUS_WAIT_PARTNER_CONFIRM.equals(serviceUnit.getStatus_active())
                            || StatusConstant.SERVICEUNIT_STATUS_WAITASSIGN_WORKER.equals(serviceUnit.getStatus_active())) {
                        orderStatus = OrderStatus.PAYED.descr;
                    }
                    else if (StatusConstant.SERVICEUNIT_STATUS_ASSIGNWORKE.equals(serviceUnit.getStatus_active())) {
                        orderStatus = OrderStatus.WAIT_SERVICE.descr;
                    }
                    else if (StatusConstant.SERVICEUNIT_STATUS_DONE.equals(serviceUnit.getStatus_active())) {
                        orderStatus = OrderStatus.DONE.descr;
                    }else if(StatusConstant.SERVICEUNIT_STATUS_REJECT.equals(serviceUnit.getStatus_active())
                            && new Integer(1).equals(serviceUnit.getRobbing())){
                        orderStatus = OrderStatus.WAIT_ROBBING.descr;
                    }

                } else if (StatusConstant.SERVICEUNIT_STATUS_UNACTIVE.equals(serviceUnit.getActive())) {
                    if(serviceUnit.getPid() == 0){
                        if(StatusConstant.SERVICEUNIT_STATUS_REJECT.equals(serviceUnit.getStatus_active())
                                && new Integer(1).equals(serviceUnit.getRobbing()) ){
                            orderStatus = OrderStatus.WAIT_ROBBING.descr;
                        }else if (StatusConstant.SERVICEUNIT_STATUS_REJECT.equals(serviceUnit.getStatus_active())) {
                            orderStatus = OrderStatus.REFUSED.descr;
                        }else if (StatusConstant.ORDER_STATUS_CANCLE.equals(order.getStatus_active())) {
                            orderStatus = OrderStatus.CANCEL.descr;
                        }
                    }else {
                        if (StatusConstant.SERVICEUNIT_STATUS_REJECT.equals(serviceUnit.getStatus_active())){
                            orderStatus = OrderStatus.REFUSED.descr;
                        }else if (StatusConstant.SERVICEUNIT_STATUS_REJECT.equals(serviceUnit.getStatus_active())
                                && new Integer(1).equals(serviceUnit.getRobbing())){
                            orderStatus = OrderStatus.WAIT_ROBBING.descr;
                        }else if (StatusConstant.ORDER_STATUS_CANCLE.equals(order.getStatus_active())) {
                            orderStatus = OrderStatus.CANCEL.descr;
                        }
                    }
                }
                break;
            case STUDENT:
                if (StatusConstant.ORDER_STATUS_WAITSERVICE.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.WAIT_SERVICE.descr;
                }
                if (StatusConstant.ORDER_STATUS_CANCLE.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.CANCEL.descr;
                }
                if (StatusConstant.ORDER_STATUS_DONE.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.DONE.descr;
                }
                if (Status.OrderStatus.wait_pay.value.equals(order.getStatus_active())) {
                    orderStatus = OrderStatus.WAIT_PAY.descr;
                }
                break;

        }
        return orderStatus;
    }

    public String getRemark() {
        if (this.order == null)
            return null;
        this.remark = order.getRemark();
        return remark;
    }


    public String getRefundStatus() {
        if (order == null)
            return null;

        switch (roles) {
            case CUSTOMER:
                if (Status.RefundStatus.wait_refund.value.equals(order.getR_status())) {
                    refundStatus = OrderStatus.WAIT_REFUND.descr;
                }
                if (Status.RefundStatus.refunded.value.equals(order.getR_status())) {
                    refundStatus = OrderStatus.REFUNDED.descr;
                }
                if (Status.RefundStatus.part_refunded.value.equals(order.getR_status())) {
                    refundStatus = OrderStatus.PART_REFUND.descr;
                }
                break;
        }
        return refundStatus;
    }

    public Boolean getAllowedToCancel() {
        return allowedToCancel;
    }

    public void setAllowedToCancel(Boolean allowedToCancel) {
        this.allowedToCancel = allowedToCancel;
    }

    public Date getPay_datetime() {
        if (this.order == null)
            return null;
        this.pay_datetime = order.getPay_datetime();
        return pay_datetime;
    }

    public Date getCreate_datetime() {
        if (this.order == null)
            return null;
        this.create_datetime = order.getCreate_datetime();
        return create_datetime;
    }

    public String getCus_username() {
        if (this.order == null)
            return null;
        this.cus_username = order.getCus_username();
        return cus_username;
    }

    public String getCus_phone() {
        if (this.order == null)
            return null;
        this.cus_phone = order.getCus_phone();
        return cus_phone;
    }

    public String getCustomer_address() {
        if (this.order == null)
            return null;
        this.customer_address = order.getCus_address();
        return customer_address;
    }

    public Long getCustomer_address_id() {
        if (this.order == null)
            return null;
        this.customer_address_id = order.getCustomer_address_id();
        return customer_address_id;
    }

    public Long getCustomer_id() {
        if (this.order == null)
            return null;
        this.customer_id = order.getUid();
        return customer_id;
    }

    public Long getProduct_id() {
        if (product == null) {
            if (serviceUnit == null)
                return null;
            this.product_id = serviceUnit.getProduct_id();
        } else {
            this.product_id = product.getProduct_id();
        }
        return product_id;
    }

    public Long getProSku_id() {
        if (proSku == null) {
            if (serviceUnit == null)
                return null;
            this.proSku_id = serviceUnit.getPsku_id();
        } else {
            this.proSku_id = proSku.getPsku_id();
        }
        return proSku_id;
    }

    public String getImage_first() {
        if (product == null)
            return null;
        this.image_first = product.getImage_first();
        return image_first;
    }

    public String getLite_image(){
        if (product == null)
            return null;
        String imgstring  =  product.getLite_image();
        try {
            Img img  = JSON.parseObject(imgstring, Img.class);
            lite_image = img.getUrl();
        }catch (Exception e){
           //nothing to do
        }
       return  lite_image;
    }
    public String getDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (serviceUnit.getC_begin_datetime() != null)
            dateTime = format.format(serviceUnit.getC_begin_datetime());
        return dateTime;
    }

    public String getdServiceStartTime() {
        Date date = serviceUnit.getC_begin_datetime();
        if(date!= null) {
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            StringBuilder  builder  = new StringBuilder(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            builder.append("(");
            builder.append(DateUtil.getWeekSay(localDateTime.getDayOfWeek()));
            builder.append(")");
            builder.append("  ");
            builder.append(getStartTime());
            if(getEndTime()!=null) {
                builder.append("-");
                builder.append(getEndTime());
            }
            dServiceStartTime = builder.toString();
        }
        return dServiceStartTime;
    }

    public String getStartTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        if (serviceUnit.getC_begin_datetime() != null)
            startTime = format.format(serviceUnit.getC_begin_datetime());
        return startTime;
    }

    public String getEndTime() {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        if (serviceUnit.getC_end_datetime() != null)
            endTime = format.format(serviceUnit.getC_end_datetime());
        return endTime;
    }

    public String getImages() {
        if (product == null)
            return null;
        this.images = product.getImages();
        return images;
    }

    public String getDescript() {
        if (product == null)
            return null;
        this.descript = product.getDescript();
        return descript;
    }

    public Long getServiceunit_id() {
        if (serviceUnit == null)
            return null;
        this.serviceunit_id = serviceUnit.getServiceunit_id();
        return serviceunit_id;
    }

    public Date getC_begin_datetime() {
        if (serviceUnit == null)
            return null;
        this.c_begin_datetime = serviceUnit.getC_begin_datetime();
        this.c_end_datetime = serviceUnit.getC_end_datetime();
        if(c_begin_datetime.equals(c_end_datetime)){
        	return null;
        }
        return c_begin_datetime;
    }

    public Date getC_end_datetime() {
        if (serviceUnit == null)
            return null;
        this.c_end_datetime = serviceUnit.getC_end_datetime();
        this.c_begin_datetime = serviceUnit.getC_begin_datetime();
        if(c_end_datetime != null){
        	if(c_end_datetime.equals(c_begin_datetime)){
            	return null;
            }
        }
        return c_end_datetime;
    }


    public Date getP_assign_datetime() {
        if (serviceUnit == null)
            return null;
        this.p_assign_datetime = serviceUnit.getP_assign_datetime();
        return p_assign_datetime;
    }

    public Date getP_confirm_datetime() {
        if (serviceUnit == null)
            return null;
        this.p_confirm_datetime = serviceUnit.getP_confirm_datetime();
        return p_confirm_datetime;
    }

    public Date getP_reject_datetime() {
        if (serviceUnit == null)
            return null;
        this.p_reject_datetime = serviceUnit.getP_reject_datetime();
        return p_reject_datetime;
    }

    public Date getP2s_assign_datetime() {
        if (serviceUnit == null)
            return null;
        this.p2s_assign_datetime = serviceUnit.getP2s_assign_datetime();
        return p2s_assign_datetime;
    }

    public Date getFinish_datetime() {
        if (serviceUnit == null)
            return null;
        this.finish_datetime = serviceUnit.getFinish_datetime();
        return finish_datetime;
    }

    public String getServiceStatus() {

        if (serviceUnit == null) {
            return null;
        }
        if (serviceUnit.getWork_status() == null) {
            serviceStatus = ServiceStatus.WAIT_SERVICE.descr;
        }
        if (StatusConstant.SERVICEUNIT_WORK_STATUS_ARRIVE.equals(serviceUnit.getWork_status())) {
            serviceStatus = ServiceStatus.ARRIVE.descr;
        }
        if (StatusConstant.SERVICEUNIT_WORK_STATUS_START.equals(serviceUnit.getWork_status())) {
            serviceStatus = ServiceStatus.START.descr;
        }
        if (StatusConstant.SERVICEUNIT_WORK_STATUS_DONE.equals(serviceUnit.getWork_status())) {
            serviceStatus = ServiceStatus.DONE.descr;
        }
        if (StatusConstant.SERVICEUNIT_WORK_STATUS_LEAVE.equals(serviceUnit.getWork_status())) {
            serviceStatus = ServiceStatus.LEAVE.descr;
        }
        if (new Integer(5).equals(serviceUnit.getWork_status())) {
            serviceStatus = ServiceStatus.OVER.descr;
        }
        return serviceStatus;
    }


    public String getDiscount_data() {
        if (order == null)
            return null;
        this.discount_data = order.getDiscount_data();
        return discount_data;
    }

    @Deprecated
    public Long getStudent_id() {
        if (student == null) {
            if (serviceUnit == null)
                return null;
            this.student_id = serviceUnit.getStudent_id();
        } else {
            this.student_id = student.getStudent_id();
        }
        return student_id;
    }

    public String getStudent_name() {
        if (student == null) {
            if (serviceUnit == null)
                return null;
            this.student_name = serviceUnit.getStudent_name();
        } else {
            this.student_name = student.getName();
        }


        return student_name;
    }

    public String getStudent_phone() {
        if (student == null)
            return null;
        this.student_phone = student.getPhone();
        return student_phone;
    }

    public Long getPartner_id() {
        if (partner == null) {
            if (serviceUnit == null)
                return null;
            this.partner_id = serviceUnit.getPartner_id();
        } else {
            this.partner_id = partner.getPartner_id();
        }


        return partner_id;
    }

    public String getPartner_name() {
        if (partner == null)
            return null;
        this.partner_name = partner.getName();
        return partner_name;
    }

    public String getPartner_phone() {
        if (partner == null)
            return null;
        this.partner_phone = partner.getPhone();
        return partner_phone;
    }

    public Long getRobbing_id() {
        if (robbing == null)
            return null;
        this.robbing_id = robbing.getRobbing_id();
        return robbing_id;
    }


    public void setOrder(Order order) {

        this.order = order;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setServiceUnit(ServiceUnit serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProSku(ProSku proSku) {
        this.proSku = proSku;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public void setRobbing(Robbing robbing) {
        this.robbing = robbing;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public List<ServiceunitPerson> getStudents() {
        return students;
    }

    public void setStudents(List<ServiceunitPerson> students) {
        this.students = students;
    }


    public enum OrderStatus {
        //状态1 创建，等待支付，2以支付等待确认，3商家确认。4确认后分配服务人员等待服务，5取消，6申请退款中，7退款完成，8拒单

        WAIT_PAY("waitPay"),
        PAYED("payed"),
        WAIT_SERVICE("waitService"),
        CANCEL("cancel"),
        DONE("done"),
        REFUSED("refused"),
        WAIT_REFUND("waitRefund"),
        PART_REFUND("partRefund"),
        REFUNDED("refunded"),
        WAIT_ROBBING("waitRobbing");


        private String name;
        public String descr;

        private OrderStatus(String descr) {
            this.name = name();
            this.descr = descr;
        }

        public static OrderStatus get(String value) {

            OrderStatus[] statuses = OrderStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].descr.equals(value))
                    return statuses[i];
            }
            return null;
        }

    }

    public enum ServiceStatus {
        WAIT_SERVICE("waitService"),
        ARRIVE("arrive"),
        START("start"),
        DONE("done"),
        LEAVE("leave"),
        OVER("over");

        private String name;
        private String descr;

        private ServiceStatus(String descr) {
            this.name = name();
            this.descr = descr;
        }

        public String getDescr() {
            return this.descr;
        }

        public static ServiceStatus get(String value) {

            ServiceStatus[] statuses = ServiceStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].descr.equals(value))
                    return statuses[i];
            }
            return null;
        }
    }




    /**************************************/
    public Order getOrder() {
        return order;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public ServiceUnit getServiceUnit() {
        return serviceUnit;
    }

    public Product getProduct() {
        return product;
    }

    public ProSku getProSku() {
        return proSku;
    }

    public Student getStudent() {
        return student;
    }

    public Partner getPartner() {
        return partner;
    }

    public Robbing getRobbing() {
        return robbing;
    }

    public Roles getRoles() {
        return roles;
    }


}
