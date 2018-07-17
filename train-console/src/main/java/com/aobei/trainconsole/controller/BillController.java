package com.aobei.trainconsole.controller;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.BillBatchService;
import com.aobei.train.service.BillService;
import com.aobei.train.service.OrderService;
import com.aobei.train.service.UsersService;
import com.aobei.trainconsole.util.ExportBillAndToOss;
import com.github.liyiorg.mbg.bean.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;

/**
 * Created by mr_bl on 2018/5/18.
 */
@Controller
@RequestMapping("/billmanager")
public class BillController {

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private BillBatchService billBatchService;

    @Autowired
    private BillService billService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CompletionService<Integer> completionService;

    @Autowired
    private ExportBillAndToOss exportBillAndToOss;

    @Autowired
    private UsersService usersService;

    /**
     * 跳转到对账批次列表页
     * @param map
     * @param p
     * @param ps
     * @param bill_date
     * @return
     */
    @RequestMapping("/goto_bill_batch_list")
    public String goto_bill_batch_list(Model map,
                                       @RequestParam(defaultValue="1") Integer p,
                                       @RequestParam(defaultValue="10") Integer ps,
                                       @RequestParam(required = false) String bill_date){
        BillBatchExample batchExample = new BillBatchExample();
        batchExample.setOrderByClause(BillBatchExample.C.bill_datetime + " desc");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (bill_date != null && !"".equals(bill_date)){
            try {
                batchExample.or().andBill_dateEqualTo(format.parse(bill_date));
                map.addAttribute("bill_date",bill_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<BillBatch> page = billBatchService.selectByExample(batchExample, p, ps);
        List<BillBatch> bill_batchs = page.getList();
        map.addAttribute("bill_batchs",bill_batchs);
        map.addAttribute("page",page);
        return "bill/bill_batch_list";
    }

    /**
     * 跳转到对账汇总数据页面，该页面展示的都是对账成功的数据
     * @param map
     * @param p
     * @param ps
     * @param s_transaction_datetime
     * @param e_transaction_datetime
     * @param qs_create_time
     * @param qe_create_time
     * @param bill_batch_id
     * @param pay_order_id
     * @param bill_type
     * @return
     */
    @RequestMapping("/goto_normal_list")
    public String goto_normal_list(Model map,
                                   @RequestParam(defaultValue="1") Integer p,
                                   @RequestParam(defaultValue="10") Integer ps,
                                   @RequestParam(required = false) Date s_transaction_datetime,
                                   @RequestParam(required = false) Date e_transaction_datetime,
                                   @RequestParam(required = false) Date qs_create_time,
                                   @RequestParam(required = false) Date qe_create_time,
                                   @RequestParam(required = false) String bill_batch_id,
                                   @RequestParam(required = false) String pay_order_id,
                                   @RequestParam(required = false) Integer bill_type){
        //初始条件，只查询正常帐
        BillExample billExample = new BillExample();
        billExample.setOrderByClause(BillExample.C.create_datetime + " desc");
        BillExample.Criteria or = billExample.or();
        or.andBill_statusEqualTo(1);
        if (bill_batch_id != null && !"".equals(bill_batch_id)){
            or.andBill_batch_idEqualTo(bill_batch_id);
            map.addAttribute("bill_batch_id",bill_batch_id);
        }
        if (pay_order_id != null && !"".equals(pay_order_id)){
            or.andPay_order_idEqualTo(pay_order_id);
            map.addAttribute("pay_order_id",pay_order_id);
        }
        if (bill_type != null){
            or.andBill_typeEqualTo(bill_type);
            map.addAttribute("bill_type",bill_type);
        }
        if (s_transaction_datetime != null && e_transaction_datetime != null){
            or.andTransaction_datetimeBetween(s_transaction_datetime,e_transaction_datetime);
            map.addAttribute("s_transaction_datetime",s_transaction_datetime);
            map.addAttribute("e_transaction_datetime",e_transaction_datetime);
        }
        if (qs_create_time != null && qe_create_time != null){
            or.andCreate_datetimeBetween(qs_create_time,qe_create_time);
            map.addAttribute("qs_create_time",qs_create_time);
            map.addAttribute("qe_create_time",qe_create_time);
        }
        Page<Bill> page = billService.selectByExample(billExample, p, ps);
        map.addAttribute("page",page);
        List<Bill> bills = page.getList();
        map.addAttribute("bills",bills);
        return "bill/bill_normal_list";
    }

    /**
     * 跳转到对账暂存池页面--长帐
     * @param map
     * @param p
     * @param ps
     * @return
     */
    @RequestMapping("/goto_cache_list")
    public String goto_cache_list(Model map,
                                  @RequestParam(defaultValue="1") Integer p,
                                  @RequestParam(defaultValue="10") Integer ps){
        BillExample billExample = new BillExample();
        billExample.setOrderByClause(BillExample.C.create_datetime + " desc");
        billExample.or().andBill_statusEqualTo(2).andBill_typeEqualTo(1);
        Page<Bill> page = billService.selectByExample(billExample, p, ps);
        map.addAttribute("page",page);
        List<Bill> bills = page.getList();
        map.addAttribute("bills",bills);
        return "bill/bill_cache_list";
    }

    /**
     * 跳转到差错列表页，缓存池对了三次以上的和金额不一致的、短帐
     * @param map
     * @param p
     * @param ps
     * @param qs_create_time
     * @param qe_create_time
     * @param bill_batch_id
     * @param pay_order_id
     * @param bill_status
     * @return
     */
    @RequestMapping("/goto_error_list")
    public String goto_error_list(Model map,
                                  @RequestParam(defaultValue="1") Integer p,
                                  @RequestParam(defaultValue="10") Integer ps,
                                  @RequestParam(required = false) Date qs_create_time,
                                  @RequestParam(required = false) Date qe_create_time,
                                  @RequestParam(required = false) String bill_batch_id,
                                  @RequestParam(required = false) String pay_order_id,
                                  @RequestParam(required = false) Integer bill_status){
        //初始条件，只查询差错帐
        BillExample billExample = new BillExample();
        billExample.setOrderByClause(BillExample.C.create_datetime + " desc");
        BillExample.Criteria or = billExample.or();
        or.andBill_statusNotEqualTo(1).andBill_typeEqualTo(2);
        if (bill_batch_id != null && !"".equals(bill_batch_id)){
            or.andBill_batch_idEqualTo(bill_batch_id);
            map.addAttribute("bill_batch_id",bill_batch_id);
        }
        if (pay_order_id != null && !"".equals(pay_order_id)){
            or.andPay_order_idEqualTo(pay_order_id);
            map.addAttribute("pay_order_id",pay_order_id);
        }
        if (bill_status != null){
            or.andBill_statusEqualTo(bill_status);
            map.addAttribute("bill_status",bill_status);
        }
        if (qs_create_time != null && qe_create_time != null){
            or.andCreate_datetimeBetween(qs_create_time,qe_create_time);
            map.addAttribute("qs_create_time",qs_create_time);
            map.addAttribute("qe_create_time",qe_create_time);
        }
        Page<Bill> page = billService.selectByExample(billExample, p, ps);
        map.addAttribute("page",page);
        List<Bill> bills = page.getList();
        map.addAttribute("bills",bills);
        return "bill/bill_error_list";
    }

    /**
     * 处理对账单，更新对账单平账金额等
     * @param bill
     * @return
     */
    @ResponseBody
    @RequestMapping("/process_bill")
    @Transactional(timeout = 5)
    public Object process_bill(Bill bill,Authentication authentication){
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        Map<String, String> map = new HashMap<>();
        BillExample billExample = new BillExample();
        if (bill != null){
            if (!"".equals(bill.getRefund_id_three()) && bill.getRefund_id_three() != null){
                billExample.or().andRefund_id_threeEqualTo(bill.getRefund_id_three());
            } else {
                billExample.or().andTransaction_idEqualTo(bill.getTransaction_id()).andRefund_id_threeIsNull();
            }
            Bill bill_db = DataAccessUtils.singleResult(billService.selectByExample(billExample));
            bill.setBill_id(bill_db.getBill_id());
            bill.setBill_status(1);
            bill.setCreate_datetime(new Date());
            logger.info("M[bill] F[process_bill] U[{}] , params param:{} .",
                    users.getUser_id(),bill);
            int i = billService.updateByPrimaryKeySelective(bill);
            logger.info("M[bill] F[process_bill] U[{}] , execute result{} .",
                    users.getUser_id(),String.format("处理差错单%s!", i > 0 ? "成功" : "失败"));
            map.put("msg", String.format("处理差错单%s!", i > 0 ? "成功" : "失败"));
        }else{
            map.put("error","您提交的信息可能出现了一些问题！");
        }
        return map;
    }

    /**
     * 导出对账汇总数据
     * @param request
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/exportExcel")
    public Object exportExcel(HttpServletRequest request, Authentication authentication) {
        // 获取前台传到的json数据
        String str = request.getParameter("params");
        String username = authentication.getName();
        long id = IdGenerator.generateId();
        BillExample billExample = orderService.generateDownloadTaskAndPottingParam(str,username,id,BillExample.class);
        exportBillAndToOss.setBillExample(billExample);
        exportBillAndToOss.setData_download_id(id);
        // 生成excel并将之上传到阿里云OSS
        completionService.submit(exportBillAndToOss);
        Map<String, String> result = new HashMap<>();
        result.put("msg", "已新建导出任务，请到导出任务列表查看详情！");
        return result;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
