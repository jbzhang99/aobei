package com.aobei.trainconsole.util;

import com.alibaba.fastjson.JSONObject;
import custom.bean.JdToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by mr_bl on 2018/6/26.
 */
public class JdAuthTokenUtil {

    private static final String client_id = "F67A59B071284E359D8D12F1E4951567";

    //todo 上线前和开发者账号中的redirect_uri同步修改
    private static final String redirect_uri = "http://console.aobei_1.com/systemmanager/result_receive";

    private static final String client_secret = "ebd674ee3a134862b7b1061bf95e4969";

    /**
     * 根据code码请求京东授权token接口获取token
     * @param code
     * @return
     * @throws IOException
     */
    public static JdToken getTokenByCode(String code) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建http GET请求
            HttpUriRequest httpPost = RequestBuilder.post()
                    .setUri("https://oauth.jd.com/oauth/token")
                    .addParameter("grant_type", "authorization_code")
                    .addParameter("client_id", client_id)
                    .addParameter("redirect_uri",redirect_uri)
                    .addParameter("code",code)
                    .addParameter("client_secret",client_secret)
                    .build();
            // 执行请求
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                JdToken jdToken = executeResult(response);
                if (jdToken != null){
                    return jdToken;
                }
            }
        }
        return null;
    }

    /**
     * 根据refresh_token码请求京东授权token接口获取token
     * @param refresh_token
     * @return
     * @throws IOException
     */
    public static JdToken getTokenByRefreshToken(String refresh_token) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建http GET请求
            HttpUriRequest httpPost = RequestBuilder.post()
                    .setUri("https://oauth.jd.com/oauth/token")
                    .addParameter("grant_type", "refresh_token")
                    .addParameter("client_id", client_id)
                    .addParameter("refresh_token",refresh_token)
                    .addParameter("client_secret",client_secret)
                    .build();
            // 执行请求
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                JdToken jdToken = executeResult(response);
                if (jdToken != null){
                    return jdToken;
                }
            }
        }
        return null;
    }

    public static JdToken executeResult(CloseableHttpResponse response) throws IOException {
        // 判断返回状态是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            // 获取服务端返回的数据
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            if(content!=null){
                JdToken jdToken = JSONObject.parseObject(content, JdToken.class);
                return jdToken;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            JdToken jdToken = getTokenByRefreshToken("8fcde8b5-bf17-42f3-a679-5f8ccbf7eaab");
            System.out.println(jdToken.getAccess_token());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
