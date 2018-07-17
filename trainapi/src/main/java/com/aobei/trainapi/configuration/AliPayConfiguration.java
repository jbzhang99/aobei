package com.aobei.trainapi.configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.aobei.train.model.AliPay;
import com.aobei.train.service.AliPayService;
import com.aobei.trainapi.server.bean.AliPayClientMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AliPayConfiguration {

     @Autowired
     AliPayService aliPayService;


    @Bean
    AliPayClientMap alipayClient(){

        List<AliPay> list  = aliPayService.selectByExample(null);

        Map<String,AlipayClient> map = new HashMap<>();
        list.stream().forEach(t->{

          AlipayClient client = new DefaultAlipayClient(
                  "https://openapi.alipay.com/gateway.do",
                  t.getApp_id(),
                  t.getPrivate_key(),
                  "json",
                  "utf-8",
                  t.getPublic_key(),
                  "RSA2"
          );
          map.put(t.getApp_id(),client);
       });

        return new AliPayClientMap(map);
    }

}
