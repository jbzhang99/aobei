package com.aobei.train.service;

import com.aobei.train.model.CmsUrlExample;
import com.aobei.train.model.CmsUrl;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CmsUrlService extends MbgReadService<Long, CmsUrl, CmsUrl, CmsUrlExample>,MbgWriteService<Long, CmsUrl, CmsUrl, CmsUrlExample>,MbgUpsertService<Long, CmsUrl, CmsUrl, CmsUrlExample>{

    int xAdd_cmsUrl(CmsUrl cmsUrl);

    int xDel_cmsUrl(Long cms_url_id);

    int xEdit_cmsUrl(CmsUrl cmsUrl,Long cms_url_id);

}