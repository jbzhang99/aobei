package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.model.*;
import com.aobei.train.service.ChannelService;
import com.aobei.train.service.CustomerService;
import com.aobei.train.service.UsersService;
import com.aobei.train.service.UsersWxInfoService;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiUserService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.Img;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Component
public class ApiUserServiceImpl implements ApiUserService {
    Logger logger  = LoggerFactory.getLogger(ApiUserServiceImpl.class);
    @Autowired
    CustomerService customerService;
    @Autowired
    UsersService usersService;
    @Autowired
    UsersWxInfoService wxInfoService;
    @Autowired
    ChannelService channelService;
    @Override
    public Customer customerInfo(Long userId) {
        CustomerExample example = new CustomerExample();
        example.or()
                .andUser_idEqualTo(userId);
        Customer customer = singleResult(customerService.selectByExample(example));
        return customer;
    }

    /**
     * 手机号和验证码只要正确，就可绑定用户。
     * @param user_id
     * @param phone
     * @param channelCode
     * @return
     */
    @Override
    public ApiResponse<Customer> bindUser(Long user_id, String phone, String channelCode) {
        ApiResponse<Customer> response = new ApiResponse<>();
        try{
            CustomerExample customerExample  = new CustomerExample();
            customerExample.or().andPhoneEqualTo(phone);
            Customer customer  = singleResult(customerService.selectByExample(customerExample));
            if(customer==null){
                customer = new Customer();
                customer.setCustomer_id(IdGenerator.generateId());
                customer.setCreate_datetime(new Date());
            }
            customer.setUser_id(user_id);
            customer.setPhone(phone);
            Integer channelId = 0;
            if (channelCode!=null) {
                ChannelExample channelExample = new ChannelExample();
                channelExample.or().andCodeEqualTo(channelCode);
                Channel channel = singleResult(channelService.selectByExample(channelExample));
                channelId= channel==null?0:channel.getChannel_id();
            }
            customer.setChannel_id(channelId);
            Users users  = usersService.selectByPrimaryKey(user_id);
            String wx_id  = users.getWx_id();
            //如果用户绑定微信的第三方登录。查看微信的一些基本信息作为顾客的基本信息
            if(wx_id!=null){
                UsersWxInfo  info = wxInfoService.selectByPrimaryKey(wx_id);
                if(info!=null){
                    if(customer.getName()==null) {
                        customer.setName(info.getNickName());
                    }
                    if(customer.getLogo_img()==null){
                        Img img = new Img();
                        img.setId(0l);
                        img.setUrl(info.getAvatarUrl());
                        customer.setLogo_img(JSON.toJSONString(img));
                    }
                }
            }
            //插入或更新顾客信息。
            customer.setLocked(0);
            int count = customerService.upsertSelective(customer);
            if (count > 0) {
                userAddRole(user_id, Roles.CUSTOMER.roleName());
                response.setMutationResult(new MutationResult());
                return response;
            }
            response.setErrors(Errors._41001);
        }catch (Exception e ){
            logger.error("ERROR binduser" ,e);
        }

        return response;
    }

/********************************************
 * 我是分割线，下面是本类中的私有方法
 *************************************************/
    /**
     * 重复方法，方便用来分项目用的
     *
     * @param user_id  用户通用ID
     * @param roleName 角色名称
     * @return int
     */
    public int userAddRole(Long user_id, String roleName) {
        logger.info("api-method:userAddRole user_id:{},roleName:{}", user_id, roleName);
        UsersExample usersExample = new UsersExample();
        usersExample.or().andUser_idEqualTo(user_id).andUser_idIsNotNull();
        usersExample.includeColumns(UsersExample.C.user_id, UsersExample.C.roles);
        // 更新用户角色
        Users users = singleResult(usersService.selectByExample(usersExample));
        String[] roles = (users.getRoles() == null ? roleName : users.getRoles()).split(",");
        Optional<String> optional = Stream.of(roles).filter(n -> n.equals(roleName)).findAny();
        if (!optional.isPresent()) {
            roles = org.springframework.util.StringUtils.addStringToArray(roles, roleName);
        }
        users.setRoles(org.springframework.util.StringUtils.arrayToCommaDelimitedString(roles));
        return usersService.updateByPrimaryKeySelective(users);
    }
}
