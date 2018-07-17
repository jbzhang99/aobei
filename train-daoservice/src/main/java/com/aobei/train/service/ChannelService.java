package com.aobei.train.service;

import com.aobei.train.model.ChannelExample;
import com.aobei.train.model.Channel;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import custom.bean.ChannelAndType;

import java.util.List;

public interface ChannelService extends MbgReadService<Integer, Channel, Channel, ChannelExample>,MbgWriteService<Integer, Channel, Channel, ChannelExample>{

    Integer xInsertChannel(Channel channel);

    Integer xUpdateChannel(Channel channel);

    Integer xDeleteChannel(Integer channel_id);

    List<ChannelAndType> xExport(Integer channel_type_id);
}