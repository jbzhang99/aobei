package com.aobei.trainconsole.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.Course;
import com.aobei.train.model.CourseExample;
import com.aobei.train.model.OssVideoExample;
import com.aobei.train.service.OssVideoService;
import com.aobei.trainconsole.configuration.CustomProperties;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.github.liyiorg.mbg.bean.Page;

@Controller
@Transactional
@RequestMapping("/videomanager")
public class VideoController {
	
	@Autowired
	private CustomProperties customProperties;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private OssVideoService ossVideoService;

	
	/**
	 * 培训视屏信息的列表页
	 * 
	 * @param map
	 * @param p
	 * @return
	 */
	@RequestMapping("/goto_video_list")
	public String goto_video_list(Model map, @RequestParam(defaultValue = "1") Integer p,
			@RequestParam(defaultValue = "10") Integer ps) {
		OssVideoExample example = new OssVideoExample();
		example.setOrderByClause(OssVideoExample.C.create_time + " desc");
		ossVideoService.selectByExample(example, p, ps);
		return "trainvideo/train_video_list";
	}

	/**
	 * 跳转到培训视屏添加页面
	 * 
	 * @param map
	 * @param page_current_page
	 * @return
	 */
	@RequestMapping("/train_video_add")
	public String train_video_add(Model map, Integer page_current_page) {
		map.addAttribute("page_current_page", page_current_page);
		return "trainvideo/train_video_add";
	}

	/**
	 * 异步查询选择的课程组名称下的课程信息
	 * 
	 * @param team_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_section_by_team_id")
	public Object get_section_by_team_id(Long team_id) {
		List<Course> course_list = null;
		if (team_id != null) {
			CourseExample example = new CourseExample();
		}
		return course_list;
	}

	/**
	 * 异步上传并保存培训视屏信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save_train_video")
	public Object save_train_video( MultipartFile train_video, MultipartFile cover_img) {
		//设置要上传的bucket
		//String bucketName = customProperties.getAliyun().getOss().getPublicBucket();
		
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("培训视屏信息添加%s!", 1 > 0 ? "成功" : "失败"));
		return map;
	}
}
