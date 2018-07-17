package com.aobei.trainconsole.controller;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.GenerateQRCode;
import com.aobei.trainconsole.util.PoiExcelExport;
import com.gexin.fastjson.JSON;
import custom.bean.ChannelAndType;
import custom.bean.Status;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.WxaAPI;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.wxa.Getwxacodeunlimit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by adminL on 2018/6/19.
 */
@Controller
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private AppPackService appPackService;
    @Autowired
    private WxAppService wxAppService;

    private static Logger logger = LoggerFactory.getLogger(ChannelController.class);


    /**
     * 渠道信息列表页
     * @param model
     * @return
     */
    @RequestMapping("/channel_list")
    public String channel_list(Model model, @RequestParam(defaultValue = "0",required = false)Integer channel_type_id){
        ChannelExample channelExample = new ChannelExample();
        ChannelExample.Criteria or = channelExample.or();
        if(channel_type_id!=null && channel_type_id!=0 ){
            or.andChannel_type_idEqualTo(channel_type_id);
        }
            or.andDeletedEqualTo(Status.DeleteStatus.no.value);
        List<Channel> channels = channelService.selectByExample(channelExample);
        //所有的渠道类型
        List<ChannelType> channelTypes = channelTypeService.selectByExample(null);
        model.addAttribute("channelTypes",channelTypes);
        model.addAttribute("channels",channels);
        model.addAttribute("channel_type_id",channel_type_id);
        return "channel/channel_list";
    }

    /**
     * 跳转到添加页面
     * @param model
     * @return
     */
    @RequestMapping("/channel_goto_add")
    public String channel_goto_add(Model model){
        List<ChannelType> channelTypes = channelTypeService.selectByExample(null);
        model.addAttribute("channelTypes",channelTypes);
        return "channel/channel_add";
    }

    /**
     * 检查code码是否重复
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("/check_code")
    public Object check_code(String code){
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        ChannelExample channelExample = new ChannelExample();
        channelExample.or()
                .andCodeEqualTo(code);
        long l = channelService.countByExample(channelExample);
        if(l!=0){
            map.put("result","已有此code码，请重新输入！");
            return map;
        }else{
            map.put("result","0");
            return map;
        }
    }

    /**
     * 添加渠道信息
     * @param channel
     * @param authentication
     * @return
     */
    @ResponseBody
    @RequestMapping("/channel_add")
    public Object channel_add(Channel channel,Authentication authentication){
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        Integer i = channelService.xInsertChannel(channel);
        /*if(i==0){
            map.put("result","添加渠道信息失败,已有此code码！");
            return map;
        }*/
        map.put("result",String.format("添加渠道信息%s",i > 0?"成功":"失败"));
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channel] F[channel_add] U[{}] param[channel:{}];  result:{}"
                ,users.getUser_id(),channel,String.format("添加渠道信息%s", i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     * 删除渠道信息
     * @param channel_id
     * @return
     */
    @ResponseBody
    @RequestMapping("/channel_del")
    public Object channel_del(Integer channel_id,Authentication authentication){
        Integer i = channelService.xDeleteChannel(channel_id);
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        map.put("result",String.format("删除渠道信息%s",i > 0?"成功":"失败"));
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channel] F[channel_del] U[{}] param[channel_id:{}];  result:{}"
                ,users.getUser_id(),channel_id,String.format("删除渠道信息%s", i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     * 跳转到渠道信息编辑页
     * @param model
     * @param channel_id
     * @return
     */
    @RequestMapping("/channel_goto_edit")
    public String channel_goto_edit(Model model,Integer channel_id){
        List<ChannelType> channelTypes = channelTypeService.selectByExample(null);
        Channel channel = channelService.selectByPrimaryKey(channel_id);
        model.addAttribute("channelTypes",channelTypes);
        model.addAttribute("channel",channel);
        return "channel/channel_edit";
    }

    /**
     * 提交编辑的渠道信息
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping("/channel_edit")
    public Object channel_edit(Channel channel,Authentication authentication){
        Integer i = channelService.xUpdateChannel(channel);
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        /*if(i==0){
            map.put("result","修改渠道信息失败,已有此code码！");
            return map;
        }*/
        map.put("result",String.format("修改渠道信息%s",i > 0?"成功":"失败"));
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channel] F[channel_edit] U[{}] param[channel:{}];  result:{}"
                ,users.getUser_id(),channel,String.format("修改渠道信息%s", i > 0 ? "成功":"失败"));
        return map;
    }

    /**
     * 导出功能
     * @param channel_type_id
     * @param response
     */
    @RequestMapping("/export")
    public void export(Integer channel_type_id,HttpServletResponse response,Authentication authentication){
        List<ChannelAndType> list = channelService.xExport(channel_type_id);
        PoiExcelExport export = new PoiExcelExport(response,"渠道信息","sheet1");
        String titleName[] = {"渠道类型","渠道名称","code码"};
        String titleColumn[]  = {"channel_type_name","channel_name","code"};
        int titleSize[] = {20,20,20};
        export.wirteExcel(titleColumn, titleName, titleSize, list);
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[channel] F[export] U[{}] condition(param)[channel_type_id:{}];  result:{}"
                ,users.getUser_id(),channel_type_id,"导出渠道信息数据");
    }

    /**
     * 加载各端数据
     * @param group_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPort")
    public Object getPort(String group_name){
        List<AppPack> list = appPackService.xGetPort(group_name);
        HashMap map = new HashMap();
        map.put("list",list);
        return map;
    }

    /**
     * 下载打包后的二维码图片数据
     * @param app 应用端id（多个）
     * @param channel_id 渠道id
     * @param size 二维码大小
     * @param response
     */
    @RequestMapping("/download_QRcode")
    public void download_QRcode(@RequestParam(value="app") String app,
                                @RequestParam(value="channel_id") Integer channel_id,
                                @RequestParam(value="size") int size,
                                HttpServletResponse response,
                                Authentication authentication){
        Channel channel = channelService.selectByPrimaryKey(channel_id);
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        Map<String, BufferedImage> map = new HashMap<String, BufferedImage>();

        List<String> apps = JSON.parseArray(app, String.class);
        for (String val:apps) {
            //微信小程序链接
            if(val.contains("wx")){
                WxAppExample wxAppExample = new WxAppExample();
                wxAppExample.or()
                        .andPathEqualTo(val);
                WxApp wxApp = DataAccessUtils.singleResult(wxAppService.selectByExample(wxAppExample));
                Token token = TokenAPI.token(wxApp.getApp_id(), wxApp.getApp_secret());
                Getwxacodeunlimit me = new Getwxacodeunlimit();
                //me.setPage("pages/index");		//设置页面
                me.setScene("channel="+channel.getCode());	//设置渠道名称
                me.setWidth(size);
                BufferedImage bi = WxaAPI.getwxacodeunlimit(token.getAccess_token(), me);
                if(size==200){
                    //转换图片大小
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = Thumbnails.of(bi)
                                .size(200, 200)
                                .asBufferedImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    map.put(val,bufferedImage);
                }else{
                    map.put(val,bi);
                }
                logger.info("M[channel] F[download_QRcode] U[{}] param[使用端app:{}]  result:{}"
                        ,users.getUser_id(),val,String.format("生成%s二维码图片",val));
            }
            //安卓应用链接
            if(Character.toString(val.charAt(0)).equals("a")){
                //String str = "https://aobei-public.oss-cn-beijing.aliyuncs.com/app_pack/"+val+"/"+channel.getChannel_id()+".apk";
                StringBuilder str = new StringBuilder();
                str.append("https://aobei-public.oss-cn-beijing.aliyuncs.com/app_pack/");
                str.append(val);
                str.append("/");
                str.append(channel.getCode());
                str.append(".apk");
                BufferedImage a_img = GenerateQRCode.getQRcode(str.toString(), size);
                map.put(val,a_img);
                logger.info("M[channel] F[download_QRcode] U[{}] param[使用端app:{}]  result:{}"
                        ,users.getUser_id(),val,String.format("生成%s二维码图片",val));
            }
            //ios应用链接
            if(Character.toString(val.charAt(0)).equals("i")){
                //String str = "https://aobei-public.oss-cn-beijing.aliyuncs.com/app_pack/"+val+"/"+channel.getChannel_id()+".apk";
                AppPackExample appPackExample = new AppPackExample();
                appPackExample.or()
                        .andApp_pack_idEqualTo(val);
                AppPack appPack = DataAccessUtils.singleResult(appPackService.selectByExample(appPackExample));
                BufferedImage i_img = GenerateQRCode.getQRcode(appPack.getApp_url(), size);
                map.put(val,i_img);
                logger.info("M[channel] F[download_QRcode] U[{}] param[使用端app:{}]  result:{}"
                        ,users.getUser_id(),val,String.format("生成%s二维码图片",val));
            }
            //h5链接
            if(Character.toString(val.charAt(0)).equals("h")){
                //String str = "https://aobei-public.oss-cn-beijing.aliyuncs.com/app_pack/"+val+"/"+channel.getChannel_id()+".apk";
                AppPackExample appPackExample = new AppPackExample();
                appPackExample.or()
                        .andApp_pack_idEqualTo(val);
                AppPack appPack = DataAccessUtils.singleResult(appPackService.selectByExample(appPackExample));
                String app_url = appPack.getApp_url();

                BufferedImage h_img = GenerateQRCode.getQRcode(app_url+"?channel="+channel.getCode(), size);
                map.put(val,h_img);
                logger.info("M[channel] F[download_QRcode] U[{}] param[使用端app:{}]  result:{}"
                        ,users.getUser_id(),val,String.format("生成%s二维码图片",val));
            }
        }
        try{
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("二维码.zip", "UTF-8"));
            for (String key : map.keySet()){
                //创建缓冲数组，储存字节数据
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //捕获流数据对象，加载到缓冲区
                ImageIO.write(map.get(key),"jpg",baos);
                //压缩流
                zos.putNextEntry(new ZipEntry(key+"_"+channel.getChannel_id()+".jpg"));
                zos.write(baos.toByteArray());
            }
            zos.flush();
            zos.close();
            logger.info("M[channel] F[download_QRcode] U[{}] result:{}"
                    ,users.getUser_id(),"zip流下载二维码");
        }catch (IOException e){
            e.printStackTrace();
            logger.error("压缩流io错误",e);
        }
    }
}
