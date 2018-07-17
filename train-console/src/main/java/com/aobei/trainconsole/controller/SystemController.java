package com.aobei.trainconsole.controller;

import com.aobei.train.model.Metadata;
import com.aobei.train.model.MetadataExample;
import com.aobei.train.service.MetadataService;
import com.aobei.trainconsole.util.JdAuthTokenUtil;
import custom.bean.JdToken;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mr_bl on 2018/5/25.
 */
@Controller
@RequestMapping("/systemmanager")
public class SystemController {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/metas")
    public String metas(Model map){
        List<Metadata> metas =
                metadataService.selectByExample(new MetadataExample());
        map.addAttribute("metas",metas);
        return "system/metas";
    }

    @RequestMapping("/jd_access_token")
    public String jd_access_token(Model map){
        String client_id = "F67A59B071284E359D8D12F1E4951567";
        String redirect_uri = "/systemmanager/result_receive";
        map.addAttribute("client_id",client_id);
        map.addAttribute("redirect_uri",redirect_uri);
        return "system/jd_access_token";
    }

    @RequestMapping("/result_receive")
    public String result_receive(String code,Model map){
        JdToken token = null;
        try {
            token = JdAuthTokenUtil.getTokenByCode(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (token != null){
            JSONObject jsonObject = new JSONObject(token);
            String json = jsonObject.toString();
            redisTemplate.opsForValue().set("JD_Token",json);
            redisTemplate.expire("JD_Token",354, TimeUnit.DAYS);
            String refreshToken = token.getRefresh_token();
            redisTemplate.opsForValue().set("JD_Refresh_Token",refreshToken);
        }
        map.addAttribute("token", token);
        return "system/jd_access_token";
    }
}
