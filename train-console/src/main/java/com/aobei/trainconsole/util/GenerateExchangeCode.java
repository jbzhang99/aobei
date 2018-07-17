package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.CouponReceive;
import com.aobei.train.model.CouponReceiveExample;
import com.aobei.train.service.CouponReceiveService;
import custom.bean.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成优惠券兑换码
 * Created by adminL on 2018/5/14.
 */
@Component
public class GenerateExchangeCode {

    @Autowired
    private CouponReceiveService couponReceiveService;

    private static String initCode = "abcdefghjklmnopqrstuvwxyz";

    public static GenerateExchangeCode generateExchangeCode;

    static {
        initCode += initCode.toUpperCase();
        initCode += "1234567890";
    }
    @PostConstruct
    public void init() {
        generateExchangeCode = this;
    }
    /**
     * 生成兑换码
     * @return
     */
    public String getExchangeCode(){
        Random random = new Random();

        StringBuilder sbl = new StringBuilder();
        for(int j=0;j<10;j++){
            sbl.append(initCode.charAt(random.nextInt(initCode.length())));
        }
        String code = sbl.toString();
        if(isContainsNumber(code)){
            return code;
        }else{
            getExchangeCode();
        }
        return code;
    }

   public List<CouponReceive> getExchangeCodeByNumber(int number, Long coupon_id){

        List<CouponReceive> list_code = new ArrayList<CouponReceive>();

        for (int i = 0; i<number ; i++){
            CouponReceive couponReceive = new CouponReceive();
            couponReceive.setCoupon_receive_id(IdGenerator.generateId());
            couponReceive.setExchange_code(ValidCode());
            couponReceive.setStatus(1);//待领取
            couponReceive.setVerification(0);
            couponReceive.setCoupon_id(coupon_id);
            couponReceive.setDeleted(Status.DeleteStatus.no.value);//未删除
            couponReceive.setCreate_datetime(new Date());
            couponReceive.setReceive_datetime(new Date());
            list_code.add(couponReceive);
        }
        if(list_code.size()!=number){
            getExchangeCode();
        }
        return list_code;
    }
    /**
     * 是否包含数字
     * @param code
     * @return
     */
    private boolean isContainsNumber(String code){
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(code);
        if (m.find()) {
            return false;
        }
        return true;
    }

    //获取优惠券兑换码并验证是否重复
    private String ValidCode(){
        String code = getExchangeCode();
        CouponReceiveExample couponReceiveExample = new CouponReceiveExample();
        couponReceiveExample.or()
                .andExchange_codeEqualTo(code)
                .andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除
        long l = generateExchangeCode.couponReceiveService.countByExample(couponReceiveExample);

        if(l>0){
            ValidCode();
        }else{
            return code;
        }
        return code;
    }

    /*public static void main(String[] args){

        System.out.println(isContainsNumber("eJAotbCpLG"));
        //System.out.println("lvp6v8Hlhp".equals(ssss()));
    }*/
}
