package com.aobei.train.service;

import com.aobei.train.model.ChannelTypeExample;
import com.aobei.train.model.ChannelType;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

import java.util.HashMap;

public interface ChannelTypeService extends MbgReadService<Integer, ChannelType, ChannelType, ChannelTypeExample>,MbgWriteService<Integer, ChannelType, ChannelType, ChannelTypeExample>{

    Integer xInsertChannelType(ChannelType channelType);

    Integer xDeleteChannelType(Integer channel_type_id);

    Integer xUpdateChannelType(ChannelType channelType);
}