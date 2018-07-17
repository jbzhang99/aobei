package com.aobei.trainapi.configuration;

import com.aobei.train.model.WxMch;
import com.aobei.train.model.WxMchExample;
import com.aobei.train.service.WxMchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weixin.popular.client.LocalHttpClient;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Component
public class Certification {

    @Autowired
    WxMchService wxMchService;
    Logger logger = LoggerFactory.getLogger(Certification.class);
    @PostConstruct
    public void initWxPayCertification(){
        WxMchExample example  = new WxMchExample();
        //example.or().andMch_idEqualTo("1498998082");

        List<WxMch> list = wxMchService.selectByExample(example);
        list.stream().forEach(t->{

            File file = new File(t.getKeyfile_path());
            try {
                FileInputStream inputStream = new FileInputStream(file);
                LocalHttpClient.initMchKeyStore(t.getMch_id(), inputStream);
            } catch (Exception e) {
                logger.error("error  [mch_id="+t.getMch_id()+"keyfile_path="+t.getKeyfile_path()+"]");

            }

        });




    }
}
