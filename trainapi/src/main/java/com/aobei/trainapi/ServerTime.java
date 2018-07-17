package com.aobei.trainapi;


import com.aobei.trainapi.server.bean.PresentTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@RestController
@RequestMapping(value = "/server/")
public class ServerTime {
    @RequestMapping(value = "/time")
    public PresentTime wxpayCallBack()  {
        PresentTime time = new PresentTime();
        long timeMillis = System.currentTimeMillis();
        time.setMillisecond(timeMillis);
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime()/1000);
        int second = Integer.parseInt(timestamp);
        time.setSecond(second);
        return time;
    }

}

