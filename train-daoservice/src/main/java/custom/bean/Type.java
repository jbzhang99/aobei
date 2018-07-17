package custom.bean;


public class Type {








    public enum PayType {
        wxpay(1),
        alipay(2),
        unionPay(3);
        private String name;
        public Integer value;

        private PayType(Integer value) {
            this.name = name();
            this.value = value;
        }

    }

    public enum RefundType{

        auto(1),
        system(2);

        private  String name;
        public Integer value;
        private RefundType(Integer value){

            this.name = name();
            this.value = value;
        }
    }

    public enum CouponEnvType {
        all_order(0),
        first_order(1);
        private String name;
        public Integer value;

        private CouponEnvType(Integer value) {
            this.name = name();
            this.value = value;
        }

        public static CouponEnvType getType(Integer value) {
            CouponEnvType[] types = CouponEnvType.values();

            for (int i = 0; i < types.length; i++) {
                if (types[i].value == value)
                    return types[i];
            }
            return null;
        }

    }
}
