package custom.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WxPaymentBody {

    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String _package;
    private String signType;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        map.put("appId",appId);
        map.put("nonceStr",nonceStr);
        map.put("package",_package);
        map.put("signType",signType);
        map.put("timeStamp",new Date().getTime()/1000+"");
        return map;
    }
}
