package com.aobei.common.puhs;

import com.alibaba.fastjson.JSON;
import com.aobei.common.bean.GTPush;
import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gexin.rp.sdk.base.impl.Target;

import java.util.List;

public class IGtPushSender {

    Logger logger  = LoggerFactory.getLogger(IGtPushSender.class);
    private GTPush push;
    private String appId;
    private String appKey;
    public IGtPushSender(GTPush push) {
        this.push = push;
        this.appId = push.getAppId();
        this.appKey = push.getAppkey();

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

    /**
     * 一个clientid只能绑定一个别名，若已绑定过别名的clientid再次绑定新别名，则认为与前一个别名自动解绑，绑定新别名。
     * @param alias
     * @param cid
     * @return
     */
    public  IAliasResult bindAlias(String appId,String alias, String cid){
         IAliasResult result  =  push.bindAlias(appId,alias,cid);
         logger.info("[IGtPush] bindAlias single result:{}", JSON.toJSONString(result));
         return result;
    }

    /**
     * 允许将多个clientid和一个别名绑定，如用户使用多终端，
     * 则可将多终端对应的clientid绑定为一个别名，目前一个别名最多支持绑定10个clientid。
     * @param targets
     * @return
     */
    public IAliasResult bindAlias(List<Target> targets){
        IAliasResult result =  push.bindAlias("",targets);
        logger.info("[IGtPush] bindAlias multi result:{}", JSON.toJSONString(result));
        return result;
    }

    public IAliasResult unbindAlias(String appId,String alias,String cid){

        IAliasResult result  = push.unBindAlias(appId,alias,cid);
        logger.info("[IGtPush] bindAlias multi result:{}", JSON.toJSONString(result));
        return result;
    }
    public IPushResult pushMessageToSingle(SingleMessage message,Target target,String messageId){
        IPushResult result  =  null;
        if(messageId==null) {
            result = push.pushMessageToSingle(message, target);
        }else {
            result = push.pushMessageToSingle(message,target,messageId);
        }

        logger.info("[IGtPush] pushMessageToSingle  result:{}", JSON.toJSONString(result));
        return  result;

    }

   public  IPushResult pushMessageToList(ListMessage message,List<Target> targets){
        String taskId = push.getContentId(message);
        IPushResult result  = push.pushMessageToList(taskId,targets);
        logger.info("[IGtPush] pushMessageToList  result:{}", JSON.toJSONString(result));
        return  result;
   }

   public IPushResult pushMessageToApp(AppMessage message,String taskGroupName) {
       IPushResult result = push.pushMessageToApp(message, taskGroupName);
       logger.info("[IGtPush] pushMessageToApp  result:{}", JSON.toJSONString(result));
       return  result;
   }



}
