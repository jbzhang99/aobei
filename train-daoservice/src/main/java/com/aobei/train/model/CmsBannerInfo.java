package com.aobei.train.model;

public class CmsBannerInfo extends CmsBanner {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OssImg ossImg;

	private String group_name;

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public OssImg getOssImg() {
		return ossImg;
	}

	public void setOssImg(OssImg ossImg) {
		this.ossImg = ossImg;
	}

	@Override
	public String toString() {
		return "CmsBannerInfo [OssImg=" + ossImg + "]";
	}
}
