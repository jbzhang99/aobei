package com.aobei.trainconsole.controller;

import com.aobei.train.model.*;
import com.aobei.train.service.CouponReceiveService;
import com.aobei.train.service.CouponService;
import com.aobei.train.service.UsersService;
import com.aobei.trainconsole.util.GenerateExchangeCode;
import com.aobei.trainconsole.util.PoiExcelExport;
import com.aobei.trainconsole.util.WriteExcel;
import com.gexin.fastjson.JSON;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Condition_type;
import custom.bean.CouponList;
import custom.bean.Programme_type;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adminL on 2018/6/27.
 */
@Controller
@RequestMapping("/couponInfo")
public class CouponInfoController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponReceiveService couponReceiveService;
    @Autowired
    private UsersService usersService;


    private static Logger logger = LoggerFactory.getLogger(CouponInfoController.class);

    @RequestMapping("/couponInfoList")
    public String couponInfoList(@RequestParam(required = false,defaultValue = "0")Integer type,
                                 @RequestParam(defaultValue="1")Integer p,
                                 @RequestParam(defaultValue="10")Integer ps,Model model){
        CouponExample example = new CouponExample();
        CouponExample.Criteria or = example.or();
        if(type!=0){
            or.andTypeEqualTo(type);
        }
        or.andTypeNotEqualTo(2);
        or.andVerifyNotEqualTo(0);
        example.setOrderByClause(CouponExample.C.deliver_datetime+" desc");
        Page<CouponList> page = couponService.xSelectCouponList(example, p, ps,2);//1为优惠券运营列表，2为优惠券财务审核列表

        model.addAttribute("number",(page.getPageNo()-1)*page.getPageSize());
        model.addAttribute("page",page);
        model.addAttribute("list",page.getList());
        model.addAttribute("type",type);
        return "coupon/coupon_info";
    }

    /**
     * 优惠券审核驳回
     * @param coupon_id
     * @param verify_comments
     * @return
     */
    @ResponseBody
    @RequestMapping("/coupon_reject")
    public Object coupon_reject(Long coupon_id,String verify_comments,Authentication authentication){
        HashMap<String, String> map = new HashMap<String, String>();
        Integer i = couponService.xVerify(coupon_id,3,verify_comments);
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        if(i>0){
            //添加审核人
            Coupon coupon = new Coupon();
            coupon.setCoupon_id(coupon_id);
            coupon.setVerify_user(users==null?0l:users.getUser_id());//审核人未空时添加0
            couponService.updateByPrimaryKeySelective(coupon);
            logger.info("M[couponInfo] F[审核驳回coupon_reject] U[{}];param[coupon_id:{}]; coupon:{}; result:{}",
                    users.getUser_id(), coupon_id, coupon, i > 0 ? "成功" : "失败");
        }
        map.put("result",String.format("审核优惠券驳回%s",i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     *优惠券审核通过
     * @param coupon_id
     * @param verify_comments
     * @return
     */
    @ResponseBody
    @RequestMapping("/coupon_pass")
    public Object coupon_pass(Long coupon_id, String verify_comments,Authentication authentication){
        HashMap<String, String> map = new HashMap<String, String>();
        Integer i = couponService.xVerify(coupon_id,2,verify_comments);
        if(i>0){
            //如果添加类型为兑换券时，审核通过就生成兑换码
            Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
            Users users = usersService.xSelectUserByUsername(authentication.getName());
            if(coupon.getType()== Status.CouponType.exchange_type.value){
                GenerateExchangeCode generateExchangeCode = new GenerateExchangeCode();
                List<CouponReceive> list = generateExchangeCode.getExchangeCodeByNumber(coupon.getNum_total(), coupon.getCoupon_id());
                couponReceiveService.batchInsertSelective(list.toArray(new CouponReceive[list.size()]));
                coupon.setNum_able(0);//优惠券为兑换券时，默认全部为待领取状态，已用完。
            }
            coupon.setVerify_user(users.getUser_id());
            couponService.updateByPrimaryKeySelective(coupon);
            logger.info("M[couponInfo] F[审核通过coupon_pass] U[{}];param[coupon_id:{}]; coupon:{}; result:{}",
                    users.getUser_id(), coupon_id, coupon, i > 0 ? "成功" : "失败");
        }
        map.put("result",String.format("审核优惠券通过%s",i > 0 ? "成功":"失败"));
        return map;
    }
    /**
     * 优惠券审核信息导出
     * @param response
     * @param type
     */
    @RequestMapping("/coupon_export")
    public void coupon_export(HttpServletResponse response,Integer type,Authentication authentication){
        String titleName[] ={"优惠券名称","优惠券类型","总数量","已领用","已下单","模板规则","生效时间","失效时间","状态","创建人","审核人"};
        CouponExample example = new CouponExample();
        CouponExample.Criteria or = example.or();
        if(type!=0){
            or.andTypeEqualTo(type);
        }
        or.andTypeNotEqualTo(2);
        or.andVerifyNotEqualTo(0);
        List<Object[]> objects = couponService.xSelectCouponList(example);
        WriteExcel writeExcel = new WriteExcel(titleName,objects);
        try {
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("优惠券审核信息.xls", "UTF-8"));
            InputStream export = writeExcel.export();
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[1024];
            int read = export.read(bytes);
            while (read!=-1){
                out.write(bytes,0,read);
                out.flush();
                read = export.read(bytes);
            }
            out.flush();
            export.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[couponInfo] F[审核信息导出coupon_export] U[{}];param[type:{}]; result:{优惠券审核信息导出成功}",
                users.getUser_id(), type);
        /*PoiExcelExport poiExcelExport = new PoiExcelExport(response,"优惠券审核信息","sheet1");
        poiExcelExport.wirteExcel(titleColumn,titleName,titleSize,objects);*/
    }

    /**
     * 传递指定商品数据集合
     *
     * @param coupon_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/to_products")
    public Object toProducts(@RequestParam(value = "coupon_id") Long coupon_id) {
        HashMap<String, List<Product>> map = couponService.getProductsList(coupon_id);
        return map;
    }

    /**
     * 获取审核信息内容
     * @param coupon_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getVerifyCom")
    public Object getVerifyCom(Long coupon_id) {
        HashMap<Object, Object> map = new HashMap<>();
        Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
        map.put("result",coupon.getVerify_comments());
        return map;
    }

    /**
     * 跳转到优惠券审核详情页
     * @param coupon_id
     * @param model
     * @return
     */
    @RequestMapping("/goto_detail")
    public String goto_detail(Long coupon_id,Model model){
        Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
        String condition = coupon.getCondition();
        Condition_type condition_type = JSON.parseObject(condition, Condition_type.class);
        String programme = coupon.getProgramme();
        Programme_type programme_type = JSON.parseObject(programme, Programme_type.class);
        StringBuilder str = new StringBuilder();
        str.append(condition_type.getTitle());
        str.append("，");
        str.append(programme_type.getTitle());
        model.addAttribute("coupon",coupon);
        model.addAttribute("title",str);
        return "coupon/coupon_detail";
    }

}
