package com.aobei.trainapi.server;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aobei.train.model.CmsBanner;
import com.aobei.train.model.OssImg;
import com.aobei.train.model.TrainCity;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.AppVersion;

import java.util.List;

public interface ApiCommonService {

    /**
     * 获取验证码：
     * @param phone 电话号码
     * @return ServiceResponse
     */
    ApiResponse<Object> getCode(Long userId, String phone);

    /**
     * 获取banner图
     * @param position  图片展示的位置
     * @return
     */
    List<CmsBanner> getBanners(String appid, String position);

    /**
     * 获取已经开放了服务的城市里诶表
     */
     List<TrainCity> openCities();

    /**
     * 阿里云    sts 授权访问
     * @return
     */
    AssumeRoleResponse.Credentials ossSts();

    /**
     * 绑定push
     * @param pushClientId
     * @param client
     * @return
     */
    MutationResult bindPush(String uuid,String pushClientId, String client);

    MutationResult unbindPush(String uuid,String pushCliendId,String client);

    AppVersion appVersionControl(String currentVersion, String client_id,String osVersion);
}
