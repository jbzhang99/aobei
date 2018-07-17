package com.aobei.trainapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.internal.OSSUtils;
import com.aobei.train.model.Bespeak;
import com.aobei.trainapi.schema.Errors;
import custom.bean.OrderInfo;
import custom.util.DateUtil;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.SecapiPayRefund;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.JsonUtil;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 测试main方法
 */
public class TestMain {

    public static void testIntegerCompire() {

        Integer integer1 = new Integer(1);
        Integer integer2 = new Integer(1);
        Integer integer3 = null;
        int int1 = 1;
        int int2 = 3;

        System.out.println(integer1 == integer2);//fasle
        System.out.println(int1 == integer1);//true
        System.out.println(integer1.equals(integer2));//true
        System.out.println(integer1.equals(int1));//true
        System.out.println(integer3 == 0);


    }


    public static void testError() {


        Errors[] errors = Errors.values();
        for (Errors error : errors) {

            System.out.println(error.name());
            System.out.println(error.getMsg());

        }

    }

    public static void testHttprequest() {

        // 'Accept': 'application/json',
        //       'Content-Type': 'application/json',
        //     'Authorization': 'Bearer ' + access_token

        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                //.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"))
                .addHeader(new BasicHeader("Authorization", "Basic d3hfbV9jdXN0b206NHg5MWI3NGUtM2I3YS1iYjZ4LWJ0djktcXpjaW83ams2Zzdm"))
                .setUri("http://localhost:9010/oauth/token")
                .addParameter("username", "WX_customer_rpm")
                .addParameter("password", "123456")
                .addParameter("grant_type", "password")
                .build();
        CloseableHttpResponse response = LocalHttpClient.execute(httpUriRequest);

        HttpEntity entity = response.getEntity();
        try {
            System.out.println("返回值" + EntityUtils.toString(entity));
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void testCalendar() throws ParseException {

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
//        Date date = format.parse("2018-03-31");
//
//        Date end = null;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
//        end = calendar.getTime();
//        System.out.println(end);

        Calendar calendar = Calendar.getInstance();

        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));


    }


    public static void testString() {

        String a = "你们是谁啊你好呢哈你们是谁啊啊我是我啊我很好啊你们是谁啊你好呢哈你们是呢哈你们是你们是";
        System.out.println(a.length());
        System.out.println(a.getBytes().length);
        if (a.getBytes().length > 100) {
            System.out.println(a.substring(0, 20));
        }
    }

    public static void testarrEquals() {

        Integer[] arr1 = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3};
        Integer[] arr2 = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3};
        System.out.println(arr1.equals(arr2));


    }


    public static void testWxRefund() {

        SecapiPayRefund secapiPayRefund = null;
        String key = null;
        PayMchAPI.secapiPayRefund(secapiPayRefund, key);


    }


    private static void test(Bespeak bespeak) {


        String json = bespeak.getBespeak_strategy();

        List<Map<String, String>> list = JSON.parseObject(json, new TypeReference<List<Map<String, String>>>() {
        });
        String day = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);


        Map<String, String> map = list.stream().filter(t -> LocalDateTime.parse(day + "T" + t.get("before_hour")
                , DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(o -> o.get("before_hour"))).get();

        Integer minutes = Integer.parseInt(map.get("after_minutes"));
        LocalDateTime localDateTime = LocalDateTime.parse(day + "T" + map.get("before_hour")
                , DateTimeFormatter.ISO_LOCAL_DATE_TIME).plusMinutes(minutes);

        for (int i = 0; i < 7; i++) {

            System.out.println(localDateTime.plusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        System.out.println(localDateTime);


    }


    private static void testWxPay() {
        // unifiedorder:{"nonce_str":"1114965324399058944",
        // "time_expire":"20180517170639",
        // "time_start":"20180517163639",
        // "fee_type":"CNY",
        // "body":"精细擦窗0428精细擦窗0428",
        // "mch_id":"1500570101",
        // "total_fee":"1",
        // "sign_type":"MD5",
        // "openid":"okMYP5SvMbwOaikGl3adSuIfFPXQ",
        // "notify_url":"https://test-api.aobei.com/callback/wx_m_custom/wxpay",
        // "out_trade_no":"1526546198_2",
        // "appid":"wx653dc689ca79ac81",
        // "trade_type":"JSAPI"

        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid("wx653dc689ca79ac81");
        unifiedorder.setTime_start("20180517163639");
        unifiedorder.setTime_expire("20180517170639");
        unifiedorder.setTrade_type("JSAPI");
        unifiedorder.setFee_type("CNY");
        unifiedorder.setTotal_fee("1");
        unifiedorder.setSign_type("MD5");
        unifiedorder.setOpenid("okMYP5SvMbwOaikGl3adSuIfFPXQ");
        unifiedorder.setNotify_url("https://test-api.aobei.com/callback/wx_m_custom/wxpay");
        unifiedorder.setNonce_str("1114965324399058944");
        unifiedorder.setBody("精细擦窗0428精细擦窗0428");
        unifiedorder.setOut_trade_no("1526546198_2");
        unifiedorder.setMch_id("1500570101");

        payUnifiedorder(unifiedorder, "4ae72b0520e94ff7ba977ff935c06277");




    }

    public static void payUnifiedorder(Unifiedorder unifiedorder, String key) {
        Map<String, String> map = MapUtil.objectToMap(unifiedorder, "detail");
        //@since 2.8.8 detail 字段签名处理
        if (unifiedorder.getDetail() != null) {
            map.put("detail", JsonUtil.toJSONString(unifiedorder.getDetail()));
        }
        if (key != null) {
            String sign = SignatureUtil.generateSign(map, unifiedorder.getSign_type(), key);
            unifiedorder.setSign(sign);
        }
        String unifiedorderXML = convertToXML(unifiedorder);

        // System.out.println(unifiedorderXML==null?null:"OK");
//        HttpUriRequest httpUriRequest = RequestBuilder.post()
//                .setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.toString()))
//                .setUri("https://api.mch.weixin.qq.com/pay/unifiedorder")
//                .setEntity(new StringEntity(null, Charset.forName("utf-8")))
//                .build();
//        return LocalHttpClient.executeXmlResult(httpUriRequest,UnifiedorderResult.class,unifiedorder.getSign_type(),key);
    }

    private static final ThreadLocal<Map<Class<?>, Marshaller>> mMapLocal = new ThreadLocal<Map<Class<?>, Marshaller>>() {
        @Override
        protected Map<Class<?>, Marshaller> initialValue() {
            return new HashMap<Class<?>, Marshaller>();
        }
    };


    public static String convertToXML(Object object) {
        try {
            Map<Class<?>, Marshaller> mMap = mMapLocal.get();

            if (!mMap.containsKey(object.getClass())) {
                JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                //设置CDATA输出字符
                marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
                    public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
                        writer.write(ac, i, j);
                    }
                });
                mMap.put(object.getClass(), marshaller);
            }
            StringWriter stringWriter = new StringWriter();
            mMap.get(object.getClass()).marshal(object, stringWriter);
            return stringWriter.getBuffer().toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println("==========================");
        return null;
    }


    public static String ossSing(String date) {
//        aliyun.setPrivateBucket("aobei-avatar");
//        aliyun.setPublicBucket("aobei-avatar");
//        aliyun.setAccessKeyId("LTAIoOJQwVKeXwj4");
//        aliyun.setAccessKeySecret("YWZcECZUgOpy8c6qUP7IhFYqwVOEkP");
//        aliyun.setEndpoint("http://oss-cn-beijing.aliyuncs.com");

        String accessKeySecret = "YWZcECZUgOpy8c6qUP7IhFYqwVOEkP";
        String accessKeyId = "LTAIoOJQwVKeXwj4";
//        RequestMessage request = new RequestMessage("aobei-avatar", "/customer/cus2.png");
//        request.addHeader("content-type", "image/jpg");
//        // request.addHeader("x-oss-date", date);
//        request.addHeader("date", date);
//        String canonicalString = SignUtils.buildCanonicalString("PUT", "/aobei-avatar/customer", request, (String) null);

        String canonicalString = "PUT\n" + date + "\n/aobei-avatar";
        System.out.println("canonicalString=" + canonicalString);
        String signature = ServiceSignature.create().computeSignature(accessKeySecret, canonicalString);
        System.out.println("signature=" + signature);
        return OSSUtils.composeRequestAuthorization(accessKeyId, signature);
    }


    public static void putObject(String signatureValue, String date) {

        String fileName = "/Users/aobei-dev/Desktop/cus2.png";
        File file = new File(fileName);
        Long len = file.length();

        System.out.println("================" + signatureValue);
        String url = "http://aobei-avatar.oss-cn-beijing.aliyuncs.com";
        RequestBuilder builder = RequestBuilder.put().setUri(url);
        builder.setHeader("Content-Length", len + "");
        // builder.setHeader("Content-Type","image/jpg");
        builder.setHeader("Authorization", signatureValue);
        builder.setHeader("PUT", "/Users/aobei-dev/Desktop/cus2.png  HTTP/1.1");
        builder.setHeader("Date", date);
        HttpUriRequest httpUriRequest = builder.build();

        CloseableHttpResponse response = LocalHttpClient.execute(httpUriRequest);
        HttpEntity entity = response.getEntity();
        String str = null;
        try {
            str = EntityUtils.toString(entity, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str);


    }


    public static void swith(int i) {


        switch (i) {
            case 1:
            case 2:
                System.out.println("1111");
                break;
            case 3:
            case 4:
                System.out.println("333");
                break;
        }

    }


    private static boolean isHaveStore(Integer[] arr, int begin, int end, int restUnit) {
        //边界值检测 如果开始时间是在00:00,00:30,10:00 ，结束时间在23:00,23:30 都属于非法劳动时间。
        //去掉非法劳动时间。
        if (end < begin || begin < 3) {
            return false;
        }
        if (end > 45) {
            restUnit = 0;
        }
        //连续时间：如果不可使用，就代表不能提供服务；
        for (int i = begin - (restUnit-1); i < end + restUnit; i++) {
            if (arr[i] != 1) {
                if ((i < begin && arr[i] == 0) || (i > end && arr[i] == 0))
                    continue;
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args){
        Pattern pattern = Pattern.compile("^[a-zA-Z]{5}(\\-)\\d{4}$", Pattern.DOTALL);

        System.out.println(pattern.matcher("abace-1233").find());
    }


}
