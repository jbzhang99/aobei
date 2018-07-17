package com.aobei.common.boot;

import com.aliyun.openservices.ons.api.MessageListener;

/**
 * 阿里云 MQ 消息监听
 * 
 * @author liyi
 *
 */
public interface OnsMessageListener extends MessageListener {

	/**
	 * 主题
	 * 
	 * @return
	 */
	String getTopic();

	/**
	 * tag
	 * 
	 * @return
	 */
	String getTag();

	/**
	 * 是否为集群式订阅
	 * 
	 * @return
	 */
	boolean isClustering();

	public default String envTag(String env) {
		return env + "_" + getTag();
	}

	public default String envCid(String env) {
		return "CID_T_" + envTag(env);
	}

}
