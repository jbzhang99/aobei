package com.aobei.trainapi.server.listener;

import com.alibaba.fastjson.JSON;
import com.aobei.train.IdGenerator;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.Constant;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * Created by 15010 on 2018/7/31.
 */
@Component
@Configuration
public class AutoCouponListerer {
    Logger logger = LoggerFactory.getLogger(AutoCancelOrderListener.class);
    @Autowired
    CouponAndCouponEnvService couponAndCouponEnvService;
    @Autowired
    CouponEnvService couponEnvService;
    @Autowired
    CouponService couponService;
    @Autowired
    StringRedisTemplate redisTemplatee;
    @Autowired
    CustomerService customerService;
    @Autowired
    CouponReceiveService couponReceiveService;
    @Autowired
    CacheReloadHandler cacheReloadHandler;

    private static String key = "couponDistributed";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private BoundListOperations<String, Object> opsList;

    @PostConstruct
    private void initOps() {
        opsList = redisTemplate.boundListOps(key);
    }

    /**
     * 调度
     */
   @Scheduled(initialDelay = 1000, fixedDelay = 1200)
    private void task_data_from_redis() {
        Object object = 1;
        // 如果有获取到list 中的数据
        while (object != null) {
            object = opsList.leftPop(1, TimeUnit.SECONDS);
            if (object != null) {
                 distributeCoupon(Long.valueOf(String.valueOf(object)));
            }
        }
    }



    public Boolean distributeCoupon(Long user_id){
        CustomerExample customerExample  = new CustomerExample();
        customerExample.or().andUser_idEqualTo(user_id);
        Customer customer  = singleResult(customerService.selectByExample(customerExample));
        if (customer==null){
            return false;
        }
        Map<Object, Object> hashMap = (Map<Object, Object>) couponMethod(customer);
        Boolean bool = (Boolean) hashMap.get("boolean");
        if (bool){
            CouponEnv couponEnv =(CouponEnv) hashMap.get("couponEnv");
            couponEnv.setCoupon_number(couponEnv.getCoupon_number() -1);
            couponEnvService.updateByPrimaryKeySelective(couponEnv);
            List<CouponAndCouponEnv> couponList =(List<CouponAndCouponEnv>) hashMap.get("couponList");
            for (CouponAndCouponEnv couponandCouponEnv: couponList) {
                Coupon coupon = couponService.selectByPrimaryKey(couponandCouponEnv.getCoupon_id());
                String key = Constant.getCouponKey(coupon.getCoupon_id());
                coupon.setNum_able(Integer.parseInt(redisTemplatee.opsForValue().increment(key,-1L)+""));
                couponService.updateByPrimaryKeySelective(coupon);
                //为用户添加使用记录
                CouponReceive couponReceive = new CouponReceive();
                couponReceive.setCoupon_receive_id(IdGenerator.generateId());
                couponReceive.setUid(customer.getCustomer_id());
                couponReceive.setCoupon_id(couponandCouponEnv.getCoupon_id());
                couponReceive.setReceive_datetime(new Date());
                couponReceive.setStatus(2);//待使用状态
                couponReceive.setVerification(0);//未核销
                couponReceive.setDeleted(Status.DeleteStatus.no.value);
                couponReceive.setCoupon_env_id(couponandCouponEnv.getCoupon_env_id());
                couponReceive.setCreate_datetime(new Date());
                couponReceiveService.insert(couponReceive);
                //每次清除对应顾客优惠券缓存信息
                cacheReloadHandler.couponListReload(customer.getCustomer_id());
                cacheReloadHandler.userCouponListReload(customer.getCustomer_id());
            }
        }else {
            return false;
        }
        return true;
    }

    //私有方法(用来校验优惠策略)
    public Object couponMethod(Customer customer) {
        Map<Object, Object> hashMap = new HashMap<>();
        //插入新注册用户优惠券
        CouponEnvExample couponEnvExample = new CouponEnvExample();
        CouponEnvExample.Criteria or = couponEnvExample.or();
        or.andTypeEqualTo(1)
                .andStatusEqualTo(1)
                .andCoupon_env_typeEqualTo(1)
                .andStart_datetimeLessThanOrEqualTo(new Date())
                .andEnd_datetimeGreaterThanOrEqualTo(new Date())
                .andCoupon_numberGreaterThan(0);
        CouponEnv couponEnv = singleResult(couponEnvService.selectByExample(couponEnvExample));
        if (!StringUtils.isEmpty(couponEnv)) {
            hashMap.put("couponEnv", couponEnv);
            String condition_env = couponEnv.getCondition_env();
            Map<String, String> envMap = new HashMap<>();
            Map map = JSON.parseObject(condition_env, envMap.getClass());
            String sdate = (String) map.get("sdate");
            String edate = (String) map.get("edate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startdate = null;
            Date endDate = null;
            try {
                startdate = format.parse(sdate);
                endDate = format.parse(edate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date cusCreateDate = customer.getCreate_datetime();
            //顾客注册时间是否在策略之内
            if (cusCreateDate.before(endDate) && cusCreateDate.after(startdate)) {
                CouponAndCouponEnvExample couponAndCouponEnvExample = new CouponAndCouponEnvExample();
                couponAndCouponEnvExample.or().andCoupon_env_idEqualTo(couponEnv.getCoupon_env_id());
                List<CouponAndCouponEnv> couponAndCouponEnvs = couponAndCouponEnvService.selectByExample(couponAndCouponEnvExample);
                hashMap.put("couponList", couponAndCouponEnvs);
                for (CouponAndCouponEnv couponandCouponEnv : couponAndCouponEnvs) {
                    //减优惠券数量
                    Coupon coupon = couponService.selectByPrimaryKey(couponandCouponEnv.getCoupon_id());
                    if (coupon.getValid() != 1 || coupon.getReceive_end_datetime().before(new Date())) {
                        hashMap.put("boolean", false);
                        return hashMap;
                    }
                    String key = Constant.getCouponKey(coupon.getCoupon_id());
                    if (coupon.getNum_limit() == 1) {
                        if (!StringUtils.isEmpty(key)) {
                            String keyValue = redisTemplatee.opsForValue().get(key);
                            if (StringUtils.isEmpty(keyValue)) {
                                hashMap.put("boolean", false);
                                return hashMap;
                            }
                            int num = Integer.parseInt(keyValue);
                            if (num == 0) {
                                redisTemplate.delete(key);
                                hashMap.put("boolean", false);
                                return hashMap;
                            }
                        }
                    }
                }
            }
        }else {
            hashMap.put("boolean",false);
        }
        hashMap.put("boolean", true);
        return hashMap;
    }
}
