package com.aobei.common.boot.autoconfigure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.aobei.common.bean.GTPush;
import com.aobei.common.boot.*;
import com.aobei.common.boot.event.listener.IGtPushSendEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aobei.common.boot.event.listener.SmsSendEventListener;
import com.aobei.common.sms.SmsAliSender;

@Configuration
@EnableConfigurationProperties(CommonProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CommonAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CommonAutoConfiguration.class);

    @SuppressWarnings("unused")
    private CommonProperties properties;

    @Bean
    public EventPublisher eventPublisher(ApplicationContext applicationContext) {
        return new EventPublisher(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(type = "org.springframework.data.redis.core.RedisTemplate<String, Object>")
    public RedisTemplate<String, Object> redisTemplateStringObject(
            RedisConnectionFactory redisConnectionFactory) {
        logger.info("INITD RedisTemplate<String, Object>");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }

    /**************************************************/
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<String, Object> template) {
        logger.info("INITD RedisCacheManager");
        RedisCacheManager redisCacheManager = new RedisCacheManager(template);
        redisCacheManager.setDefaultExpiration(5 * 60);
//        Map<String,Long> expiresMap=new HashMap<>();
//        expiresMap.put("Product",5L);
        redisCacheManager.setCachePrefix(new DefaultRedisCachePrefix());
        redisCacheManager.setUsePrefix(true);
        return redisCacheManager;
    }

    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };

    }

    /**
     * 生成 RedisIdGenerator
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public RedisIdGenerator redisIdGenerator(
            StringRedisTemplate redisTemplate) {
        logger.info("INITD RedisIdGenerator");
        RedisIdGenerator redisIdGenerator = new RedisIdGenerator();
        redisIdGenerator.setRedisTemplate(redisTemplate);
        return redisIdGenerator;
    }

    /**
     * 实例化短信发送事件监听，开发人员无须调用
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "aobei.common.boot.event-sms", havingValue = "true")
    public SmsSendEventListener smsSendEventListener(RedisTemplate<String, Object> redisTemplate) {
        logger.info("INITD SmsSendEventListener");
        return new SmsSendEventListener(redisTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "aobei.common.boot.event-push", havingValue = "true")
    public IGtPushSendEventListener iGtPushSendEventListener(RedisTemplate<String, Object> redisTemplate) {
        logger.info("INITD iGtPushSendEventListener");
        return new IGtPushSendEventListener(redisTemplate);
    }

    @Configuration
    protected static class AliyunConfiguration {

        private static final Logger logger = LoggerFactory.getLogger(AliyunConfiguration.class);

        @Autowired
        private CommonProperties properties;

        @Autowired
        private Environment env;

        private String envName;

        @PostConstruct
        public void envNameGet() {
            if (env.getActiveProfiles().length > 0) {
                envName = env.getActiveProfiles()[0];
            }
        }

        /**
         * 短信发送client
         *
         * @return
         * @throws ClientException
         */
        @Bean
        @ConditionalOnMissingBean(IAcsClient.class)
        @ConditionalOnProperty(prefix = "aobei.common.boot.aliyun.sms", name = "product")
        public IAcsClient iAcsClient() throws ClientException {
            logger.info("INITD IAcsClient");
            // 可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            // 初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile(
                    properties.getAliyun().getSms().getRegionId(),
                    properties.getAliyun().getSms().getAccessKeyId(),
                    properties.getAliyun().getSms().getAccessKeySecret());
            DefaultProfile.addEndpoint(
                    properties.getAliyun().getSms().getEndpoint(),
                    properties.getAliyun().getSms().getRegionId(),
                    properties.getAliyun().getSms().getProduct(),
                    properties.getAliyun().getSms().getDomain());
            IAcsClient acsClient = new DefaultAcsClient(profile);
            return acsClient;
        }

        @Bean
        @ConditionalOnMissingBean(SmsAliSender.class)
        @ConditionalOnBean(IAcsClient.class)
        public SmsAliSender smsAliSender(IAcsClient acsClient) {
            logger.info("INITD SmsAliSender");
            return new SmsAliSender(acsClient);
        }

        /**
         * 实例化 OSSClient <br>
         * <p>
         * 开发人员禁止调用 shutdown 方法
         *
         * @return
         */
        @Bean(destroyMethod = "shutdown")
        @ConditionalOnMissingBean(OSSClient.class)
        @ConditionalOnProperty(prefix = "aobei.common.boot.aliyun.oss", name = "endpoint")
        public OSSClient ossClient() {
            logger.info("INITD OSSClient");
            String endpoint = properties.getAliyun().getOss().getEndpoint();
            String accessKeyId = properties.getAliyun().getOss().getAccessKeyId();
            String accessKeySecret = properties.getAliyun().getOss().getAccessKeySecret();
            OSSClient ossClient = new OSSClient(endpoint, new DefaultCredentialProvider(accessKeyId, accessKeySecret), null);
            return ossClient;
        }

        /**
         * MQ Producer
         *
         * @return
         */
        @Bean(initMethod = "start", destroyMethod = "shutdown")
        @ConditionalOnMissingBean(Producer.class)
        @ConditionalOnProperty(prefix = "aobei.common.boot.aliyun.ons", name = "producerId")
        public OnsProducer onsProducer() {
            logger.info("INITD ProducerBean");
            Properties props = new Properties();
            // 您在 MQ 控制台创建的 Producer ID
            props.put(PropertyKeyConst.ProducerId, properties.getAliyun().getOns().getProducerId());
            // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
            props.put(PropertyKeyConst.AccessKey, properties.getAliyun().getOns().getAccessKeyId());
            // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
            props.put(PropertyKeyConst.SecretKey, properties.getAliyun().getOns().getAccessKeySecret());
            // 设置 TCP 接入域名（此处以公共云的公网接入为例）
            props.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
            OnsProducer producerBean = new OnsProducer();
            producerBean.setProperties(props);
            producerBean.setEnvName(envName);
            return producerBean;
        }


        /**
         * MQ Consumer
         *
         * @param listeners
         * @return
         */
        @Bean
        @ConditionalOnBean(OnsMessageListener.class)
        @ConditionalOnProperty(prefix = "aobei.common.boot.aliyun.ons", name = "consumerId")
        public List<ConsumerBean> onsConsumers(List<OnsMessageListener> listeners) {
            return listeners.stream()
                    .map(t -> {
                        Properties props = new Properties();
                        //消费者 集群 or 广播 模式
                        props.put(PropertyKeyConst.MessageModel, t.isClustering() ? PropertyValueConst.CLUSTERING : PropertyValueConst.BROADCASTING);
                        // 您在 MQ 控制台创建的 Consumer ID
                        props.put(PropertyKeyConst.ConsumerId, t.envCid(envName));
                        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
                        props.put(PropertyKeyConst.AccessKey, properties.getAliyun().getOns().getAccessKeyId());
                        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
                        props.put(PropertyKeyConst.SecretKey, properties.getAliyun().getOns().getAccessKeySecret());
                        // 设置 TCP 接入域名（此处以公共云公网环境接入为例）
                        props.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
                        ConsumerBean consumerBean = new ConsumerBean();
                        consumerBean.setProperties(props);
                        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
                        Subscription subscription = new Subscription();
                        subscription.setTopic(t.getTopic());
                        subscription.setExpression(t.envTag(envName));
                        subscriptionTable.put(subscription, t);
                        consumerBean.setSubscriptionTable(subscriptionTable);
                        logger.info("INITD ConsumerBean {} [Listener topic:{} cid:{} expression:{}]", t.isClustering() ? "CLUSTERING" : "BROADCASTING", t.getTopic(), t.envCid(envName), t.envTag(envName));
                        return consumerBean;
                    })
                    .peek(n -> n.start())
                    .collect(Collectors.toList());
        }

    }

    @Configuration
    protected static class IGtPushConfiguration {
        private static final Logger logger = LoggerFactory.getLogger(IGtPushConfiguration.class);
        @Autowired
        private CommonProperties properties;

        @Bean
        @ConditionalOnMissingBean(IGtPushProvider.class)
        @ConditionalOnProperty(prefix = "aobei.common.boot.igtpush", name = "host")
        public IGtPushProvider pushProvider() {


            GTPush customerPush = null;
            if (properties.getIgtpush().getCustomer().getAppId() != null) {
                customerPush = new GTPush(
                        properties.getIgtpush().getHost(),
                        properties.getIgtpush().getCustomer().getAppKey(),
                        properties.getIgtpush().getCustomer().getMasterSecret(),
                        properties.getIgtpush().getCustomer().getAppId());
                logger.info("INITD CustomerPush");
            }
            GTPush partnerPush = null;
            if (properties.getIgtpush().getPartner().getAppId() != null) {
                partnerPush = new GTPush(
                        properties.getIgtpush().getHost(),
                        properties.getIgtpush().getPartner().getAppKey(),
                        properties.getIgtpush().getPartner().getMasterSecret(),
                        properties.getIgtpush().getPartner().getAppId());
                logger.info("INITD PartnerPush");
            }
            GTPush studentPush = null;
            if (properties.getIgtpush().getStudent().getAppId() != null) {
                studentPush = new GTPush(
                        properties.getIgtpush().getHost(),
                        properties.getIgtpush().getStudent().getAppKey(),
                        properties.getIgtpush().getStudent().getMasterSecret(),
                        properties.getIgtpush().getStudent().getAppId());
                logger.info("INITD StudentPush");
            }
            GTPush teacherPush = null;
            if (properties.getIgtpush().getTeacher().getAppId() != null) {
                teacherPush = new GTPush(
                        properties.getIgtpush().getHost(),
                        properties.getIgtpush().getTeacher().getAppKey(),
                        properties.getIgtpush().getTeacher().getMasterSecret(),
                        properties.getIgtpush().getTeacher().getAppId());
                logger.info("INITD TeacherPush");
            }
            return new IGtPushProvider(customerPush, partnerPush, studentPush, teacherPush);


        }

    }

}
