package com.aobei.trainapi;

import com.aobei.train.model.OssImg;
import com.aobei.trainapi.util.MyFileHandleUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOss {
    @Autowired
    MyFileHandleUtil util;

    @Test
    public  void signPrivateUrl(){

        OssImg ossImg  = new OssImg();
        ossImg.setUrl("http://aobei-test.oss-cn-beijing.aliyuncs.com/image/user/idcard/2018/2/7/1043158944833101824.png");
        ossImg.setName("1043158944833101824.png");

      String url  =   util.get_signature_url(ossImg.getUrl(),60*60l);


        System.out.println(url);


    }



}
