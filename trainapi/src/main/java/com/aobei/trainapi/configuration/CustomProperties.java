package com.aobei.trainapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    private String oauth2key;


    public String getOauth2key() {
        return oauth2key;
    }

    public void setOauth2key(String oauth2key) {
        this.oauth2key = oauth2key;
    }

    private Aliyun aliyun = new Aliyun();

    public Aliyun getAliyun() {
        return aliyun;
    }

    public void setAliyun(Aliyun aliyun) {
        this.aliyun = aliyun;
    }

    private Wx wx = new Wx();

    public Wx getWx() {
        return wx;
    }

    public void setWx(Wx wx) {
        this.wx = wx;
    }

    private Alipay alipay = new Alipay();

    public Alipay getAlipay() {
        return alipay;
    }

    public void setAlipay(Alipay alipay) {
        this.alipay = alipay;
    }

    @ConfigurationProperties(prefix = "custom.aliyun")
    public class Aliyun {
        private Ons ons = new Ons();
        private Oss oss = new Oss();

        public Ons getOns() {
            return ons;
        }

        public void setOns(Ons ons) {
            this.ons = ons;
        }

        public Oss getOss() {
            return oss;
        }

        public void setOss(Oss oss) {
            this.oss = oss;
        }
    }
    @ConfigurationProperties(prefix = "custom.aliyun.ons")
    public class Ons {
        private String topic;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
    @ConfigurationProperties(prefix = "custom.aliyun.oss")
    public class Oss {
        private String publicBucket;
        private String privateBucket;
        private String accessKeyId;
        private String accessKeySecret;
        private String rolArn;
        private String endpoint;
        public String getPublicBucket() {
            return publicBucket;
        }

        public void setPublicBucket(String publicBucket) {
            this.publicBucket = publicBucket;
        }

        public String getPrivateBucket() {
            return privateBucket;
        }

        public void setPrivateBucket(String privateBucket) {
            this.privateBucket = privateBucket;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getRolArn() {
            return rolArn;
        }

        public void setRolArn(String rolArn) {
            this.rolArn = rolArn;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    @ConfigurationProperties(prefix = "custom.wx")
    public class Wx {

        private String payNotifyUrl;
        private String refundNotifyUrl;

        public String getPayNotifyUrl() {
            return payNotifyUrl;
        }

        public void setPayNotifyUrl(String payNotifyUrl) {
            this.payNotifyUrl = payNotifyUrl;
        }

        public String getRefundNotifyUrl() {
            return refundNotifyUrl;
        }

        public void setRefundNotifyUrl(String refundNotifyUrl) {
            this.refundNotifyUrl = refundNotifyUrl;
        }
    }
    @ConfigurationProperties(prefix = "custom.alipay")
    public class Alipay{
        private String payNotifyUrl;
        private String refundNotifyUrl;

        public String getPayNotifyUrl() {
            return payNotifyUrl;
        }

        public void setPayNotifyUrl(String payNotifyUrl) {
            this.payNotifyUrl = payNotifyUrl;
        }

        public String getRefundNotifyUrl() {
            return refundNotifyUrl;
        }

        public void setRefundNotifyUrl(String refundNotifyUrl) {
            this.refundNotifyUrl = refundNotifyUrl;
        }

    }


}
