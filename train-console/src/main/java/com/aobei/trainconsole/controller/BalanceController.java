package com.aobei.trainconsole.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.*;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.BalanceHistory;
import custom.bean.StepData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletionService;

@Controller
@RequestMapping("/balance")
public class BalanceController {

	private static final Logger logger= LoggerFactory.getLogger(BalanceController.class);
    @Autowired
    private ExportBalanceAndToOss exportBalanceAndToOss;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CompletionService<Integer> completionService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private FallintoService fallintoService;
    @Autowired
    private BalanceOrderService balanceOrderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PartnerFallintoService partnerFallintoService;
    @Autowired
    private FallintoRefundService fallintoRefundService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private ExportRefundBalanceAndToOss exportRefundBalanceAndToOss;
    @Autowired
    private FallintoCompensationService fallintoCompensationService;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private ExportCompensationBalanceAndToOss exportCompensationBalanceAndToOss;

    @Autowired
    private FallintoDeductMoneyService fallintoDeductMoneyService;

    @Autowired
    private DeductMoneyService deductMoneyService;

    @Autowired
    private ExportDeductMoneyBalanceAndToOss exportDeductMoneyBalanceAndToOss;

    @Autowired
    private FallintoFineMoneyService fallintoFineMoneyService;

    @Autowired
    private ExportFineMoneyBalanceAndToOss exportFineMoneyBalanceAndToOss;
    /**
     * 结算策略列表
     * @param p
     * @param ps
     * @param model
     * @return
     */
    @RequestMapping("/balance_tactics_list")
    public String balance_tactics_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model){
        //从数据库查询
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.setOrderByClause(FallintoExample.C.create_datetime+" desc");
        fallintoExample.or().andDeletedEqualTo(0);
        Page<Fallinto> fallintoPage = this.fallintoService.selectByExample(fallintoExample, p, ps);
        List<Fallinto> list = fallintoPage.getList();
        //删除最后一页只有一条数据时，跳转到上一页
        if(list.size() == 0 & p>1) {
            fallintoPage = this.fallintoService.selectByExample(fallintoExample,p-1,ps);
            list = fallintoPage.getList();
        }
        model.addAttribute("fallintos",list);
        model.addAttribute("page",fallintoPage);
        return "balance/balance_tactics_list";
    }

    /**
     * 跳转添加结算页面
     * @param p
     * @param model
     * @return
     */
    @RequestMapping("/addTactics")
    public String addTactics(@RequestParam(defaultValue ="1" ) int p,Model model){
        model.addAttribute("p",p);
        return "balance/balance_tactics_add";
    }

    /**
     * 添加结算功能
     * @param request
     * @param authentication
     * @return
     */
    @RequestMapping("/addFallinto")
    @ResponseBody
    @Transactional
    public Object addFallinto(HttpServletRequest request, Authentication authentication) {
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());

        String str = request.getParameter("result");
        logger.info("M[balance] F[addFallinto] U[{}] ,params request:{}",
                users.getUser_id(),str);
        int num=this.balanceOrderService.xAddFallinto(str,users);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("结算策略%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[addFallinto] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }

    /**
     * 生效
     * @param fallinto_id
     * @param pageNo
     * @return
     */
    @RequestMapping("/effect/{fallinto_id}/{pageNo}")
    @ResponseBody
    @Transactional
    public Object effect(@PathVariable(value = "fallinto_id") Long fallinto_id,@PathVariable(value = "pageNo") int pageNo, Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[effect] U[{}] ,params fallinto_id:{}",
                users.getUser_id(),fallinto_id);
        Fallinto fallinto = this.fallintoService.selectByPrimaryKey(fallinto_id);
        fallinto.setActived(1);
        fallinto.setIs_actived(1);
        int num = this.fallintoService.updateByPrimaryKeySelective(fallinto);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("生效%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[effect] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }

    /**
     * 失效
     * @param fallinto_id
     * @param pageNo
     * @return
     */
    @RequestMapping("/efficacy/{fallinto_id}/{pageNo}")
    @ResponseBody
    @Transactional
    public Object efficacy(@PathVariable(value = "fallinto_id") Long fallinto_id,@PathVariable(value = "pageNo") int pageNo, Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[efficacy] U[{}] ,params fallinto_id:{}",
                users.getUser_id(),fallinto_id);
        Map<String, Object> resultMap=new HashMap<>();
        Boolean flag=true;
        PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
        partnerFallintoExample.or().andFallinto_idEqualTo(fallinto_id);
        List<PartnerFallinto> partnerFallintos = this.partnerFallintoService.selectByExample(partnerFallintoExample);
        if(partnerFallintos.size()!=0){
            //resultMap.put("message", String.format("该策略使用中，请先调整指定失效！"));
            flag=false;
        }else{
            Fallinto fallinto = this.fallintoService.selectByPrimaryKey(fallinto_id);
            fallinto.setActived(0);
            int num = this.fallintoService.updateByPrimaryKeySelective(fallinto);
           // resultMap.put("message", String.format("失效%s!",num>0 ? "成功" : "失败"));
            flag=true;
        }
        logger.info("M[balance] F[efficacy] U[{}] ,execute result:{}",
                users.getUser_id(),flag);
        return flag;
    }

    /**
     * 删除
     * @param fallinto_id
     * @param pageNo
     * @return
     */
    @RequestMapping("/del/{fallinto_id}/{pageNo}")
    @ResponseBody
    @Transactional
    public Object del(@PathVariable(value = "fallinto_id") Long fallinto_id,@PathVariable(value = "pageNo") int pageNo, Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[del] U[{}] ,params fallinto_id:{}",
                users.getUser_id(),fallinto_id);
        Fallinto fallinto = this.fallintoService.selectByPrimaryKey(fallinto_id);
        fallinto.setDeleted(1);
        int num = this.fallintoService.updateByPrimaryKeySelective(fallinto);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("删除%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[del] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }


    /**
     * 查看
     * @param fallinto_id
     * @param pageNo
     * @param model
     * @return
     */
    @RequestMapping("/look/{fallinto_id}/{pageNo}")
    public Object look(@PathVariable(value = "fallinto_id") Long fallinto_id,@PathVariable(value = "pageNo") int pageNo,Model model){
        Fallinto fallinto = this.fallintoService.selectByPrimaryKey(fallinto_id);
        List<StepData> stepDatas = JSONObject.parseArray(fallinto.getStep_data(),StepData.class);
        model.addAttribute("fallinto",fallinto);
        model.addAttribute("stepDatas",stepDatas);
        return "balance/balance_tactics_look";
    }

    /**
     * 待结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @return
     */
	@RequestMapping("/balance_waiting_list")
	public String balance_waiting_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                    String balanceDate){
        BalanceOrderExample balanceExample = new BalanceOrderExample();
        BalanceOrderExample.Criteria criteria = balanceExample.or().andStatusEqualTo(1);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)){
            criteria.andBalance_cycleLike("%"+balanceDate+"%");
        }
        Page<BalanceOrder> page = this.balanceOrderService.selectByExample(balanceExample, p, ps);
        List<BalanceOrder> balanceOrderList = page.getList();
        //商品
        ProductExample productExample = new ProductExample();
        productExample.or().andDeletedEqualTo(0);
        List<Product> products = productService.selectByExample(productExample);

        //合伙人结算策略集合
        List<PartnerFallinto> partnerFallintos = partnerFallintoService.selectByExample(new PartnerFallintoExample());

        //结算
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.or().andDeletedEqualTo(0);
        List<Fallinto> fallintos = fallintoService.selectByExample(fallintoExample);

        model.addAttribute("page",page);
        model.addAttribute("balanceOrderList",balanceOrderList);
        model.addAttribute("products",products);
        model.addAttribute("partnerFallintos",partnerFallintos);
        model.addAttribute("fallintos",fallintos);
        return "balance/complete_waiting_list";
	}

    /**
     * 挂起列表
     * @param p
     * @param ps
     * @param model
     * @return
     */
    @RequestMapping("/balance_hangup_list")
    public String balance_hangup_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                       String orderId){
        BalanceOrderExample balanceExample = new BalanceOrderExample();
        BalanceOrderExample.Criteria criteria = balanceExample.or().andStatusEqualTo(3);
        if(orderId !=null || !StringUtils.isEmpty(orderId)){
            criteria.andPay_order_idLike("%"+orderId+"%");
        }
        Page<BalanceOrder> page = this.balanceOrderService.selectByExample(balanceExample, p, ps);
        List<BalanceOrder> balanceOrderList = page.getList();
        //商品
        ProductExample productExample = new ProductExample();
        productExample.or().andDeletedEqualTo(0);
        List<Product> products = productService.selectByExample(productExample);

        //合伙人结算策略集合
        List<PartnerFallinto> partnerFallintos = partnerFallintoService.selectByExample(new PartnerFallintoExample());

        //结算
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.or().andDeletedEqualTo(0);
        List<Fallinto> fallintos = fallintoService.selectByExample(fallintoExample);

        model.addAttribute("page",page);
        model.addAttribute("balanceOrderList",balanceOrderList);
        model.addAttribute("products",products);
        model.addAttribute("partnerFallintos",partnerFallintos);
        model.addAttribute("fallintos",fallintos);
        return "balance/complete_hangup_list";
    }
    /**
     * 訂單已完成结算完成列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @param orderId
     * @return
     */
    @RequestMapping("/complete_list")
    public String complete_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                       String balanceDate,String orderId){
        BalanceOrderExample balanceExample = new BalanceOrderExample();
        BalanceOrderExample.Criteria criteria = balanceExample.or().andStatusEqualTo(2);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)){
            criteria.andBalance_cycleLike("%"+balanceDate+"%").andPay_order_idLike("%"+orderId+"%");
        }

        if(orderId !=null || !StringUtils.isEmpty(orderId)){
            criteria.andPay_order_idLike("%"+orderId+"%");
        }
        Page<BalanceOrder> page = this.balanceOrderService.selectByExample(balanceExample, p, ps);
        List<BalanceOrder> balanceOrderList = page.getList();
        //商品
        ProductExample productExample = new ProductExample();
        productExample.or().andDeletedEqualTo(0);
        List<Product> products = productService.selectByExample(productExample);

        //合伙人结算策略集合
        List<PartnerFallinto> partnerFallintos = partnerFallintoService.selectByExample(new PartnerFallintoExample());

        //结算
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.or().andDeletedEqualTo(0);
        List<Fallinto> fallintos = fallintoService.selectByExample(fallintoExample);

        model.addAttribute("page",page);
        model.addAttribute("balanceOrderList",balanceOrderList);
        model.addAttribute("products",products);
        model.addAttribute("partnerFallintos",partnerFallintos);
        model.addAttribute("fallintos",fallintos);
        return "balance/complete_list";
    }

    /**
     * 导出（订单已完成待结算）
     */
    @ResponseBody
	@RequestMapping("/exportBalance")
	public Object exportBalance(int status,HttpServletRequest req, Authentication authentication){
        String str = req.getParameter("params")==null?"":req.getParameter("params");
        String username = authentication.getName();
        long id = IdGenerator.generateId();
        BalanceOrderExample balanceOrderExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, BalanceOrderExample.class);
        balanceOrderExample.or().andStatusEqualTo(status);
        exportBalanceAndToOss.setBalanceOrderExample(balanceOrderExample);
        exportBalanceAndToOss.setData_download_id(id);
        exportBalanceAndToOss.setType(status);
        // 生成excel并将之上传到阿里云OSS
        completionService.submit(exportBalanceAndToOss);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
        return result;
    }


    /**
     * 挂起
     * @return
     */
    @RequestMapping("/hangUp/{balance_order_id}")
    @ResponseBody
    @Transactional
    public Object hangUp(@PathVariable(value = "balance_order_id") Long balance_order_id,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[hangUp] U[{}] ,params balance_order_id:{}",
                users.getUser_id(),balance_order_id);
        int num=this.balanceOrderService.xHangUp(balance_order_id,users);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("挂起%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[hangUp] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }

    /**
     * 解除挂起
     * @return
     */
    @RequestMapping("/hangDown/{balance_order_id}")
    @ResponseBody
    public Object hangDown(@PathVariable(value = "balance_order_id") Long balance_order_id,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        BalanceOrder balanceOrder = balanceOrderService.selectByPrimaryKey(balance_order_id);
        balanceOrder.setStatus(2);
        List<BalanceHistory> balanceHistoryList=new ArrayList<>();
        if(balanceOrder.getChange_history()!=null){
            balanceHistoryList = JSONArray.parseArray(balanceOrder.getChange_history(), BalanceHistory.class);
        }
        BalanceHistory balanceHistory=new BalanceHistory();
        balanceHistory.setType("结算");
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        balanceHistory.setD(localDateTime.toString());
        balanceHistory.setOperation(users.getUsername());
        balanceHistoryList.add(balanceHistory);
        balanceOrder.setChange_history(JSONArray.toJSONString(balanceHistoryList));
        balanceOrder.setBalance_datetime(new Date());
        int num = this.balanceOrderService.updateByPrimaryKeySelective(balanceOrder);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("解除挂起%s!",num>0 ? "成功" : "失败"));
        return resultMap;
    }

    /**
     * 查询订单完成结算最大结算期
     * @param cycle
     * @param authentication
     * @return
     */
    @RequestMapping("/selectCycleMax")
    @ResponseBody
    public Object selectCycleMax(String cycle,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        String balance_cycle=this.balanceOrderService.selectMaxBalanceCycle();
        Boolean flag=true;
        if(Integer.parseInt(cycle)>Integer.parseInt(balance_cycle)){
            flag=false;
        }
        return flag;
    }

    /**
     * 订单完成结算
     * @param id
     * @param cycle
     * @param pageNo
     * @param authentication
     * @return
     */
    @RequestMapping("/updCycle")
    @Transactional
    public String updCycle(Long id,String cycle,int pageNo,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[updCycle] U[{}] ,params id:{}",
                users.getUser_id(),id);
        BalanceOrder balanceOrder = this.balanceOrderService.selectByPrimaryKey(id);
        balanceOrder.setBalance_cycle(cycle);
        balanceOrder.setStatus(2);
        balanceOrder.setBalance_datetime(new Date());
        int num = this.balanceOrderService.updateByPrimaryKeySelective(balanceOrder);
        logger.info("M[balance] F[updCycle] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return "redirect:/balance/balance_hangup_list?p="+pageNo;
    }


    /**
     * 退款待结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @return
     */
    @RequestMapping("/refund_waiting_list")
    public String refund_waiting_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                    String balanceDate){
        FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
        FallintoRefundExample.Criteria criteria = fallintoRefundExample.or().andStatusEqualTo(1);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)){
            criteria.andBalance_cycleLike("%"+balanceDate+"%");
        }
        Page<FallintoRefund> page = fallintoRefundService.selectByExample(fallintoRefundExample, p, ps);
        List<FallintoRefund> fallintoRefundList = page.getList();

        List<Long> idList=new ArrayList<>();
        fallintoRefundList.stream().forEach(fallintoRefund -> {
            idList.add(fallintoRefund.getRefund_id());
        });

        List<Refund> refunds =new ArrayList<>();
        if(!idList.isEmpty()){
            RefundExample refundExample = new RefundExample();
            refundExample.or().andRefund_idIn(idList);
            refunds = refundService.selectByExample(refundExample);
        }

        model.addAttribute("refunds",refunds);
        model.addAttribute("fallintoRefundList",fallintoRefundList);
        model.addAttribute("page",page);
        return "balance/refund_waiting_list";
    }


    /**
     * 退款挂起
     * @return
     */
    @RequestMapping("/refundHangUp/{fallinto_refund_id}")
    @ResponseBody
    @Transactional
    public Object refundHangUp(@PathVariable(value = "fallinto_refund_id") Long fallinto_refund_id,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[refundHangUp] U[{}] ,params fallinto_refund_id:{}",
                users.getUser_id(),fallinto_refund_id);
        FallintoRefund fallintoRefund = fallintoRefundService.selectByPrimaryKey(fallinto_refund_id);
        fallintoRefund.setStatus(3);
        int num = fallintoRefundService.updateByPrimaryKeySelective(fallintoRefund);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("挂起%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[refundHangUp] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }
    /**
     * 退款挂起列表
     * @param p
     * @param ps
     * @param model
     * @return
     */
    @RequestMapping("/refund_hangup_list")
    public String refund_hangup_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                      String OrderId,Long refund_id){
        FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
        FallintoRefundExample.Criteria criteria = fallintoRefundExample.or().andStatusEqualTo(3);
        if(OrderId !=null || !StringUtils.isEmpty(OrderId)){
            criteria.andPay_order_idLike("%"+OrderId+"%");
        }
        if(refund_id !=null || !StringUtils.isEmpty(refund_id)){
            criteria.andRefund_idEqualTo(refund_id);
        }
        Page<FallintoRefund> page = fallintoRefundService.selectByExample(fallintoRefundExample, p, ps);
        List<FallintoRefund> fallintoRefundList = page.getList();

        List<Long> idList=new ArrayList<>();
        fallintoRefundList.stream().forEach(fallintoRefund -> {
            idList.add(fallintoRefund.getRefund_id());
        });

        List<Refund> refunds =new ArrayList<>();
        if(!idList.isEmpty()){
            RefundExample refundExample = new RefundExample();
            refundExample.or().andRefund_idIn(idList);
            refunds = refundService.selectByExample(refundExample);
        }

        model.addAttribute("refunds",refunds);
        model.addAttribute("fallintoRefundList",fallintoRefundList);
        model.addAttribute("page",page);
        return "balance/refund_hangup_list";
    }
    /**
     * 查找退款最大结算期
     * @param cycle
     * @param authentication
     * @return
     */
    @RequestMapping("/selectCycleMaxRefund")
    @ResponseBody
    public Object selectCycleMaxRefund(String cycle,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        String balance_cycle=this.fallintoRefundService.selectMaxBalanceCycle();
        Boolean flag=true;
        if(Integer.parseInt(cycle)>Integer.parseInt(balance_cycle)){
            flag=false;
        }
        return flag;
    }

    /**
     * 退款订单结算
     * @param id
     * @param cycle
     * @param pageNo
     * @param authentication
     * @return
     */
    @RequestMapping("/updCycleRefund")
    @Transactional
    public String updCycleRefund(Long id,String cycle,int pageNo,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[updCycleRefund] U[{}] ,params id:{}",
                users.getUser_id(),id);
        FallintoRefund fallintoRefund = this.fallintoRefundService.selectByPrimaryKey(id);
        fallintoRefund.setBalance_cycle(cycle);
        fallintoRefund.setStatus(2);
        int num = this.fallintoRefundService.updateByPrimaryKeySelective(fallintoRefund);
        logger.info("M[balance] F[updCycleRefund] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return "redirect:/balance/refund_hangup_list?p="+pageNo;
    }

    /**
     * 退款结算完成列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @return
     */
    @RequestMapping("/refund_list")
    public String refund_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                String balanceDate,Long refundId){
        FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
        FallintoRefundExample.Criteria criteria = fallintoRefundExample.or().andStatusEqualTo(2);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)){
            criteria.andBalance_cycleLike("%"+balanceDate+"%");
        }
        if(refundId !=null || !StringUtils.isEmpty(refundId)){
            criteria.andRefund_idEqualTo(refundId);
        }
        Page<FallintoRefund> page = fallintoRefundService.selectByExample(fallintoRefundExample, p, ps);
        List<FallintoRefund> fallintoRefundList = page.getList();

        List<Long> idList=new ArrayList<>();
        fallintoRefundList.stream().forEach(fallintoRefund -> {
            idList.add(fallintoRefund.getRefund_id());
        });

        List<Refund> refunds =new ArrayList<>();
        if(!idList.isEmpty()){
            RefundExample refundExample = new RefundExample();
            refundExample.or().andRefund_idIn(idList);
            refunds = refundService.selectByExample(refundExample);
        }

        model.addAttribute("refunds",refunds);
        model.addAttribute("fallintoRefundList",fallintoRefundList);
        model.addAttribute("page",page);

        return "balance/refund_list";
    }
    /**
     * 导出（订单已完成待结算）
     */
    @ResponseBody
    @RequestMapping("/exportRefund")
    public Object exportRefund(int status,HttpServletRequest req, Authentication authentication){
        String str = req.getParameter("params")==null?"":req.getParameter("params");
        String username = authentication.getName();
        long id = IdGenerator.generateId();
        FallintoRefundExample fallintoRefundExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, FallintoRefundExample.class);
        fallintoRefundExample.or().andStatusEqualTo(status);
        exportRefundBalanceAndToOss.setFallintoRefundExample(fallintoRefundExample);
        exportRefundBalanceAndToOss.setData_download_id(id);
        exportRefundBalanceAndToOss.setType(status);
        //生成excel并将之上传到阿里云OSS
        completionService.submit(exportRefundBalanceAndToOss);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
        return result;
    }


    /***
     * 赔偿待结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @return
     */
    @RequestMapping("/compensation_waiting_list")
    public String compensation_waiting_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                      String balanceDate){
        FallintoCompensationExample fallintoCompensationExample = new FallintoCompensationExample();
        FallintoCompensationExample.Criteria criteria = fallintoCompensationExample.or().andStatusEqualTo(1);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)) {
            criteria.andBalance_cycleLike("%" + balanceDate + "%");
        }
        Page<FallintoCompensation> page = fallintoCompensationService.selectByExample(fallintoCompensationExample, p, ps);
        List<FallintoCompensation> fallintoCompensationList = page.getList();

        model.addAttribute("fallintoCompensationList",fallintoCompensationList);
        model.addAttribute("page",page);
        return "balance/compensation_waiting_list";
    }

    /**
     * 赔偿挂起
     * @param fallinto_compensation_id
     * @param authentication
     * @return
     */
    @RequestMapping("/compensationHangUp/{fallinto_compensation_id}")
    @ResponseBody
    @Transactional
    public Object compensationHangUp(@PathVariable(value = "fallinto_compensation_id") Long fallinto_compensation_id,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[compensationHangUp] U[{}] ,params fallinto_compensation_id:{}",
                users.getUser_id(),fallinto_compensation_id);
        FallintoCompensation fallintoCompensation = this.fallintoCompensationService.selectByPrimaryKey(fallinto_compensation_id);
        fallintoCompensation.setStatus(3);
        int num = this.fallintoCompensationService.updateByPrimaryKeySelective(fallintoCompensation);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("挂起%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[compensationHangUp] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }


    /**
     * 赔偿挂起列表
     * @param p
     * @param ps
     * @param model
     * @param OrderId
     * @param compensation_id
     * @return
     */
    @RequestMapping("/compensation_hangup_list")
    public String compensation_hangup_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                     String OrderId,Long compensation_id){
        FallintoCompensationExample fallintoCompensationExample = new FallintoCompensationExample();
        FallintoCompensationExample.Criteria criteria = fallintoCompensationExample.or().andStatusEqualTo(3);
        if(OrderId !=null || !StringUtils.isEmpty(OrderId)) {
            criteria.andBalance_cycleLike("%" + OrderId + "%");
        }
        if(compensation_id !=null || !StringUtils.isEmpty(compensation_id)) {
            criteria.andCompensation_idEqualTo(compensation_id);
        }

        Page<FallintoCompensation> page = fallintoCompensationService.selectByExample(fallintoCompensationExample, p, ps);
        List<FallintoCompensation> fallintoCompensationList = page.getList();

        model.addAttribute("fallintoCompensationList",fallintoCompensationList);
        model.addAttribute("page",page);
        return "balance/compensation_hangup_list";
    }


    /**
     * 查找赔偿结算最大结算期
     * @param cycle
     * @param authentication
     * @return
     */
    @RequestMapping("/selectCycleMaxCompensation")
    @ResponseBody
    public Object selectCycleMaxCompensation(String cycle,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        String balance_cycle=this.fallintoCompensationService.selectMaxBalanceCycle();
        Boolean flag=true;
        if(Integer.parseInt(cycle)>Integer.parseInt(balance_cycle)){
            flag=false;
        }
        return flag;
    }

    /**
     * 赔偿完成结算
     * @param id
     * @param cycle
     * @param pageNo
     * @param authentication
     * @return
     */
    @RequestMapping("/updCycleCompensation")
    @Transactional
    public String updCycleCompensation(Long id,String cycle,int pageNo,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[updCycleCompensation] U[{}] ,params id:{}",
                users.getUser_id(),id);
        FallintoCompensation fallintoCompensation = this.fallintoCompensationService.selectByPrimaryKey(id);
        fallintoCompensation.setStatus(2);
        fallintoCompensation.setBalance_cycle(cycle);
        int num = this.fallintoCompensationService.updateByPrimaryKeySelective(fallintoCompensation);
        logger.info("M[balance] F[updCycleCompensation] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return "redirect:/balance/compensation_hangup_list?p="+pageNo;
    }


    /**
     * 赔偿已结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @param orderId
     * @return
     */
    @RequestMapping("/compensation_list")
    public String compensation_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                    String balanceDate,String orderId){
        FallintoCompensationExample fallintoCompensationExample = new FallintoCompensationExample();
        FallintoCompensationExample.Criteria criteria = fallintoCompensationExample.or().andStatusEqualTo(2);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)){
            criteria.andBalance_cycleLike("%"+balanceDate+"%");
        }
        if(orderId !=null || !StringUtils.isEmpty(orderId)){
            criteria.andPay_order_idLike("%"+orderId+"%");
        }

        Page<FallintoCompensation> page = fallintoCompensationService.selectByExample(fallintoCompensationExample, p, ps);
        List<FallintoCompensation> fallintoCompensationList = page.getList();

        model.addAttribute("fallintoCompensationList",fallintoCompensationList);
        model.addAttribute("page",page);
        return "balance/compensation_list";
    }

    /**
     * 赔偿导出
     * @param status
     * @param req
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/exportCompensation")
    public Object exportCompensation(int status,HttpServletRequest req, Authentication authentication){
        String str = req.getParameter("params")==null?"":req.getParameter("params");
        String username = authentication.getName();
        long id = IdGenerator.generateId();
        FallintoCompensationExample fallintoCompensationExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, FallintoCompensationExample.class);
        fallintoCompensationExample.or().andStatusEqualTo(status);
        exportCompensationBalanceAndToOss.setFallintoCompensationExample(fallintoCompensationExample);
        exportCompensationBalanceAndToOss.setData_download_id(id);
        exportCompensationBalanceAndToOss.setType(status);
        //生成excel并将之上传到阿里云OSS
        completionService.submit(exportCompensationBalanceAndToOss);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
        return result;
    }


    /**
     * 扣款待结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @return
     */
    @RequestMapping("/deduct_money_waiting_list")
    public String deduct_money_waiting_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                            String balanceDate){

        FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
        FallintoDeductMoneyExample.Criteria criteria = fallintoDeductMoneyExample.or().andStatusEqualTo(1);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)) {
            criteria.andBalance_cycleLike("%" + balanceDate + "%");
        }
        Page<FallintoDeductMoney> page = fallintoDeductMoneyService.selectByExample(fallintoDeductMoneyExample, p, ps);
        List<FallintoDeductMoney> fallintoDeductMoneyList = page.getList();
        model.addAttribute("fallintoDeductMoneyList",fallintoDeductMoneyList);
        model.addAttribute("page",page);
        return "balance/deduct_money_waiting_list";
    }


    /**
     * 扣款挂起
     * @param fallinto_deduct_money_id
     * @param authentication
     * @return
     */
    @RequestMapping("/deductMoneyHangUp/{fallinto_deduct_money_id}")
    @ResponseBody
    @Transactional
    public Object deductMoneyHangUp(@PathVariable(value = "fallinto_deduct_money_id") Long fallinto_deduct_money_id,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[deductMoneyHangUp] U[{}] ,params fallinto_deduct_money_id:{}",
                users.getUser_id(),fallinto_deduct_money_id);
        FallintoDeductMoney fallintoDeductMoney = this.fallintoDeductMoneyService.selectByPrimaryKey(fallinto_deduct_money_id);
        fallintoDeductMoney.setStatus(3);
        int num = this.fallintoDeductMoneyService.updateByPrimaryKeySelective(fallintoDeductMoney);

        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("挂起%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[deductMoneyHangUp] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }


    /**
     * 扣款挂起列表
     * @param p
     * @param ps
     * @param model
     * @param OrderId
     * @param deduct_money_id
     * @return
     */
    @RequestMapping("/deduct_money_hangup_list")
    public String deduct_money_hangup_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                           String OrderId,Long deduct_money_id){
        FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
        FallintoDeductMoneyExample.Criteria criteria = fallintoDeductMoneyExample.or().andStatusEqualTo(3);
        if(OrderId !=null || !StringUtils.isEmpty(OrderId)) {
            criteria.andPay_order_idLike("%" + OrderId + "%");
        }
        if(deduct_money_id !=null || !StringUtils.isEmpty(deduct_money_id)) {
            criteria.andDeduct_money_idEqualTo(deduct_money_id);
        }
        Page<FallintoDeductMoney> page = fallintoDeductMoneyService.selectByExample(fallintoDeductMoneyExample, p, ps);
        List<FallintoDeductMoney> fallintoDeductMoneyList = page.getList();
        model.addAttribute("fallintoDeductMoneyList",fallintoDeductMoneyList);
        model.addAttribute("page",page);
        return "balance/deduct_money_hangup_list";
    }

    /**
     * 查找扣款结算最大结算期
     * @param cycle
     * @param authentication
     * @return
     */
    @RequestMapping("/selectCycleMaxDeductMoney")
    @ResponseBody
    public Object selectCycleMaxDeductMoney(String cycle,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        String balance_cycle=this.fallintoDeductMoneyService.selectMaxBalanceCycle();
        Boolean flag=true;
        if(Integer.parseInt(cycle)>Integer.parseInt(balance_cycle)){
            flag=false;
        }
        return flag;
    }

    /**
     * 扣款已结算
     * @param id
     * @param cycle
     * @param pageNo
     * @param authentication
     * @return
     */
    @RequestMapping("/updCycleDeductMoney")
    @Transactional
    public String updCycleDeductMoney(Long id,String cycle,int pageNo,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[updCycleDeductMoney] U[{}] ,params id:{}",
                users.getUser_id(),id);
        FallintoDeductMoney fallintoDeductMoney = this.fallintoDeductMoneyService.selectByPrimaryKey(id);
        fallintoDeductMoney.setStatus(2);
        fallintoDeductMoney.setBalance_cycle(cycle);
        int num = this.fallintoDeductMoneyService.updateByPrimaryKeySelective(fallintoDeductMoney);
        logger.info("M[balance] F[updCycleDeductMoney] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return "redirect:/balance/deduct_money_hangup_list?p="+pageNo;
    }

    /**
     * 扣款已结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @param orderId
     * @return
     */
    @RequestMapping("/deduct_money_list")
    public String deduct_money_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                    String balanceDate,String orderId){
        FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
        FallintoDeductMoneyExample.Criteria criteria = fallintoDeductMoneyExample.or().andStatusEqualTo(2);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)) {
            criteria.andBalance_cycleLike("%" + balanceDate + "%");
        }
        if(orderId !=null || !StringUtils.isEmpty(orderId)) {
            criteria.andPay_order_idLike("%" + orderId + "%");
        }
        Page<FallintoDeductMoney> page = fallintoDeductMoneyService.selectByExample(fallintoDeductMoneyExample, p, ps);
        List<FallintoDeductMoney> fallintoDeductMoneyList = page.getList();
        model.addAttribute("fallintoDeductMoneyList",fallintoDeductMoneyList);
        model.addAttribute("page",page);
        return "balance/deduct_money_list";
    }


    /**
     * 扣款导出
     * @param status
     * @param req
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/exportDeductMoney")
    public Object exportDeductMoney(int status,HttpServletRequest req, Authentication authentication){
        String str = req.getParameter("params")==null?"":req.getParameter("params");
        String username = authentication.getName();
        long id = IdGenerator.generateId();
        FallintoDeductMoneyExample fallintoDeductMoneyExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, FallintoDeductMoneyExample.class);
        fallintoDeductMoneyExample.or().andStatusEqualTo(status);
        exportDeductMoneyBalanceAndToOss.setFallintoDeductMoneyExample(fallintoDeductMoneyExample);
        exportDeductMoneyBalanceAndToOss.setData_download_id(id);
        exportDeductMoneyBalanceAndToOss.setType(status);
        //生成excel并将之上传到阿里云OSS
        completionService.submit(exportDeductMoneyBalanceAndToOss);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
        return result;
    }


    /**
     * 罚款待结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @return
     */
    @RequestMapping("/fine_money_waiting_list")
    public String fine_money_waiting_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                            String balanceDate){
        FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
        FallintoFineMoneyExample.Criteria criteria = fallintoFineMoneyExample.or().andStatusEqualTo(1);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)) {
            criteria.andBalance_cycleLike("%" + balanceDate + "%");
        }
        Page<FallintoFineMoney> page = fallintoFineMoneyService.selectByExample(fallintoFineMoneyExample, p, ps);
        List<FallintoFineMoney> fallintoFineMoneyList = page.getList();
        model.addAttribute("fallintoFineMoneyList",fallintoFineMoneyList);
        model.addAttribute("page",page);
        return "balance/fine_money_waiting_list";
    }


    /**
     * 罚款挂起
     * @param fallinto_fine_money_id
     * @param authentication
     * @return
     */
    @RequestMapping("/fineMoneyHangUp/{fallinto_fine_money_id}")
    @ResponseBody
    @Transactional
    public Object fineMoneyHangUp(@PathVariable(value = "fallinto_fine_money_id") Long fallinto_fine_money_id,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[fineMoneyHangUp] U[{}] ,params fallinto_fine_money_id:{}",
                users.getUser_id(),fallinto_fine_money_id);
        FallintoFineMoney fallintoFineMoney = fallintoFineMoneyService.selectByPrimaryKey(fallinto_fine_money_id);
        fallintoFineMoney.setStatus(3);
        int num = this.fallintoFineMoneyService.updateByPrimaryKeySelective(fallintoFineMoney);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("message", String.format("挂起%s!",num>0 ? "成功" : "失败"));
        logger.info("M[balance] F[fineMoneyHangUp] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return resultMap;
    }


    /**
     * 罚款挂起列表
     * @param p
     * @param ps
     * @param model
     * @param OrderId
     * @param fine_money_id
     * @return
     */
    @RequestMapping("/fine_money_hangup_list")
    public String fine_money_hangup_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                           String OrderId,Long fine_money_id){
        FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
        FallintoFineMoneyExample.Criteria criteria = fallintoFineMoneyExample.or().andStatusEqualTo(3);
        if(OrderId !=null || !StringUtils.isEmpty(OrderId)) {
            criteria.andPay_order_idLike("%" + OrderId + "%");
        }
        if(fine_money_id !=null || !StringUtils.isEmpty(fine_money_id)) {
            criteria.andFine_money_idEqualTo(fine_money_id);
        }
        Page<FallintoFineMoney> page = fallintoFineMoneyService.selectByExample(fallintoFineMoneyExample, p, ps);
        List<FallintoFineMoney> fallintoFineMoneyList = page.getList();
        model.addAttribute("fallintoFineMoneyList",fallintoFineMoneyList);
        model.addAttribute("page",page);
        return "balance/fine_money_hangup_list";
    }

    /**
     * 罚款获取最大结算期
     * @param cycle
     * @param authentication
     * @return
     */
    @RequestMapping("/selectCycleMaxFineMoney")
    @ResponseBody
    public Object selectCycleMaxFineMoney(String cycle,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        String balance_cycle=this.fallintoFineMoneyService.selectMaxBalanceCycle();
        Boolean flag=true;
        if(Integer.parseInt(cycle)>Integer.parseInt(balance_cycle)){
            flag=false;
        }
        return flag;
    }


    /**
     * 罚款已结算
     * @param id
     * @param cycle
     * @param pageNo
     * @param authentication
     * @return
     */
    @RequestMapping("/updCycleFineMoney")
    @Transactional
    public String updCycleFileMoney(Long id,String cycle,int pageNo,Authentication authentication){
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[balance] F[updCycleFileMoney] U[{}] ,params id:{}",
                users.getUser_id(),id);
        FallintoFineMoney fallintoFineMoney = this.fallintoFineMoneyService.selectByPrimaryKey(id);
        fallintoFineMoney.setStatus(2);
        fallintoFineMoney.setBalance_cycle(cycle);
        int num = this.fallintoFineMoneyService.updateByPrimaryKeySelective(fallintoFineMoney);
        logger.info("M[balance] F[updCycleFileMoney] U[{}] ,execute result:{}",
                users.getUser_id(),num>0 ? "成功" : "失败");
        return "redirect:/balance/fine_money_hangup_list?p="+pageNo;
    }

    /**
     * 罚款已结算列表
     * @param p
     * @param ps
     * @param model
     * @param balanceDate
     * @param orderId
     * @return
     */
    @RequestMapping("/fine_money_list")
    public String fine_money_list(@RequestParam(defaultValue ="1" ) int p,@RequestParam(value="ps",defaultValue="10") Integer ps,Model model,
                                    String balanceDate,String orderId){
        FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
        FallintoFineMoneyExample.Criteria criteria = fallintoFineMoneyExample.or().andStatusEqualTo(2);
        if(balanceDate !=null || !StringUtils.isEmpty(balanceDate)) {
            criteria.andBalance_cycleLike("%" + balanceDate + "%");
        }
        if(orderId !=null || !StringUtils.isEmpty(orderId)) {
            criteria.andPay_order_idEqualTo(orderId);
        }
        Page<FallintoFineMoney> page = fallintoFineMoneyService.selectByExample(fallintoFineMoneyExample, p, ps);
        List<FallintoFineMoney> fallintoFineMoneyList = page.getList();
        model.addAttribute("fallintoFineMoneyList",fallintoFineMoneyList);
        model.addAttribute("page",page);
        return "balance/fine_money_list";
    }

    /**
     * 罚款导出
     * @param status
     * @param req
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/exportFineMoney")
    public Object exportFineMoney(int status,HttpServletRequest req, Authentication authentication){
        String str = req.getParameter("params")==null?"":req.getParameter("params");
        String username = authentication.getName();
        long id = IdGenerator.generateId();
        FallintoFineMoneyExample fallintoFineMoneyExample = orderService.generateDownloadTaskAndPottingParam(str, username, id, FallintoFineMoneyExample.class);
        fallintoFineMoneyExample.or().andStatusEqualTo(status);
        exportFineMoneyBalanceAndToOss.setFallintoFineMoneyExample(fallintoFineMoneyExample);
        exportFineMoneyBalanceAndToOss.setData_download_id(id);
        exportFineMoneyBalanceAndToOss.setType(status);
        //生成excel并将之上传到阿里云OSS
        completionService.submit(exportFineMoneyBalanceAndToOss);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
        return result;
    }
}
