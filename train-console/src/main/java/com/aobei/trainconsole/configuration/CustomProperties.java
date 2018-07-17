package com.aobei.trainconsole.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

	private Aliyun aliyun = new Aliyun();

	public Aliyun getAliyun() {
		return aliyun;
	}

	public void setAliyun(Aliyun aliyun) {
		this.aliyun = aliyun;
	}

	@ConfigurationProperties(prefix = "custom.aliyun")
	public class Aliyun {

//		private AliyunOSS oss = new AliyunOSS();
		private Ons ons = new Ons();

		public Ons getOns() {
			return ons;
		}

		public void setOns(Ons ons) {
			this.ons = ons;
		}

//		public AliyunOSS getOss() {
//			return oss;
//		}
//
//		public void setOss(AliyunOSS oss) {
//			this.oss = oss;
//		}

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
//	@ConfigurationProperties(prefix = "custom.aliyun.oss")
//	public class AliyunOSS {
//		private String privateBucket;
//		private String publicBucket;
//		private String avatarBucket;
//
//		private List<String> buckets = new ArrayList<>();
//
//		public String getPrivateBucket() {
//			return privateBucket;
//		}
//		public void setPrivateBucket(String privateBucket) {
//			this.privateBucket = privateBucket;
//		}
//		public String getPublicBucket() {
//			return publicBucket;
//		}
//		public void setPublicBucket(String publicBucket) {
//			this.publicBucket = publicBucket;
//		}
//
//		public String getAvatarBucket() {
//			return avatarBucket;
//		}
//
//		public void setAvatarBucket(String avatarBucket) {
//			this.avatarBucket = avatarBucket;
//		}
//	}

}
