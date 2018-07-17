package com.aobei.trainconsole.controller;


import com.aobei.train.IdGenerator;
import com.aobei.train.model.Insurance;
import com.aobei.train.model.InsuranceExample;
import com.aobei.train.model.Users;
import com.aobei.train.service.InsuranceService;
import com.aobei.train.service.UsersService;
import com.github.liyiorg.mbg.bean.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/insurance")
public class InsuranceController {

	private static final Logger logger= LoggerFactory.getLogger(InsuranceController.class);

	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private UsersService usersService;

	/**
	 * 保险列表
	 * @return
	 */
	@RequestMapping("/insuranceList")
	@Transactional
	public String insuranceList(Model model, @RequestParam(value="p",defaultValue="1") Integer p, @RequestParam(value="ps",defaultValue="10") Integer ps){
		//从数据库查询
        InsuranceExample insuranceExample = new InsuranceExample();
        insuranceExample.setOrderByClause(InsuranceExample.C.create_datetime+" desc");
        Page<Insurance> insurancePage = this.insuranceService.selectByExample(insuranceExample, p, ps);
        List<Insurance> list = insurancePage.getList();
        //删除最后一页只有一条数据时，跳转到上一页
        if(list.size() == 0 & p>1) {
            insurancePage = this.insuranceService.selectByExample(insuranceExample,p-1,ps);
            list = insurancePage.getList();
        }
        model.addAttribute("insurances",list);
        model.addAttribute("page",insurancePage);
        return "insurance/insurance_list";
	}

    /**
     * 跳转到新增页面
     * @return
     */
    @RequestMapping("/insuranceAddShow")
    @Transactional
	public String insuranceAddShow(@RequestParam(value="p") int p,Model model){
        model.addAttribute("pageNo",p);
	    return "insurance/insurance_add";
    }

    /**
     * 添加保险
     * @return
     */
    @RequestMapping("/addInsurance")
    @Transactional
    public String addInsurance(Insurance insurance,String startDatetime,String endDatetime,Authentication authentication) {
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[insurance] F[addInsurance] U[{}] ,params insurance:{}, startDatetime:{},endDatetime:{}",
                users.getUser_id(),insurance,startDatetime,endDatetime);
        int num = this.insuranceService.xAddInsurance(insurance, startDatetime, endDatetime);
        logger.info("M[insurance] F[addInsurance] U[{}] ,execute result:{}",
                users.getUser_id(),num>0?"成功":"失败");

        return "redirect:/insurance/insuranceList";
    }


    /**
     * 跳转到编辑页面
     * @param insurance_id
     * @param p
     * @param model
     * @return
     */
    @RequestMapping("/insuranceEditShow")
    @Transactional
    public String insuranceEditShow(Long insurance_id, int p, Model model){
        Insurance insurance = this.insuranceService.selectByPrimaryKey(insurance_id);
        model.addAttribute("insurance",insurance);
        model.addAttribute("pageNo",p);
        return "insurance/insurance_edit";
    }


    /**
     * 编辑保险
     * @param insurance
     * @param startDatetime
     * @param endDatetime
     * @return
     */
    @RequestMapping("/editInsurance")
    @Transactional
    public String editInsurance(Insurance insurance,String startDatetime,String endDatetime,Authentication authentication) {

        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[insurance] F[editInsurance] U[{}] ,params insurance:{}, startDatetime:{},endDatetime:{}",
                users.getUser_id(),insurance,startDatetime,endDatetime);
        int num = this.insuranceService.xEditInsurance(insurance, startDatetime, endDatetime);
        logger.info("M[insurance] F[editInsurance] U[{}] ,execute result:{}",
                users.getUser_id(),num>0?"成功":"失败");

        return "redirect:/insurance/insuranceList";
    }
	
}
