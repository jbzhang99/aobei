package com.aobei.train.service;

import com.aobei.train.model.MsgtextExample;
import com.aobei.train.model.Msgtext;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface MsgtextService extends MbgReadService<String, Msgtext, Msgtext, MsgtextExample>,MbgWriteService<String, Msgtext, Msgtext, MsgtextExample>,MbgUpsertService<String, Msgtext, Msgtext, MsgtextExample>{

}