package custom.bean;

import java.io.Serializable;
import java.util.Map;

public class SmsMessage implements Serializable {


    private static final long serialVersionUID = 6386962515774533821L;

    public SmsMessage() {
    }

//    public SmsMessage(String templateCode, String phoneNumber,  Map<String, Object> paramsMap, String signName, Long user_id) {
//        this.templateCode = templateCode;
//        this.phoneNumber = phoneNumber;
//        this.paramsMap = paramsMap;
//        this.signName = signName;
//        this.user_id = user_id;
//    }

    String templateCode;
    String phoneNumber;
    //String params;
    String signName;
    Long messageId;
   // Map<String, Object> paramsMap;
    SmsParams params;

//    public Map<String, Object> getParamsMap() {
//        return paramsMap;
//    }
//
//    public void setParamsMap(Map<String, Object> paramsMap) {
//        this.paramsMap = paramsMap;
//    }


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    public String getParams() {
//        return params;
//    }
//
//    public void setParams(String params) {
//        this.params = params;
//    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public SmsParams getParams() {
        return params;
    }

    public void setParams(SmsParams params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "templateCode='" + templateCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", params='" + params + '\'' +
                ", signName='" + signName + '\'' +
                '}';
    }
}
