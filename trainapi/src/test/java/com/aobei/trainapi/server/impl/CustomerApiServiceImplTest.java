package com.aobei.trainapi.server.impl;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.schema.input.CustomerAddressInput;
import com.aobei.trainapi.schema.input.OrderInput;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.util.AccessTokenUtil;
import com.aobei.trainapi.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import custom.bean.*;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsCollectionContaining.hasItem;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CustomerApiServiceImplTest {

    @Autowired
    CustomerApiService customerApiService;
    @Autowired
    RedisService redisService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    MetadataService metadataService;
    @Autowired
    CouponService couponService;
    @Autowired
    CouponReceiveService receiveService;
    static Long customer_id = 1054072042874716160l;
    static String phone = "18600203527";
    static Long product_id = 1046807902222573568l;
    static Long psku_id = 1064949566870937600l;
    static long user_id = 1075114059960786944l;
    static String path = "wx_m_customer";

    @Test
    public void customerInfo() {
        Customer customer1 = customerApiService.customerInfo(1093988850029l);
        Customer customer2 = customerApiService.customerInfo(1046103419031896064l);
        Assert.assertNull(customer1);
        Assert.assertNotNull(customer2);
        Assert.assertEquals(customer2.getName(), "rpm");
    }

    @Test
    public void checkVerificationCode() {
        String code = "3567";

        String key = Constant.getVerificationCodeKey(user_id, phone);
        redisService.setStringValue(key, code, 5, TimeUnit.MINUTES);
        long min = redisService.getExpire(key, TimeUnit.MINUTES);
        boolean result = customerApiService.checkVerificationCode(user_id, code, phone);
        boolean result1 = customerApiService.checkVerificationCode(user_id, code, phone);
        Assert.assertTrue(min > 0 && min < 5);
        Assert.assertTrue(result);
        Assert.assertFalse(result1);
    }

    @Test
    public void smsCount() {
        String type = "code";
        String key = Constant.getMaxnumSendKey(phone, type);
        for (int i = 0; i < 5; i++) {
            redisService.increment(key, 1);
        }
        Assert.assertEquals(customerApiService.smsCount(phone, type), 5);
        redisService.delete(key);

    }

    @Test
    public void homeCategoryList() {
        List<Category> list = customerApiService.homeCategoryList();
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 4);

        Category category = new Category();
        category.setName("redis缓存中的分类");
        list = new ArrayList<>();
        list.add(category);
        list.add(category);
        try {
            String json = JacksonUtil.object_to_json(list);
            redisService.setStringValue(Constant.getHomeCategoryKey("customer"), json, 100, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        list = customerApiService.homeCategoryList();
        for (Category category1 : list) {
            Assert.assertEquals(category1.getName(), "redis缓存中的分类");
        }
    }

    @Test
    public void productListByCategoryLevelCode() {

        String leveCode = "0001";
        List<ProductWithCoupon> list = customerApiService.productListByCategoryLevelCode(leveCode, 1, 5, "", customer_id);

        Assert.assertNotNull(list);
        for (Product product : list) {
            Assert.assertTrue(product.getCategory_level_code().startsWith(leveCode));
        }


    }

    @Test
    public void homeProductList() {

        List<ProductWithCoupon> list = customerApiService.homeProductList(1, 2, "wx_m_custom", customer_id);
        Assert.assertEquals(list.size(), 2);
        list = customerApiService.homeProductList(1, 3, "wx_m_custom", customer_id);
        Assert.assertEquals(list.size(), 3);
    }

    @Test
    public void bannerList() {
    }

    @Test
    public void productDetail() {
        ApiResponse<ProductInfo> productInfo = customerApiService.productDetail(product_id, 12355L);
        Product product = productInfo.getT().getProduct();
        List<ProSku> proSkus = productInfo.getT().getProSkus();
        Assert.assertEquals(product_id, product.getProduct_id());
        Assert.assertTrue(proSkus.size() > 0);

    }

    @Test
    public void proSkuList() {
        List<ProSku> proSkus = customerApiService.proSkuList(product_id, 1, 10);
        Assert.assertTrue(proSkus.size() > 0);
    }

    @Test
    public void addressList() {
        List<CustomerAddress> addresses1 = customerApiService.addressList(customer_id);

        List<CustomerAddress> addresses2 = addresses1.stream()
                .filter(t -> t.getCustomer_id().equals(customer_id))
                .collect(Collectors.toList());
        Assert.assertEquals(addresses2.size(), addresses1.size());
    }

    @Test
    public void getDefaultAddress() {
        CustomerAddress address = customerApiService.getDefaultAddress(customer_id);
        Assert.assertEquals(address.getDefault_address(), new Integer(1));

    }

    @Test
    public void productAvailableTimes() {

        Customer customer = customerApiService.customerInfo(user_id);
        CustomerAddress address = customerApiService.getDefaultAddress(customer_id);
        ProductInfo productInfo = customerApiService.productDetail(product_id, customer_id).getT();
        // ProSku proSku  = DataAccessUtils.singleResult(productInfo.getProSkus().stream().filter(t->t.getPsku_id()==psku_id).collect(Collectors.toList()));
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_RESERVE_DAYS_SPAN);
        Integer span = metadata.getMeta_value() == null ? Constant.MAXNUM_RESERVATION_SPAN : Integer.parseInt(metadata.getMeta_value());

        List<TimeModel> timeModels = customerApiService.productAvailableTimes(customer, product_id, psku_id, address.getCustomer_address_id(), 1);
        Assert.assertTrue(timeModels.size() == span.intValue());
        List<String> days = daysSapn(span, false);
        Assert.assertThat(days, hasItem(timeModels.get(0).getDateTime()));


    }

    @Test
    public void couponList() {
        Customer customer = customerApiService.customerInfo(user_id);
        List<CouponResponse> coupons = customerApiService.couponList(customer, 1, 5);
        if (coupons == null)
            return;
        for (CouponResponse response : coupons) {
            Assert.assertNotNull(response.getValue());
            Assert.assertNotNull(response.getCoupon_receive_id());
        }
    }

    @Test
    public void orderList() {
        Customer customer = customerApiService.customerInfo(user_id);
        List<OrderInfo> orderInfos = customerApiService.orderList("", customer.getCustomer_id(), 1, 5);
        Assert.assertNotNull(orderInfos);
        orderInfos = customerApiService.orderList("cancel", customer.getCustomer_id(), 1, 5);
        for (OrderInfo info :
                orderInfos) {
            Assert.assertEquals(info.getOrderStatus(), "cancel");
        }
    }

    @Test
    public void orderDetail() {
        Customer customer = customerApiService.customerInfo(1078710100371218432l);
        OrderInfo orderInfo = DataAccessUtils.singleResult(customerApiService.orderList("cancel", customer.getCustomer_id(), 1, 1));


        OrderInfo orderInfo1 = customerApiService.orderDetail(customer, "1524832155");
        Assert.assertEquals(orderInfo.getPay_order_id(), orderInfo1.getPay_order_id());
        Assert.assertEquals(orderInfo.getPartner_name(), orderInfo1.getPartner_name());
    }

    @Test
    public void confirmPayStatus() {
        Customer customer = customerApiService.customerInfo(user_id);

        ApiResponse<Order> response = customerApiService.confirmPayStatus(customer, "1234567899", path);

        if (response.getErrors() != null)
            Assert.assertEquals(response.getErrors().name(), "_41007");
        response = customerApiService.confirmPayStatus(customer, "1072735212895977472716160", path);
        Assert.assertEquals(response.getT().getName(), "各种保洁2313");
        Assert.assertEquals(response.getT().getCus_phone(), "18600203527");
        Assert.assertEquals(response.getT().getCus_username(), "rpm");

    }

    @Test
    public void getPayOrder() {
        Customer customer = customerApiService.customerInfo(user_id);
        Order order = customerApiService.getPayOrder(customer, "1072735212895977472716160");
        Assert.assertEquals(order.getName(), "各种保洁2313");
        Assert.assertEquals(order.getCus_phone(), "18600203527");
        Assert.assertEquals(order.getCus_username(), "rpm");
    }

    @Test
    public void sendVerificationCode() {

        ApiResponse response = customerApiService.sendVerificationCode(user_id, "18600203527");
        if (response.getErrors() != null) {
            Assert.assertEquals(response.getErrors().name(), "_41012");
        } else {
            Assert.assertEquals(response.getMutationResult().getStatus(), 0);
        }

    }

    @Test
    public void bindUser() {

        ApiResponse response = customerApiService.bindUser(user_id, "rpm", "18600203527", 1234567);
        if (response.getErrors() != null) {
            Assert.assertEquals(response.getErrors().name(), "_41017");
        } else {
            Assert.assertEquals(response.getMutationResult().getStatus(), 0);
        }
    }

    @Test
    public void changePhone() {
        Customer customer = customerApiService.customerInfo(user_id);
        Assert.assertEquals(phone, customer.getPhone());
        String phone1 = "18600203528";
        ApiResponse response = customerApiService.changePhone(customer, phone1);
        if (response.getErrors() != null) {
            Assert.assertEquals(response.getErrors().name(), "_41017");
        } else {
            Assert.assertEquals(response.getMutationResult().getStatus(), 0);
        }
        customer = customerApiService.customerInfo(user_id);
        Assert.assertEquals(phone1, customer.getPhone());
        response = customerApiService.changePhone(customer, phone);
        if (response.getErrors() != null) {
            Assert.assertEquals(response.getErrors().name(), "_41017");
        } else {
            Assert.assertEquals(response.getMutationResult().getStatus(), 0);
        }

    }

    @Test
    public void setPassword() {
        customerApiService.setPassword(user_id, "12345678", "12345678");
        Header header = new BasicHeader("Authorization", "Basic d3hfbV9jdXN0b206NHg5MWI3NGUtM2I3YS1iYjZ4LWJ0djktcXpjaW83ams2Zzdm");
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        AccessTokenUtil.Auth auth = AccessTokenUtil.buildAuth("WX_customer_rpm", "123456", null, headers);
        AccessTokenUtil.AccessToken token = AccessTokenUtil.getAccessToken("https://dev-auth.aobei.com/oauth/token", auth);
        // AccessTokenUtil.AccessToken token = AccessTokenUtil.getAccessToken("http://localhost:9010/oauth/token", auth);
        Assert.assertEquals(token.getError(), "invalid_grant");
        Assert.assertEquals(token.getError_description(), "Bad credentials");
        customerApiService.setPassword(user_id, "123456", "123456");
        AccessTokenUtil.Auth auth1 = AccessTokenUtil.buildAuth("WX_customer_rpm", "123456", null, headers);
        AccessTokenUtil.AccessToken token1 = AccessTokenUtil.getAccessToken("https://dev-auth.aobei.com/oauth/token", auth1);
        Assert.assertEquals(token1.getRole(), "ROLE_CUSTOMER");
        Assert.assertEquals(token1.getUuid(), "1046103419031896064");

    }

    @Test
    public void addressAdd() {
        Customer customer = customerApiService.customerInfo(user_id);
        CustomerAddressInput input = new CustomerAddressInput();
        input.setAddress("北京市 大兴区 ");
        input.setLbs_lat("123.1111");
        input.setLbs_lng("123.2222");
        input.setProvince("北京");
        input.setCity("北京市");
        input.setDistrict("大兴区");
        CustomerAddress address = customerApiService.addressAdd(customer, input);
        Assert.assertEquals(address.getCity() + "", 110100 + "");

    }

    @Test
    public void addressUpdate() {
        Customer customer = customerApiService.customerInfo(user_id);
        CustomerAddressInput input = new CustomerAddressInput();
        input.setCustomer_address_id(1077083981368246272l);
        input.setAddress("北京市 大兴区 ");
        input.setLbs_lat("123.1111");
        input.setLbs_lng("49.19929");
        input.setProvince("北京");
        input.setCity("北京市");
        input.setDistrict("大兴区");
        CustomerAddress address = customerApiService.addressUpdate(customer, input);
        Assert.assertEquals(address.getLbs_lng(), "49.19929");

    }

    @Test
    public void addressDelete() {


        Customer customer = customerApiService.customerInfo(user_id);
        CustomerAddressInput input = new CustomerAddressInput();
        input.setAddress("北京市 大兴区 ");
        input.setLbs_lat("123.1111");
        input.setLbs_lng("123.2222");
        input.setProvince("北京");
        input.setCity("北京市");
        input.setDistrict("大兴区");
        CustomerAddress address = customerApiService.addressAdd(customer, input);
        Assert.assertEquals(address.getCity() + "", 110100 + "");
        customerApiService.addressDelete(customer, address.getCustomer_address_id());


    }

    @Test
    public void setDefaultAddress() {
        //1054854158793293824
        //1054855104877281280
        Customer customer = customerApiService.customerInfo(user_id);
        customerApiService.setDefaultAddress(customer.getCustomer_id(), 1054855104877281280l);
        CustomerAddressExample example = new CustomerAddressExample();
        example.or().andCustomer_idEqualTo(customer.getCustomer_id());
        List<CustomerAddress> list = customerApiService.addressList(customer.getCustomer_id());
        for (CustomerAddress customerAddress : list) {

            if (customerAddress.getCustomer_address_id().equals(1054855104877281280l)) {
                Assert.assertEquals(customerAddress.getDefault_address() + "", "1");
            } else {
                Assert.assertEquals(customerAddress.getDefault_address() + "", "0");
            }

        }
    }

    @Test
    public void recalculatePrice() {

        Customer customer = customerApiService.customerInfo(user_id);

        ApiResponse<Integer> response = customerApiService.recalculatePrice(customer, psku_id, 0l, 1);
        int price = response.getT();
        ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
        int price2 = proSku.getPrice();
        Assert.assertEquals(price, price2);
    }

    @Test
    public void createOrder() {
        Customer customer = customerApiService.customerInfo(user_id);
        OrderInput input = new OrderInput();
        input.setCustomer_address_id(1054855104877281280l);
        input.setNum(1);
        input.setPsku_id(psku_id);
        input.setProduct_id(product_id);
        input.setBegin_datetime("2018-03-27 10:00");
        input.setEnd_datatime("2018-03-27 12:00");
        ApiResponse<Order> response = customerApiService.createOrder(customer, input, "12345","self");
        Order order = response.getT();

        Order order1 = customerApiService.getPayOrder(customer, order.getPay_order_id());

        Assert.assertNotNull(order1);
        Assert.assertEquals(order.getCus_username(), order1.getCus_username());

    }

    @Test
    public void orderPaysuccess() {
        //测试支付成功订单服务单的生成
        String pay_order_id = "1524481533";
        ApiResponse apiResponse = customerApiService.orderPaysuccess(pay_order_id, 1);
    }

    @Test
    public void cancelOrder() {
        Customer customer = customerApiService.customerInfo(user_id);
        //1077183807674105856716160
        customerApiService.cancelOrder(customer, "1077183807674105856716160", " 单元测试，未支付取消");
        Order order = customerApiService.getPayOrder(customer, "1077183807674105856716160");

        Assert.assertEquals(order.getStatus_active() + "", "4");
    }

    @Test
    public void serviceEvaluate() {
        Customer customer = customerApiService.customerInfo(user_id);
        ApiResponse response = customerApiService.serviceEvaluate(customer, "1077183807674105856716160", 5, "单元测试评价");
        if (response.getErrors() != null) {
            Order order = customerApiService.getPayOrder(customer, "1077183807674105856716160");
            if (order == null) {
                Assert.assertTrue(response.getErrors().name().equals("_41007"));
            } else {
                Set<String> set = new HashSet<>();
                set.add("_41014");
                set.add("_41236");
                Assert.assertThat(set, hasItem(response.getErrors().name()));
            }

        }
    }

    @Test
    public void serviceComplete() {
        Customer customer = customerApiService.customerInfo(user_id);
        ApiResponse response = customerApiService.serviceComplete(customer, "1077183807674105856716160");
        if (response.getErrors() != null) {
            Order order = customerApiService.getPayOrder(customer, "1077183807674105856716160");
            if (order == null) {
                Assert.assertTrue(response.getErrors().name().equals("_41007"));
            } else {
                Set<String> set = new HashSet<>();
                set.add("_41041");
                Assert.assertThat(set, hasItem(response.getErrors().name()));
            }

        }
    }


    @Test
    public void transactionTest() {
        customerApiService.transactionTest();
    }


    /**
     * 获取指定时间。
     * 目前需求：不能预约当天的
     *
     * @return
     */
    private List<String> daysSapn(int span, boolean today) {
        List<String> list = new ArrayList<>();

        if (today) {
            for (int i = 0; i < span; i++) {
                list.add(getFetureDate(i));

            }
        } else {
            for (int i = 1; i <= span; i++) {
                list.add(getFetureDate(i));

            }
        }

        return list;
    }


    /**
     * 计算从今天算起，今后日期的字符串值
     *
     * @param past
     * @return
     */
    private String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }


    @Test
    public void newCouponlist() {
        Customer customer = customerApiService.customerInfo(user_id);
        List<CouponResponse> expireRespone = customerApiService.expireCouponList(customer, 1);
        List<CouponResponse> usedRespone = customerApiService.usedCouponList(customer, 1);
        expireRespone.stream().forEach(t -> {
            Assert.assertEquals(t.getStatus() + 0L, 5L);
        });
        usedRespone.stream().forEach(t -> {
            Set<Integer> set = new HashSet<>();
            set.add(3);
            set.add(4);
            Assert.assertThat(set, hasItem(t.getStatus()));

        });
    }
}