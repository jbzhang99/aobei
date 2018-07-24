package com.aobei.train.service;

import com.aobei.train.model.VideoContentExample;
import com.aobei.train.model.VideoContent;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;
import org.springframework.web.multipart.MultipartFile;

public interface VideoContentService extends MbgReadService<Long, VideoContent, VideoContent, VideoContentExample>,MbgWriteService<Long, VideoContent, VideoContent, VideoContentExample>,MbgUpsertService<Long, VideoContent, VideoContent, VideoContentExample>{

    /**
     * 保存videoContent
     * @param imgFile 封面图片文件
     * @param videoContent
     * @return
     */
    boolean saveVideoContent(MultipartFile imgFile,VideoContent videoContent);

    /**
     * 更新 videoContent
     * @param imgFile 封面图片文件
     * @param videoContent
     * @return
     */
    boolean updateVideoContent(MultipartFile imgFile,VideoContent videoContent);
}