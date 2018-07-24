package com.aobei.trainconsole.controller;

import com.aobei.train.model.*;
import com.aobei.train.service.CancleStrategyService;
import com.aobei.train.service.ProductService;
import com.aobei.train.service.UsersService;
import custom.bean.Status;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adminL on 2018/6/19.
 */
@Controller
@RequestMapping("/cancleStrategy")
public class CancleStrategyController {

    @Autowired
    private CancleStrategyService strategyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UsersService usersService;

    private static Logger logger = LoggerFactory.getLogger(CancleStrategyController.class);
    /**
     * 取消策略列表页
     * @param model
     * @return
     */
    @RequestMapping("/CancleStrategyList")
    public String CancleStrategyList(Model model){
        CancleStrategyExample cancleStrategyExample = new CancleStrategyExample();
        cancleStrategyExample.or()
                .andDeletedEqualTo(Status.DeleteStatus.no.value);
        List<CancleStrategy> CancleS = strategyService.selectByExample(cancleStrategyExample);
        model.addAttribute("CancleS", CancleS);
        return "cancle/list_cancle_os";
    }
    /**
     * 跳转到添加页
     * @param model
     * @return
     */
    @RequestMapping("/goto_add_cancle_os")
    public String goto_add_cancle_os(Model model){
        ProductExample productExample = new ProductExample();
        productExample.or()
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andOnlineEqualTo(1);
        List<Product> products = productService.selectByExample(productExample);
        model.addAttribute("products",products);
        return "cancle/add_cancle_os";
    }
    /**
     * 添加取消策略
     * @param list
     * @param strategy
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/add_cancle_os")
    public Object add_cancle_os(String list, CancleStrategy strategy,Authentication authentication){
        HashMap<Object, String> map = new HashMap<>();
        if(StringUtils.isEmpty(list)){
            map.put("result","取消策略为空，添加失败！");
            return map;
        }
        Integer i = strategyService.xInsertCancleStratrgy(list, strategy, authentication);
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[cancle_os] F[add_cancle_os] U[{}] param[CancleStrategy:{}];  result:{}"
                ,users.getUser_id(),strategy,String.format("添加取消策略%s", i > 0 ? "成功":"失败"));
        map.put("result",String.format("添加取消策略%s",i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     * 跳转到编辑页面
     * @param model
     * @param cancle_strategy_id
     * @return
     */
    @RequestMapping("/goto_edit_cancle_os")
    public String goto_edit_cancle_os(Model model,Integer cancle_strategy_id){

        CancleStrategy cancleStrategy = strategyService.selectByPrimaryKey(cancle_strategy_id);
        model.addAttribute("cancleStrategy", cancleStrategy);
        model.addAttribute("list", cancleStrategy.getCancle_strategy());
        model.addAttribute("cancle_strategy_id",cancle_strategy_id);
        model.addAttribute("type", cancleStrategy.getCancle_type());
        return "cancle/edit_cancle_os";
    }

    /**
     * 保存编辑的数据
     * @param list
     * @param strategy
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/edit_cancle_os")
    public Object edit_cancle_os(String list, CancleStrategy strategy,Authentication authentication){
        HashMap<Object, String> map = new HashMap<>();
        if(StringUtils.isEmpty(list)){
            map.put("result","取消策略为空，添加失败！");
            return map;
        }
        Integer i = strategyService.xUpdateCancleStratrgy(list, strategy, authentication);
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[cancle_os] F[edit_cancle_os] U[{}] param[CancleStrategy:{}];  result:{}"
                ,users.getUser_id(),strategy,String.format("编辑取消策略%s", i > 0 ? "成功":"失败"));
        map.put("result",String.format("编辑取消策略%s",i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     * 删除取消策略
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteCancleStrategy")
    public Object deleteCancleStrategy(Integer cancle_strategy_id,Authentication authentication){
        HashMap<Object, String> map = new HashMap<>();
        ProductExample productExample = new ProductExample();
        productExample.or()
                .andCancle_strategy_idEqualTo(cancle_strategy_id);
        long l = productService.countByExample(productExample);
        if(l>0){
            map.put("result","删除失败，已有商品关联此策略");
            return map;
        }
        CancleStrategy cancleStrategy = new CancleStrategy();
        cancleStrategy.setCancle_strategy_id(cancle_strategy_id);
        cancleStrategy.setDeleted(1);
        int i = strategyService.updateByPrimaryKeySelective(cancleStrategy);
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[cancle_os] F[deleteCancleStrategy] U[{}] param[cancle_strategy_id:{}];  result:{}"
                ,users.getUser_id(),cancle_strategy_id,String.format("删除取消策略%s", i > 0 ? "成功":"失败"));
        map.put("result",String.format("删除取消策略%s",i > 0 ? "成功":"失败"));
        return map;
    }
}
