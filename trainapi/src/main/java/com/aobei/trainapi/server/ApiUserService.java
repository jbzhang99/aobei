package com.aobei.trainapi.server;

import com.aobei.train.model.Customer;
import com.aobei.train.model.Student;
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
     * 新版绑定用户(服务人员端)
     * @param user_id
     * @param phone
     * @param channel
     * @return
     */
    ApiResponse<Student> bindUserStudent(Long user_id, String phone, String channel);

    /**
     * 更新用户角色
     * @param user_id
     * @param roleName
     * @return
     */
    int userAddRole(Long user_id, String roleName);

    /**
     * 顾客解绑
     * @param customer
     * @return
     */
    ApiResponse customerRemoveTheBing(Customer customer);
}
