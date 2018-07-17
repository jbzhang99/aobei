package com.aobei.authserver.repository;

import java.util.List;

import com.aobei.authserver.model.Wxapp;

public interface WxappRepository {

	/**
	 * 获取所有微信app 信息
	 * @return
	 */
	List<Wxapp> findAll();
}
