package custom.bean;

import com.gexin.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renpiming on 2018/6/20.
 */
public class TransmissionContent {

    public static final String WEBVIEW = "webview";
    public static final String PRODUCT_DETAIL = "productdetail";
    public static final String ORDER_DETAIL = "orderdetail";
    public static final String HOME_PAGE="homepage";


    public static final String CUSTOM = "abcustom";
    public static final String STUDENT = "abstudent";
    public static final String PARTNER = "abpartner";
    public static final String TEACHER = "abteacher";


    public String href;

    public String sound;

    public String title;

    public String text;

    public TransmissionContent() {
    }

    public TransmissionContent(String scheme, String host, Map<String, String> params) {


        StringBuilder builder = new StringBuilder();
        builder.append(scheme);
        builder.append("://");
        builder.append(host);
        builder.append("?");
        if (params != null) {
            builder.append(programParams(params));
        }
        setHref(builder.toString());
    }

    public String getHref() {
        if(href!=null){
            try {
                return URLEncoder.encode(href,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return href;
    }

    public String  getHrefNotEncode(){
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String programParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry entry : params.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue());
            builder.append("&");
        }
        String s = builder.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }


    public static void main(String[] args) {
        Map<String,String> params  = new HashMap<>();
        params.put("pay_order_id","112333211");
        params.put("abcedf","sdkgk");
        TransmissionContent content  = new TransmissionContent(TransmissionContent.CUSTOM,TransmissionContent.ORDER_DETAIL,params);
        content.setSound("music");
        content.setTitle("title");
        content.setText("text");
        System.out.println(JSON.toJSONString(content));
    }
}
