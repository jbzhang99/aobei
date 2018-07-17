package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSON;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.DiscountData;
import custom.bean.OrderInfo;
import custom.bean.Status;
import custom.bean.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.mapper.CouponEnvMapper;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponEnvServiceImpl extends MbgServiceSupport<CouponEnvMapper, Long, CouponEnv, CouponEnv, CouponEnvExample> implements CouponEnvService {

    @Autowired
    private CouponEnvMapper couponEnvMapper;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private CouponReceiveService couponReceiveService;
    @Autowired
    CouponService couponService;
    @Autowired
    OrderService orderService;

    @Autowired
    private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory) {
        super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(couponEnvMapper);
    }

    @Override
    public DiscountData xDiscountPolicy(Customer customer, ProSku proSku, int num) {
        Date now = new Date();
        CouponEnvExample couponEnvExample = new CouponEnvExample();
        couponEnvExample.or().andStart_datetimeLessThanOrEqualTo(now)
                .andEnd_datetimeGreaterThanOrEqualTo(now)
                .andStatusEqualTo(1);
        List<CouponEnv> couponEnvs = selectByExample(couponEnvExample);
        if (couponEnvs == null || couponEnvs.size() == 0) {
            return new DiscountData();
        }
        return discountData(customer, couponEnvs, proSku, num);
    }


    private DiscountData discountData(Customer customer, List<CouponEnv> list, ProSku proSku, int num) {


        List<DiscountData> discountDatas = list.stream().map(t -> {
            Type.CouponEnvType type = Type.CouponEnvType.getType(t.getType());
            Map<String, Object> map = JSON.parseObject(t.getCondition_env(), Map.class);

            switch (type) {
                case all_order:
                    return discountData(t, proSku, num);
                case first_order:
                    if (isFirstOrderPolicyCustomer(customer, map)
                            && isFirstOrder(customer, proSku)
                            && isRemaining(t, customer.getCustomer_id())) {
                        return discountData(t, proSku, num);
                    }
                    break;
                default:
                    break;
            }
            return null;
        }).collect(Collectors.toList());
        discountDatas = discountDatas.stream().
                filter(t -> t != null && t.isDiscount()).collect(Collectors.toList());
        if (discountDatas == null || discountDatas.size() == 0) {
            return new DiscountData();
        }
        return discountDatas.stream().min(Comparator.comparing(t -> t)).get();
    }


    private boolean isFirstOrder(Customer customer, ProSku proSku) {

        List<Integer> status = new ArrayList<>();
        status.add(Status.OrderStatus.wait_service.value);
        status.add(Status.OrderStatus.wait_pay.value);
        status.add(Status.OrderStatus.wait_confirm.value);
        status.add(Status.OrderStatus.done.value);
        OrderExample orderExample = new OrderExample();
        orderExample.or()
                .andUidEqualTo(customer.getCustomer_id())
                .andStatus_activeIn(status);
        long count = orderService.countByExample(orderExample);
        System.out.println("是否是第一单==========" + (count == 0));
        return count == 0l;
    }

    private boolean isFirstOrderPolicyCustomer(Customer customer, Map<String, Object> map) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date sdate = null;
        Date edate = null;
        try {
            sdate = format.parse(map.get("sdate").toString());
            edate = format.parse(map.get("edate").toString());
            // System.out.println("是否满足优惠策略条件=========="+map);
            return (customer.getCreate_datetime().after(sdate) && customer.getCreate_datetime().before(edate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    private boolean isRemaining(CouponEnv env, Long customer_id) {
        CouponReceiveExample example1 = new CouponReceiveExample();
        example1.or()
                .andCoupon_env_idEqualTo(env.getCoupon_env_id())
                .andUidEqualTo(customer_id)
                .andDeletedEqualTo(Status.DeleteStatus.no.value);
        if (couponReceiveService.countByExample(example1) > 0)
            return false;
        CouponReceiveExample example = new CouponReceiveExample();
        example.or().andCoupon_env_idEqualTo(env.getCoupon_env_id())
                .andDeletedEqualTo(Status.DeleteStatus.no.value);
        long count = couponReceiveService.countByExample(example);

        //System.out.println("是否还有剩余========="+(env.getCoupon_number() > count));
        return env.getCoupon_number() > count;
    }

    DiscountData discountData(CouponEnv env, ProSku proSku, int num) {
        DiscountData discountData = new DiscountData();
        Coupon coupon = couponService.selectByPrimaryKey(env.getCoupon_id());
        int price = couponService.recalculatePrice(coupon, proSku, num);
        if (price != -1 && price < (proSku.getPrice() * num)) {
            discountData.setDiscount(true);
            discountData.setPayPrice(price);
            discountData.setCouponEnv(env);
            discountData.setCoupon(coupon);
            Map<String, Object> map = new HashMap<>();
            map.put("coupon", coupon);
            map.put("couponEnv", env);
            discountData.setDiscountData(JSON.toJSONString(map));
        } else {
            discountData.setDiscount(false);
        }
        return discountData;
    }


}