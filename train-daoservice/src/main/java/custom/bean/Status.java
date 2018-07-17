package custom.bean;

public class Status {


    public enum OrderStatus {
        // 1 待付款  2 待确认  3 待服务 4 已取消 5 已完成
        wait_pay(1),
        wait_confirm(2),
        wait_service(3),
        cancel(4),
        done(5);
        public Integer value;
        private String name;

        private OrderStatus(Integer value) {
            this.name = name();
            this.value = value;
        }

    }

    public enum PayStatus {
        //支付状态 0 待支付  1 已支付
        wait_pay(0),
        payed(1);
        public Integer value;
        private String name;

        private PayStatus(Integer value) {
            this.name = name();
            this.value = value;
        }

    }

    public enum ServiceStatus {
        //服务单状态   1 待指派合伙人  2 待合伙人接单  3 待合伙指派  4 已指派学员  5  完成  6 拒单
        wait_assign_partner(1),
        wait_partner_confirm(2),
        wait_assign_worker(3),
        assign_worker(4),
        done(5),
        reject(6);
        public Integer value;
        private String name;

        private ServiceStatus(Integer value) {
            this.name = name();
            this.value = value;
        }
    }

    public enum WorkStatus {
        //人员工作状态1 到达 2 开始 3 结束 4 离开
        wait_service(0),
        arrive(1),
        start(2),
        done(3),
        leave(4);
        public Integer value;
        private String name;

        private WorkStatus(Integer value) {
            this.name = name();
            this.value = value;
        }
    }

    public enum RefundStatus {
        //退款状态  3 已退部分款
        // 0 待退款 1 退款中 2 已退款  3 退款调用失败 4 申请被拒绝
        wait_refund(0),
        refunding(1),
        refunded(2),
        fail(3),
        part_refunded(3),
        reject(4);
        public Integer value;
        private String name;

        private RefundStatus(Integer value) {
            this.name = name();
            this.value = value;
        }
    }

    public enum DeleteStatus {
        no(0),
        yes(1);
        public Integer value;
        private String name;

        private DeleteStatus(Integer value) {
            this.name = name();
            this.value = value;
        }
    }
    public enum ServiceUnitActive{

        unactive(0),
        active(1);
        public Integer value;
        private String name;

        private ServiceUnitActive(Integer value) {
            this.name = name();
            this.value = value;
        }

    }
    public enum RobbingActive{
        unactive(0),
        active(1);
        public Integer value;
        private String name;

        private RobbingActive(Integer value) {
            this.name = name();
            this.value = value;
        }
    }

    public enum ValidStatus{
        invalid(0),
        valid(1);
        public Integer value;
        private String name;

        private ValidStatus(Integer value) {
            this.name = name();
            this.value = value;
        }
    }


    public enum OnlineStatus{
        online(1),
        offline(0);
        public Integer value;
        private String name;

        private OnlineStatus(Integer value) {
            this.name = name();
            this.value = value;
        }
    }

    public enum CouponType {
        //优惠券类型   1 派发券  2 赔偿券  3 领取券  4 兑换券
        distribute_type(1),
        compensation_type(2),
        get_type(3),
        exchange_type(4);
        public Integer value;
        private String name;

        private CouponType(Integer value) {
            this.name = name();
            this.value = value;
        }
    }
}
