package com.aobei.pushserver.send;

import com.alibaba.fastjson.JSON;
import com.aobei.common.bean.IGtPushData;
import com.aobei.common.bean.GTPush;
import com.aobei.common.boot.IGtPushProvider;
import com.aobei.common.boot.autoconfigure.CommonProperties;
import com.aobei.common.puhs.IGtPushSender;
import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.*;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class Pusher {
    Logger logger = LoggerFactory.getLogger(Pusher.class);
    @Autowired
    IGtPushProvider provider;

    @Autowired
    CommonProperties properties;


    public boolean bind(IGtPushData data) {
        logger.info("[PUSH BIND DATA] DATA:{}", JSON.toJSONString(data));
        String alias = data.getAlia();
        String cid = data.getCid();
        IGtPushSender sender = getPushSender(data);
        if (sender == null) {
            logger.warn("[PUSH BIND FAIL] client push not open,please config push properties of client:{} DATA:{}", data.getClient(), JSON.toJSONString(data));
            return false;
        }
        IAliasResult result = sender.bindAlias(sender.getAppId(), alias, cid);
        if (result.getResult()) {
            logger.info("[PUSH BIND SUCCESS] RESULT:{}", JSON.toJSONString(result));
        } else {
            logger.info("[PUSH BIND FAIL] RESULT:{}", JSON.toJSONString(result));
        }
        return result.getResult();
    }


    public boolean unbind(IGtPushData data){
        String alias = data.getAlia();
        String cid = data.getCid();
        IGtPushSender sender = getPushSender(data);
        if (sender == null) {
            logger.warn("[PUSH BIND FAIL] client push not open,please config push properties of client:{} DATA:{}", data.getClient(), JSON.toJSONString(data));
            return false;
        }
        IAliasResult result = sender.unbindAlias(sender.getAppId(), alias, cid);
        if (result.getResult()) {
            logger.info("[PUSH BIND SUCCESS] RESULT:{}", JSON.toJSONString(result));
        } else {
            logger.info("[PUSH BIND FAIL] RESULT:{}", JSON.toJSONString(result));
        }

        return result.getResult();
    }
    /**
     * AppId 对应我们在个推中获得的APPID。APPID会有多个。所以在properties中会改变一些定义；
     * 此处在研究完三种推送方法后，再进行设置。
     *
     * @param data
     * @return
     * @TODO 此处还会比较复杂。
     */
    public boolean send(IGtPushData data) {
        logger.info("[PUSH SEND DATA] DATA:{}", JSON.toJSONString(data));
        String messageId = data.getMessageId();
        IGtPushSender sender = getPushSender(data);
        if (sender == null) {
            logger.warn("[PUSH SEND FAIL] client push not open,please config push properties of client:{} DATA:{}", data.getClient(), JSON.toJSONString(data));
            return false;
        }
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAPNInfo(getAPNPayload(data));
        template.setAppId(sender.getAppId());
        template.setAppkey(sender.getAppKey());
        template.setTransmissionContent(data.getTransmissionContent());
        template.setTransmissionType(data.getSetTransmissionType());
        if (!StringUtils.isEmpty(data.getDurationStart()) && !StringUtils.isEmpty(data.getDurationend())) {
            try {
                template.setDuration(data.getDurationStart(), data.getDurationend());
            } catch (Exception e) {
               logger.warn("[PUSH SEND DATA]  template.setDuration error  DATA:{}",data);
            }
        }
        IPushResult result = null;
        try {

            if (data.getType() == IGtPushData.SINGLE) {
                Target target = new Target();
                SingleMessage message = getMessage(data, template, SingleMessage.class);
                target.setAlias(data.getAlias().get(0));
                target.setAppId(sender.getAppId());
                result = sender.pushMessageToSingle(message, target, messageId);
            } else if (data.getType() == IGtPushData.GROUP) {
                List<Target> targets = new ArrayList<>();
                for (String alia : data.getAlias()) {
                    Target target = new Target();
                    target.setAlias(alia);
                    target.setAppId(sender.getAppId());
                    targets.add(target);
                }
                ListMessage message = getMessage(data, template, ListMessage.class);
                result = sender.pushMessageToList(message, targets);
            } else if (data.getType() == IGtPushData.APP) {
                //未验证方法
                AppMessage message = getMessage(data, template, AppMessage.class);
                AppConditions cdt = new AppConditions();
                List<String> appIdList = new ArrayList<String>();
                appIdList.add(sender.getAppId());
                message.setAppIdList(appIdList);
                //手机类型
                List<String> phoneTypeList = data.getPhoneTypeList();
                //省份
                List<String> provinceList = data.getProvinceList();
                //自定义tag
                List<String> tagList = data.getTagList();
                cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
                cdt.addCondition(AppConditions.REGION, provinceList);
                cdt.addCondition(AppConditions.TAG, tagList);
                message.setConditions(cdt);
                try {
                    message.setPushTime(data.getPushTime());
                } catch (ParseException e) {
                    logger.info("[PUSH SEND INFO] ");
                }

                result = sender.pushMessageToApp(message, messageId);

            }

        } catch (Exception e) {
            logger.error("[PUSH SEND ERROR] DATA:{}", JSON.toJSONString(data), e);
        }

        if (result != null && result.getResponse().get("result").equals("ok")) {
            logger.info("[PUSH SEND SUCCESS] RESULT:{}", JSON.toJSONString(result));
        } else {
            logger.info("[PUSH SEND FAIL] RESULT:{}", JSON.toJSONString(result));
        }
        return false;
    }

    private <T> T getMessage(IGtPushData data, TransmissionTemplate template, Class<T> clazz)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        T instance = clazz.newInstance();
//        String style = data.getStyle();
//        switch (style) {
//            case IGtPushData.Style0:
//                Style0 style0 = (Style0) Class.forName(style).newInstance();
//                style0.setTitle(data.getTitle());
//                style0.setText(data.getText());
//                // 配置通知栏图标
//                style0.setLogo("icon.png");
//                // 配置通知栏网络图标
//                style0.setLogoUrl("");
//                style0.setRing(true);
//                style0.setVibrate(true);
//                style0.setClearable(true);
//                template.setStyle(style0);
//                break;
//            case IGtPushData.Style1:
//                Style1 style1 = (Style1) Class.forName(style).newInstance();
//                style1.setTitle(data.getTitle());
//                style1.setText(data.getText());
//                // 配置通知栏图标
//                style1.setLogo("icon.png");
//                // 配置通知栏网络图标
//                style1.setLogoUrl("");
//                style1.setRing(true);
//                style1.setVibrate(true);
//                style1.setClearable(true);
//                template.setStyle(style1);
//                break;
//            case IGtPushData.Style4:
//                Style4 style4 = (Style4) Class.forName(style).newInstance();
//                // 配置通知栏图标
//                style4.setLogo("icon.png");
//                // 配置通知栏网络图标
//                style4.setRing(true);
//                style4.setVibrate(true);
//                style4.setClearable(true);
//                style4.setBanner_url("");
//                template.setStyle(style4);
//                break;
//            case IGtPushData.Style6:
//                Style6 style6 = (Style6) Class.forName(style).newInstance();
//                style6.setTitle(data.getTitle());
//                style6.setText(data.getText());
//                // 配置通知栏图标
//                style6.setLogo("icon.png");
//                // 配置通知栏网络图标
//                style6.setLogoUrl("");
//                style6.setRing(true);
//                style6.setVibrate(true);
//                style6.setClearable(true);
//                style6.setBigStyle1("bigImageUrl");
//                style6.setBigStyle2("bigText");
//                style6.setBigStyle3("bigImageUrl", "bannerUrl");
//                template.setStyle(style6);
//                break;
//        }

        clazz.getMethod("setData", ITemplate.class).invoke(instance, template);
        clazz.getMethod("setOffline", boolean.class).invoke(instance, true);
        clazz.getMethod("setOfflineExpireTime", long.class).invoke(instance, 24 * 3600 * 1000);
        clazz.getMethod("setPushNetWorkType", int.class).invoke(instance, 0);
        return instance;

    }

    private  APNPayload getAPNPayload(IGtPushData data){
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(0);
        if(data.getSound()!=null) {
            payload.setSound(data.getSound());
        }
        payload.setAlertMsg(getDictionaryAlertMsg(data.getTitle(),data.getText()));
        try {
            Map<String,String>  custom  = JSON.parseObject(data.getTransmissionContent(),Map.class);
            Set<String> keySet  =  custom.keySet();
            for(String key :keySet){
                payload.addCustomMsg(key,custom.get(key));
            }
        }catch (Exception e){
            logger.warn("PUSH SEND WARN  iOS custom params is not set!");
        }

        return payload;
    }
    private  APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title,String body){
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(body);
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // iOS8.2以上版本支持
        alertMsg.setTitle(title);
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }
    private IGtPushSender getPushSender(IGtPushData data) {
        GTPush push = null;
        IGtPushData.Client client  = data.getClient();
        if (client==null){
            return null;
        }
        switch (client) {
            case customer:
                push = provider.getCustomerPush();
                break;
            case partner:
                push = provider.getPartnerPush();
                break;
            case student:
                push = provider.getStudentPush();
                break;
            case teacher:
                push = provider.getTeacherPush();
                break;
        }
         return push == null ? null : new IGtPushSender(push);

    }
}
