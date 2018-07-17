package com.aobei.trainconsole.controller;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by adminL on 2018/5/7.
 */
@Controller
@RequestMapping("/proEvaluate")
public class ProEvaluateController {

    @Autowired
    private ProEvaluateService proEvaluateService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private UsersService usersService;


    private static Logger logger = LoggerFactory.getLogger(CouponEnvController.class);
    /**
     * 评价审核列表页
     * @param p
     * @param ps
     * @param model
     * @param pay_order_id
     * @param comment
     * @return
     */
    @RequestMapping("/proEvaluate_list")
    public String proEvaluate_list(
            @RequestParam(defaultValue="1") Integer p,
            @RequestParam(defaultValue="10")Integer ps, Model model,
            @RequestParam(required = false,value="order_input")String pay_order_id,
            @RequestParam(required = false,value="com_input")String comment,
            @RequestParam(required = false,value="verify")Integer verify){
        ProEvaluateExample proEvaluateExample = new ProEvaluateExample();
        ProEvaluateExample.Criteria or = proEvaluateExample.or();
        proEvaluateExample.setOrderByClause(ProEvaluateExample.C.product_evaluate_id+" desc");
                or.andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除的数据
                if(!StringUtils.isEmpty(pay_order_id)){
                    or.andPay_order_idEqualTo(pay_order_id);
                }
                if(!StringUtils.isEmpty(comment)){
                    or.andCommentLike("%"+comment+"%");
                }
                if(verify!=null && verify!=5){
                    or.andVerifyEqualTo(verify);
                    model.addAttribute("verify",verify);
                }else{
                    model.addAttribute("verify",5);
                }
        Page<ProEvaluate> page = proEvaluateService.selectByExample(proEvaluateExample,p,ps);
        model.addAttribute("page",page);
        model.addAttribute("list", page.getList());
        model.addAttribute("current",p);
        model.addAttribute("pay_order_id",pay_order_id);
        model.addAttribute("comment",comment);

        return "proEvaluate/proEvaluate_list";
    }

    /**
     * 通过id获取评价
     * @param product_evaluate_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCommentById")
    public Object getCommentById( @RequestParam(value="product_evaluate_id")Long product_evaluate_id){
        HashMap<String,String> map = new HashMap<>();
        if(product_evaluate_id!=null){
            ProEvaluate proEvaluate = proEvaluateService.selectByPrimaryKey(product_evaluate_id);
            if(proEvaluate==null){
                map.put("comment", "没有该商品信息！");
                return map;
            }
            map.put("comment", proEvaluate.getComment());
            return map;
        }else{
            map.put("comment", "没有该商品信息！");
            return map;
        }
    }

    /**
     * 单个通过
     * @param product_evaluate_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/passVer")
    public Object passVer( @RequestParam(value="product_evaluate_id")Long product_evaluate_id,Authentication authentication) {
        int i = proEvaluateService.xVerify(product_evaluate_id, 1);//审核通过
        HashMap<String,String> map = new HashMap();
        Users users = usersService.xSelectUserByUsername(authentication.getName());

        if(i>0){
            map.put("result", String.format("审核%s", i > 0 ? "成功" : "失败"));
            logger.info("M[proEvaluate] F[proEvaluate_passVer] U[{}];result[{}]",
                    users.getUser_id(),String.format("审核%s", i > 0 ? "成功" : "失败"));
            return map;
        }else{
            map.put("result","审核信息失败！商品评价信息为空");
            logger.info("M[proEvaluate] F[proEvaluate_passVer] U[{}];result[{}]",
                    users.getUser_id(),String.format("审核%s", i > 0 ? "成功" : "失败"));
            return map;
        }
    }

    /**
     * 单个拒绝
     * @param product_evaluate_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/noPassVer")
    public Object noPassVer( @RequestParam(value="product_evaluate_id")Long product_evaluate_id,Authentication authentication) {
        int i = proEvaluateService.xVerify(product_evaluate_id, 2);//审核不通过
        HashMap<String,String> map = new HashMap();
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        if(i>0){
            map.put("result", String.format("审核%s", i > 0 ? "成功" : "失败"));
            logger.info("M[proEvaluate] F[proEvaluate_noPassVer] U[{}];result[{}]",
                    users.getUser_id(),String.format("审核%s", i > 0 ? "成功" : "失败"));
            return map;
        }else{
            map.put("result","审核信息失败！商品评价信息为空");
            logger.info("M[proEvaluate] F[proEvaluate_passVer] U[{}];result[{}]",
                    users.getUser_id(),String.format("审核%s", i > 0 ? "成功" : "失败"));
            return map;
        }
    }

    /**
     * 详细订单数据
     * @param pay_order_id
     * @param model
     * @return
     */
    @RequestMapping("/goto_Order")
    public String goto_Order(@RequestParam(value="pay_order_id")String pay_order_id,Model model) {

        ServiceUnitExample sue = new ServiceUnitExample();
        sue.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(sue));

        Order order = orderService.selectByPrimaryKey(pay_order_id);
        model.addAttribute("order", order);
        model.addAttribute("su", serviceUnit);
        return "proEvaluate/order_detail";
    }

    /**
     * 所有选中的通过
     * @param list_proEva_ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/allPass")
    public Object allPass( @RequestParam(value="list_proEva_ids")String list_proEva_ids,Authentication authentication) {
        HashMap map = proEvaluateService.xAllVerify(list_proEva_ids, 1);//通过

        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[proEvaluate] F[proEvaluate_allPass] U[{}] param[{}];result[{}]",
                users.getUser_id(),list_proEva_ids,map.get("result"));
        //审核通过
        return map;
    }

    /**
     * 所有选中的拒绝
     * @param list_proEva_ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/allReject")
    public Object allReject( @RequestParam(value="list_proEva_ids")String list_proEva_ids,Authentication authentication) {
        HashMap map = proEvaluateService.xAllVerify(list_proEva_ids, 2);//拒绝

        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[proEvaluate] F[proEvaluate_allReject] U[{}] param[{}];result[{}]",
                users.getUser_id(),list_proEva_ids,map.get("result"));
        //审核未通过
        return map;
    }

}
