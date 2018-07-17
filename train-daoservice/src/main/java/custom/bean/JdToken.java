package custom.bean;

import java.io.Serializable;

/**
 * Created by mr_bl on 2018/6/26.
 */
public class JdToken implements Serializable{

    /**
     * token
     */
    private String access_token;
    /**
     * code吗
     */
    private int code;
    /**
     * 失效时间（从当前时间算起，单位：秒）
     */
    private int expires_in;
    /**
     * 刷新token
     */
    private String refresh_token;
    /**
     * 范围
     */
    private String scope;
    /**
     * 授权的时间点（UNIX时间戳，单位：毫秒）
     */
    private String time;
    /**
     * token类型 （暂无意义）
     */
    private String token_type;
    /**
     * 授权用户对应的京东ID
     */
    private String uid;
    /**
     * 授权用户对应的京东昵称
     */
    private String user_nick;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }
}
