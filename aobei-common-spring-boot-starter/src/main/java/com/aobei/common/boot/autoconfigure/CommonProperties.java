package com.aobei.common.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aobei.common.boot")
public class CommonProperties {

    private boolean eventSms;

    private Aliyun aliyun = new Aliyun();
    private Igtpush igtpush = new Igtpush();

    public boolean isEventSms() {
        return eventSms;
    }

    public void setEventSms(boolean eventSms) {
        this.eventSms = eventSms;
    }

    public Aliyun getAliyun() {
        return aliyun;
    }

    public void setAliyun(Aliyun aliyun) {
        this.aliyun = aliyun;
    }

    public Igtpush getIgtpush() {
        return igtpush;
    }

    public void setIgtpush(Igtpush igtpush) {
        this.igtpush = igtpush;
    }

    public class AliyunBase {

        private String accessKeyId;

        private String accessKeySecret;

        private Aliyun aliyun;

        public AliyunBase() {
            super();
        }

        public AliyunBase(Aliyun aliyun) {
            super();
            this.aliyun = aliyun;
        }

        public String getAccessKeyId() {
            if (accessKeyId == null && aliyun != null) {
                return aliyun.getAccessKeyId();
            }
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            if (accessKeySecret == null && aliyun != null) {
                return aliyun.getAccessKeySecret();
            }
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
    }

    @ConfigurationProperties(prefix = "aobei.common.boot.aliyun")
    public class Aliyun extends AliyunBase {

        private AliyunOSS oss = new AliyunOSS(this);

        private AliyunSms sms = new AliyunSms(this);

        private AliyunOns ons = new AliyunOns(this);

        public AliyunOSS getOss() {
            return oss;
        }

        public void setOss(AliyunOSS oss) {
            this.oss = oss;
        }

        public AliyunSms getSms() {
            return sms;
        }

        public void setSms(AliyunSms sms) {
            this.sms = sms;
        }

        public AliyunOns getOns() {
            return ons;
        }

        public void setOns(AliyunOns ons) {
            this.ons = ons;
        }

    }

    @ConfigurationProperties(prefix = "aobei.common.boot.aliyun.oss")
    public class AliyunOSS extends AliyunBase {
        private String endpoint;

        public AliyunOSS() {
            super();
        }

        public AliyunOSS(Aliyun aliyun) {
            super(aliyun);
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

    }

    @ConfigurationProperties(prefix = "aobei.common.boot.aliyun.sms")
    public class AliyunSms extends AliyunBase {
        private String endpoint;
        private String regionId;
        private String domain;
        private String product;

        public AliyunSms() {
            super();
        }

        public AliyunSms(Aliyun aliyun) {
            super(aliyun);
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

    }

    @ConfigurationProperties(prefix = "aobei.common.boot.aliyun.ons")
    public class AliyunOns extends AliyunBase {

        private String producerId;
        private String consumerId;

        public AliyunOns() {
            super();
        }

        public AliyunOns(Aliyun aliyun) {
            super(aliyun);
        }

        public String getProducerId() {
            return producerId;
        }

        public void setProducerId(String producerId) {
            this.producerId = producerId;
        }

        public String getConsumerId() {
            return consumerId;
        }

        public void setConsumerId(String consumerId) {
            this.consumerId = consumerId;
        }
    }


    public class IgtpushBase {
        private String appId;
        private String appKey;
        private String masterSecret;


        public IgtpushBase() {
            super();
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getMasterSecret() {
            return masterSecret;
        }

        public void setMasterSecret(String masterSecret) {
            this.masterSecret = masterSecret;
        }


    }
    @ConfigurationProperties(prefix = "aobei.common.boot.igtpush")
    public class Igtpush {
        public Igtpush() {
            super();
        }

        PushCustomer customer = new PushCustomer();
        PushPartner partner = new PushPartner();
        PushStudent student = new PushStudent();
        PushTeacher teacher = new PushTeacher();
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public PushCustomer getCustomer() {
            return customer;
        }

        public void setCustomer(PushCustomer customer) {
            this.customer = customer;
        }

        public PushPartner getPartner() {
            return partner;
        }

        public void setPartner(PushPartner partner) {
            this.partner = partner;
        }

        public PushStudent getStudent() {
            return student;
        }

        public void setStudent(PushStudent student) {
            this.student = student;
        }

        public PushTeacher getTeacher() {
            return teacher;
        }

        public void setTeacher(PushTeacher teacher) {
            this.teacher = teacher;
        }
    }

    @ConfigurationProperties(prefix = "aobei.common.boot.igtpush.customer")
    public class PushCustomer extends IgtpushBase {
        public PushCustomer() {
            super();
        }
    }

    @ConfigurationProperties(prefix = "aobei.common.boot.igtpush.partner")
    public class PushPartner extends IgtpushBase {
        public PushPartner() {
            super();
        }
    }

    @ConfigurationProperties(prefix = "aobei.common.boot.igtpush.student")
    public class PushStudent extends IgtpushBase {
        public PushStudent() {
            super();
        }
    }

    @ConfigurationProperties(prefix = "aobei.common.boot.igtpush.teacher")
    public class PushTeacher extends IgtpushBase {
        public PushTeacher() {
            super();
        }
    }
}
