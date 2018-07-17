package com.aobei.common.boot;

import org.springframework.beans.BeanUtils;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;

public class OnsProducer extends ProducerBean {

	private String envName;

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	@Override
	public SendResult send(Message message) {
		if (envName != null) {
			Message temp = new Message();
			BeanUtils.copyProperties(message, temp);
			temp.setTag(envName + "_" + temp.getTag());
			return super.send(temp);
		}
		return super.send(message);
	}

	@Override
	public void sendOneway(Message message) {
		if (envName != null) {
			Message temp = new Message();
			BeanUtils.copyProperties(message, temp);
			temp.setTag(envName + "_" + temp.getTag());
			super.sendOneway(temp);
		}
		super.sendOneway(message);
	}

	@Override
	public void sendAsync(Message message, SendCallback sendCallback) {
		if (envName != null) {
			Message temp = new Message();
			BeanUtils.copyProperties(message, temp);
			temp.setTag(envName + "_" + temp.getTag());
			super.sendAsync(temp, sendCallback);
		}
		super.sendAsync(message, sendCallback);
	}

}
