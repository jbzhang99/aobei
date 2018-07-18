package com.aobei.trainconsole.util;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mr_bl on 2018/7/4.
 */
public class QimoServer {

    private static final String ACCOUNT = "N00000023280";//账户
    private static final String SECRET = "b2f19090-7f35-11e8-a021-337829478c65";//api密码
    private static final String HOST = "http://apis.7moor.com";
    private static Logger logger = LoggerFactory.getLogger(QimoServer.class);

    /**
     * token验证
     * 返回值样例：{code : 200 , message :"token check success!"}
     *   code值详解:
     *   200：合法请求 校验成功
     *   500：服务器错误
     *   501：未查询到对应token数据
     *   502：很抱歉,token已经超时
     *   503：token不匹配,非法请求
     */
    public static CloseableHttpResponse checkTokenLegal(String token,String tokenid) throws Exception{
        String interfacePath = "/v20160818/sso/checkTokenLegal/";
        StringEntity requestEntity = null;
        //根据需要发送的数据做相应替换
        requestEntity = new StringEntity("{\"token\":"+token+"\",tokenid\":"+tokenid+"}","UTF-8");
        return service(interfacePath, requestEntity);

    }

    /**
     * 工单流入
     * 返回值样例：{code : 200 , message :"ok"}
     * code：
     *    200	请求成功
     *    400	请求体参数错误
     *    403	鉴权参数错误
     *    500	服务器错误
     */
    public static CloseableHttpResponse handleBusiness(String requestBodyJson) throws Exception{
        String interfacePath = "/v20170704/business/handleBusiness/";
        StringEntity requestEntity = null;
        //根据需要发送的数据做相应替换
        requestEntity = new StringEntity(requestBodyJson,"UTF-8");
        return service(interfacePath, requestEntity);

    }

    public static String md5 (String text) {
        return DigestUtils.md5Hex(text).toUpperCase();
    }
    public static String base64 (String text) {
        byte[] b = text.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }
    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
    public static CloseableHttpResponse service(String interfacePath,
                                                StringEntity requestEntity) throws Exception{
        String time = getDateTime();
        String sig = md5(ACCOUNT + SECRET + time);
        String url = HOST + interfacePath + ACCOUNT + "?sig=" + sig;
        String auth = base64(ACCOUNT + ":" + time);
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type","application/json;charset=utf-8");
        post.addHeader("Authorization",auth);
        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        return client.execute(post);
    }

    public static void main(String[] args) {

        String time = getDateTime();
        String sig = md5(ACCOUNT + SECRET + time);
        //查询坐席状态接口
        String interfacePath = "/v20160818/user/queryUserState/";
        String url = HOST + interfacePath + ACCOUNT + "?sig=" + sig;
        String auth = base64(ACCOUNT + ":" + time);
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type","application/json;charset=utf-8");
        post.addHeader("Authorization",auth);
        StringEntity requestEntity = null;
        //根据需要发送的数据做相应替换
        requestEntity = new StringEntity("{\"exten\":\"8000\"}","UTF-8");
        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            logger.info("the response is : " + EntityUtils.toString(entity,"utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 签名 校验
     * @param authorizationHeard 请求头数据
     * @param sig 签名数据
     * @return
     */
    public static boolean sigValidate(String authorizationHeard, String sig){
        if(Objects.nonNull(authorizationHeard) && Objects.nonNull(sig)){
            // base64 解码
            String authorization = new String(java.util.Base64.getDecoder().decode(authorizationHeard));
            // 获取数据    格式   账户Id + 冒号 + 时间戳
            String[] authData = authorization.split(":");
            // 检查开发者账号是否一致
            if(ACCOUNT.equals(authData[0])){
                // 生成 MD5 签名
                String md5Sign = DigestUtils.md5Hex(authorization.replace(":", SECRET)).toUpperCase();
                return sig.equals(md5Sign);
            }
        }
        return false;
    }


}
