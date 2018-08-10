package com.aobei.train.service.impl;

import com.aliyun.oss.OSSClient;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.OssImg;
import com.aobei.train.model.OssVideo;
import com.aobei.train.service.OssImgService;
import com.aobei.train.service.OssVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.aobei.train.model.VideoContent;
import com.aobei.train.mapper.VideoContentMapper;
import com.aobei.train.model.VideoContentExample;import com.aobei.train.service.VideoContentService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class VideoContentServiceImpl extends MbgServiceSupport<VideoContentMapper, Long, VideoContent, VideoContent, VideoContentExample> implements VideoContentService{

	private static Logger logger = LoggerFactory.getLogger(VideoContentServiceImpl.class);

	@Autowired
	private VideoContentMapper videoContentMapper;

	@Autowired
	private OssVideoService ossVideoService;

	@Autowired
	private OssImgService ossImgService;

	@Autowired
	private Environment environment;

	@Autowired
	OSSClient ossClient;


	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(videoContentMapper);
	}

	@Override
	@Transactional(timeout = 6000)
	public boolean saveVideoContent(MultipartFile img_file, VideoContent videoContent) {
		boolean envPro = environment.acceptsProfiles("pro", "uat");
		String ossImageFileKey = "video_content/image/" + DateTimeFormatter.ofPattern("yyyy/MM/dd/").format(LocalDate.now()) + UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(img_file.getOriginalFilename());
		String bucket = envPro ? "aobei-public" : "aobei-test-public";
		try{
			ossClient.putObject(bucket, ossImageFileKey, img_file.getInputStream());
		}catch (Exception e){
			logger.error("oss put data error", e);
		}
		String ossUrl = String.format("https://%s.oss-cn-beijing.aliyuncs.com/%s", bucket, ossImageFileKey);
		OssImg ossImg = new OssImg();
		ossImg.setOss_img_id(IdGenerator.generateId());
		ossImg.setBucket(bucket);
		ossImg.setName(StringUtils.getFilename(ossImageFileKey));
		ossImg.setUrl(ossUrl);
		ossImg.setEffect("视频内容");
		ossImg.setFormat(StringUtils.getFilenameExtension(img_file.getOriginalFilename()));
		ossImg.setAccess_permissions("public");
		ossImg.setCreate_time(new Date());
		// 保存图片信息
		ossImgService.insert(ossImg);

		OssVideo ossVideo = new OssVideo();
		ossVideo.setOss_video_id(IdGenerator.generateId());
		ossVideo.setBucket(bucket);
		ossVideo.setName(StringUtils.getFilename(videoContent.getVideo_url()));
		ossVideo.setUrl(videoContent.getVideo_url());
		ossVideo.setEffect("视频内容");
		ossVideo.setFormat(StringUtils.getFilenameExtension(videoContent.getVideo_url()));
		ossVideo.setAccess_permissions("public");
		ossVideo.setCreate_time(new Date());
		// 保存图片信息
		ossVideoService.insert(ossVideo);

		videoContent.setVideo_content_id(IdGenerator.generateId());
		videoContent.setCreate_datetime(new Date());
		videoContent.setOrder_num(0);
		videoContent.setOnline(0);
		videoContent.setVideo_upload_status(2);	//已上传
		videoContent.setImg_url(String.format("{\"id\":\"%s\",\"url\":\"%s\"}", ossImg.getOss_img_id(), ossImg.getUrl()));
		videoContent.setVideo_url(String.format("{\"id\":\"%s\",\"url\":\"%s\"}", ossVideo.getOss_video_id(), ossVideo.getUrl()));
		// 保存视频内容
		videoContentMapper.insert(videoContent);
		return true;
	}

	@Override
	@Transactional(timeout = 6000)
	public boolean updateVideoContent(MultipartFile img_file, VideoContent videoContent) {
		Objects.requireNonNull(videoContent);
		Objects.requireNonNull(videoContent.getVideo_content_id(), "视频数据ID不能为空");
		boolean envPro = environment.acceptsProfiles("pro", "uat");
		String bucket = envPro ? "aobei-public" : "aobei-test-public";
		// 封面图片数据有更新
		if(img_file != null && !img_file.isEmpty()){
			String ossImageFileKey = "video_content/image/" + DateTimeFormatter.ofPattern("yyyy/MM/dd/").format(LocalDate.now()) + UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(img_file.getOriginalFilename());
			try{
				ossClient.putObject(bucket, ossImageFileKey, img_file.getInputStream());
			}catch (Exception e){
				logger.error("oss put data error", e);
			}
			String ossUrl = String.format("https://%s.oss-cn-beijing.aliyuncs.com/%s", bucket, ossImageFileKey);
			OssImg ossImg = new OssImg();
			ossImg.setOss_img_id(IdGenerator.generateId());
			ossImg.setBucket(bucket);
			ossImg.setName(StringUtils.getFilename(ossImageFileKey));
			ossImg.setUrl(ossUrl);
			ossImg.setEffect("视频内容");
			ossImg.setFormat(StringUtils.getFilenameExtension(img_file.getOriginalFilename()));
			ossImg.setAccess_permissions("public");
			ossImg.setCreate_time(new Date());
			// 保存图片信息
			ossImgService.insert(ossImg);
			videoContent.setImg_url(String.format("{\"id\":\"%s\",\"url\":\"%s\"}", ossImg.getOss_img_id(), ossImg.getUrl()));
		}
		// 视频数据 有更新
		if(videoContent.getVideo_url() != null && videoContent.getVideo_url().startsWith("http")){
			OssVideo ossVideo = new OssVideo();
			ossVideo.setOss_video_id(IdGenerator.generateId());
			ossVideo.setBucket(bucket);
			ossVideo.setName(StringUtils.getFilename(videoContent.getVideo_url()));
			ossVideo.setUrl(videoContent.getVideo_url());
			ossVideo.setEffect("视频内容");
			ossVideo.setFormat(StringUtils.getFilenameExtension(videoContent.getVideo_url()));
			ossVideo.setAccess_permissions("public");
			ossVideo.setCreate_time(new Date());
			// 保存图片信息
			ossVideoService.insert(ossVideo);
			videoContent.setVideo_url(String.format("{\"id\":\"%s\",\"url\":\"%s\"}", ossVideo.getOss_video_id(), ossVideo.getUrl()));
		}
		videoContentMapper.updateByPrimaryKeySelective(videoContent);
		return true;
	}
}