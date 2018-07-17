package com.aobei.authserver.repository;

import com.aobei.authserver.model.Channel;

public interface ChannelRepository {
	
	/**
	 * 根据code 获取渠道信息
	 * @param userId
	 * @return
	 */
	Channel findByCode(String code);
    
}
