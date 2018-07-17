package custom.bean;

public class StatusConstant {

    /*********************订单表***************************/
    /**订单状态**/
    public static final Integer ORDER_STATUS_WAITPAY=1;
    public static final Integer ORDER_STATUS_WAITCONFIRM=2;
    public static final Integer ORDER_STATUS_WAITSERVICE=3;
    public static final Integer ORDER_STATUS_CANCLE=4;
    public static final Integer ORDER_STATUS_DONE= 5;
    /**退款状态**/
    public static final Integer ORDER_REFUND_STATUS_WAITREFUND =1;
    public static final Integer ORDER_REFUND_STATUS_REFUNDED =2;
    public static final Integer ORDER_REFUND_STATUS_PARTREFUNDED =3;

    /** 支付状态**/
    public static final Integer PAY_STATUS_WAITPAY=0;
    public static final Integer PAY_STATUS_PAYED=1;


    /*********************服务单表***************************/
    /** 服务单有效有效状态**/
    public static final Integer SERVICEUNIT_STATUS_UNACTIVE=0;
    public static final Integer SERVICEUNIT_STATUS_ACTIVE=1;
    /** 服务单服务状态**/
    public static final Integer SERVICEUNIT_WORK_STATUS_ARRIVE=1;
    public static final Integer SERVICEUNIT_WORK_STATUS_START=2;
    public static final Integer SERVICEUNIT_WORK_STATUS_DONE=3;
    public static final Integer SERVICEUNIT_WORK_STATUS_LEAVE=4;
    /**服务单状态**/
    public static final Integer SERVICEUNIT_STATUS_WAITASSIGN_PARTNER=1;
    public static final Integer SERVICEUNIT_STATUS_WAIT_PARTNER_CONFIRM =2;
    public static final Integer SERVICEUNIT_STATUS_WAITASSIGN_WORKER=3;
    public static final Integer SERVICEUNIT_STATUS_ASSIGNWORKE=4;
    public static final Integer SERVICEUNIT_STATUS_DONE=5;
    public static final Integer SERVICEUNIT_STATUS_REJECT=6;

    /*********************退款申请表***************************/
    //0 待退款 1 退款中 2 已退款  3 退款调用失败 4 申请被拒绝




}
