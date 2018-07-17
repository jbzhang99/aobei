package custom.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {


    public static Map<String, Integer> timeUnisMap = new HashMap<>();
    public static List<String> timeUnitList = new ArrayList<>();
    public static String defaultAvailableTime = "[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
            "1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1," +
            "1,1,1,0,0,0,0,0]";
    public static String defaultNoTime = "[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
            "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
            "0,0,0,0,0,0,0,0]";
    public static Integer[] defaultAvailableTimeArrar = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1,1,1 };
    public static Integer[] defaultStudentsStopSingle = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3};
    static {
        timeUnisMap.put("00:00", 0);
        timeUnisMap.put("00:30", 1);
        timeUnisMap.put("01:00", 2);
        timeUnisMap.put("01:30", 3);
        timeUnisMap.put("02:00", 4);
        timeUnisMap.put("02:30", 5);
        timeUnisMap.put("03:00", 6);
        timeUnisMap.put("03:30", 7);
        timeUnisMap.put("04:00", 8);
        timeUnisMap.put("04:30", 9);
        timeUnisMap.put("05:00", 10);
        timeUnisMap.put("05:30", 11);
        timeUnisMap.put("06:00", 12);
        timeUnisMap.put("06:30", 13);
        timeUnisMap.put("07:00", 14);
        timeUnisMap.put("07:30", 15);
        timeUnisMap.put("08:00", 16);
        timeUnisMap.put("08:30", 17);
        timeUnisMap.put("09:00", 18);
        timeUnisMap.put("09:30", 19);
        timeUnisMap.put("10:00", 20);
        timeUnisMap.put("10:30", 21);
        timeUnisMap.put("11:00", 22);
        timeUnisMap.put("11:30", 23);
        timeUnisMap.put("12:00", 24);
        timeUnisMap.put("12:30", 25);
        timeUnisMap.put("13:00", 26);
        timeUnisMap.put("13:30", 27);
        timeUnisMap.put("14:00", 28);
        timeUnisMap.put("14:30", 29);
        timeUnisMap.put("15:00", 30);
        timeUnisMap.put("15:30", 31);
        timeUnisMap.put("16:00", 32);
        timeUnisMap.put("16:30", 33);
        timeUnisMap.put("17:00", 34);
        timeUnisMap.put("17:30", 35);
        timeUnisMap.put("18:00", 36);
        timeUnisMap.put("18:30", 37);
        timeUnisMap.put("19:00", 38);
        timeUnisMap.put("19:30", 39);
        timeUnisMap.put("20:00", 40);
        timeUnisMap.put("20:30", 41);
        timeUnisMap.put("21:00", 42);
        timeUnisMap.put("21:30", 43);
        timeUnisMap.put("22:00", 44);
        timeUnisMap.put("22:30", 45);
        timeUnisMap.put("23:00", 46);
        timeUnisMap.put("23:30", 47);


        timeUnitList.add("00:00");
        timeUnitList.add("00:30");
        timeUnitList.add("01:00");
        timeUnitList.add("01:30");
        timeUnitList.add("02:00");
        timeUnitList.add("02:30");
        timeUnitList.add("03:00");
        timeUnitList.add("03:30");
        timeUnitList.add("04:00");
        timeUnitList.add("04:30");
        timeUnitList.add("05:00");
        timeUnitList.add("05:30");
        timeUnitList.add("06:00");
        timeUnitList.add("06:30");
        timeUnitList.add("07:00");
        timeUnitList.add("07:30");
        timeUnitList.add("08:00");
        timeUnitList.add("08:30");
        timeUnitList.add("09:00");
        timeUnitList.add("09:30");
        timeUnitList.add("10:00");
        timeUnitList.add("10:30");
        timeUnitList.add("11:00");
        timeUnitList.add("11:30");
        timeUnitList.add("12:00");
        timeUnitList.add("12:30");
        timeUnitList.add("13:00");
        timeUnitList.add("13:30");
        timeUnitList.add("14:00");
        timeUnitList.add("14:30");
        timeUnitList.add("15:00");
        timeUnitList.add("15:30");
        timeUnitList.add("16:00");
        timeUnitList.add("16:30");
        timeUnitList.add("17:00");
        timeUnitList.add("17:30");
        timeUnitList.add("18:00");
        timeUnitList.add("18:30");
        timeUnitList.add("19:00");
        timeUnitList.add("19:30");
        timeUnitList.add("20:00");
        timeUnitList.add("20:30");
        timeUnitList.add("21:00");
        timeUnitList.add("21:30");
        timeUnitList.add("22:00");
        timeUnitList.add("22:30");
        timeUnitList.add("23:00");
        timeUnitList.add("23:30");

    }

    public static final String SIGN_NAME = "浦尔家";
    //您有一张新的${product_name}订单,请及时确认。
    public static final String SEND_TO_PARTNER_WHEN_CUSTOMER_PAYED = "SMS_128635496";

    //您有一张新的${product_name}订单，订单号：${pay_order_id}}，还有10分钟将被自视为动拒单，请及时确认。
    @Deprecated
    public static final String SEND_TO_PARTNER_AFTER_CUSTOMER_PAYED_20MINUTE = "SMS_125023728";

    //（变量重复不能申请）您的有一个新的${product_name}任务，于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}进行服务。
    public static final String SEND_TO_WORK_WHEN_ORDER_ASSIGN = "SMS_125023823";
    //您的${product_name}订单已确认，服务人员${worker_name}将于${c_begin_datetime}，去${cus_address}为您进行服务。
    //public static final String SEND_TO_CUSTOMER_WHEN_ORDER_ASSIGN_WORK = "SMS_138061061";
    public static final String SEND_TO_CUSTOMER_WHEN_ORDER_ASSIGN_WORK = "SMS_125018781";

    //您于${c_begin_datetime}去${cus_address}，为${cus_username}，进行${product_name}服务的订单已取消。
    public static final String SEND_TO_PARTNER_WHEN_ORDER_CANCEL = "SMS_125018783";

    //您于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}，进行${product_name}服务的订单已取消。
    public static final String SEND_TO_WORK_WHEN_ORDER_CANCEL = "SMS_125018784";

    //您预约的${product_name}，${c_begin_datetime}去${cus_address}的订单已经自动取消。
    public static final String SEND_TO_CUSTOMER_WHEN_ORDER_CANCEL = "SMS_125028761";

    //因您在${pay_timeout}分钟内未支付，您预约的${product_name}，${c_begin_datetime}去${cus_address}的订单已经自动取消。
    @Deprecated
    public static final String SEND_TO_CUSTOMER_WHEN_PAY_TIMEOUT = "SMS_127150582";

    //两小时后，您将于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}进行${product_name}服务。
    @Deprecated
    public static final String SEND_TO_WORK_BEFORE_SERVICE_2HOURS = "SMS_125023730";

    //两小时后，服务人员${worker_name}将于${c_begin_datetime}去${cus_address}为您服务，请等待服务人员到来
    @Deprecated
    public static final String SEND_TO_CUSOTMER_BEFORE_SERVICE_2HOURS = "SMS_125118551";

    //明天，服务人员${worker_name}将于${c_begin_datetime}去${cus_address}为您服务，请安排好您的时间等待服务人员上门为您服务。
    public static final String SEND_TO_CUSTOMER_BEFORE_SERVICE_26HOURS = "SMS_125028762";

    //服务人员${worker_name}于${c_begin_datetime}去${cus_address}为您进行${product_name}服务的订单已完成，请去我的订单里进行评价.
    @Deprecated
    public static final String SEND_TO_CUSTOMER_WHEN_SERVICE_DONE = "SMS_125023731";

    //您的${produce_name}订单服务人员变为${worker_name}，服务时间变更为${c_begin_datetime}去${cus_address}为您进行服务。
    public static final String SEND_TO_CUSTOMER_WHEN_ORDER_ALTER = "SMS_130923210";

    //您的验证码是${code}，有效期5分钟，请尽快填写验证码完成验证。不要告诉其他人哦。
    public static final String SEND_VERIFICATION_CODE = "SMS_125018908";

    //已为您发送一张价值${coupon_price}元的${produce_name}浦尔家优惠券，请在${coupon_expire}内使用。
    public static final String USER_GOT_A_COUPON = "SMS_125023732";
    /**
     * 一些数值型常量
     */

    public static final int VERIFICATION_CODE_TIMEOUT = 5;
    public static final int ORDER_PAY_TIMEOUT = 30;
    public static final int SEARCH_RADIUS = 5000;
    public static final int SERVICE_STATUS_UPDATE_RADIUS = 1000;
    public static final int SERVICE_INTERVAL_UNIT = 2;
    public static final int MAXNUM_RESERVATION_SPAN = 3;
    public static final int MAXNUM_STUDENT_STOP_SPAN=7;
    public static final Long ONE_HOUR=60*60*1000l;
    public static final Long HALF_HOUR=30*60*1000l;

    public static final String MKEY_VERIFICATION_CODE_TIMEOUT="verification_code_timeout";
    public static final String MKEY_SERVICE_INTERVAL_UNIT="service_interval_unit";
    public static final String MKEY_MAX_SIGN_RADIUS="max_sign_radius";
    public static final String MKEY_MAX_SEARCH_RADIUS="max_search_radius";
    public static final String MKEY_MAX_RESERVE_DAYS_SPAN="max_reserve_days_span";
    public static final String MKEY_MAX_PAY_TIMEOUT="max_pay_timeout";
    public static final String MKEY_MAX_LEAVE_DAYS_SPAN="max_leave_days_span";
    public static final String MKEY_MAX_AUTO_COMPLETE_TIME="max_auto_complete_time";
    public static final String MKEY_MAX_CANCEL_TIMEOUT="max_cancel_timeout";
    public static final String MKEY_MAX_ROBBING_TIMEOUT="max_robbing_timeout";
    /**
     * 以下为redis中pub/sub的tocpic值（channel值）
     */

    public static final String REDIS_SMS_TOPIC = "sms";
    public static final String REDIS_COMMAND_TOPIC = "command";
    /**
     * 以下为redis中生成key使用到的key的前缀，
     */
    public static final String REDIS_KEY_VERIFICATION_PRE = "verification_code_";
    public static final String REDIS_KEY_AND = "_and_";
    public static final String REDIS_KEY_LAST_CUSTOMER_ADDRESS_PRE = "last_customer_address_";
    public static final String REDIS_KEY_MAXNUM_SEND_PRE = "maxnum_send_";
    public static final String REDIS_KEY_PARTNER_STATION = "partner_station_";
    public static final String REDIS_KEY_STUDENT_TASK_SCHEDULE = "worker_task_schedule_";
    public static final String REDIS_KEY_STATION_TASK_SCHEDULE = "station_task_schedule_";
    public static final String REDIS_KEY_HOME_CATEGORY = "home_category_";
    public static final String REDIS_KEY_WX_UNIFIEDORDER = "wx_unifiedorder_";
    public static final String REDIS_KEY_ROBBING_ORDER = "robbing_order_";
    public static final String REDIS_KEY_PRETAKEN = "taken_";


    /**
     * 一些原子操作的key
     */
    public static final String REDIS_COUPON_PRE="coupon_";

    /**
     * mq 发送的所有tag
     */
    public static final String TAG_SMS = "sms";
    public static final String TAG_COMMAND = "command";

    public static String getVerificationCodeKey(Long user_id, String phone) {
        return REDIS_KEY_VERIFICATION_PRE + user_id
                + REDIS_KEY_AND + phone;
    }
    
    //抢单key
    public static String getRobbingOrderKey(Long serviceUnit) {
        return REDIS_KEY_ROBBING_ORDER + serviceUnit;
    }

    public static String getLastUserCustomerAddressKey(Long customer_id) {

        return REDIS_KEY_LAST_CUSTOMER_ADDRESS_PRE + customer_id;
    }

    public static String getMaxnumSendKey(String phone, String type) {

        return REDIS_KEY_MAXNUM_SEND_PRE + phone + REDIS_KEY_AND + type;
    }

    public static String getPartnerStationKey(int city) {

        return REDIS_KEY_PARTNER_STATION + city;
    }

    public static String getStudentTaskScheduleKey(Long student_id, String dateTime) {
        return REDIS_KEY_STUDENT_TASK_SCHEDULE + student_id + REDIS_KEY_AND + dateTime;
    }

    public static String getSationTaskScheduleKey(Long station_id, Long proSku_id, String dateTime) {
        return REDIS_KEY_STATION_TASK_SCHEDULE + station_id + REDIS_KEY_AND + proSku_id + REDIS_KEY_AND + dateTime;
    }

    public static String getHomeCategoryKey(String appid) {
        return REDIS_KEY_HOME_CATEGORY + appid;
    }

    public static String getWxUnifiedorderKey(String pay_order_id) {
        return REDIS_KEY_WX_UNIFIEDORDER + pay_order_id;
    }

    public static String getPreTakenKey(Long student_id,String datetime,int start,int end){
        return  REDIS_KEY_PRETAKEN+student_id+REDIS_KEY_AND+datetime+REDIS_KEY_AND+start+REDIS_KEY_AND+end;
    }

    public static String getCouponKey(Long coupon_id){
        return  REDIS_COUPON_PRE+coupon_id;
    }

}
