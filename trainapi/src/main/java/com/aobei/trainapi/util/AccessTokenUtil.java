package com.aobei.trainapi.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccessTokenUtil {

    public static AccessToken getAccessToken(String url, Auth auth) {
        RequestBuilder builder = RequestBuilder.post().setUri(url)
                .addParameter("username", auth.getUserName())
                .addParameter("password", auth.getPassword())
                .addParameter("grant_type", auth.getGrantType());
        for (Header header : auth.getHeaders()) {
            builder.addHeader(header);
        }
        HttpUriRequest httpUriRequest = builder.build();
        CloseableHttpResponse response  =  LocalHttpClient.execute(httpUriRequest);
        HttpEntity entity = response.getEntity();
        String str = null;
        try {
            str = EntityUtils.toString(entity, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

       AccessToken token =  JSON.parseObject(str,AccessToken.class);
        return JsonUtil.parseObject(str, AccessToken.class);
    }

    public static Auth buildAuth(String username, String password, String grantType, List<Header> headers) {

        Auth auth = new Auth();
        auth.setUserName(username);
        auth.setPassword(password);
        auth.setGrantType(grantType);
        auth.setHeaders(headers);
        return auth;
    }

    public static class Auth {

        private String userName;
        private String password;
        private String grantType;
        List<Header> headers;

        public void addHeader(Header header) {
            if (this.headers == null) {
                this.headers = new ArrayList<>();
            }
            this.headers.add(header);

        }

        public List<Header> getHeaders() {
            return headers;
        }

        public void setHeaders(List<Header> headers) {
            this.headers = headers;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            if (grantType == null) {
                grantType = "password";
            }
            this.grantType = grantType;
        }
    }


    public static class AccessToken {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private String role;
        private String uuid;
        private String jti;
        private String error;
        private String error_description;
        private String client_id;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getJti() {
            return jti;
        }

        public void setJti(String jti) {
            this.jti = jti;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError_description() {
            return error_description;
        }

        public void setError_description(String error_description) {
            this.error_description = error_description;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }
    }


    public static void main(String[] args) {

        Header header = new BasicHeader("Authorization", "Basic d3hfbV9jdXN0b206NHg5MWI3NGUtM2I3YS1iYjZ4LWJ0djktcXpjaW83ams2Zzdm");
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        Auth auth = AccessTokenUtil.buildAuth("second", "123456", "password", headers);

        AccessToken token = AccessTokenUtil.getAccessToken("https://dev-auth.aobei.com/oauth/token", auth);
        System.out.println(token.getClient_id());


    }
}
