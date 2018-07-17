package com.aobei.trainconsole.controller;

import com.aobei.train.model.*;
import com.aobei.train.service.ChannelService;
import com.aobei.train.service.ChannelTypeService;
import com.aobei.train.service.UsersService;
import custom.bean.Status;
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
 * Created by adminL on 2018/6/13.
 */
@Controller
@RequestMapping("/channelType")
public class ChannelTypeController {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private UsersService usersService;

    private static Logger logger = LoggerFactory.getLogger(ChannelTypeController.class);


    /**
     * 渠道类型展示列表页
     * @param model
     * @return
     */
    @RequestMapping("/channelType_list")
    public String channelType_list(Model model){
        List<ChannelType> channelTypes = channelTypeService.selectByExample(null);
        model.addAttribute("channelTypes",channelTypes);
        return "channel/channelType_list";
    }

    /**
     * 跳转到添加页
     * @return
     */
    @RequestMapping("/channelType_goto_add")
    public String channelType_goto_add(){
        return "channel/channelType_add";
    }

    /**
     * 添加渠道类型
     * @param channelType
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/channelType_add")
    public Object channelType_add(ChannelType channelType,Authentication authentication){
        Integer i = channelTypeService.xInsertChannelType(channelType);
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        map.put("result",String.format("添加渠道类型%s", i > 0 ? "成功":"失败"));
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channelType] F[add_channelType] U[{}] param[ChannelType:{}];  result:{}"
                ,users.getUser_id(),channelType,String.format("添加渠道类型%s", i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     * 删除渠道类型
     * @param channel_type_id
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/channelType_del")
    public Object channelType_del(Integer channel_type_id,Authentication authentication){
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        Integer i = channelTypeService.xDeleteChannelType(channel_type_id);
        map.put("result",String.format("删除类型%s", i > 0 ? "成功":"失败，已有渠道关联此类型 "));
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channelType] F[channelType_del] U[{}] param[channel_type_id:{}];  result:{}"
                ,users.getUser_id(),channel_type_id,String.format("删除渠道类型%s", i > 0 ? "成功":"失败，已有渠道关联此类型"));
        return map;
    }

    /**
     * 跳转到编辑页
     * @param channel_type_id
     * @param model
     * @return
     */
    @RequestMapping("/channelType_goto_edit")
    public String channelType_goto_edit(Integer channel_type_id,Model model){
        model.addAttribute("channelType",channelTypeService.selectByPrimaryKey(channel_type_id));
        return "channel/channelType_edit";
    }

    /**
     * 编辑保存数据方法
     * @param channelType
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/channelType_edit")
    public Object channelType_edit(ChannelType channelType,Authentication authentication){
        Integer i = channelTypeService.xUpdateChannelType(channelType);
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        map.put("result",String.format("修改渠道类型%s", i > 0 ? "成功":"失败"));
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channelType] F[channelType_edit] U[{}] param[ChannelType:{}];  result:{}"
                ,users.getUser_id(),channelType,String.format("修改渠道类型%s", i > 0 ? "成功":"失败"));
        return map;
    }


}
