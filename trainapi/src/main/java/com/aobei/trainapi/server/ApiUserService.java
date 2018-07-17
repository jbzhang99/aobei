package com.aobei.trainapi.server;

import com.aobei.train.model.Customer;
import com.aobei.trainapi.server.bean.ApiResponse;

public interface ApiUserService {

    /**
     * 获取顾客信息的基本信息
     * @param userId
     * @return
     */
    Customer customerInfo(Long userId);

    /**
     * 新版绑定用户
     * @param user_id
     * @param phone
     * @param channel
     * @return
     */
    ApiResponse<Customer> bindUser(Long user_id, String phone, String channel);

    /**
     * 更新用户角色
     * @param user_id
     * @param roleName
     * @return
     */
    int userAddRole(Long user_id, String roleName);
}
