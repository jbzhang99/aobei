package com.aobei.trainconsole.controller;


import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.FallintoService;
import com.aobei.train.service.InsuranceService;
import com.aobei.train.service.UsersService;
import com.github.liyiorg.mbg.bean.Page;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/fallinto")
public class FallintoController {

	private static final Logger logger= LoggerFactory.getLogger(FallintoController.class);

	@Autowired
	private FallintoService fallintoService;

    @Autowired
    private UsersService usersService;

    @RequestMapping("/addFallinto")
    @ResponseBody
    @Transactional(timeout = 5)
    public String addFallinto(HttpServletRequest request, Authentication authentication) {
        String str = request.getParameter("result");
        List<Fallinto> fallintos = JSONObject.parseArray(str, Fallinto.class);
        Fallinto fallinto=new Fallinto();
        if(fallintos.size()>0){
            fallinto=fallintos.get(1);
        }
        fallinto.setFallinto_id(IdGenerator.generateId());
        fallinto.setCreate_datetime(new Date());
        fallinto.setDeleted(0);
        fallinto.setActived(0);
        this.fallintoService.insert(fallinto);
        //获取登录的用户id
        /*Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[fallinto] F[addFallinto] U[{}] ,params fallinto:{}",
                users.getUser_id(),fallinto);
        fallinto.setFallinto_id(IdGenerator.generateId());
        fallinto.setCreate_datetime(new Date());
        fallinto.setDeleted(0);
        int num = this.fallintoService.insert(fallinto);
        logger.info("M[fallinto] F[addFallinto] U[{}] ,execute result:{}",
                users.getUser_id(),num>0?"成功":"失败");*/
        return "redirect:/balance/balance_tactics_list";
    }


	/**
	 * 策略列表
	 * @return
	 */
    /*@RequestMapping("/fallintoList")
    @Transactional
    public String fallintoList(Model model, @RequestParam(value="p",defaultValue="1") Integer p, @RequestParam(value="ps",defaultValue="10") Integer ps){
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
    }*/
	@RequestMapping("/fallintoList")
	public String fallintoList(Model model, @RequestParam(value="p",defaultValue="1") Integer p, @RequestParam(value="ps",defaultValue="10") Integer ps){
		//从数据库查询
        FallintoExample fallintoExample = new FallintoExample();
        fallintoExample.setOrderByClause(FallintoExample.C.create_datetime+" desc");
        Page<Fallinto> fallintoPage = this.fallintoService.selectByExample(fallintoExample, p, ps);
        List<Fallinto> list = fallintoPage.getList();
        //删除最后一页只有一条数据时，跳转到上一页
        if(list.size() == 0 & p>1) {
            fallintoPage = this.fallintoService.selectByExample(fallintoExample,p-1,ps);
            list = fallintoPage.getList();
        }
        model.addAttribute("fallintos",list);
        model.addAttribute("page",fallintoPage);
        return "fallinto/fallinto_list";
	}

    /**
     * 跳转到新增页面
     * @return
     */
    @RequestMapping("/fallintoAddShow")
    @Transactional
	public String fallintoAddShow(int p,Model model){
        model.addAttribute("pageNo",p);
	    return "fallinto/fallinto_add";
    }

    /**
     * 添加策略
     * @return
     */
    /*@RequestMapping("/addFallinto")
    @Transactional
    public String addFallinto(Fallinto fallinto,Authentication authentication) {
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[fallinto] F[addFallinto] U[{}] ,params fallinto:{}",
                users.getUser_id(),fallinto);
        fallinto.setFallinto_id(IdGenerator.generateId());
        fallinto.setCreate_datetime(new Date());
        fallinto.setDeleted(0);
        int num = this.fallintoService.insert(fallinto);
        logger.info("M[fallinto] F[addFallinto] U[{}] ,execute result:{}",
                users.getUser_id(),num>0?"成功":"失败");

        return "redirect:/fallinto/fallintoList";
    }*/


    /**
     * 跳转到编辑页面
     * @param p
     * @param model
     * @return
     */
    @RequestMapping("/fallintoEditShow")
    public String fallintoEditShow(Long fallinto_id, int p, Model model){
        Fallinto fallinto = this.fallintoService.selectByPrimaryKey(fallinto_id);
        model.addAttribute("fallinto",fallinto);
        model.addAttribute("pageNo",p);
        return "fallinto/fallinto_edit";
    }


    /**
     * 编辑保险
     * @return
     */
    @RequestMapping("/editFallinto")
    @Transactional(timeout = 5)
    public String editFallinto(Fallinto fallinto,Authentication authentication) {
        //获取登录的用户id
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[fallinto] F[editFallinto] U[{}] ,params fallinto:{}",
                users.getUser_id(),fallinto);
        int num = this.fallintoService.updateByPrimaryKeySelective(fallinto);
        logger.info("M[fallinto] F[editFallinto] U[{}] ,execute result:{}",
                users.getUser_id(),num>0?"成功":"失败");

        return "redirect:/fallinto/fallintoList";
    }
	
}
