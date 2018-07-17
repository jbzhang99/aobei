package com.aobei.trainapi.server.impl;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aobei.common.bean.IGtPushData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.IGtPushEvent;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.schema.CustomerQuery;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.PartnerQuery;
import com.aobei.trainapi.schema.Query;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiCommonService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.AppVersion;
import com.aobei.trainapi.server.handler.SmsHandler;
import custom.bean.Constant;
import custom.bean.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Component
public class ApiCommonServiceImpl implements ApiCommonService {

    Logger logger = LoggerFactory.getLogger(ApiCommonServiceImpl.class);
    @Autowired
    SmsHandler smsHandler;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    CmsBannerService cmsBannerService;
    @Autowired
    OssImgService ossImgService;
    @Autowired
    TrainCityService trainCityService;
    @Autowired
    CustomProperties properties;
    @Autowired
    EventPublisher publisher;
    @Autowired
    CustomerQuery customerQuery;
    @Autowired
    Query query;
    @Autowired
    PartnerQuery partnerQuery;
    @Autowired
    private AppGrowthService appGrowthService;
    @Autowired
    private AppPackService appPackService;
    @Override
    public ApiResponse<Object> getCode(Long userId, String phone) {
        logger.info("api-method:getCode:params phone:{}", phone);
        ApiResponse<Object> response = new ApiResponse<>();
        Random rd = new Random();
        String code = 100000 + rd.nextInt(899999) + "";
        String key = Constant.getMaxnumSendKey(phone, "code");
        String value = redisTemplate.opsForValue().get(key);
        int count = 0;
        if (StringUtils.isNumeric(value)) {
            count = Integer.parseInt(value);
            if (count > 10) {
                response.setErrors(Errors._41012);
                return response;
            }
        }
        String key2 = Constant.getVerificationCodeKey(userId, phone);
        redisTemplate.opsForValue().set(key2, code, 5l, TimeUnit.MINUTES);
        smsHandler.sendCode(phone, code);
        redisTemplate.opsForValue().increment(key, 1l);
        Long ex = redisTemplate.opsForValue().getOperations().getExpire(key, TimeUnit.SECONDS);
        if (ex == -1) {
            redisTemplate.opsForValue().getOperations().expire(key, 1, TimeUnit.DAYS);
        }
        response.setMutationResult(new MutationResult());
        return response;
    }

    @Override
    public List<CmsBanner> getBanners(String appid, String position) {
        logger.info("api-method:getBanners:params position:{}", position);
        CmsBannerExample example = new CmsBannerExample();
        example.or()
                .andAppEqualTo(appid)
                .andSignEqualTo(1)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andOnline_datetimeLessThanOrEqualTo(new Date())
                .andOffline_datetimeGreaterThanOrEqualTo(new Date());
        example.setOrderByClause(CmsBannerExample.C.serial_number +" ASC");
        List<CmsBanner> list = cmsBannerService.selectByExample(example);


        return list.stream().map(t -> {
            OssImg ossImg = ossImgService
                    .selectByPrimaryKey(Long.parseLong(t.getImg_cover() == null ? "0" : t.getImg_cover()));
            t.setImg_cover(ossImg.getUrl()==null?"":ossImg.getUrl());
            try {
                t.setHref(URLEncoder.encode(t.getHref(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return t;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TrainCity> openCities() {
    	TrainCityExample trainCityExample = new TrainCityExample();
    	trainCityExample.or().andOpenEqualTo(1);
    	List<TrainCity> listCity = trainCityService.selectByExample(trainCityExample);
    	return listCity;
    }

    @Override
    public AssumeRoleResponse.Credentials ossSts() {
        String endpoint = properties.getAliyun().getOss().getEndpoint();
        String accessKeyId = properties.getAliyun().getOss().getAccessKeyId();
        String accessKeySecret = properties.getAliyun().getOss().getAccessKeySecret();
        String roleArn = properties.getAliyun().getOss().getRolArn();
        String roleSessionName = "session-name";
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            // Init ACS Client
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy); // Optional
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return  response.getCredentials();
        } catch (ClientException e) {
            logger.error("Failed：Error code:{} Error message:{} RequestId:{}",e.getErrCode(),e.getErrMsg(),e.getRequestId());
        }
        return null;
    }

    @Override
    public MutationResult bindPush(String uuid, String pushClientId, String client) {
        if (uuid==null){
            logger.warn("PUSH BIND  not support Tourist bind");
            return new MutationResult();
        }

        IGtPushData data = new IGtPushData();
        data.setCid(pushClientId);
        data.setType(IGtPushData.BIND);
        switch (client) {
            case "i_custom":
            case "a_custom":
                data.setClient(IGtPushData.Client.customer);
                Customer customer = customerQuery.customer_info();
                data.setAlia(customer.getCustomer_id() + "");
                break;
            case "i_partner":
            case "a_partner":
                data.setClient(IGtPushData.Client.partner);
                Partner partner = partnerQuery.partner_bindinfo();
                data.setAlia(partner.getPartner_id()+"");
                break;
            case "i_student":
            case "a_student":
                data.setClient(IGtPushData.Client.student);
                Student student  = query.my_student_bindinfo();
                data.setAlia(student.getStudent_id()+"");
                break;
            case "i_teacher":
            case "a_teacher":
                data.setClient(IGtPushData.Client.teacher);
                Teacher teacher  = query.my_teacher_bindinfo();
                data.setAlia(teacher.getTeacher_id()+"");
                break;
        }
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
        return new MutationResult();
    }

    @Override
    public MutationResult unbindPush(String uuid,String pushClientId, String client) {
        if (uuid==null){
            logger.warn("PUSH BIND  not support Tourist bind");
            return new MutationResult();
        }
        IGtPushData data = new IGtPushData();
        data.setCid(pushClientId);
        data.setType(IGtPushData.UNBIND);
        switch (client) {
            case "i_custom":
            case "a_custom":
                data.setClient(IGtPushData.Client.customer);
                Customer customer = customerQuery.customer_info();
                data.setAlia(customer.getCustomer_id() + "");
                break;
            case "i_partner":
            case "a_partner":
                data.setClient(IGtPushData.Client.partner);
                Partner partner = partnerQuery.partner_bindinfo();
                data.setAlia(partner.getPartner_id()+"");
                break;
            case "i_student":
            case "a_student":
                data.setClient(IGtPushData.Client.student);
                Student student  = query.my_student_bindinfo();
                data.setAlia(student.getStudent_id()+"");
                break;
            case "i_teacher":
            case "a_teacher":
                data.setClient(IGtPushData.Client.teacher);
                Teacher teacher  = query.my_teacher_bindinfo();
                data.setAlia(teacher.getTeacher_id()+"");
                break;
        }
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
        return new MutationResult();
    }

    /**
     * app版本控制
     * @param currentVersion
     * @return
     */
    @Override
    public AppVersion appVersionControl(String currentVersion, String client_id,String osVersion) {
        logger.info("api-method:appVersionControl:params currentVersion:{},client_id:{}",currentVersion,client_id);
        if (StringUtils.isEmpty(currentVersion)){
            Errors._42031.throwError();
        }
        AppVersion appVersion = new AppVersion();
        AppGrowthExample appGrowthExample = new AppGrowthExample();
        appGrowthExample.or().andApp_pack_idEqualTo(client_id);
        appGrowthExample.setOrderByClause(AppGrowthExample.C.create_datetime + " desc");
        AppGrowth appGrowth = singleResult(appGrowthService.selectByExample(appGrowthExample));
        //客户端当前版本号
        String[] currentversion = currentVersion.split("\\.");
        //最新版本号
        String[] version = appGrowth.getCurrent_version().split("\\.");
        //最小升级版本号
        String[] smallVersion =  appGrowth.getGrowth_version().split("\\.");
        //如果用户从非法路径进来,全部设置为false
        if(appGrowth == null ){
            appVersion.setCanUpgrade(false);
            appVersion.setForcedUpgrade(false);
            return appVersion;
        }else {
            AppPack appPack = appPackService.selectByPrimaryKey(client_id);
            appVersion.setAppUrl(appPack.getApp_url());
        }
        BeanUtils.copyProperties(appGrowth,appVersion);
        int idx = 0;
        int diff = 0;
        int minLength = Math.min(version.length, currentversion.length);//取最小长度值
        //设置是否可升级
        while (idx < minLength
                && (diff = version[idx].length() - currentversion[idx].length()) == 0   //先比较长度
                && (diff = version[idx].compareTo(currentversion[idx])) == 0) {         //再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : version.length - currentversion.length;
        if(diff > 0){
            appVersion.setCanUpgrade(true);
        }else {
            appVersion.setCanUpgrade(false);
        }
        //设置强制升级
        int idx2 = 0;
        int diff2 = 0;
        int minLength2 = Math.min(currentversion.length, smallVersion.length);//取最小长度值
        while (idx2 < minLength2
                && (diff2 = currentversion[idx2].length() - smallVersion[idx2].length()) == 0   //先比较长度
                && (diff2 = currentversion[idx2].compareTo(smallVersion[idx2])) == 0) {         //再比较字符
            ++idx2;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff2 = (diff2 != 0) ? diff2 : currentversion.length - smallVersion.length;
        if(diff2 >= 0){
            appVersion.setForcedUpgrade(false);
        }else {
            appVersion.setForcedUpgrade(true);
        }
        logger.info("api-method:appVersionControl:process appVersion:{}",appVersion);
        return appVersion;
    }
}
