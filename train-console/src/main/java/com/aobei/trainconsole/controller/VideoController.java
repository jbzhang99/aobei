package com.aobei.trainconsole.controller;

import com.aobei.train.model.Users;
import com.aobei.train.model.VideoContent;
import com.aobei.train.model.VideoContentExample;
import com.aobei.train.service.OssVideoService;
import com.aobei.train.service.UsersService;
import com.aobei.train.service.VideoContentService;
import com.aobei.trainconsole.configuration.CustomProperties;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.github.liyiorg.mbg.bean.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@Transactional
@RequestMapping("/videomanager")
public class VideoController {

    private static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private CustomProperties customProperties;

    @Autowired
    private MyFileHandleUtil myFileHandleUtil;

    @Autowired
    private OssVideoService ossVideoService;

    @Autowired
    private VideoContentService videoContentService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private Environment environment;

    @Value("${aobei.common.boot.aliyun.access-key-id}")
    private String ossAccessKeyId;

    @Value("${aobei.common.boot.aliyun.access-key-secret}")
    private String ossAccessKeySecret;


    /**
     * 视频列表页
     *
     * @param clientType
     * @param p
     * @param ps
     * @param modelMap
     * @return
     */
    @GetMapping("/video_list")
    public String gotoVideoList(
            @RequestParam(required = false) String clientType,
            @RequestParam(defaultValue = "1", required = false) Integer p,
            @RequestParam(defaultValue = "10") Integer ps,
            ModelMap modelMap) {
        VideoContentExample videoContentExample = new VideoContentExample();
        if (StringUtils.hasText(clientType)) {
            videoContentExample.or().andClient_typeEqualTo(clientType);
        }
        videoContentExample.setOrderByClause(VideoContentExample.C.create_datetime + " desc");
        Page<VideoContent> page = videoContentService.selectByExample(videoContentExample, p, ps);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("clientType", clientType);
        return "videomanager/video_list";
    }

    /**
     * 去视屏添加页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/to_video_update")
    public String toVideoUpdate(
            @RequestParam(required = false) Long id,
            ModelMap modelMap) {
        if (id != null) {
            VideoContent videoContent = videoContentService.selectByPrimaryKey(id);
            modelMap.addAttribute("videoContent", videoContent);
        }

        boolean envPro = environment.acceptsProfiles("pro", "uat");
        // 根据环境设置对应 bucket
        modelMap.addAttribute("bucket", envPro ? "aobei-public" : "aobei-test-public");
        modelMap.addAttribute("accessKeyId", ossAccessKeyId);
        modelMap.addAttribute("accessKeySecret", ossAccessKeySecret);
        modelMap.addAttribute("fileKey", "/video_content/video/" + DateTimeFormatter.ofPattern("yyyy/MM/dd/").format(LocalDate.now()) + UUID.randomUUID().toString() + ".mp4");
        return "videomanager/video_update";
    }


    /**
     * 去视屏添加页面
     */
    @PostMapping("/video_update")
    public String videoUpdate(
            Authentication authentication,
            @ModelAttribute VideoContent videoContent,
            @RequestParam(required = false) MultipartFile imgFile) {
        Users user = usersService.xSelectUserByUsername(authentication.getName());

        if (videoContent.getVideo_content_id() != null) {
            logger.info("M[VIDEO_CONTENT]F[UPDATE]U[{}] DATA:{}", user.getUser_id(), videoContent);
            videoContentService.updateVideoContent(imgFile, videoContent);
        } else {
            logger.info("M[VIDEO_CONTENT]F[INSERT]U[{}] DATA:{}", user.getUser_id(), videoContent);
            // 添加数据
            videoContentService.saveVideoContent(imgFile, videoContent);
        }
        return "redirect:/videomanager/video_list";
    }


    /**
     * 更新在线状态
     *
     * @param id
     * @param online
     * @return
     */
    @PostMapping("/updateOnline")
    @ResponseBody
    public String updateOnline(
            Long id,
            int online) {
        VideoContent videoContent = new VideoContent();
        videoContent.setVideo_content_id(id);
        videoContent.setOnline(online);
        int count = videoContentService.updateByPrimaryKeySelective(videoContent);
        return String.format("{\"status\":%b}", count == 1);
    }

    /**
     * 更新排序权重
     *
     * @param id
     * @param orderNum
     * @return
     */
    @PostMapping("/updateOrderNum")
    @ResponseBody
    public String updateOrderNum(
            Long id,
            int orderNum) {
        VideoContent videoContent = new VideoContent();
        videoContent.setVideo_content_id(id);
        videoContent.setOrder_num(orderNum);
        int count = videoContentService.updateByPrimaryKeySelective(videoContent);
        return String.format("{\"status\":%b}", count == 1);
    }
}
