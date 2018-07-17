package com.aobei.train.service;

import com.aobei.train.model.MessageExample;
import com.aobei.train.model.Message;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface MessageService extends MbgReadService<Long, Message, Message, MessageExample>,MbgWriteService<Long, Message, Message, MessageExample>,MbgUpsertService<Long, Message, Message, MessageExample>{

}