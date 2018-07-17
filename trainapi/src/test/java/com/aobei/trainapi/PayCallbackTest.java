package com.aobei.trainapi;

import com.aobei.train.model.Users;
import com.aobei.train.service.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")
public class PayCallbackTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UsersService usersService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                // 打印输出
                .alwaysDo(print())
                // 返回检查  HTTP状态
                .alwaysExpect(status().isOk())
                .build();
    }

    @Test
    public void callback_wxpay() throws Exception {
        this.mockMvc
                .perform(
                        //请求设置  URL
                        post("/callback/wx_m_custom/wxpay")
                                .content("<xml>\n" +
                                        "    <appid>wx731d62ae850c6c5e</appid>\n" +
                                        "    <mch_id>1498998082</mch_id>\n" +
                                        "    <nonce_str>1086776921141846016</nonce_str>\n" +
                                        "    <sign><![CDATA[E382F6173109A378E4A321A3B090F311]]></sign>\n" +
                                        "    <sign_type>MD5</sign_type>\n" +
                                        "    <body><![CDATA[家庭服务一居室]]></body>\n" +
                                        "    <out_trade_no>1086776916167401472959936</out_trade_no>\n" +
                                        "    <fee_type>CNY</fee_type>\n" +
                                        "    <total_fee>1</total_fee>\n" +
                                        "    <time_start>20180408191144</time_start>\n" +
                                        "    <time_expire>20180408194144</time_expire>\n" +
                                        "    <notify_url>https://test-api.aobei.com/callback/wx_m_custom/wxpay</notify_url>\n" +
                                        "    <trade_type>JSAPI</trade_type>\n" +
                                        "    <openid>oMrP40BMg5cs3VEdZWl_YH0nPgyQ</openid>\n" +
                                        "</xml>")
                                //请求调协  请求头
                                .accept(MediaType.APPLICATION_XML)
                                .contentType("application/octet-stream")
                )
                //返回检查   数据 xml 比对
                .andExpect(xpath("/xml/return_code").string("FAIL"))
                .andExpect(xpath("/xml/return_msg").string("ERROR"))
        ;

    }

    @Test
    public void callback_alipay() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gmt_create","2018-05-31 18:18:26");
        params.add("charset","utf-8");
        params.add("seller_email","abkj20171010@aobei.com");
        params.add("subject","vbbbbcbv");
        params.add("body","vbbbbcbv");
        params.add("buyer_id","2088412647793195");
        params.add("invoice_amount","0.01");
        params.add("notify_id","d121577f519a08d8e46da80435fe485hgx");
        params.add("fund_bill_list","[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]");
        params.add("notify_type","trade_status_sync");
        params.add("trade_status","TRADE_SUCCESS");
        params.add("receipt_amount","0.01");
        params.add("seller_id","2088131337265043");
        params.add("app_id","2018052960271202");
        params.add("gmt_payment","2018-05-31 18:18:28");
        params.add("notify_time","018-05-31 18:18:28");
        params.add("version","1.0");
        params.add("out_trade_no","1527764112_1");
        params.add("total_amount","0.01");
        params.add("trade_no","2018053121001004190521059896");
        params.add("auth_app_id","2018052960271202");
        params.add("buyer_logon_id","136****9970");
        params.add("point_amount","0.00");

        this.mockMvc
                .perform(
                        post("/callback/2018052960271202/alipay")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .params(params)
                )
                .andExpect(content().string("fail"));


    }
}